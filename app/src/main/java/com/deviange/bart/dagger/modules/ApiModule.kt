package com.deviange.bart.dagger.modules

import com.davidliu.bartapi.BartApi
import com.davidliu.bartapi.gson.BooleanSerializer
import com.deviange.bart.BuildConfig
import com.github.ajalt.timberkt.Timber.v
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
    fun okhttp(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        v { message }
                    }

                })
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(loggingInterceptor)
            }
        }.build()
    }

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