package br.com.zup.pix.chave.controller

import br.com.zup.KeyManagerRemoveGrpcServiceGrpc
import br.com.zup.RemoveChavePixResponse
import br.com.zup.pix.chave.RemoveChaveResponse
import br.com.zup.pix.compartilhado.KeyManagerGrpcFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RemoveControllerTest {

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @field:Inject
    lateinit var clientGrpc: KeyManagerRemoveGrpcServiceGrpc.KeyManagerRemoveGrpcServiceBlockingStub

    @Test
    internal fun `DEVE remover um chave Pix existente`() {

        val pixId = UUID.randomUUID().toString()
        val clienteId = UUID.randomUUID().toString()

        val removeChavePixResponse = RemoveChavePixResponse.newBuilder()
            .setPixID(pixId)
            .setClienteId(clienteId)
            .build()

        Mockito.`when`(clientGrpc.remove(Mockito.any()))
            .thenReturn(removeChavePixResponse)

        val request = HttpRequest.DELETE<Any>("/api/v1/clientes/${clienteId}/pix/${pixId}")
        val response = client.toBlocking().exchange(request, RemoveChaveResponse::class.java)
        val responseBody = response.body()

        assertEquals(HttpStatus.OK, response.status)
        assertEquals(pixId, responseBody.pixId.toString())
        assertEquals(clienteId, responseBody.clienteId.toString())

    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class RemoveMockitoStubFactory {
        @Singleton
        fun stubMockRemove() = Mockito.mock(KeyManagerRemoveGrpcServiceGrpc
            .KeyManagerRemoveGrpcServiceBlockingStub::class.java)
    }
}