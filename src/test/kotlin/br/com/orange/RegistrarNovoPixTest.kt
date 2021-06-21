package br.com.orange

import br.com.orange.repository.PixRepository
import io.grpc.Channel
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
internal class RegistrarNovoPixTest {

    @field:Inject
    lateinit var pixRepository: PixRepository

    @field:Inject
    lateinit var grpcClient: KeyManagerRegisterServiceGrpc.KeyManagerRegisterServiceBlockingStub

    @Test
    fun `Deve registrar uma chave PIX`() {
        val request = KeyManagerRequest.newBuilder()
            .setClientId("5260263c-a3c1-4727-ae32-3bdb2538841b")
            .setKeyType(KeyManagerRequest.Key.RANDOM)
            .setAccountType(KeyManagerRequest.Account.CONTA_CORRENTE)
            .build()

        val result = grpcClient.register(request)

        Assertions.assertNotNull(result.idPix)
        Assertions.assertEquals("PIX created successfully", result.message)
    }

    @Factory
    class ClientsFactory {
        @Bean
        fun registerClient(@GrpcChannel(GrpcServerChannel.NAME) channel: Channel): KeyManagerRegisterServiceGrpc.KeyManagerRegisterServiceBlockingStub {
            return KeyManagerRegisterServiceGrpc.newBlockingStub(channel)
        }
    }
}
