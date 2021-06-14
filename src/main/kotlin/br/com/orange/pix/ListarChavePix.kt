package br.com.orange.pix

import br.com.orange.KeyManagerFindAllReply
import br.com.orange.KeyManagerFindAllRequest
import br.com.orange.KeyManagerFindAllServiceGrpc.*
import io.grpc.stub.StreamObserver
import javax.inject.Singleton

@Singleton
class ListarChavePix: KeyManagerFindAllServiceImplBase() {
    override fun findAll(request: KeyManagerFindAllRequest?, responseObserver: StreamObserver<KeyManagerFindAllReply>?) {

        val msg = KeyManagerFindAllReply.newBuilder().setCreated("sdfsdf").build()
        responseObserver?.onNext(msg)
        responseObserver?.onCompleted()
    }
}