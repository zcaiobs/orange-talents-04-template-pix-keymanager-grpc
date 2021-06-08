package br.com.orange.service

import br.com.orange.domain.PixRequest
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client

@Client("http://localhost:8082/api/v1/pix/keys")
interface ExternalService {
    @Post(produces = ["application/xml"], consumes = ["application/xml"])
    fun createNewKey(pixRequest: PixRequest): Any
}