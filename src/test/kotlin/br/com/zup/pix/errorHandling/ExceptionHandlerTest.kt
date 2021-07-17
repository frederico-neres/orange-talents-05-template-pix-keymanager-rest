package br.com.zup.pix.errorHandling

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@MicronautTest
internal class ExceptionHandlerTest {

    val requestGenerica = HttpRequest.GET<Any>("/")

    @Test
    internal fun `DEVE retornar 404 quando statusException for not found`() {

        val mensagem = "nao encontrado"
        val notFoundException = StatusRuntimeException(
            Status.NOT_FOUND
                .withDescription(mensagem)
        )

        val resposta = ExceptionHandler().handle(requestGenerica, notFoundException)

        assertEquals(HttpStatus.NOT_FOUND, resposta.status)
        assertNotNull(resposta.body())
        assertEquals(mensagem, (resposta.body() as ExceptionHandlerResponse).message)
    }

    @Test
    internal fun `DEVE retornar 422 quando statusException for already existis`() {

        val mensagem = "chave ja existente"
        val alreadyExistsException = StatusRuntimeException(
            Status.ALREADY_EXISTS
                .withDescription(mensagem)
        )

        val resposta = ExceptionHandler().handle(requestGenerica, alreadyExistsException)

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resposta.status)
        assertNotNull(resposta.body())
        assertEquals(mensagem, (resposta.body() as ExceptionHandlerResponse).message)
    }

    @Test
    internal fun `DEVE retornar 400 quando statusException for invalid argument`() {

        val mensagem = "Dados da requisição estão inválidos"
        val invalidArgumentException = StatusRuntimeException(Status.INVALID_ARGUMENT)

        val resposta = ExceptionHandler().handle(requestGenerica, invalidArgumentException)

        assertEquals(HttpStatus.BAD_REQUEST, resposta.status)
        assertNotNull(resposta.body())
    }

    @Test
    internal fun `DEVE retornar 500 quando qualquer outro erro for lancado`() {

        val internalException = StatusRuntimeException(Status.INTERNAL)

        val resposta = ExceptionHandler().handle(requestGenerica, internalException)


        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resposta.status)
        assertNotNull(resposta.body())
    }
}