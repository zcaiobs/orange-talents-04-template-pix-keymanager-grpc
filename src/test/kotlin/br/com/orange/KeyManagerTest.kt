package br.com.orange

import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@MicronautTest
internal class KeyManagerTest(
    private val grpcClient: KeyManagerServiceGrpc.KeyManagerServiceBlockingStub
) {

    @Test
    fun `Deve registrar uma chave PIX`() {
        val pix = KeyManagerRequest.newBuilder()
            .setClientId("0d1bb194-3c52-4e67-8c35-a93c0af9284f")
            .setKeyType(KeyManagerRequest.Key.RANDOM)
            .setKeyValue("")
            .setAccountType(KeyManagerRequest.Account.CONTA_CORRENTE)
            .build()

        val resp = grpcClient.register(pix)

        Assertions.assertTrue(resp.idPix.isNotBlank())
    }

    @Factory
    class Clients {
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): KeyManagerServiceGrpc.KeyManagerServiceBlockingStub? {
            return KeyManagerServiceGrpc.newBlockingStub(channel)
        }
    }
}
