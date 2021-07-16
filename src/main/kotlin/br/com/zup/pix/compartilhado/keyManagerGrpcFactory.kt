package br.com.zup.pix.compartilhado

import br.com.zup.KeyManagerConsultaGrpcServiceGrpc
import br.com.zup.KeyManagerGrpcServiceGrpc
import br.com.zup.KeyManagerRemoveGrpcServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class keyManagerGrpcFactory(@GrpcChannel("keyManager") val channel: ManagedChannel) {

    @Singleton
    fun registraChave() = KeyManagerGrpcServiceGrpc.newBlockingStub(channel)
    @Singleton
    fun removeChave()   = KeyManagerRemoveGrpcServiceGrpc.newBlockingStub(channel)
    @Singleton
    fun consultaChave() = KeyManagerConsultaGrpcServiceGrpc.newBlockingStub(channel)
}