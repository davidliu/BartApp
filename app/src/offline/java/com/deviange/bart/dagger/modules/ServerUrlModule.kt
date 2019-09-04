package com.deviange.bart.dagger.modules

import android.os.StrictMode
import com.deviange.bart.dagger.InjectionNames
import com.deviange.bartapi.mock.BartMockDispatcher
import dagger.Module
import dagger.Provides
import okhttp3.mockwebserver.MockWebServer
import java.net.URL
import javax.inject.Named
import javax.inject.Singleton

@Module
object ServerUrlModule {

    @Singleton
    @JvmStatic
    @Provides
    fun mockWebServer(): MockWebServer {

        return MockWebServer().apply {
            dispatcher = BartMockDispatcher()

            val threadPolicy = StrictMode.getThreadPolicy()
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX)
            start()
            StrictMode.setThreadPolicy(threadPolicy)
        }
    }

    @JvmStatic
    @Provides
    @Named(InjectionNames.serverUrl)
    fun serverUrl(server: MockWebServer): URL {
        val threadPolicy = StrictMode.getThreadPolicy()
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX)
        val url = server.url("/api/").toUrl()
        StrictMode.setThreadPolicy(threadPolicy)

        return url
    }
}