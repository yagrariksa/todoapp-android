package com.todo.app.network

import com.todo.app.models.Acc
import com.todo.app.models.DefaultResponse
import com.todo.app.models.Todo
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

    @POST("register")
    @FormUrlEncoded
    suspend fun register(
        @Field("name") name: String? = null,
        @Field("email") email: String? = null,
        @Field("password") password: String? = null,
    ): Response<DefaultResponse<Acc>>

    @GET("todo")
    suspend fun getAll(
        @Query("day") day: Int? = null,
        @Header("Authorization") pref: String? = null,
        @Header("Accept") accept: String = "application/json",
    ): Response<DefaultResponse<List<Todo>>>

    @POST("todo")
    @FormUrlEncoded
    suspend fun create(
        @Field("name") name: String? = null,
        @Field("url") url: String? = null,
        @Field("day") day: Int? = null,
        @Header("Authorization") pref: String? = null,
        @Header("Accept") accept: String = "application/json",
    ): Response<DefaultResponse<Todo>>

    @PUT("todo")
    suspend fun update(
        @Query("name") name: String? = null,
        @Query("url") url: String? = null,
        @Query("day") day: Int? = null,
        @Query("id") id: Int? = null,
        @Header("Authorization") pref: String? = null,
        @Header("Accept") accept: String = "application/json",
    ): Response<DefaultResponse<Todo>>

    @DELETE("todo")
    suspend fun delete(
        @Query("id") id: Int? = null,
        @Header("Authorization") pref: String? = null,
        @Header("Accept") accept: String = "application/json",
    ): Response<DefaultResponse<Todo>>

    @GET("todo/one")
    suspend fun getOne(
        @Query("id") id: Int? = null,
        @Header("Authorization") pref: String? = null,
        @Header("Accept") accept: String = "application/json",
    ): Response<DefaultResponse<Todo>>
}