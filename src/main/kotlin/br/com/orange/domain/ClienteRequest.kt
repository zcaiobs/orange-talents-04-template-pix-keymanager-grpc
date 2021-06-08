package br.com.orange.domain

class ClienteRequest {
    val tipo: String = ""
    val instituicao: Instituicao? = null
    val agencia: String = ""
    val numero: String = ""
    val titular: Titular? = null

    class Instituicao(var nome: String, var ispb: String) {
        override fun toString(): String {
            return "Instituicao(nome='$nome', ispb='$ispb')"
        }
    }

    class Titular(var id: String, var nome: String, var cpf: String) {
        override fun toString(): String {
            return "Titular(id='$id', nome='$nome', cpf='$cpf')"
        }
    }

    override fun toString(): String {
        return "ClienteRequest(tipo='$tipo', instituicao=$instituicao, agencia='$agencia'," +
                " numero='$numero', titular=$titular)"
    }
}