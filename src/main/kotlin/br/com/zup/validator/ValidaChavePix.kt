package br.com.zup.pix.validator

import br.com.zup.pix.chave.RegistraChaveRequest
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import java.lang.annotation.Documented
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.reflect.KClass

@Documented
@Constraint(validatedBy = [ValidaChavePixValidator::class])
@Target(AnnotationTarget.CLASS)
@Retention(RUNTIME)
annotation class ValidaChavePix(
    val message: kotlin.String = "Chave pix inválida",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)

@Singleton
class ValidaChavePixValidator: ConstraintValidator<ValidaChavePix, RegistraChaveRequest> {
    override fun isValid(
        value: RegistraChaveRequest?,
        annotationMetadata: AnnotationValue<ValidaChavePix>,
        context: ConstraintValidatorContext,
    ): Boolean {
        val chaveRequest = value as RegistraChaveRequest
        return chaveRequest.tipoChave.validaChave(chaveRequest.chave)
    }

}