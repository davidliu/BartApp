package com.deviange.bart.dagger.modules

import android.app.Application
import android.content.Context
import com.deviange.bart.BartApplication
import dagger.Binds
import dagger.Module

@Module
interface AppModule {
    @Binds
    fun application(application: BartApplication): Application

    @Binds
    fun context(application: Application): Context
}