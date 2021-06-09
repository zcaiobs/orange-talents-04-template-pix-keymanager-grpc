package br.com.orange.domain

import java.time.LocalDateTime

class PixResponse {
    val keyType: KeyType? = null
    val key: String = ""
    val bankAccount: BankAccount? = null
    val owner: Owner? = null
    val createdAt: LocalDateTime? = null

    enum class KeyType {
        CPF, CNPJ, PHONE, EMAIL, RANDOM
    }

    class BankAccount(var participant: String, var branch: String, var accountNumber: String, accountType: AccountType) {
        var accountType: String = accountType.type

        enum class AccountType(var type: String) {
            CACC("CONTA_CORRENTE"),
            SVGS("CONTA_POUPANCA")
        }

        fun toBank(): Pix.BankAccount {
            return Pix.BankAccount(this.participant, this.branch, this.accountNumber, this.accountType)
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

        fun toOwner(): Pix.Owner {
            return Pix.Owner(this.name, this.taxIdNumber)
        }

        override fun toString(): String {
            return "Owner(name='$name', taxIdNumber='$taxIdNumber', type=$type)"
        }
    }

    override fun toString(): String {
        return "PixResponse(keyType=$keyType, key='$key', bankAccount='$bankAccount', " +
                "owner='$owner', createdAt=$createdAt)"
    }

    fun toPix(): Pix {
        return Pix(this.keyType!!.name, this.key, this.bankAccount!!.toBank(), this.owner!!.toOwner(), this.createdAt)
    }
}