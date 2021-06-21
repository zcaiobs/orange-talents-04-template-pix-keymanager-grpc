package br.com.orange.pix

import br.com.orange.KeyManagerRemoveReply
import br.com.orange.KeyManagerRemoveRequest
import br.com.orange.KeyManagerRemoveServiceGrpc.*
import br.com.orange.repository.PixRepository
import br.com.orange.service.ExternalService
import io.grpc.Status
import io.grpc.stub.StreamObserver
import javax.inject.Singleton

@Singleton
class RemoverChavePix(private val externalService: ExternalService,
                      private val pixRepository: PixRepository): KeyManagerRemoveServiceImplBase() {
    override fun remove(request: KeyManagerRemoveRequest?, responseObserver: StreamObserver<KeyManagerRemoveReply>?) {
        if(request!!.clientId.isNotBlank() && request.idPix.isNotBlank()) {
            try{
                val pix = pixRepository.findById(request.idPix).get()
                val pixRemove = DeletePixKeyRequest(pix.keyvalue, pix.bankAccount?.participant)
                val result = externalService.deleteKey(pix.keyvalue, pixRemove)
                pixRepository.delete(pix)
                val msg = KeyManagerRemoveReply.newBuilder().setDeletedAt(result.deletedAt).build()
                responseObserver?.onNext(msg)
            } catch (ex: NoSuchElementException) {
                responseObserver?.onError(Status.NOT_FOUND
                    .withDescription("Valor não encontrado.")
                    .withCause(ex)
                    .asRuntimeException())
            }

        } else {
            responseObserver?.onError(Status.INVALID_ARGUMENT
                .withDescription("Valor inválido.")
                .asRuntimeException())
        }
        responseObserver?.onCompleted()
    }

    data class DeletePixKeyRequest(val key: String?, val participant: String?)
}