package br.com.orange.validator

import br.com.orange.KeyManagerRequest
import br.com.orange.repository.PixRepository
import io.micronaut.context.annotation.Context

@Context
class PixValidator(val pixRepository: PixRepository) {
    fun isValid(request: KeyManagerRequest?): Boolean {
        val regexCPF = "^[0-9]{11}$".toRegex()
        val regexPHONE = "^\\+[1-9][0-9]\\d{1,14}$".toRegex()
        val regexEMAIL = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+).*".toRegex()

        if (request!!.clientId.isNotBlank()) {
            if (request.keyType.name == "CPF") {
                return regexCPF.matches(request.keyValue)
            }
            if (request.keyType.name == "PHONE") {
                return regexPHONE.matches(request.keyValue)
            }
            if (request.keyType.name == "EMAIL") {
                return regexEMAIL.matches(request.keyValue)
            }
            if (request.keyType.name == "RANDOM") {
                return true
            }
        }
        return false
    }
}