package com.omnisoft.retrofitpractice.Retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor(baseUrl: String) {
    val api: API

    companion object {
        private var instance: RetrofitClient? = null
        fun getInstance(baseUrl: String): RetrofitClient? {
            if (instance == null) {
                instance = RetrofitClient(baseUrl)
            }
            return instance
        }
    }

    init {
        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(baseUrl).build()
        api = retrofit.create(API::class.java)
    }
}