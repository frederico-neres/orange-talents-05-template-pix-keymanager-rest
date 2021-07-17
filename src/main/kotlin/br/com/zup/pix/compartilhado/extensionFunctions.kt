package br.com.zup.pix.compartilhado

import br.com.zup.RegistrarChavePixResponse
import br.com.zup.RemoveChavePixResponse
import br.com.zup.pix.chave.RegistraChaveResponse
import br.com.zup.pix.chave.RemoveChaveResponse
import java.util.*

fun RegistrarChavePixResponse.toRegistraChaveResponse(): RegistraChaveResponse {

    return RegistraChaveResponse(
        pixId = UUID.fromString(this.pixID),
        clienteId = UUID.fromString(this.clienteId),
    )
}


fun RemoveChavePixResponse.toRemoveChaveResponse(): RemoveChaveResponse {

    return RemoveChaveResponse(
        pixId = UUID.fromString(this.pixID),
        clienteId = UUID.fromString(this.clienteId),
    )
}