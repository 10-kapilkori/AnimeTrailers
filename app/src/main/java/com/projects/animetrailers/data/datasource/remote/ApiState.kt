package com.projects.animetrailers.data.datasource.remote

sealed class ApiState<out T> {
    data object Loading : ApiState<Nothing>()
    data class Success<T>(val data: T) : ApiState<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : ApiState<Nothing>()
}

