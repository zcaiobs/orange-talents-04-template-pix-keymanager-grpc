package br.com.orange.pix

import br.com.orange.KeyManagerFindReply
import br.com.orange.KeyManagerFindRequest
import br.com.orange.KeyManagerFindServiceGrpc.*
import br.com.orange.repository.PixRepository
import io.grpc.stub.StreamObserver
import javax.inject.Singleton

@Singleton
class ConsultarChavePix(private val pixRepository: PixRepository): KeyManagerFindServiceImplBase() {
    override fun find(request: KeyManagerFindRequest?, responseObserver: StreamObserver<KeyManagerFindReply>?) {

        val pix = pixRepository.findById(request?.keymanagerFind!!.idPix).get()

        val msg = KeyManagerFindReply.newBuilder().setClientId(request.keymanagerFind.idPix)
                .setIdPix(request.keymanagerFind.idPix)
                .setKeyType(KeyManagerFindReply.Key.valueOf(pix.keyType!!.name))
                .setKeyValue(pix.keyvalue)
                .setTitular(KeyManagerFindReply.Titular.newBuilder()
                    .setName(pix.owner!!.name)
                    .setCpf(pix.owner!!.taxIdNumber))
                .setConta(KeyManagerFindReply.InstituicaoFinanceira.newBuilder()
                    .setNome(pix.bankAccount!!.nameBank)
                    .setAgencia(pix.bankAccount!!.branch)
                    .setConta(pix.bankAccount!!.accountNumber)
                    .setTipo(pix.bankAccount!!.accountType?.let {
                        KeyManagerFindReply.InstituicaoFinanceira.Account.valueOf(
                            it
                        )
                    }))
            .setCriadoEm(pix.createdAt.toString()).build()

        responseObserver?.onNext(msg)
        responseObserver?.onCompleted()
    }
}