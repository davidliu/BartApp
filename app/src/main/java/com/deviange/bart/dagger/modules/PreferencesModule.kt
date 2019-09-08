package com.deviange.bart.dagger.modules

import android.content.Context
import android.content.SharedPreferences
import com.deviange.bart.dagger.InjectionNames
import com.deviange.bart.preferences.BartSharedPreferences
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
object PreferencesModule {
    @Provides
    @JvmStatic
    @Named(InjectionNames.PREFS)
    fun sharedPrefs(context: Context) = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    @Provides
    @JvmStatic
    fun bartSharedPreferences(
        @Named(InjectionNames.PREFS) sharedPreferences: SharedPreferences,
        gson: Gson
    ) = BartSharedPreferences(sharedPreferences, gson)

    @Provides
    @JvmStatic
    fun favoriteStationsPreference(bartSharedPreferences: BartSharedPreferences) =
        bartSharedPreferences.favoriteStations
}