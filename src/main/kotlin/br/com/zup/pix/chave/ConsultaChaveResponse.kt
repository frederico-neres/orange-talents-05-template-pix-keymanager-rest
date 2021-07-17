package br.com.zup.pix.chave

import br.com.zup.ConsultaChavePixResponse
import io.micronaut.core.annotation.Introspected
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Introspected
class ConsultaChaveResponse(
    consultaChavePixResponse: ConsultaChavePixResponse
) {
    val pixId: String = consultaChavePixResponse.pixId
    val clienteId: String = consultaChavePixResponse.clientId
    val chave: ChaveResponse = ChaveResponse(consultaChavePixResponse.chave)
}

class ChaveResponse(chave: ConsultaChavePixResponse.ChavePix) {
    val tipoDeChave: TipoChave = TipoChave.valueOf(chave.tipoDeChave.name)
    val chave: String = chave.chave;
    val criadaEm = chave.criadaEm.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }
    var conta: ContaResponse = ContaResponse(chave.conta)
}

class ContaResponse(conta: ConsultaChavePixResponse.ChavePix.ContaInfo) {

    val tipoDeConta: TipoConta = TipoConta.valueOf(conta.tipoDeConta.name)
    val instituicao: String = conta.instituicao
    val nomeDoTitular: String  = conta.nomeDoTitular
    val cpfDoTitular: String  = conta.cpfDoTitular
    val agencia: String  = conta.agencia
    val numeroDaConta: String  = conta.numeroDaConta
}