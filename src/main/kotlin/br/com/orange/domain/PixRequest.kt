package br.com.orange.domain

import br.com.orange.KeyManagerRequest

class PixRequest() {
    var keyType: String = ""
    var key: String = ""
    var bankAccount: BankAccount? = null
    var owner: Owner? = null

    constructor(clienteRequest: ClienteRequest, request: KeyManagerRequest): this() {
        this.keyType = request.keyType.name
        this.key = request.keyValue
        this.bankAccount = clienteRequest.instituicao?.let {
            BankAccount(it.ispb, clienteRequest.agencia, clienteRequest.numero, BankAccount.AccountType.valueOf(request.accountType.name)) }
        this.owner = clienteRequest.titular?.let { Owner(it.nome, it.cpf) }
    }

    class BankAccount(var participant: String, var branch: String, var accountNumber: String, accountType: AccountType) {
        var accountType: String = accountType.type

        enum class AccountType(var type: String) {
            CONTA_CORRENTE("CACC"),
            CONTA_POUPANCA("SVGS")
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
        return "PixRequest(keyType='$keyType', key='$key', bankAccount=$bankAccount, owner=$owner)"
    }
}