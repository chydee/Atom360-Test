package com.chydee.atom360testapp.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chydee.atom360testapp.data.dataStoreArchitecture.NetworkRepository
import com.chydee.atom360testapp.data.pojo.Summary
import com.chydee.atom360testapp.utils.Resource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainActivityViewModel @Inject constructor(var repo: NetworkRepository) : ViewModel() {

    private val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.IO)

    private val _covidSummary = MutableLiveData<Resource<Summary>>()
    val covidSummary: LiveData<Resource<Summary>>
        get() = _covidSummary

    fun getSummary() {
        _covidSummary.value = Resource.Loading()
        scope.launch {
            repo.getSummary()
                .catch { cause: Throwable ->
                    cause.printStackTrace()
                    _covidSummary.postValue(Resource.Failure(cause.message!!))
                }
                .collect {
                    _covidSummary.postValue(Resource.Success(it))
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}