package br.com.orange.pix

import br.com.orange.KeyManagerReply
import br.com.orange.KeyManagerRequest
import br.com.orange.KeyManagerServiceGrpc.*
import br.com.orange.domain.PixRequest
import br.com.orange.service.LegacyService
import io.grpc.stub.StreamObserver
import javax.inject.Singleton

@Singleton
class RegistrarNovoPix(val legacyService: LegacyService): KeyManagerServiceImplBase() {
    override fun register(request: KeyManagerRequest?, responseObserver: StreamObserver<KeyManagerReply>?) {
        with(request!!) {
            val step1 = legacyService.findClientById(request.clientId, request.accountType.name)
            val step2 = PixRequest(step1, request)
            println(step1)
            println(step2)
        }

        val msg = KeyManagerReply.newBuilder().setMessage("OK").build()
        responseObserver?.onNext(msg)
        responseObserver?.onCompleted()
    }
}