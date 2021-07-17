package br.com.zup.pix.chave.controller

import br.com.zup.KeyManagerRemoveGrpcServiceGrpc
import br.com.zup.RemoveChavePixRequest
import br.com.zup.pix.chave.RemoveChaveResponse
import br.com.zup.pix.compartilhado.toRemoveChaveResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import java.util.*

@Controller("/api/v1/clientes/{clienteId}")
class RemoveController(
    private val keyManagerRemove: KeyManagerRemoveGrpcServiceGrpc.KeyManagerRemoveGrpcServiceBlockingStub
) {

    @Delete("/pix/{pixId}")
    fun remove(@PathVariable clienteId: UUID, @PathVariable pixId: UUID): HttpResponse<RemoveChaveResponse> {

        val response = keyManagerRemove.remove(RemoveChavePixRequest.newBuilder()
            .setPixID(pixId.toString())
            .setClienteId(clienteId.toString())
            .build())

        return HttpResponse.ok(response.toRemoveChaveResponse())
    }
}