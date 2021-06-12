package br.com.orange.repository

import br.com.orange.domain.Pix
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface PixRepository: JpaRepository<Pix, String> {
    fun existsByKeyvalue(key: String): Boolean
}