package com.deviange.bart.stations.estimates

import androidx.lifecycle.*
import com.davidliu.bartapi.BartApi
import com.davidliu.bartapi.common.Direction
import com.davidliu.bartapi.estimated.EstimatedRoute
import com.deviange.bart.R
import com.deviange.bart.base.livedata.CombinedLiveData
import com.deviange.bart.base.livedata.SingleLiveEvent
import com.deviange.bart.base.ui.ExpandableHeaderItem
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.Group
import com.xwray.groupie.Section
import kotlinx.coroutines.launch

class StationEstimatesViewModel
@AssistedInject
constructor(
    private val bartApi: BartApi,
    @Assisted private val station: String,
    @Assisted private val handle: SavedStateHandle
) : ViewModel() {

    private val clickListener = { item: DepartureItem -> clickEvents.postValue(item) }
    private val northRoutes: MutableLiveData<List<RouteDeparture>>
    private val southRoutes: MutableLiveData<List<RouteDeparture>>

    private val estimates: CombinedLiveData<List<RouteDeparture>, List<RouteDeparture>, Pair<List<RouteDeparture>?, List<RouteDeparture>?>>
    val displayItems: LiveData<List<Group>>
    val isRefreshing = MutableLiveData(false)
    val clickEvents = SingleLiveEvent<DepartureItem>()
    init {
        val northKey = estimatesKey(station, Direction.NORTHBOUND)
        val southKey = estimatesKey(station, Direction.SOUTHBOUND)

        northRoutes = handle.getLiveData(estimatesKey(station, Direction.NORTHBOUND))
        southRoutes = handle.getLiveData(estimatesKey(station, Direction.SOUTHBOUND))

        estimates = CombinedLiveData(northRoutes, southRoutes) { north, south -> Pair(north, south) }

        displayItems = MediatorLiveData<List<Group>>()
        displayItems.addSource(estimates) { departuresPair ->
            val (northDepartures, southDepartures) = departuresPair
            val northExpandable = ExpandableGroup(ExpandableHeaderItem(R.string.northbound), true)
            val northSection = Section()
            northSection.addAll(mapDepartureToItems(northDepartures))
            northExpandable.add(northSection)

            val southExpandable = ExpandableGroup(ExpandableHeaderItem(R.string.southbound), true)
            val southSection = Section()
            southSection.addAll(mapDepartureToItems(southDepartures))
            southExpandable.add(southSection)
            displayItems.postValue(listOf(northExpandable, southExpandable))
        }

        if (!handle.contains(northKey) || !handle.contains(southKey)) {
            refresh()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            isRefreshing.postValue(true)
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

            isRefreshing.postValue(false)
        }
    }


    private fun mapDepartureToItems(departures: List<RouteDeparture>?) =
        departures.orEmpty()
            .map { departure -> DepartureItem(departure, { item: DepartureItem -> clickEvents.postValue(item) }) }

    private fun getFlatEstimatesList(routes: List<EstimatedRoute>): List<RouteDeparture> {
        return routes
            .flatMap { route ->
                route.estimates
                    .map { estimate ->
                        RouteDeparture(route, estimate)
                    }
            }
            .sortedBy { routeDeparture -> routeDeparture.departure.minutes }
    }

    companion object {
        const val ESTIMATES_KEY = "estimates"

        private fun estimatesKey(station: String, direction: Direction) = ESTIMATES_KEY + station + direction.name
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(station: String, handle: SavedStateHandle): StationEstimatesViewModel
    }
}