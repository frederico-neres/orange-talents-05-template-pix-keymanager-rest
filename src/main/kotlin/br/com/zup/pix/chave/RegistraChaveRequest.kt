package br.com.zup.pix.chave

import br.com.zup.RegistrarChavePixRequest
import br.com.zup.TipoChavePix
import br.com.zup.pix.validator.ValidaChavePix
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@ValidaChavePix
@Introspected
data class RegistraChaveRequest(
    @field:NotNull val tipoChave: TipoChave,
    @field:NotBlank val chave: String,
    @field:NotNull val tipoConta: TipoConta
) {

    fun toRegistrarChavePixRequest(clienteId: String): RegistrarChavePixRequest {
        return RegistrarChavePixRequest.newBuilder()
            .setClienteId(clienteId)
            .setTipo(TipoChavePix.valueOf(tipoChave.name))
            .setChave(chave)
            .setConta(br.com.zup.TipoConta.valueOf(tipoConta.name))
            .build()
    }
}
