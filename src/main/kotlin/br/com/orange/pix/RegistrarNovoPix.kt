package br.com.orange.pix

import br.com.orange.KeyManagerRegisterServiceGrpc.*
import br.com.orange.KeyManagerReply
import br.com.orange.KeyManagerRequest
import br.com.orange.domain.PixRequest
import br.com.orange.repository.PixRepository
import br.com.orange.service.ExternalService
import br.com.orange.service.LegacyService
import br.com.orange.validator.PixValidator
import io.grpc.Status
import io.grpc.stub.StreamObserver
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class RegistrarNovoPix(
    private val legacyService: LegacyService,
    private val externalService: ExternalService,
    private val pixRepository: PixRepository,
    private val pixValidator: PixValidator
) : KeyManagerRegisterServiceImplBase() {

    private val log: Logger = LoggerFactory.getLogger(RegistrarNovoPix::class.java)

    override fun register(request: KeyManagerRequest?, responseObserver: StreamObserver<KeyManagerReply>?) {
        if (pixValidator.isValid(request)) {
            if(!pixRepository.existsByKeyvalue(request!!.keyValue)) {
                val step1 = legacyService.findClientById(request.clientId, request.accountType.name)
                val step2 = externalService.createNewKey(PixRequest.toRequest(step1, request))
                val result = pixRepository.save(step2.toPix(step1.instituicao!!.nome, step1.titular!!.id))

                log.info("Pix created - {}", result.id)
                val msg = KeyManagerReply.newBuilder()
                    .setMessage("PIX created successfully")
                    .setIdPix(result.id).build()
                responseObserver?.onNext(msg)
                responseObserver?.onCompleted()
            } else {
                responseObserver?.onError(
                    Status.ALREADY_EXISTS
                        .withDescription("Está chave PIX já existe")
                        .asRuntimeException()
                )
            }
        } else {
            log.error("Valor inválido {}")
            responseObserver?.onError(
                Status.INVALID_ARGUMENT
                    .withDescription("Valor inválido")
                    .asRuntimeException()
            )
        }
    }
}