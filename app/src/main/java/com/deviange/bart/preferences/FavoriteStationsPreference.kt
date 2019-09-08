package com.deviange.bart.preferences

import androidx.lifecycle.LiveData
import com.deviange.bart.base.sharedpreferences.PreferenceProvider

class FavoriteStationsPreference(
    getter: () -> LiveData<Array<String>>,
    setter: (Array<String>) -> Unit
) : PreferenceProvider<Array<String>>(getter, setter)