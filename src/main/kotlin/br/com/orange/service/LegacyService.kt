package br.com.orange.service

import br.com.orange.domain.ClienteRequest
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

@Client("http://localhost:9091/api/v1/clientes/")
interface LegacyService {
    @Get("/{id}/contas")
    fun findClientById(@PathVariable id: String, @QueryValue tipo: String?): ClienteRequest
}