package br.com.orange

import br.com.orange.domain.Pix
import br.com.orange.domain.PixRequest
import br.com.orange.repository.PixRepository
import io.grpc.Channel
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import javax.inject.Inject

@MicronautTest(transactional = false)
class ConsultarChavePixTest {

    @field:Inject
    lateinit var pixRepository: PixRepository

    @field:Inject
    lateinit var grpcClient: KeyManagerFindServiceGrpc.KeyManagerFindServiceBlockingStub

    @BeforeEach
    fun clean() {
        pixRepository.deleteAll()
    }

    @Test
    fun `Deve retornar uma chave PIX`() {
        val clientId = "5260263c-a3c1-4727-ae32-3bdb2538841b"
        val pix = Pix()
        pix.keyvalue = "45376545623"
        pix.keyType = Pix.KeyType.CPF
        pix.createdAt = LocalDateTime.now()
        pix.owner = Pix.Owner("abc", "45376545623")
        pix.owner?.type = Pix.Owner.PersonType.NATURAL_PERSON
        pix.bankAccount = Pix.BankAccount(clientId, "A", "B", "C", "D", PixRequest.BankAccount.AccountType.CONTA_CORRENTE.name)

        val target = pixRepository.save(pix)

        val result = grpcClient.find(KeyManagerFindRequest.newBuilder()
            .setKeymanagerFind(KeyManagerFindRequest.KeyManagerFind.newBuilder()
            .setClientId(clientId).setIdPix(target.id).build()).build())

        Assertions.assertEquals(clientId, result.clientId)
        Assertions.assertEquals(target.id, result.idPix)
    }

    @Factory
    class ClientsFactory {
        @Bean
        fun consultaClient(@GrpcChannel(GrpcServerChannel.NAME) channel: Channel): KeyManagerFindServiceGrpc.KeyManagerFindServiceBlockingStub? {
            return KeyManagerFindServiceGrpc.newBlockingStub(channel)
        }
    }
}