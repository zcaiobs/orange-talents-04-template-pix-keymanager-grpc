package br.com.orange.validator

import br.com.orange.KeyManagerRequest
import io.micronaut.context.annotation.Context

@Context
class PixValidator {
    fun isValid(request: KeyManagerRequest?): Boolean {
        val regexCPF = "^[0-9]{11}$".toRegex()
        val regexPHONE = "^\\+[1-9][0-9]\\d{1,14}$".toRegex()
        val regexEMAIL = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+).*".toRegex()

        if (request?.clientId.isNullOrEmpty()) {
            return false
        }

        return when (request?.keyType) {
            KeyManagerRequest.Key.CPF -> regexCPF.matches(request.keyValue)
            KeyManagerRequest.Key.PHONE -> regexPHONE.matches(request.keyValue)
            KeyManagerRequest.Key.EMAIL -> regexEMAIL.matches(request.keyValue)
            KeyManagerRequest.Key.RANDOM -> true
            else -> false
        }
    }
}