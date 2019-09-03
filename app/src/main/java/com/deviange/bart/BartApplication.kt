package com.deviange.bart

import com.deviange.bart.dagger.DaggerAppComponent
import com.github.ajalt.timberkt.Timber
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class BartApplication : DaggerApplication() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(timber.log.Timber.DebugTree())
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.factory().create(this)

}