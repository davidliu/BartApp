package com.deviange.bart.dagger

import com.deviange.bart.BartApplication
import com.deviange.bart.dagger.modules.ApiModule
import com.deviange.bart.dagger.modules.DaggerAndroidInjectionModule
import dagger.Component
import dagger.android.AndroidInjector

@Component(
    modules = [
        ApiModule::class,
        DaggerAndroidInjectionModule::class
    ]
)
interface AppComponent : AndroidInjector<BartApplication> {

}