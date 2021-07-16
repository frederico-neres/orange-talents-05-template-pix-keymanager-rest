package br.com.zup.pix.chave.controller

import br.com.zup.KeyManagerGrpcServiceGrpc
import br.com.zup.pix.chave.RegistraChaveRequest
import br.com.zup.pix.chave.RegistraChaveResponse
import br.com.zup.pix.compartilhado.toRegistraChaveResponse
import br.com.zup.pix.validator.ValidUUID
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import java.util.*
import javax.validation.Valid


@Validated
@Controller("/api/v1/clientes/{clienteId}")
class RegistraController(private val keyManagerGrpcFactory: KeyManagerGrpcServiceGrpc.KeyManagerGrpcServiceBlockingStub) {

    @Post("/pix")
    fun registra(@PathVariable @ValidUUID clienteId: UUID,
                 @Body @Valid registraChaveRequest: RegistraChaveRequest): HttpResponse<RegistraChaveResponse> {

        val request = registraChaveRequest.toRegistrarChavePixRequest(clienteId.toString())
        val GrpcchavePixResponse = keyManagerGrpcFactory.registra(request)
        val response = GrpcchavePixResponse.toRegistraChaveResponse()

        return HttpResponse.created(response, location(
            clienteId = response.clienteId.toString(),
            pixId = response.pixId.toString()
        ))
    }

    private fun location(clienteId: String, pixId: String) = HttpResponse
        .uri("/api/v1/clientes/${clienteId}/pix/${pixId}")
}


