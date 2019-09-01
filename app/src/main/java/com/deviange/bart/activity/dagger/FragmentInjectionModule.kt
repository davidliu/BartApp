package com.deviange.bart.activity.dagger

import com.deviange.bart.dagger.scope.FragmentScope
import com.deviange.bart.stations.StationsFragment
import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Module(
    includes = [
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class
    ]
)
abstract class FragmentInjectionModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun stationsFragment(): StationsFragment
}