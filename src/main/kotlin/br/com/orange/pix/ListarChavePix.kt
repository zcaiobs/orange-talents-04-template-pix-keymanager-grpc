package br.com.orange.pix

import br.com.orange.KeyManagerFindAllReply
import br.com.orange.KeyManagerFindAllRequest
import br.com.orange.KeyManagerFindAllServiceGrpc.*
import br.com.orange.repository.PixRepository
import io.grpc.Status
import io.grpc.stub.StreamObserver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListarChavePix(@Inject val pixRepository: PixRepository): KeyManagerFindAllServiceImplBase() {
    override fun findAll(request: KeyManagerFindAllRequest?, responseObserver: StreamObserver<KeyManagerFindAllReply>?) {
        if ( !request?.clienteId.isNullOrBlank()) {
            val result = pixRepository.listarChave(request!!.clienteId)
            val list = result.map { KeyManagerFindAllReply.ListaDeChaves.newBuilder()
                .setPixId(it.id)
                .setKeyType(KeyManagerFindAllReply.Key.valueOf(it.keyType!!.name))
                .setKeyValue(it.keyvalue)
                .setConta(KeyManagerFindAllReply.Account.valueOf(it.bankAccount!!.accountType!!))
                .setCreated(it.createdAt.toString()).build()}

            responseObserver?.onNext(KeyManagerFindAllReply.newBuilder()
                .addAllChaves(list.toMutableList()).setClienteId(request.clienteId).build())
        } else {
            responseObserver?.onError(Status.INVALID_ARGUMENT.withDescription("Valor inválido.")
                .asRuntimeException())
        }
        responseObserver?.onCompleted()
    }
}