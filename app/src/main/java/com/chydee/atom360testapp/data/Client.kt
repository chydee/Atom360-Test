package com.chydee.atom360testapp.data

import android.content.Context
import com.chydee.atom360testapp.utils.Atom360App
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class Client @Inject constructor(context: Context) {

    private var gsonConverterFactory: GsonConverterFactory? = null
    private val cacheSize = (5 * 1024 * 1024).toLong()
    private val cache = Cache(context.cacheDir, cacheSize)

    companion object {
        private const val baseUrl: String = "https://corona.lmao.ninja/v2/"
    }

    val okHttpClient = OkHttpClient.Builder()
        .cache(cache)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            // Get the request from the chain.
            var request = chain.request()
            request = if (Atom360App.hasNetwork(context)) {
                request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
            } else {
                request.newBuilder().header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                ).build()
            }
            chain.proceed(request)
        }

    private val gsonConverter: GsonConverterFactory
        get() {
            if (gsonConverterFactory == null) {
                gsonConverterFactory = GsonConverterFactory
                    .create(
                        GsonBuilder()
                            .setLenient()
                            .disableHtmlEscaping()
                            .create()
                    )
            }
            return gsonConverterFactory!!
        }

    val service: CovidService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient.build())
        .addConverterFactory(gsonConverter)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(CovidService::class.java)

}