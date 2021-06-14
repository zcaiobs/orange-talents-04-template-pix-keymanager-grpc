package br.com.orange.domain

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
class Pix() {
    @Id
    var id: String? = UUID.randomUUID().toString()
    @Enumerated(EnumType.STRING)
    var keyType: KeyType? = null
    var keyvalue: String? = null
    @Embedded
    var bankAccount: BankAccount? = null
    @Embedded
    var owner: Owner? = null
    var createdAt: LocalDateTime? = null

    constructor(
        keyType: String, key: String?, bankAccount: BankAccount?,
        owner: Owner?, createdAt: LocalDateTime?) : this() {
        this.keyType =  KeyType.valueOf(keyType)
        this.keyvalue = key
        this.bankAccount = bankAccount
        this.owner = owner
        this.createdAt = createdAt
    }

    enum class KeyType {
        CPF, CNPJ, PHONE, EMAIL, RANDOM
    }

    @Embeddable
    class BankAccount() {
        var clienteId: String? = null
        var nameBank: String? = null
        var accountType: String? = null
        var participant: String? = null
        var branch: String? = null
        var accountNumber: String? = null

        constructor(clienteId: String ,name: String, participant: String, branch: String, accountNumber: String, accountType: String) : this() {
            this.clienteId = clienteId
            this.nameBank = name
            this.accountType = accountType
            this.participant = participant
            this.branch = branch
            this.accountNumber = accountNumber
        }

        enum class AccountType(var type: String) {
            CONTA_CORRENTE("CACC"),
            CONTA_POUPANCA("SVGS")
        }

        override fun toString(): String {
            return "BankAccount(accountType=$accountType, participant=$participant, " +
                    "branch=$branch, accountNumber=$accountNumber)"
        }
    }

    @Embeddable
    class Owner() {
        var name: String? = null
        var taxIdNumber: String? = null

        @Enumerated(EnumType.STRING)
        var type: PersonType = PersonType.NATURAL_PERSON

        constructor(name: String?, taxIdNumber: String?) : this() {
            this.name = name
            this.taxIdNumber = taxIdNumber
        }

        enum class PersonType {
            NATURAL_PERSON,
            LEGAL_PERSON
        }

        override fun toString(): String {
            return "Owner(name=$name, taxIdNumber=$taxIdNumber, type=$type)"
        }
    }

    override fun toString(): String {
        return "Pix(id=$id, keyType=$keyType, keyvalue=$keyvalue," +
                " bankAccount=$bankAccount, owner=$owner, createdAt=$createdAt)"
    }
}