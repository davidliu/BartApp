package com.deviange.bart.dagger.modules

import com.deviange.bart.activity.MainActivity
import com.deviange.bart.activity.dagger.ViewModelAssistedFactoriesModule
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
abstract class DaggerAndroidInjectionModule {
    @ContributesAndroidInjector(
        modules = [
            ViewModelAssistedFactoriesModule::class
        ]
    )
    abstract fun mainActivity(): MainActivity
}