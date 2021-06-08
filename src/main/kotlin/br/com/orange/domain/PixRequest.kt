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
            BankAccount(it.ispb, clienteRequest.agencia, clienteRequest.numero, clienteRequest.tipo) }
        this.owner = clienteRequest.titular?.let { Owner(it.nome, it.cpf) }
    }

    class BankAccount(var participant: String, var branch: String, var accountNumber: String, var accountType: String) {
        override fun toString(): String {
            return "BankAccount(participant='$participant', branch='$branch', accountNumber='$accountNumber', accountType='$accountType')"
        }
    }

    class Owner(var name: String, var taxIdNumber: String) {
        val type: Type = Type.NATURAL_PERSON

        enum class Type {
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