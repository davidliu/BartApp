package com.deviange.bart.activity

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import com.deviange.bart.R
import com.deviange.bart.activity.dagger.BaseActivityModule
import dagger.Binds
import dagger.Module
import dagger.android.support.DaggerAppCompatActivity


class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setHomeButtonEnabled(true)

        supportFragmentManager.addOnBackStackChangedListener {
            supportActionBar?.setDisplayHomeAsUpEnabled(supportFragmentManager.backStackEntryCount > 0)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()
        if (id == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

@Module(includes = [BaseActivityModule::class])
abstract class MainActivityModule {
    @Binds
    abstract fun activity(activity: MainActivity): Activity
}