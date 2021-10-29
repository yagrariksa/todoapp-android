package com.todo.app.models

import com.google.gson.annotations.SerializedName

data class DefaultResponse<out T : Any?>(
    @SerializedName("status")
    val status: Boolean? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("data")
    val data: T? = null
)
