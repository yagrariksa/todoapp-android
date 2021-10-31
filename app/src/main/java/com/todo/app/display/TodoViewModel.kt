package com.todo.app.display

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.todo.app.models.Acc
import com.todo.app.models.DefaultResponse
import com.todo.app.models.Todo
import com.todo.app.network.ApiFactory
import com.todo.app.network.RequestState
import com.todo.app.network.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class TodoViewModel : ViewModel() {

    private val _status = MutableLiveData<RequestState>()
    val status: LiveData<RequestState>
        get() = _status

    private val _data = MutableLiveData<DefaultResponse<List<Todo>>>()
    val data: LiveData<DefaultResponse<List<Todo>>>
        get() = _data

    private val _one = MutableLiveData<DefaultResponse<Todo>>()
    val one: LiveData<DefaultResponse<Todo>>
        get() = _one

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private var job = Job()
    private val uiScope = CoroutineScope(job + Dispatchers.Main)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun getAll(
        day: Int,
        pref: String
    ) {
        _status.postValue(RequestState.REQUEST_START)
        uiScope.launch {
            try {
                when (val response = ApiFactory.getAll(day, pref)) {
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
                            ) as DefaultResponse<List<Todo>>
                        )
                    }
                }
            } catch (e: Exception) {
                _status.postValue(RequestState.REQUEST_ERROR)
                _error.postValue(e.localizedMessage)
            }
        }
    }

    fun getOne(
        id: Int,
        pref: String
    ) {
        _status.postValue(RequestState.REQUEST_START)
        uiScope.launch {
            try {
                when (val response = ApiFactory.getOne(id, pref)) {
                    is Result.Success -> {
                        _status.postValue(RequestState.REQEUST_END)
                        _one.postValue(response.data)
                    }
                    is Result.Error -> {
                        _status.postValue(RequestState.REQUEST_ERROR)
                        _one.postValue(
                            Gson().fromJson(
                                response.exception,
                                DefaultResponse::class.java
                            ) as DefaultResponse<Todo>
                        )
                    }
                }
            } catch (e: Exception) {
                _status.postValue(RequestState.REQUEST_ERROR)
                _error.postValue(e.localizedMessage)
            }
        }
    }

    fun create(
        name: String,
        url: String,
        day: Int,
        pref: String
    ) {
        _status.postValue(RequestState.REQUEST_START)
        uiScope.launch {
            try {
                when (val response = ApiFactory.create(name, url, day, pref)) {
                    is Result.Success -> {
                        _status.postValue(RequestState.REQEUST_END)
                        _one.postValue(response.data)
                    }
                    is Result.Error -> {
                        _status.postValue(RequestState.REQUEST_ERROR)
                        _one.postValue(
                            Gson().fromJson(
                                response.exception,
                                DefaultResponse::class.java
                            ) as DefaultResponse<Todo>
                        )
                    }
                }
            } catch (e: Exception) {
                _status.postValue(RequestState.REQUEST_ERROR)
                _error.postValue(e.localizedMessage)
            }
        }
    }

    fun update(
        name: String,
        url: String,
        day: Int,
        id: Int,
        pref: String
    ) {
        _status.postValue(RequestState.REQUEST_START)
        uiScope.launch {
            try {
                when (val response = ApiFactory.update(name, url, day, id, pref)) {
                    is Result.Success -> {
                        _status.postValue(RequestState.REQEUST_END)
                        _one.postValue(response.data)
                    }
                    is Result.Error -> {
                        _status.postValue(RequestState.REQUEST_ERROR)
                        _one.postValue(
                            Gson().fromJson(
                                response.exception,
                                DefaultResponse::class.java
                            ) as DefaultResponse<Todo>
                        )
                    }
                }
            } catch (e: Exception) {
                _status.postValue(RequestState.REQUEST_ERROR)
                _error.postValue(e.localizedMessage)
            }
        }
    }

    fun delete(
        id: Int,
        pref: String
    ) {
        _status.postValue(RequestState.REQUEST_START)
        uiScope.launch {
            try {
                when (val response = ApiFactory.delete(
                    id = id, prefs = pref
                )) {
                    is Result.Success -> {
                        _status.postValue(RequestState.REQEUST_END)
                        _one.postValue(response.data)
                    }
                    is Result.Error -> {
                        _status.postValue(RequestState.REQUEST_ERROR)
                        _one.postValue(
                            Gson().fromJson(
                                response.exception,
                                DefaultResponse::class.java
                            ) as DefaultResponse<Todo>
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