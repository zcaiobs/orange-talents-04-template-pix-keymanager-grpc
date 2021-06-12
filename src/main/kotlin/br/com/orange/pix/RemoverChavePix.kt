package br.com.orange.pix

import br.com.orange.KeyManagerRemoveReply
import br.com.orange.KeyManagerRemoveRequest
import br.com.orange.KeyManagerRemoveServiceGrpc.*
import br.com.orange.repository.PixRepository
import br.com.orange.service.ExternalService
import io.grpc.stub.StreamObserver
import javax.inject.Singleton

@Singleton
class RemoverChavePix(private val externalService: ExternalService,
                      private val pixRepository: PixRepository): KeyManagerRemoveServiceImplBase() {
    override fun remove(request: KeyManagerRemoveRequest?, responseObserver: StreamObserver<KeyManagerRemoveReply>?) {
        val pix = pixRepository.findById(request!!.idPix).get()
        val pixRemove = DeletePixKeyRequest(pix.keyvalue, pix.bankAccount?.participant)
        val result = externalService.deleteKey(pix.keyvalue, pixRemove)
        pixRepository.delete(pix)
        val msg = KeyManagerRemoveReply.newBuilder().setMessage(result.toString()).build()
        responseObserver?.onNext(msg)
        responseObserver?.onCompleted()
    }

    data class DeletePixKeyRequest(val key: String?, val participant: String?)
}