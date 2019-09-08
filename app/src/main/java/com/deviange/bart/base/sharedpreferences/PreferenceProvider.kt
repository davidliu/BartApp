package com.deviange.bart.base.sharedpreferences

import androidx.lifecycle.LiveData

abstract class PreferenceProvider<T>(
    private val getter: () -> LiveData<T>,
    private val setter: (T) -> Unit
) {
    val liveData by lazy { getter() }
    fun setValue(value: T) = setter(value)
}