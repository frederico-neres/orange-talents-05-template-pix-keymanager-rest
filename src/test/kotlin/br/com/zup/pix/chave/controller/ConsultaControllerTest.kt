package br.com.zup.pix.chave.controller

import br.com.zup.*
import br.com.zup.pix.chave.ListaChaveResponse
import br.com.zup.pix.compartilhado.KeyManagerGrpcFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
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
    val CHAVE_CELULAR = "63993057245"
    val TIPO_CHAVE_PIX_EMAIL = TipoChavePix.EMAIL
    val TIPO_CHAVE_PIX_CELULAR = TipoChavePix.CELULAR
    val INSTITUICAO = "ITAÚ UNIBANCO S.A."
    val NOME_DO_TITULAR = "Fred"
    val CPF_DO_TITULAR = "02467781054"
    val AGENCIA = "0001"
    val NUMERODACONTA = "291900"

    lateinit var  clienteId: String
    lateinit var pixId: String

    @BeforeEach
    internal fun setUp() {
        clienteId = UUID.randomUUID().toString()
        pixId = UUID.randomUUID().toString()
    }

    @Test
    internal fun `DEVE consultar e trazer os dados de uma chave Pix`() {

        val respostaGrpc = consultaChavePixRequest(clienteId, pixId)

        Mockito.`when`(clientGrpc.consulta(Mockito.any()))
            .thenReturn(respostaGrpc)

        val request = HttpRequest.GET<Any>("/api/v1/clientes/${clienteId}/pix/${pixId}")
        val response = client.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())

    }

    @Test
    internal fun `DEVE listar todas as chaves Pix do cliente`() {

        val chaveEmail = detalhesChave(CHAVE_EMAIL, TIPO_CHAVE_PIX_EMAIL)
        val chaveCelular = detalhesChave(CHAVE_CELULAR, TIPO_CHAVE_PIX_CELULAR)

         val listaChavePixResponse = ListaChavePixResponse.newBuilder()
            .setClientId(clienteId)
            .addAllChavesPix(listOf(chaveEmail, chaveCelular))
            .build()

        Mockito.`when`(clientGrpc.consultaTodas(Mockito.any()))
            .thenReturn(listaChavePixResponse)

        val request = HttpRequest.GET<Any>("/api/v1/clientes/${clienteId}/pix")
        val response = client.toBlocking().exchange(request, ListaChaveResponse::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
        assertEquals(2, (response.body() as ListaChaveResponse).chavesPix.size)

    }

    private fun detalhesChave(chave: String, tipoChave: TipoChavePix): ListaChavePixResponse.DetalhesChave {
        return ListaChavePixResponse.DetalhesChave.newBuilder()
            .setPixId(UUID.randomUUID().toString())
            .setTipoChave(tipoChave)
            .setChave(chave)
            .setTipoConta(TipoConta.CONTA_CORRENTE)
            .setCriadoEm(
                LocalDateTime.now().let {
                    val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                    Timestamp.newBuilder()
                        .setSeconds(createdAt.epochSecond)
                        .setNanos(createdAt.nano)
                        .build()
                }
            )
            .build()
    }

    private fun consultaChavePixRequest(clienteId: String, pixId: String) =
        ConsultaChavePixResponse.newBuilder()
            .setClientId(clienteId)
            .setPixId(pixId)
            .setChave(ConsultaChavePixResponse.ChavePix
                .newBuilder()
                .setTipoDeChave(TIPO_CHAVE_PIX_EMAIL)
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