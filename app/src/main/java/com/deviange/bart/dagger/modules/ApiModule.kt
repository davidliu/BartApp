package com.deviange.bart.dagger.modules

import com.davidliu.bartapi.BartApi
import com.davidliu.bartapi.gson.BooleanSerializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
object ApiModule {

    @Provides
    @Reusable
    @JvmStatic
    fun gson(): Gson =
        GsonBuilder()
            .registerTypeAdapter(Boolean::class.java, BooleanSerializer())
            .create()

    @Provides
    @Reusable
    @JvmStatic
    fun okhttp(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    @JvmStatic
    @Reusable
    fun retrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("https://api.bart.gov/api/")
            .build()

    @Provides
    @JvmStatic
    @Reusable
    fun bartApi(retrofit: Retrofit): BartApi = retrofit.create(BartApi::class.java)

}