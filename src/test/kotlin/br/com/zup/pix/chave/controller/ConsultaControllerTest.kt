package br.com.zup.pix.chave.controller

import br.com.zup.ConsultaChavePixResponse
import br.com.zup.KeyManagerConsultaGrpcServiceGrpc
import br.com.zup.TipoChavePix
import br.com.zup.TipoConta
import br.com.zup.pix.compartilhado.KeyManagerGrpcFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ConsultaControllerTest {

    @field:Inject
    lateinit var clientGrpc:KeyManagerConsultaGrpcServiceGrpc.KeyManagerConsultaGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    val CHAVE_EMAIL = "fred@gmail.com"
    val INSTITUICAO = "ITAÚ UNIBANCO S.A."
    val NOME_DO_TITULAR = "Fred"
    val CPF_DO_TITULAR = "02467781054"
    val AGENCIA = "0001"
    val NUMERODACONTA = "291900"

    @Test
    internal fun `DEVE consultar e trazer os dados de uma chave pix`() {

        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val respostaGrpc = carregaChavePixResponse(clienteId, pixId)

        Mockito.`when`(clientGrpc.consulta(Mockito.any()))
            .thenReturn(respostaGrpc)

        val request = HttpRequest.GET<Any>("/api/v1/clientes/${clienteId}/pix/${pixId}")
        val response = client.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())

    }

    private fun carregaChavePixResponse(clienteId: String, pixId: String) =
        ConsultaChavePixResponse.newBuilder()
            .setClientId(clienteId)
            .setPixId(pixId)
            .setChave(ConsultaChavePixResponse.ChavePix
                .newBuilder()
                .setTipoDeChave(TipoChavePix.EMAIL)
                .setChave(CHAVE_EMAIL)
                .setConta(
                    ConsultaChavePixResponse.ChavePix.ContaInfo.newBuilder()
                        .setTipoDeConta(TipoConta.CONTA_CORRENTE)
                        .setInstituicao(INSTITUICAO)
                        .setNomeDoTitular(NOME_DO_TITULAR)
                        .setCpfDoTitular(CPF_DO_TITULAR)
                        .setAgencia(AGENCIA)
                        .setNumeroDaConta(NUMERODACONTA)
                        .build()
                )
                .setCriadaEm(LocalDateTime.now().let {
                    val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                    Timestamp.newBuilder()
                        .setSeconds(createdAt.epochSecond)
                        .setNanos(createdAt.nano)
                        .build()
                })
            ).build()

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory {
        @Singleton
        fun stubMock() = Mockito.mock(KeyManagerConsultaGrpcServiceGrpc
            .KeyManagerConsultaGrpcServiceBlockingStub::class.java)
    }
}