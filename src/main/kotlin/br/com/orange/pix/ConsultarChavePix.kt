package br.com.orange.pix

import br.com.orange.KeyManagerFindReply
import br.com.orange.KeyManagerFindRequest
import br.com.orange.KeyManagerFindServiceGrpc.*
import br.com.orange.domain.Pix
import br.com.orange.repository.PixRepository
import br.com.orange.service.ExternalService
import br.com.orange.service.LegacyService
import io.grpc.Status
import io.grpc.stub.StreamObserver
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

@Singleton
class ConsultarChavePix(
    private val pixRepository: PixRepository,
    private val externalService: ExternalService,
    private val legacyService: LegacyService
) : KeyManagerFindServiceImplBase() {
    override fun find(request: KeyManagerFindRequest?, responseObserver: StreamObserver<KeyManagerFindReply>?) {
        if (request?.keyValue == "") {
            val pix = pixRepository.findPix(request.keymanagerFind!!.idPix, request.keymanagerFind!!.clientId)
            pix.ifPresentOrElse({ responseObserver?.onNext(dataPix(request, it)) },
                { responseObserver?.onError(Status.NOT_FOUND.asRuntimeException()) })
        } else {
            try {
                val result = pixRepository.findByKeyvalue(request!!.keyValue)
                responseObserver?.onNext(dataPix(request, result))
            } catch (ex: Exception) {
                responseObserver?.onError(Status.NOT_FOUND.asRuntimeException())
            }
        }
        responseObserver?.onCompleted()
    }

    private fun dataPix(request: KeyManagerFindRequest?, pix: Pix): KeyManagerFindReply {
        return KeyManagerFindReply.newBuilder().setClientId(request?.keymanagerFind?.clientId)
            .setIdPix(request?.keymanagerFind?.idPix)
            .setKeyType(KeyManagerFindReply.Key.valueOf(pix.keyType!!.name))
            .setKeyValue(pix.keyvalue)
            .setTitular(
                KeyManagerFindReply.Titular.newBuilder()
                    .setName(pix.owner!!.name)
                    .setCpf(pix.owner!!.taxIdNumber)
            )
            .setConta(
                KeyManagerFindReply.InstituicaoFinanceira.newBuilder()
                    .setNome(pix.bankAccount!!.nameBank)
                    .setAgencia(pix.bankAccount!!.branch)
                    .setConta(pix.bankAccount!!.accountNumber)
                    .setTipo(pix.bankAccount!!.accountType?.let {
                        KeyManagerFindReply
                            .InstituicaoFinanceira.Account.valueOf(it)
                    })
            )
            .setCriadoEm(pix.createdAt.toString()).build()
    }
}

//@MustBeDocumented
//@Target(AnnotationTarget.FIELD, AnnotationTarget.CONSTRUCTOR, AnnotationTarget.EXPRESSION)
//@Retention(AnnotationRetention.SOURCE)
//@Constraint(validatedBy = [RequestValidator::class])
//annotation class RequestValid(
//    val message: String = "Request inválido."
//)
//
//@Singleton
//class RequestValidator : ConstraintValidator<RequestValid, String?> {
//
//    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
//        println("Yes")
//        return value == null
//    }
//}