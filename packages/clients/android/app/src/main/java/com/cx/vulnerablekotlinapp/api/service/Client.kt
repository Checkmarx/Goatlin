package com.cx.vulnerablekotlinapp.api.service

import com.cx.vulnerablekotlinapp.api.model.Account
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
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://192.168.1.87:8080")
                    .build()

            return retrofit.create(Client::class.java)
        }
    }
}