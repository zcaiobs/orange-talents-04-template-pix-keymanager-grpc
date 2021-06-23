package br.com.orange

import br.com.orange.domain.ClienteRequest
import br.com.orange.domain.PixRequest
import br.com.orange.domain.PixResponse
import br.com.orange.repository.PixRepository
import br.com.orange.service.ExternalService
import br.com.orange.service.LegacyService
import io.grpc.Channel
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.time.LocalDateTime
import javax.inject.Inject

@MicronautTest
internal class RegistrarNovoPixTest {

    @field:Inject
    lateinit var pixRepository: PixRepository

    @field:Inject
    lateinit var grpcClient: KeyManagerRegisterServiceGrpc.KeyManagerRegisterServiceBlockingStub

    @MockBean(ExternalService::class)
    fun externalServiceMock() = Mockito.mock(ExternalService::class.java)

    @MockBean(LegacyService::class)
    fun legacyServiceMock() = Mockito.mock(LegacyService::class.java)

    @field:Inject
    lateinit var externalService: ExternalService

    @field:Inject
    lateinit var legacyService: LegacyService

    @Test
    fun `Deve registrar uma chave PIX`() {
        val request: KeyManagerRequest? = KeyManagerRequest.newBuilder()
            .setClientId("abcd")
            .setKeyType(KeyManagerRequest.Key.RANDOM)
            .setAccountType(KeyManagerRequest.Account.CONTA_CORRENTE)
            .build()

        val clientRequest = ClienteRequest("CACC",
            ClienteRequest.Instituicao("Bank", "12342162"),
            "6536", "234",
            ClienteRequest.Titular("2313", "acbs", "612531"),
        )

        val pixResp = PixResponse(PixResponse.KeyType.RANDOM, "abcd", "213123123",
            PixResponse.BankAccount("abcd", "Bank", "12342162",
                "23123", "123123", PixResponse.BankAccount.AccountType.CACC),
            PixResponse.Owner("abc", "12312313"), LocalDateTime.now())

        Mockito.`when`(legacyService.findClientById(request!!.clientId, request.accountType.name))
            .thenReturn(clientRequest)

        Mockito.`when`(externalService.createNewKey(PixRequest.toRequest(clientRequest, request)))
            .thenReturn(pixResp)

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
