package com.deviange.bart.stations.estimates

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistry
import com.davidliu.bartapi.BartApi
import com.davidliu.bartapi.common.Direction
import com.davidliu.bartapi.estimated.EstimatedRoute
import com.deviange.bart.livedata.CombinedLiveData
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
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

    val northRoutes: MutableLiveData<List<RouteDeparture>>
    val southRoutes: MutableLiveData<List<RouteDeparture>>

    val estimates: CombinedLiveData<List<RouteDeparture>, List<RouteDeparture>, Pair<List<RouteDeparture>?, List<RouteDeparture>?>>

    init {
        val northKey = estimatesKey(station, Direction.NORTHBOUND)
        val southKey = estimatesKey(station, Direction.SOUTHBOUND)

        northRoutes = handle.getLiveData(estimatesKey(station, Direction.NORTHBOUND))
        southRoutes = handle.getLiveData(estimatesKey(station, Direction.SOUTHBOUND))

        estimates = CombinedLiveData(northRoutes, southRoutes) { north, south -> Pair(north, south) }

        if (!handle.contains(northKey) || !handle.contains(southKey)) {
            refresh()
        }
    }

    fun refresh(onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            val response = bartApi.getEstimatedDepartureTimesSuspend(station)
            val station = response.root.station[0]
            val northRoutesList = station.routes
                .orEmpty()
                .filter { route -> Direction.fromString(route.estimates.firstOrNull()?.direction) == Direction.NORTHBOUND }
            val southRoutesList = station.routes
                .orEmpty()
                .filter { route -> Direction.fromString(route.estimates.firstOrNull()?.direction) == Direction.SOUTHBOUND }

            northRoutes.postValue(getFlatEstimatesList(northRoutesList))
            southRoutes.postValue(getFlatEstimatesList(southRoutesList))

            onComplete?.invoke()

        }
    }

    private fun getFlatEstimatesList(routes: List<EstimatedRoute>): List<RouteDeparture> {
        return routes
            .flatMap { route ->
                route.estimates
                    .map { estimate ->
                        RouteDeparture(route, estimate)
                    }
            }
            .sortedBy { routeDeparture ->
                routeDeparture.departure.minutes
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