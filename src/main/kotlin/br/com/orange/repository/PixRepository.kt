package br.com.orange.repository

import br.com.orange.domain.Pix
import io.micronaut.context.annotation.Value
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import io.micronaut.http.annotation.PathVariable
import org.hibernate.annotations.Parameter
import java.util.*

@Repository
interface PixRepository: JpaRepository<Pix, String> {
    @Query("select p from Pix p where p.id = :id and p.bankAccount.clienteId = :cliente")
    fun findPix(id: String, cliente: String): Optional<Pix>
    fun existsByKeyvalue(key: String): Boolean
    fun findByKeyvalue(key: String): Pix
}