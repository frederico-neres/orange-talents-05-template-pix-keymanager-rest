package br.com.zup.pix.chave

import io.micronaut.core.annotation.Introspected
import java.util.*

@Introspected
data class RemoveChaveResponse(
    val pixId: UUID,
    val clienteId: UUID,
)
