package com.chydee.atom360testapp.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chydee.atom360testapp.data.dataStoreArchitecture.NetworkRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainActivityViewModelFactory(private val repo: NetworkRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            return MainActivityViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class")
    }
}