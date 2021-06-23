package br.com.orange.domain

import io.micronaut.core.annotation.Introspected
import java.time.LocalDateTime

@Introspected
data class PixResponse(
    val keyType: KeyType?,
    val clienteId: String?,
    val key: String,
    val bankAccount: BankAccount?,
    val owner: Owner?,
    val createdAt: LocalDateTime?
) {
    enum class KeyType {
        CPF, CNPJ, PHONE, EMAIL, RANDOM
    }

    class BankAccount(
        var clienteId: String?,
        var nameBank: String?,
        var participant: String,
        var branch: String,
        var accountNumber: String,
        accountType: AccountType
    ) {
        var accountType: String = accountType.type

        enum class AccountType(var type: String) {
            CACC("CONTA_CORRENTE"),
            SVGS("CONTA_POUPANCA")
        }

        fun toBank(name: String, clienteId: String): Pix.BankAccount {
            return Pix.BankAccount(clienteId, name, this.participant, this.branch, this.accountNumber, this.accountType)
        }
    }

    class Owner(var name: String, var taxIdNumber: String) {
        val type: PersonType = PersonType.NATURAL_PERSON

        enum class PersonType {
            NATURAL_PERSON,
            LEGAL_PERSON
        }

        fun toOwner(): Pix.Owner {
            return Pix.Owner(this.name, this.taxIdNumber)
        }
    }

    fun toPix(name: String, clienteId: String): Pix {
        return Pix(
            this.keyType!!.name,
            this.key,
            this.bankAccount!!.toBank(name, clienteId),
            this.owner!!.toOwner(),
            this.createdAt
        )
    }
}