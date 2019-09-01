package com.deviange.bart.dagger

import com.deviange.bart.BartApplication
import com.deviange.bart.dagger.modules.ActivityInjectionModule
import com.deviange.bart.dagger.modules.ApiModule
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApiModule::class,
        ActivityInjectionModule::class
    ]
)
interface AppComponent : AndroidInjector<BartApplication> {

}