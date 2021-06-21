package br.com.orange.service

import br.com.orange.domain.PixRequest
import br.com.orange.domain.PixResponse
import br.com.orange.domain.RemoveResponse
import br.com.orange.pix.RemoverChavePix
import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client

@Client("http://localhost:8082/api/v1/pix/keys")
interface ExternalService {

    @Get("/{key}", produces = ["application/xml"], consumes = ["application/json"])
    fun findKey(@PathVariable key: String): Any

    @Post(produces = ["application/xml"], consumes = ["application/json"])
    fun createNewKey(@Body pixRequest: PixRequest): PixResponse

    @Delete(value = "/{key}" ,produces = ["application/xml"], consumes = ["application/json"])
    fun deleteKey(@PathVariable key: String?, @Body deletePixKeyRequest: RemoverChavePix.DeletePixKeyRequest): RemoveResponse
}