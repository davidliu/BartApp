package com.deviange.bart.stations.estimates

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistry
import com.davidliu.bartapi.BartApi
import com.davidliu.bartapi.common.Direction
import com.davidliu.bartapi.estimated.EstimateDepartureTime
import com.deviange.bart.livedata.CombinedLiveData
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class StationEstimatesViewModel
@AssistedInject
constructor(
    private val bartApi: BartApi,
    @Assisted private val station: String,
    @Assisted private val handle: SavedStateHandle,
    @Assisted private val savedStateKey: String,
    @Assisted private val savedStateRegistry: SavedStateRegistry
) : ViewModel() {

    val northBoundEstimates: MutableLiveData<List<EstimateDepartureTime>>
    val southBoundEstimates: MutableLiveData<List<EstimateDepartureTime>>

    val estimates: CombinedLiveData<List<EstimateDepartureTime>, List<EstimateDepartureTime>, Pair<List<EstimateDepartureTime>?, List<EstimateDepartureTime>?>>

    init {
        val northKey = estimatesKey(station, Direction.NORTHBOUND)
        val southKey = estimatesKey(station, Direction.SOUTHBOUND)

        northBoundEstimates = handle.getLiveData(estimatesKey(station, Direction.NORTHBOUND))
        southBoundEstimates = handle.getLiveData(estimatesKey(station, Direction.SOUTHBOUND))

        estimates = CombinedLiveData(northBoundEstimates, southBoundEstimates) { north, south -> Pair(north, south) }

        if (!handle.contains(northKey) || !handle.contains(southKey)) {
            refresh()
        }
    }

    fun refresh(onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            val jobs = listOf(
                async {
                    val northResponse = bartApi.getEstimatedDepartureTimesSuspend(station, null, Direction.NORTHBOUND)
                    val allEstimates = northResponse.root.station[0].routes
                        .flatMap { it.estimates }
                        .toList()
                    northBoundEstimates.postValue(allEstimates)
                }, async {
                    val southResponse = bartApi.getEstimatedDepartureTimesSuspend(station, null, Direction.SOUTHBOUND)
                    val allEstimates = southResponse.root.station[0].routes
                        .flatMap { it.estimates }
                        .toList()
                    southBoundEstimates.postValue(allEstimates)
                }
            )
            jobs.awaitAll()
            onComplete?.invoke()

        }
    }

    override fun onCleared() {
        savedStateRegistry.unregisterSavedStateProvider(savedStateKey)
    }

    companion object {
        const val ESTIMATES_KEY = "estimates"

        private fun estimatesKey(station: String, direction: Direction) = ESTIMATES_KEY + station + direction.name
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(station: String, handle: SavedStateHandle, savedStateKey: String, savedStateRegistry: SavedStateRegistry): StationEstimatesViewModel
    }
}