package br.com.orange.domain

import io.micronaut.core.annotation.Introspected

@Introspected
data class ClienteRequest(
    val tipo: String,
    val instituicao: Instituicao?,
    val agencia: String,
    val numero: String,
    val titular: Titular?
) {
    class Instituicao(var nome: String, var ispb: String) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Instituicao

            if (nome != other.nome) return false
            if (ispb != other.ispb) return false

            return true
        }

        override fun hashCode(): Int {
            var result = nome.hashCode()
            result = 31 * result + ispb.hashCode()
            return result
        }

        override fun toString(): String {
            return "Instituicao(nome='$nome', ispb='$ispb')"
        }
    }

    class Titular(var id: String, var nome: String, var cpf: String) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Titular

            if (id != other.id) return false
            if (nome != other.nome) return false
            if (cpf != other.cpf) return false

            return true
        }

        override fun hashCode(): Int {
            var result = id.hashCode()
            result = 31 * result + nome.hashCode()
            result = 31 * result + cpf.hashCode()
            return result
        }

        override fun toString(): String {
            return "Titular(id='$id', nome='$nome', cpf='$cpf')"
        }
    }
}