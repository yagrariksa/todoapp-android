package com.todo.app.models

import com.google.gson.annotations.SerializedName

data class Acc(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("api_token")
    val token: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("email")
    val email: String? = null,
)
