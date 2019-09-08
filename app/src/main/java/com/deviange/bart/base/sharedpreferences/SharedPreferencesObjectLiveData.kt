package com.deviange.bart.base.sharedpreferences

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesObjectLiveData<T>(sharedPrefs: SharedPreferences, val gson: Gson, key: String, defValue: T) :
    SharedPreferenceLiveData<T>(sharedPrefs, key, defValue) {

    override fun getValueFromPreferences(key: String, defValue: T): T {
        val jsonValue = sharedPrefs.getString(key, null) ?: return defValue
        val typeOfT = object : TypeToken<Array<T>>() {}.type

        return gson.fromJson(jsonValue, typeOfT)
    }
}

fun <T> SharedPreferences.objectLiveData(gson: Gson, key: String, defValue: T):
        SharedPreferenceLiveData<T> {
    return SharedPreferencesObjectLiveData(this, gson, key, defValue)
}