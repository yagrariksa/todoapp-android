package com.todo.app.network

import android.util.Log
import com.todo.app.models.Acc
import com.todo.app.models.DefaultResponse
import com.todo.app.models.Todo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

object ApiFactory {

    const val BASE_URL = "https://yagrariksa-todoapp.herokuapp.com/api/"

    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client = OkHttpClient().newBuilder()
        .addInterceptor(interceptor)
        .build()

    lateinit var apiService: ApiService

    init {
        makeService()
    }

    private fun makeService() {
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        this.apiService = retrofit.create(ApiService::class.java)
    }

    private suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): Result<T> {
        return try {
            val myResp = call.invoke()
            myResp.code()
            if (myResp.isSuccessful) {
                Result.Success(myResp.body()!!)
            } else {
                if (myResp.code() > 400) {
                    Log.i("RESPONSE_CODE", myResp.code().toString())
                }
                Result.Error(myResp.errorBody().toString() ?: "Something goes wrong")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Internet error runs")
        }
    }

    suspend fun login(
        email: String? = null,
        password: String? = null
    ): Result<DefaultResponse<Acc>> {
        return safeApiCall {
            apiService.login(email, password)
        }
    }

    suspend fun check(
        pref: String? = null
    ): Result<DefaultResponse<Acc>> {
        return safeApiCall {
            apiService.check("Bearer " + pref)
        }
    }

    suspend fun register(
        name: String? = null,
        email: String? = null,
        password: String? = null
    ): Result<DefaultResponse<Acc>> {
        return safeApiCall {
            apiService.register(name, email, password)
        }
    }

    suspend fun getAll(
        day: Int? = null,
        prefs: String? = null
    ): Result<DefaultResponse<List<Todo>>> {
        return safeApiCall {
            apiService.getAll(day, "Bearer " + prefs)
        }
    }

    suspend fun getOne(
        id: Int? = null,
        prefs: String? = null
    ): Result<DefaultResponse<Todo>> {
        return safeApiCall {
            apiService.getOne(id, "Bearer " + prefs)
        }
    }

    suspend fun create(
        name: String? = null,
        url: String? = null,
        day: Int? = null,
        prefs: String? = null,
        hour: Int? = null,
        minute: Int? = null,
    ): Result<DefaultResponse<Todo>> {
        return safeApiCall {
            apiService.create(
                name = name,
                url = url,
                day = day,
                pref = "Bearer " + prefs,
                hour = hour,
                minute = minute
            )
        }
    }

    suspend fun update(
        name: String? = null,
        url: String? = null,
        day: Int? = null,
        id: Int? = null,
        prefs: String? = null,
        hour: Int? = null,
        minute: Int? = null,
    ): Result<DefaultResponse<Todo>> {
        return safeApiCall {
            apiService.update(
                name = name,
                url = url,
                day = day,
                id = id,
                pref = "Bearer " + prefs,
                hour = hour,
                minute = minute
            )
        }
    }

    suspend fun delete(
        id: Int? = null,
        prefs: String? = null
    ): Result<DefaultResponse<Todo>> {
        return safeApiCall {
            apiService.delete(
                id = id,
                pref = "Bearer " + prefs
            )
        }
    }


}