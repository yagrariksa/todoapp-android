package com.todo.app.models

import com.google.gson.annotations.SerializedName

data class Todo(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("url")
    val url: String? = null,

    @SerializedName("day")
    val day: Int? = null,

    @SerializedName("hour")
    val hour: Int? = null,

    @SerializedName("minute")
    val minute: Int? = null,

    )
