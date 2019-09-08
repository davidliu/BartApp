package com.deviange.bart.base.sharedpreferences

import android.content.SharedPreferences
import com.github.ajalt.timberkt.v
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferenceArrayLiveData<T>(
    sharedPrefs: SharedPreferences,
    private val gson: Gson,
    key: String,
    defValue: Array<T>,
    private val klazz: Class<T>
) :
    SharedPreferenceLiveData<Array<T>>(sharedPrefs, key, defValue) {

    override fun getValueFromPreferences(key: String, defValue: Array<T>): Array<T> {
        val jsonValue = sharedPrefs.getString(key, null) ?: return defValue
        val typeOfT = TypeToken.get(klazz).type

        return gson.fromJson<Array<T>>(jsonValue, TypeToken.getArray(typeOfT).type);
    }
}

inline fun <reified T> SharedPreferences.arrayLiveData(gson: Gson, key: String, defValue: Array<T>):
        SharedPreferenceLiveData<Array<T>> {
    v { T::class.toString() }
    return SharedPreferenceArrayLiveData(this, gson, key, defValue, T::class.java)
}