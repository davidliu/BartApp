package com.deviange.bart.stations

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidliu.bartapi.BartApi
import com.davidliu.bartapi.stations.StationMeta
import com.deviange.bart.dagger.viewmodel.ViewModelAssistedFactory
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.launch

class StationsViewModel
@AssistedInject
constructor(
    private val bartApi: BartApi,
    @Assisted private val handle: SavedStateHandle
) : ViewModel() {


    val stations = MutableLiveData<List<StationMeta>>()
    @AssistedInject.Factory
    interface Factory : ViewModelAssistedFactory<StationsViewModel>

    init{
        refresh()
    }

    fun refresh(){
        viewModelScope.launch {
            val response = bartApi.getAllStationsSuspend()
            val stationList = response.root.stations.stationList
                .sortedBy { meta -> meta.name }

            stations.postValue(stationList)
        }
    }
}