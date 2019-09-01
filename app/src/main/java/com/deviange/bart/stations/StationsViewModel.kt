package com.deviange.bart.stations

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.davidliu.bartapi.BartApi
import com.deviange.bart.dagger.viewmodel.ViewModelAssistedFactory
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject

class StationsViewModel
@AssistedInject
constructor(
    private val bartApi: BartApi,
    @Assisted private val handle: SavedStateHandle
) : ViewModel() {

    @AssistedInject.Factory
    interface Factory : ViewModelAssistedFactory<StationsViewModel>


}