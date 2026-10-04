package com.wesley.locacaocarros.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("posts")
    suspend fun sincronizarVeiculo(
        @Body dados: SincronizacaoRequest
    ): SincronizacaoResponse

    @POST("posts")
    suspend fun sincronizarLocacao(
        @Body dados: SincronizacaoRequest
    ): SincronizacaoResponse
}