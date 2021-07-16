package br.com.zup.pix.errorHandling

import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.server.exceptions.ExceptionHandler
import javax.inject.Singleton
import io.grpc.Status
import io.micronaut.http.HttpStatus


@Singleton
class ExceptionHandler: ExceptionHandler<StatusRuntimeException, HttpResponse<Any>> {
    override fun handle(request: HttpRequest<*>?, exception: StatusRuntimeException): HttpResponse<Any> {


        val statusCode = exception.status.code
        val description = exception.status.description ?: ""

        val (httpStatus, httpMessage) = when(statusCode) {
            Status.NOT_FOUND.code -> Pair(HttpStatus.NOT_FOUND, description)
            Status.ALREADY_EXISTS.code -> Pair(HttpStatus.UNPROCESSABLE_ENTITY, description)
            Status.FAILED_PRECONDITION.code -> Pair(HttpStatus.UNPROCESSABLE_ENTITY, description)
            Status.INVALID_ARGUMENT.code -> Pair(HttpStatus.BAD_REQUEST, description)
            else -> Pair(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível registrar a chave Pix")
        }

        return HttpResponse.status<ExceptionHandlerResponse?>(httpStatus)
            .body(ExceptionHandlerResponse(
                message = httpMessage!!,
                httpStatus = "${httpStatus.code.toString()} : ${httpStatus}"
            ))
    }
}

data class ExceptionHandlerResponse(val message: String, val httpStatus: String)
