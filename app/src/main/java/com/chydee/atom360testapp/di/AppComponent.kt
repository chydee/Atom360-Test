package com.chydee.atom360testapp.di

import android.content.Context
import com.chydee.atom360testapp.ui.MainActivity
import dagger.BindsInstance
import dagger.Component

@Component(modules = [NetworkModule::class])
interface AppComponent {
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(activity: MainActivity)
}