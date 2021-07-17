package br.com.zup.pix.chave

import br.com.zup.ListaChavePixResponse
import io.micronaut.core.annotation.Introspected
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Introspected
data class ListaChaveResponse(
    val clienteId: String,
    val chavesPix: List<DetalhesChaveResponse>
) {

    constructor(consultaChavePixResponse: ListaChavePixResponse):
            this(consultaChavePixResponse.clientId,
                consultaChavePixResponse.chavesPixList.map { DetalhesChaveResponse(it, consultaChavePixResponse.clientId) })

}

data class DetalhesChaveResponse(
    val pixId: String,
    val clienteId: String,
    val tipoDeChave: TipoChave,
    val chave: String,
    var tipoDaConta: TipoConta,
    val criadaEm: LocalDateTime
) {

    constructor(detalhesChave: ListaChavePixResponse.DetalhesChave, clienteId: String): this(
        pixId = detalhesChave.pixId,
        clienteId = clienteId,
        tipoDeChave = TipoChave.valueOf(detalhesChave.tipoChave.name),
        chave  = detalhesChave.chave,
        tipoDaConta = TipoConta.valueOf(detalhesChave.tipoConta.name),
        criadaEm = detalhesChave.criadoEm.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    })
}
