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
    class Instituicao(var nome: String, var ispb: String)
    class Titular(var id: String, var nome: String, var cpf: String)
}