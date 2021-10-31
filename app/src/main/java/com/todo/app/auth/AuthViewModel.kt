package com.todo.app.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.todo.app.models.Acc
import com.todo.app.models.DefaultResponse
import com.todo.app.network.ApiFactory
import com.todo.app.network.RequestState
import com.todo.app.network.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class AuthViewModel : ViewModel() {

    private val _status = MutableLiveData<RequestState>()
    val status: LiveData<RequestState>
        get() = _status

    private val _data = MutableLiveData<DefaultResponse<Acc>>()
    val data: LiveData<DefaultResponse<Acc>>
        get() = _data

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private var job = Job()
    private val uiScope = CoroutineScope(job + Dispatchers.Main)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun doLogin(
        email: String,
        password: String,
    ) {
        _status.postValue(RequestState.REQUEST_START)
        uiScope.launch {
            try {
                when (val response = ApiFactory.login(email, password)) {
                    is Result.Success -> {
                        _status.postValue(RequestState.REQEUST_END)
                        _data.postValue(response.data)
                    }
                    is Result.Error -> {
                        _status.postValue(RequestState.REQUEST_ERROR)
                        _data.postValue(
                            Gson().fromJson(
                                response.exception,
                                DefaultResponse::class.java
                            ) as DefaultResponse<Acc>
                        )
                    }
                }
            } catch (e: Exception) {
                _status.postValue(RequestState.REQUEST_ERROR)
                _error.postValue(e.localizedMessage)
            }
        }
    }

    fun authCheck(
        pref: String,
    ) {
        _status.postValue(RequestState.REQUEST_START)
        uiScope.launch {
            try {
                when (val response = ApiFactory.check(pref)) {
                    is Result.Success -> {
                        _status.postValue(RequestState.REQEUST_END)
                        _data.postValue(response.data)
                    }
                    is Result.Error -> {
                        _status.postValue(RequestState.REQUEST_ERROR)
                        _data.postValue(
                            Gson().fromJson(
                                response.exception,
                                DefaultResponse::class.java
                            ) as DefaultResponse<Acc>
                        )
                    }
                }

            } catch (e: Exception) {
                _status.postValue(RequestState.REQUEST_ERROR)
                _error.postValue(e.localizedMessage)
            }
        }
    }


    fun register(
        name: String,
        email: String,
        password: String,
    ) {
        _status.postValue(RequestState.REQUEST_START)
        Log.e(
            "IDENTITY", name + " " + email + " " + password
        )
        uiScope.launch {
            try {
                when (val response = ApiFactory.register(name, email, password)) {
                    is Result.Success -> {
                        _status.postValue(RequestState.REQEUST_END)
                        _data.postValue(response.data)
                    }
                    is Result.Error -> {
                        _status.postValue(RequestState.REQUEST_ERROR)
                        _data.postValue(
                            Gson().fromJson(
                                response.exception,
                                DefaultResponse::class.java
                            ) as DefaultResponse<Acc>
                        )
                    }
                }
            } catch (e: Exception) {
                _status.postValue(RequestState.REQUEST_ERROR)
                _error.postValue(e.localizedMessage)
            }
        }
    }
}