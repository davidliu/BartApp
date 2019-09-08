package com.deviange.bart.base.sharedpreferences

import androidx.lifecycle.LiveData

abstract class PreferenceProvider<T>(
    val getter: () -> LiveData<T>,
    val setter: (T) -> Unit
) {
    val value by lazy { getter() }
    fun setValue(value: T) {
        setter(value)
    }
}