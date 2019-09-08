package com.deviange.bart.preferences

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.deviange.bart.base.sharedpreferences.*
import com.google.gson.Gson

class BartSharedPreferences(
    private val sharedPrefs: SharedPreferences,
    private val gson: Gson
) {

    val favoriteStations by lazy {
        FavoriteStationsPreference(
            liveDataGetter(gson, FAVORITE_STATIONS, arrayOf()),
            setter(FAVORITE_STATIONS)
        )
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified T> liveDataGetter(gson: Gson, key: String, defValue: T): () -> LiveData<T> {
        return {
            when (T::class) {
                Boolean::class -> sharedPrefs.booleanLiveData(key, defValue as Boolean)
                String::class -> sharedPrefs.stringLiveData(key, defValue as String)
                Int::class -> sharedPrefs.intLiveData(key, defValue as Int)
                Float::class -> sharedPrefs.floatLiveData(key, defValue as Float)
                Set::class -> sharedPrefs.stringSetLiveData(key, defValue as Set<String>)
                Array<String>::class -> sharedPrefs.stringArrayLiveData(gson, key, defValue as Array<String>)
                else -> throw IllegalArgumentException("Bad preference type!")
            } as LiveData<T>
        }
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified T> setter(key: String): (T) -> Unit {
        return { value ->
            when (T::class) {
                Boolean::class -> sharedPrefs.edit().putBoolean(key, value as Boolean).apply()
                String::class -> sharedPrefs.edit().putString(key, value as String).apply()
                Int::class -> sharedPrefs.edit().putInt(key, value as Int).apply()
                Float::class -> sharedPrefs.edit().putFloat(key, value as Float).apply()
                Set::class -> sharedPrefs.edit().putStringSet(key, value as Set<String>).apply()
                Array<String>::class -> sharedPrefs.edit().putString(key, gson.toJson(value)).apply()
                else -> throw IllegalArgumentException("Bad preference type!")
            }
        }
    }

    companion object {
        const val FAVORITE_STATIONS = "favorite_stations"
    }

}

