package com.cx.vulnerablekotlinapp.api.service

import com.cx.vulnerablekotlinapp.api.model.Account
import com.cx.vulnerablekotlinapp.helpers.PreferenceHelper
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface Client {
    @POST("accounts")
    fun signup (@Body data: Account): Call<Void>

    companion object {
        fun create(): Client {
            val hostname: String = PreferenceHelper.getString("ip_address", "127.0.0.1")
            val port: String = PreferenceHelper.getString("port", "8080")
            val baseUrl: String = "http://${hostname}:${port}"

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(baseUrl)
                    .build()

            return retrofit.create(Client::class.java)
        }
    }
}