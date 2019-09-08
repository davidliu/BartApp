package com.deviange.bart.preferences

import androidx.lifecycle.LiveData
import com.deviange.bart.base.sharedpreferences.PreferenceProvider

class FavoriteStationsPreference(
    getter: () -> LiveData<Array<FavoriteStation>>,
    setter: (Array<FavoriteStation>) -> Unit
) : PreferenceProvider<Array<FavoriteStation>>(getter, setter)

data class FavoriteStation(
    val id: String,
    val name: String
)