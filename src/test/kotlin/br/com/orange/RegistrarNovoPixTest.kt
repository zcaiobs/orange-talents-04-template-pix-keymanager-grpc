package br.com.orange

import br.com.orange.domain.ClienteRequest
import br.com.orange.domain.Pix
import br.com.orange.domain.PixRequest
import br.com.orange.domain.PixResponse
import br.com.orange.repository.PixRepository
import br.com.orange.service.ExternalService
import br.com.orange.service.LegacyService
import io.grpc.Channel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

@MicronautTest(transactional = false)
class RegistrarNovoPixTest {

    @MockBean(LegacyService::class)
    fun legacyServiceMock() = Mockito.mock(LegacyService::class.java)

    @MockBean(ExternalService::class)
    fun externalServiceMock() = Mockito.mock(ExternalService::class.java)

    @field:Inject
    lateinit var pixRepository: PixRepository

    @field:Inject
    lateinit var legacyService: LegacyService

    @field:Inject
    lateinit var externalService: ExternalService

    @field:Inject
    lateinit var grpcClient: KeyManagerRegisterServiceGrpc.KeyManagerRegisterServiceBlockingStub

    @Test
    fun `Deve registar uma chave PIX`() {
        val request = KeyManagerRequest.newBuilder()
            .setClientId("5260263c-a3c1-4727-ae32-3bdb2538841b")
            .setKeyType(KeyManagerRequest.Key.RANDOM)
            .setKeyValue("")
            .setAccountType(KeyManagerRequest.Account.CONTA_CORRENTE)
            .build()

        val clienteRequest = ClienteRequest(
            "CONTA_CORRENTE", ClienteRequest.Instituicao("abc", "1234324"),
            "12312", "213534123", ClienteRequest.Titular("131kj231kj3n1", "abc", "123132132")
        )

        val pixResponse = PixResponse(
            PixResponse.KeyType.RANDOM, "5260263c-a3c1-4727-ae32-3bdb2538841b", UUID.randomUUID().toString(),
            PixResponse.BankAccount(
                "5260263c-a3c1-4727-ae32-3bdb2538841b",
                "Bank",
                "1234324",
                "12312",
                "213123",
                PixResponse.BankAccount.AccountType.CACC
            ),
            PixResponse.Owner("abc", "123132132"), LocalDateTime.now()
        )

        Mockito.`when`(legacyService.findClientById(request.clientId, request.accountType.name))
            .thenReturn(clienteRequest)

        Mockito.`when`(externalService.createNewKey(PixRequest.toRequest(clienteRequest, request)))
            .thenReturn(pixResponse)

        val result = grpcClient.register(request)

        Assertions.assertNotNull(result.idPix)
        Assertions.assertEquals("PIX created successfully", result.message)
    }

    @Test
    fun `Não deve registar uma chave PIX caso exista uma chave igual já cadastrada`() {
        pixRepository.save(
            Pix(
                "EMAIL", "teste@email.com",
                Pix.BankAccount("5260263c-a3c1-4727-ae32-3bdb2538841b", "", "", "", "", ""), null, LocalDateTime.now()
            )
        )

        val request = KeyManagerRequest.newBuilder()
            .setClientId("5260263c-a3c1-4727-ae32-3bdb2538841b")
            .setKeyType(KeyManagerRequest.Key.EMAIL)
            .setKeyValue("teste@email.com")
            .setAccountType(KeyManagerRequest.Account.CONTA_CORRENTE)
            .build()

        val clienteRequest = ClienteRequest(
            "CONTA_CORRENTE", ClienteRequest.Instituicao("abc", "1234324"),
            "12312", "213534123", ClienteRequest.Titular("131kj231kj3n1", "abc", "123132132")
        )

        val pixResponse = PixResponse(
            PixResponse.KeyType.EMAIL, "5260263c-a3c1-4727-ae32-3bdb2538841b", "teste@email.com",
            PixResponse.BankAccount(
                "5260263c-a3c1-4727-ae32-3bdb2538841b",
                "Bank",
                "1234324",
                "12312",
                "213123",
                PixResponse.BankAccount.AccountType.CACC
            ),
            PixResponse.Owner("abc", "123132132"), LocalDateTime.now()
        )

        Mockito.`when`(legacyService.findClientById(request.clientId, request.accountType.name))
            .thenReturn(clienteRequest)

        Mockito.`when`(externalService.createNewKey(PixRequest.toRequest(clienteRequest, request)))
            .thenReturn(pixResponse)

        val result = assertThrows<StatusRuntimeException> {
            grpcClient.register(request)
        }

        with(result) {
            Assertions.assertEquals(Status.ALREADY_EXISTS.code, this.status.code)
            Assertions.assertEquals("ALREADY_EXISTS: Está chave PIX já existe", this.message)
        }
    }

    @Test
    fun `Não deve registar uma chave PIX caso a chave seja inválida`() {
        val request = KeyManagerRequest.newBuilder()
            .setClientId("5260263c-a3c1-4727-ae32-3bdb2538841b")
            .setKeyType(KeyManagerRequest.Key.CPF)
            .setKeyValue("teste@email.com")
            .setAccountType(KeyManagerRequest.Account.CONTA_CORRENTE)
            .build()

        val result = assertThrows<StatusRuntimeException> {
            grpcClient.register(request)
        }

        with(result) {
            Assertions.assertEquals(Status.INVALID_ARGUMENT.code, this.status.code)
            Assertions.assertEquals("INVALID_ARGUMENT: Valor inválido", this.message)
        }
    }


    @Factory
    class ClientFactory {
        @Bean
        fun registarClien(@GrpcChannel(GrpcServerChannel.NAME) channel: Channel): KeyManagerRegisterServiceGrpc.KeyManagerRegisterServiceBlockingStub? {
            return KeyManagerRegisterServiceGrpc.newBlockingStub(channel)
        }
    }
}