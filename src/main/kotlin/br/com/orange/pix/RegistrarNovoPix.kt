package br.com.orange.pix

import br.com.orange.KeyManagerReply
import br.com.orange.KeyManagerRequest
import br.com.orange.KeyManagerServiceGrpc.*
import br.com.orange.domain.PixRequest
import br.com.orange.repository.PixRepository
import br.com.orange.service.ExternalService
import br.com.orange.service.LegacyService
import io.grpc.stub.StreamObserver
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class RegistrarNovoPix(
    private val legacyService: LegacyService,
    private val externalService: ExternalService,
    private val pixRepository: PixRepository): KeyManagerServiceImplBase() {

    val log: Logger = LoggerFactory.getLogger(RegistrarNovoPix::class.java)

    override fun register(request: KeyManagerRequest?, responseObserver: StreamObserver<KeyManagerReply>?) {

            val step1 = legacyService.findClientById(request!!.clientId, request.accountType.name)
            val step2 = externalService.createNewKey(PixRequest(step1, request))
            val result = pixRepository.save(step2.toPix())
            log.info("Pix created - {}", result.id)
            val msg = KeyManagerReply.newBuilder().setMessage(step2.toString()).build()

        responseObserver?.onNext(msg)
        responseObserver?.onCompleted()
    }
}