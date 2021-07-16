package br.com.zup.pix.chave.controller

import br.com.zup.KeyManagerGrpcServiceGrpc
import br.com.zup.RegistrarChavePixResponse
import br.com.zup.pix.chave.RegistraChaveRequest
import br.com.zup.pix.chave.RegistraChaveResponse
import br.com.zup.pix.chave.TipoChave
import br.com.zup.pix.chave.TipoConta
import br.com.zup.pix.compartilhado.KeyManagerGrpcFactory
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import io.grpc.Status
import io.micronaut.http.client.exceptions.HttpClientResponseException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach


@MicronautTest
internal class RegistraControllerTest(

) {

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @field:Inject
    lateinit var clientGrpc: KeyManagerGrpcServiceGrpc.KeyManagerGrpcServiceBlockingStub

    lateinit var clienteId: String
    lateinit var pixId: String

    @BeforeEach
    internal fun setUp() {
        Mockito.reset(clientGrpc)
        clienteId = UUID.randomUUID().toString()
        pixId = UUID.randomUUID().toString()
    }

    @Test
    fun `DEVE cadastrar uma chave Pix`() {

        val requestRegistraChave = registraChaveRequest()

        val request = HttpRequest.POST("/api/v1/clientes/${clienteId}/pix", requestRegistraChave)

        Mockito.`when`(this.clientGrpc.registra(
            requestRegistraChave.toRegistrarChavePixRequest(clienteId)
        )).thenReturn(
            RegistrarChavePixResponse.newBuilder()
                .setPixID(pixId)
                .setClienteId(clienteId).build()
        )

        val response = client.toBlocking().exchange(request, RegistraChaveResponse::class.java)
        val responseBody = response.body()

        assertEquals(HttpStatus.CREATED.code, response.status.code)
        assertEquals("/api/v1/clientes/${clienteId}/pix/${pixId}", response.header("Location"))
        assertEquals(pixId, responseBody.pixId.toString())
        assertEquals(clienteId, responseBody.clienteId.toString())
    }

    @Test
    fun `Nao deve cadastrar uma chave Pix quando cliente inexistente`() {

        val request = HttpRequest.POST("/pix/${clienteId}/registra", registraChaveRequest())

        Mockito.`when`(this.clientGrpc.registra(registraChaveRequest()
            .toRegistrarChavePixRequest(clienteId)))
            .thenThrow(StatusRuntimeException(Status.NOT_FOUND))

        val response = assertThrows(HttpClientResponseException::class.java){
            client.toBlocking().exchange(request, RegistraChaveRequest::class.java) }

        assertNotNull(response)
        assertEquals(HttpStatus.NOT_FOUND, response.status)
    }

    fun registraChaveRequest(): RegistraChaveRequest {
        return RegistraChaveRequest(
            tipoChave = TipoChave.CPF,
            chave = "38997280741",
            tipoConta = TipoConta.CONTA_CORRENTE
        )
    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory {
        @Singleton
        fun stubMock() = Mockito.mock(KeyManagerGrpcServiceGrpc.
        KeyManagerGrpcServiceBlockingStub::class.java)
    }
}