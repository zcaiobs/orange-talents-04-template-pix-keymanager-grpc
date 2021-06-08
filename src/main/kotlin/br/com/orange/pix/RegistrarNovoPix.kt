package br.com.orange.pix

import br.com.orange.KeyManagerReply
import br.com.orange.KeyManagerRequest
import br.com.orange.KeyManagerServiceGrpc.*
import br.com.orange.domain.PixRequest
import br.com.orange.service.ExternalService
import br.com.orange.service.LegacyService
import io.grpc.stub.StreamObserver
import javax.inject.Singleton

@Singleton
class RegistrarNovoPix(val legacyService: LegacyService,
                       val externalService: ExternalService): KeyManagerServiceImplBase() {
    override fun register(request: KeyManagerRequest?, responseObserver: StreamObserver<KeyManagerReply>?) {

            val step1 = legacyService.findClientById(request!!.clientId, request.accountType.name)
            val step2 = externalService.createNewKey(PixRequest(step1, request))
        println(step2)
            val msg = KeyManagerReply.newBuilder().setMessage(step2.toString()).build()

        responseObserver?.onNext(msg)
        responseObserver?.onCompleted()
    }
}