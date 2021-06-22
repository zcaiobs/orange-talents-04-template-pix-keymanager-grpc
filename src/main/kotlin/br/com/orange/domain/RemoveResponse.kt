package br.com.orange.domain

import io.micronaut.core.annotation.Introspected

@Introspected
data class RemoveResponse(val key: String, val participant: String, val deletedAt: String)