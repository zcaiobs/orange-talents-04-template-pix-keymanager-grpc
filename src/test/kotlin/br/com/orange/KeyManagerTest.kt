package br.com.orange

import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.inject.Inject

class KeyManagerTest {

    @Test
    fun testItWorks() {
        val test = Teste.RANDOM.name
        println(test)
    }
    enum class Teste {
        RANDOM
    }

}
