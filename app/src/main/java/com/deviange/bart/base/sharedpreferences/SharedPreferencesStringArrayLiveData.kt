package com.deviange.bart.base.sharedpreferences

import android.content.SharedPreferences
import com.google.gson.Gson

class SharedPreferenceStringArrayLiveData(sharedPrefs: SharedPreferences, val gson: Gson, key: String, defValue: Array<String>) :
    SharedPreferenceLiveData<Array<String>>(sharedPrefs, key, defValue) {
    override fun getValueFromPreferences(key: String, defValue: Array<String>): Array<String> {
        val jsonValue = sharedPrefs.getString(key, null) ?: return defValue
        return gson.fromJson(jsonValue, Array<String>::class.java)
    }
}

fun SharedPreferences.stringArrayLiveData(gson: Gson, key: String, defValue: Array<String>): SharedPreferenceLiveData<Array<String>> {
    return SharedPreferenceStringArrayLiveData(this, gson, key, defValue)
}