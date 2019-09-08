package com.deviange.bart.dagger.modules

import com.davidliu.bartapi.common.ApiConstants
import com.deviange.bart.dagger.InjectionNames
import dagger.Module
import dagger.Provides
import java.net.URL
import javax.inject.Named

@Module
object ServerUrlModule {

    @Provides
    @JvmStatic
    @Named(InjectionNames.SERVER_URL)
    fun serverUrl(): URL {
        return URL(ApiConstants.API_BASE_URL)
    }
}