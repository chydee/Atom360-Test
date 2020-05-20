package com.chydee.atom360testapp.utils


sealed class Resource<out T> {
    class Loading<out T> : Resource<T>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Failure<out T>(val message: String) : Resource<T>()
}