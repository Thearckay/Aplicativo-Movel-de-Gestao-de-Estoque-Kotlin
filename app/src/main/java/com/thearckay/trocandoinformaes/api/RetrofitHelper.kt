package com.thearckay.trocandoinformaes.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {

    companion object {
        private const val BASE_URL = "http://10.0.2.2:8080"

        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val authService: AuthService by lazy {
            retrofit.create(AuthService::class.java)
        }
    }
}