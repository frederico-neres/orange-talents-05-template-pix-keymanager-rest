package br.com.zup.pix.chave

import br.com.zup.ListaChavePixResponse
import io.micronaut.core.annotation.Introspected
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Introspected
class ListaChaveResponse(
    consultaChavePixResponse: ListaChavePixResponse
) {
    val clienteId: String = consultaChavePixResponse.clientId
    val chavesPix = consultaChavePixResponse.chavesPixList.map { DetalhesChaveResponse(it) }
}

class DetalhesChaveResponse(detalhesChave: ListaChavePixResponse.DetalhesChave) {

    val clientId: String = ""
    val pixId: String = ""
    val tipoDeChave: TipoChave = TipoChave.valueOf(detalhesChave.tipoChave.name)
    val chave: String = detalhesChave.chave;
    var tipoDaConta: TipoConta = TipoConta.valueOf(detalhesChave.tipoConta.name)
    val criadaEm = detalhesChave.criadoEm.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }
}
