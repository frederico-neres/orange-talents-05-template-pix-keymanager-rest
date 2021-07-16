package br.com.zup.pix.compartilhado

import br.com.zup.RegistrarChavePixResponse
import br.com.zup.pix.chave.RegistraChaveResponse
import java.util.*

fun RegistrarChavePixResponse.toRegistraChaveResponse(): RegistraChaveResponse {

    return RegistraChaveResponse(
        pixId = UUID.fromString(this.pixID),
        clienteId = UUID.fromString(this.clienteId),
    )
}