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

        override fun toString(): String {
            return "Owner(name='$name', taxIdNumber='$taxIdNumber', type=$type)"
        }
    }

    override fun toString(): String {
        return "PixResponse(keyType=$keyType, key='$key', bankAccount='$bankAccount', " +
                "owner='$owner', createdAt=$createdAt)"
    }


}