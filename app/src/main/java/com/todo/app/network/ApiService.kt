package com.todo.app.network

import com.todo.app.models.Acc
import com.todo.app.models.DefaultResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("login")
    @FormUrlEncoded
    suspend fun login(
        @Field("email") email: String? = null,
        @Field("password") password: String? = null
    ): Response<DefaultResponse<Acc>>

    @GET("check")
    suspend fun check(
        @Header("Authorization") pref: String? = null,
        @Header("Accept") accept: String = "application/json",
    ): Response<DefaultResponse<Acc>>
}