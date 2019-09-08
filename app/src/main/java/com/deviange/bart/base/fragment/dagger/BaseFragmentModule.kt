package com.deviange.bart.base.fragment.dagger

import androidx.fragment.app.Fragment
import dagger.Module
import dagger.Provides

/**
 * Provides some default binds for other modules.
 */
@Module
object BaseFragmentModule {

    @Provides
    @JvmStatic
    fun fragmentManager(fragment: Fragment) =
        fragment.requireFragmentManager()


}