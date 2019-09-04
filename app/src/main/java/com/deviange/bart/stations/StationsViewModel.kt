package com.deviange.bart.stations

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidliu.bartapi.BartApi
import com.deviange.bart.dagger.modules.DispatchersModule
import com.deviange.bart.dagger.viewmodel.ViewModelAssistedFactory
import com.deviange.bartdb.stations.StationDao
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Named

class StationsViewModel
@AssistedInject
constructor(
    private val bartApi: BartApi,
    private val stationsDao: StationDao,
    @Named(DispatchersModule.IO)
    private val ioDispatcher: CoroutineDispatcher,
    @Assisted private val handle: SavedStateHandle
) : ViewModel() {

    val stations = stationsDao.observeAllStations()
    val isRefreshing = MutableLiveData<Boolean>(false)

    fun refresh() {
        viewModelScope.launch(ioDispatcher) {
            isRefreshing.postValue(true)
            val response = bartApi.getAllStationsSuspend()
            val stationList = response.root.stations.stationList
            stationsDao.upsertList(stationList)
            isRefreshing.postValue(false)
        }
    }

    @AssistedInject.Factory
    interface Factory : ViewModelAssistedFactory<StationsViewModel>
}