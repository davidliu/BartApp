package com.deviange.bart.dagger

import com.deviange.bart.BartApplication
import com.deviange.bart.dagger.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ApiModule::class,
        ActivityInjectionModule::class,
        DatabaseModule::class,
        DispatchersModule::class
    ]
)
interface AppComponent : AndroidInjector<BartApplication> {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: BartApplication
        ): AppComponent
    }
}