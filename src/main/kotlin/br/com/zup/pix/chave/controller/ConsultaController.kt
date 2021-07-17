package br.com.zup.pix.chave.controller

import br.com.zup.ConsultaChavePixRequest
import br.com.zup.KeyManagerConsultaGrpcServiceGrpc
import br.com.zup.ListaChavePixRequest
import br.com.zup.pix.chave.ConsultaChaveResponse
import br.com.zup.pix.chave.ListaChaveResponse
import br.com.zup.pix.compartilhado.toConsultaChaveResponse
import br.com.zup.pix.compartilhado.toListaChaveResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable

@Controller("/api/v1/clientes/{clienteId}")
class ConsultaController(
    private val keyManagerConsulta: KeyManagerConsultaGrpcServiceGrpc.KeyManagerConsultaGrpcServiceBlockingStub
) {

    @Get("/pix/{pixId}")
    fun consulta(@PathVariable clienteId: String, @PathVariable pixId: String): HttpResponse<ConsultaChaveResponse> {

        val response = keyManagerConsulta.consulta(ConsultaChavePixRequest.newBuilder()
            .setPixId(ConsultaChavePixRequest.FiltroPorPix.newBuilder()
                .setPixId(pixId)
                .setClientId(clienteId)
                .build())
            .build())

        return HttpResponse.ok(response.toConsultaChaveResponse())
    }

    @Get("/pix")
    fun consultaTodas(@PathVariable clienteId: String): HttpResponse<ListaChaveResponse> {
        val responseKeyManagerConsulta = keyManagerConsulta.consultaTodas(ListaChavePixRequest.newBuilder()
            .setClientId(clienteId)
            .build())

        val response = responseKeyManagerConsulta.toListaChaveResponse()
        return HttpResponse.ok(response)
    }
}