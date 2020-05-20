package com.chydee.atom360testapp.data

import com.chydee.atom360testapp.data.pojo.Summary
import retrofit2.http.GET

interface CovidService {

    @GET("countries")
    suspend fun getSummary(): Summary
}