package com.deviange.bart.activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.deviange.bart.R
import com.deviange.bart.activity.dagger.BaseActivityModule
import dagger.Binds
import dagger.Module
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.e("LOL", "asdf")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
    }
}

@Module(includes = [BaseActivityModule::class])
abstract class MainActivityModule {
    @Binds
    abstract fun activity(activity: MainActivity): Activity
}