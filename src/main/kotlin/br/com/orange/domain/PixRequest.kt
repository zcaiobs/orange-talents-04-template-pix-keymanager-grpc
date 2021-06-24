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

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as BankAccount

            if (participant != other.participant) return false
            if (branch != other.branch) return false
            if (accountNumber != other.accountNumber) return false
            if (accountType != other.accountType) return false

            return true
        }

        override fun hashCode(): Int {
            var result = participant.hashCode()
            result = 31 * result + branch.hashCode()
            result = 31 * result + accountNumber.hashCode()
            result = 31 * result + accountType.hashCode()
            return result
        }

        override fun toString(): String {
            return "BankAccount(participant='$participant', branch='$branch', accountNumber='$accountNumber', accountType='$accountType')"
        }
    }

    class Owner(var name: String, var taxIdNumber: String) {
        val type: PersonType = PersonType.NATURAL_PERSON

        enum class PersonType {
            NATURAL_PERSON,
            LEGAL_PERSON
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Owner

            if (name != other.name) return false
            if (taxIdNumber != other.taxIdNumber) return false
            if (type != other.type) return false

            return true
        }

        override fun hashCode(): Int {
            var result = name.hashCode()
            result = 31 * result + taxIdNumber.hashCode()
            result = 31 * result + type.hashCode()
            return result
        }

        override fun toString(): String {
            return "Owner(name='$name', taxIdNumber='$taxIdNumber', type=$type)"
        }
    }
}