package com.deviange.bart.stations.estimates

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidliu.bartapi.BartApi
import com.davidliu.bartapi.estimated.EstimateDepartureTime
import com.deviange.bart.dagger.scope.FragmentScope
import com.deviange.bart.dagger.viewmodel.ViewModelAssistedFactory
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.launch

class StationsEstimatesViewModel
@FragmentScope
@AssistedInject
constructor(
    private val bartApi: BartApi,
    @Assisted private val handle: SavedStateHandle
) : ViewModel() {

    val estimates = MutableLiveData<List<EstimateDepartureTime>>()

    @AssistedInject.Factory
    interface Factory : ViewModelAssistedFactory<StationsEstimatesViewModel>

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val response = bartApi.getAllStationsSuspend()
            val stationList = response.root.stations.stationList
                .sortedBy { meta -> meta.name }

//            estimates.postValue(stationList)
        }
    }
}