package com.cx.vulnerablekotlinapp.api.service

import com.cx.vulnerablekotlinapp.api.model.Account
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
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
            val certificatePinner = CertificatePinner.Builder()
                    .add("192.169.1.87:8080", "sha256/5Kl14sIBRoArZ8ujwNLWoLOI1QmsvE58nmXTO/9GSJw=")
                    .build()

            val client: OkHttpClient = OkHttpClient.Builder()
                    .certificatePinner(certificatePinner)
                    .build()

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://192.168.1.87:8080")
                    .client(client)
                    .build()

            return retrofit.create(Client::class.java)
        }
    }
}