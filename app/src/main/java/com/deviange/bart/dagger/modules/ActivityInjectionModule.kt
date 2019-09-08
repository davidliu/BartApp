package com.deviange.bart.dagger.modules

import com.deviange.bart.activity.MainActivity
import com.deviange.bart.activity.dagger.FragmentInjectionModule
import com.deviange.bart.activity.dagger.ViewModelAssistedFactoriesModule
import com.deviange.bart.dagger.scope.ActivityScope
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
abstract class ActivityInjectionModule {
    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            ViewModelAssistedFactoriesModule::class,
            FragmentInjectionModule::class,
            MainActivity.MainActivityModule::class
        ]
    )
    abstract fun mainActivity(): MainActivity
}