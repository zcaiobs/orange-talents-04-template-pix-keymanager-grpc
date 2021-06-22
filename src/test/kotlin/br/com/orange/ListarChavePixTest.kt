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
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import javax.inject.Inject

@MicronautTest(transactional = false)
class ListarChavePixTest {

    @field:Inject
    lateinit var pixRepository: PixRepository

    @field:Inject
    lateinit var grpcClient: KeyManagerFindAllServiceGrpc.KeyManagerFindAllServiceBlockingStub

    @Test
    fun `Deve retornar uma lista de chaves PIX`() {
        val clientId = "5260263c-a3c1-4727-ae32-3bdb2538841b"
        val list = mutableListOf<Pix>()
        for(i in 1..3) {
            list.add(makeList(clientId))
        }
        pixRepository.saveAll(list)

        val result = grpcClient.findAll(KeyManagerFindAllRequest.newBuilder().setClienteId(clientId).build())

        Assertions.assertEquals(3, result.chavesList.size)
        Assertions.assertEquals(clientId, result.clienteId)
        Assertions.assertTrue(result.chavesList.isNotEmpty())
    }

    @Factory
    class ClientsFactory {
        @Bean
        fun listarClient(@GrpcChannel(GrpcServerChannel.NAME) channel: Channel): KeyManagerFindAllServiceGrpc.KeyManagerFindAllServiceBlockingStub? {
            return KeyManagerFindAllServiceGrpc.newBlockingStub(channel)
        }
    }

    private fun makeList(clientId: String): Pix {
        val pix = Pix()
        pix.keyvalue = "45376545623"
        pix.keyType = Pix.KeyType.CPF
        pix.createdAt = LocalDateTime.now()
        pix.owner = Pix.Owner("abc", "45376545623")
        pix.owner?.type = Pix.Owner.PersonType.NATURAL_PERSON
        pix.bankAccount = Pix.BankAccount(clientId,
            "A",
            "B",
            "C",
            "D",
            PixRequest.BankAccount.AccountType.CONTA_CORRENTE.name)
        return pix
    }
}