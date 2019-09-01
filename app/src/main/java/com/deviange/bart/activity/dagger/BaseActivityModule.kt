package com.deviange.bart.activity.dagger

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides

/**
 * Provides some default binds for other modules.
 */
@Module
object BaseActivityModule {

    @Provides
    @JvmStatic
    fun appCompatActivity(activity: Activity): AppCompatActivity =
        activity as AppCompatActivity
}