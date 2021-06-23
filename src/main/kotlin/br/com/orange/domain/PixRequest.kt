package br.com.orange.domain

import br.com.orange.KeyManagerRequest
import io.micronaut.core.annotation.Introspected

@Introspected
data class PixRequest(
    var keyType: String,
    var key: String,
    var bankAccount: BankAccount?,
    var owner: Owner?
) {
    companion object {
        fun toRequest(clienteRequest: ClienteRequest, request: KeyManagerRequest): PixRequest {
            return PixRequest(
                request.keyType.name, request.keyValue,
                BankAccount(
                    clienteRequest.instituicao!!.ispb,
                    clienteRequest.agencia,
                    clienteRequest.numero,
                    BankAccount.AccountType.valueOf(request.accountType.name)
                ),
                Owner(clienteRequest.titular!!.nome, clienteRequest.titular.cpf)
            )
        }
    }

    class BankAccount(
        var participant: String,
        var branch: String,
        var accountNumber: String,
        accountType: AccountType
    ) {
        var accountType: String = accountType.type

        enum class AccountType(var type: String) {
            CONTA_CORRENTE("CACC"),
            CONTA_POUPANCA("SVGS")
        }
    }

    class Owner(var name: String, var taxIdNumber: String) {
        val type: PersonType = PersonType.NATURAL_PERSON

        enum class PersonType {
            NATURAL_PERSON,
            LEGAL_PERSON
        }
    }
}