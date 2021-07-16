package br.com.zup.pix.chave

import io.micronaut.core.annotation.Introspected
import java.util.*

@Introspected
data class RegistraChaveResponse(
    val pixId: UUID,
    val clienteId: UUID,
)
