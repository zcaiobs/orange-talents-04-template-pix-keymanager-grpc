package br.com.orange

import br.com.orange.domain.Pix
import br.com.orange.domain.RemoveResponse
import br.com.orange.pix.RemoverChavePix
import br.com.orange.repository.PixRepository
import br.com.orange.service.ExternalService
import io.grpc.Channel
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Context
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false)
class RemoverChavePixTest {

    @field:Inject
    lateinit var pixRepository: PixRepository

    @field:Inject
    lateinit var grpcClient: KeyManagerRemoveServiceGrpc.KeyManagerRemoveServiceBlockingStub

    @field:Inject
    lateinit var externalService: ExternalService

    @MockBean(ExternalService::class)
    fun externalServiceMock() = Mockito.mock(ExternalService::class.java)

    @BeforeEach
    fun clean() {
        pixRepository.deleteAll()
    }

    @Test
    fun `Deve remover uma chave PIX`() {
        val clientId = "abcd"
        val participant = "60701190"
        val keyValue = "12345"
        val resp = RemoveResponse( keyValue, participant, LocalDateTime.now().toString())
        val key = Pix()
        key.bankAccount?.clienteId = clientId
        key.bankAccount?.participant = participant
        key.keyvalue = keyValue
        val target = pixRepository.save(key)
        val res = pixRepository.findById(target.id).get()

        Mockito.`when`(externalService
            .deleteKey(res.keyvalue, RemoverChavePix.DeletePixKeyRequest(res.keyvalue, res.bankAccount?.participant)))
            .thenReturn(resp)

        val result = grpcClient.remove(KeyManagerRemoveRequest.newBuilder().setClientId(clientId).setIdPix(target.id).build())

        Assertions.assertNotNull(result.deletedAt)
    }

    @Factory
    class ClientsFactory {
        @Bean
        fun removeClient(@GrpcChannel(GrpcServerChannel.NAME) channel: Channel): KeyManagerRemoveServiceGrpc.KeyManagerRemoveServiceBlockingStub? {
            return KeyManagerRemoveServiceGrpc.newBlockingStub(channel)
        }
    }
}