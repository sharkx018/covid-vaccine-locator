package com.example.vaccinechecker.data.api

import android.content.Context
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

object ApiService {

    private var retrofit: Retrofit? = null
    private lateinit var apiService: BackendApis
    public var base_url = "https://cdn-api.co-vin.in/"

    fun getApiService(context: Context): BackendApis {
        if (!::apiService.isInitialized) {

            // Initialize ApiService if not initialized yet
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okhttpClient(context))
                    .build()
                apiService = retrofit?.create(BackendApis::class.java)!!
            }
        }


        return apiService
    }

    /**
     * Initialize OkhttpClient with our interceptor
     */
    private fun okhttpClient(context: Context): OkHttpClient {

        var interceptor = HttpLoggingInterceptor()

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectionSpecs(
                Arrays.asList(
                    ConnectionSpec.MODERN_TLS,
                    ConnectionSpec.COMPATIBLE_TLS
                )
            )
            .followRedirects(true)
            .followSslRedirects(true)
            .retryOnConnectionFailure(true)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .cache(null)
            .build()

    }
}