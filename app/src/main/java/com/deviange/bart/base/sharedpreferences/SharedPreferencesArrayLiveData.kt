package com.deviange.bart.base.sharedpreferences

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferenceArrayLiveData<T>(sharedPrefs: SharedPreferences, val gson: Gson, key: String, defValue: Array<T>) :
    SharedPreferenceLiveData<Array<T>>(sharedPrefs, key, defValue) {

    override fun getValueFromPreferences(key: String, defValue: Array<T>): Array<T> {
        val jsonValue = sharedPrefs.getString(key, null) ?: return defValue
        val typeOfT = object : TypeToken<Array<T>>() {}.type

        return gson.fromJson(jsonValue, typeOfT)
    }
}

fun <T> SharedPreferences.arrayLiveData(gson: Gson, key: String, defValue: Array<T>):
        SharedPreferenceLiveData<Array<T>> {
    return SharedPreferenceArrayLiveData(this, gson, key, defValue)
}