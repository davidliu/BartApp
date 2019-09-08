package com.deviange.bart.activity

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import com.deviange.bart.R
import com.deviange.bart.activity.dagger.BaseActivityModule
import com.deviange.bart.navigation.getTopFragment
import dagger.Binds
import dagger.Module
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        onStackUpdate()

        supportFragmentManager.addOnBackStackChangedListener {
            onStackUpdate()
        }
    }


    fun onStackUpdate() {
        supportActionBar?.run {
            title = supportFragmentManager.getTopFragment()?.getTitle() ?: getString(R.string.app_name)
            setDisplayHomeAsUpEnabled(supportFragmentManager.backStackEntryCount > 0)

        }
        app_bar_layout.setExpanded(true, true)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()
        if (id == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    @Module(includes = [BaseActivityModule::class])
    abstract class MainActivityModule {
        @Binds
        abstract fun activity(activity: MainActivity): Activity
    }
}
