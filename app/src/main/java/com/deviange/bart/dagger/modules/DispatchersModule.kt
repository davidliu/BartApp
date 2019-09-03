package com.deviange.bart.dagger.modules

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
object DispatchersModule {

    const val MAIN = "MAIN"
    const val IO = "io"
    const val COMPUTATION = "computation"

    @Provides
    @Named(MAIN)
    @JvmStatic
    fun mainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @Named(IO)
    @JvmStatic
    fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Named(COMPUTATION)
    @JvmStatic
    fun computationDispatcher(): CoroutineDispatcher = Dispatchers.Default

}