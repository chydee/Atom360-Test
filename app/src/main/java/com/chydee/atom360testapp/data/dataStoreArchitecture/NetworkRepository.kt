package com.chydee.atom360testapp.data.dataStoreArchitecture

import com.chydee.atom360testapp.data.Client
import com.chydee.atom360testapp.data.pojo.Summary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ExperimentalCoroutinesApi
class NetworkRepository @Inject constructor(private val client: Client) {
    private val getService = client.service
    fun getSummary(): Flow<Summary> = flow {
        val response = getService.getSummary()
        emit(response)
    }.flowOn(Dispatchers.IO)
}