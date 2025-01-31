package com.deviange.bart.stations

import androidx.lifecycle.*
import com.davidliu.bartapi.BartApi
import com.deviange.bart.R
import com.deviange.bart.base.livedata.CombinedLiveData
import com.deviange.bart.base.livedata.SingleLiveEvent
import com.deviange.bart.base.ui.ExpandableHeaderItem
import com.deviange.bart.dagger.modules.DispatchersModule
import com.deviange.bart.dagger.viewmodel.ViewModelAssistedFactory
import com.deviange.bart.navigation.FragmentNavEvent
import com.deviange.bart.navigation.NavEvent
import com.deviange.bart.preferences.FavoriteStation
import com.deviange.bart.preferences.FavoriteStationsPreference
import com.deviange.bart.stations.estimates.StationEstimatesFragment
import com.deviange.bartdb.stations.StationDao
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.Group
import com.xwray.groupie.Section
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
    private val favoriteStationsPreference: FavoriteStationsPreference,
    @Assisted private val handle: SavedStateHandle
) : ViewModel() {

    private val favorites = favoriteStationsPreference.liveData
    private val stations = stationsDao.observeAllStations()
    val displayItems: LiveData<List<Group>>
    val navEvent = SingleLiveEvent<NavEvent>()
    val isRefreshing = MutableLiveData<Boolean>(false)

    private val favoritesExpandable = ExpandableGroup(ExpandableHeaderItem(R.string.favorites), true)
    private val favoritesSection = Section()

    private val allStationsExpandable = ExpandableGroup(ExpandableHeaderItem(R.string.all_stations), true)
    private val allStationsSection = Section()
    init {
        refresh()
        val favoriteItems = MediatorLiveData<List<Group>>()
        val favoritesItemClick = { item: FavoriteStationItem ->
            val fragment = StationEstimatesFragment.newInstance(item.station.id, item.station.name)
            navEvent.postValue(FragmentNavEvent(fragment))
        }
        favoritesExpandable.add(favoritesSection)
        favoriteItems.addSource(favorites) { stations ->
            val items = stations.map { station -> FavoriteStationItem(station, favoritesItemClick) }
            favoritesSection.update(items)

            favoriteItems.postValue(
                if (items.isEmpty()) listOf()
                else listOf(favoritesExpandable)
            )
        }

        val stationItemClick = { item: StationMetaItem ->
            val fragment = StationEstimatesFragment.newInstance(item.stationId, item.name)
            navEvent.postValue(FragmentNavEvent(fragment))
        }
        val stationFavoriteClick = { item: StationMetaItem ->
            if (!item.isFavorited) {
                val newArray = arrayOf(*favorites.value.orEmpty(), FavoriteStation(item.stationId, item.name))
                favoriteStationsPreference.setValue(newArray)
            } else {
                val newArray = favorites.value
                    .orEmpty()
                    .filter { favorite -> favorite.id != item.stationId }
                    .toTypedArray()
                favoriteStationsPreference.setValue(newArray)
            }
        }
        allStationsExpandable.add(allStationsSection)
        val stationItems = CombinedLiveData(favorites, stations) { favorites, stations ->
            val items = stations.orEmpty()
                .map { station ->
                    val isFavorited = favorites.orEmpty()
                        .firstOrNull { favorite -> favorite.id == station.id } != null

                    StationMetaItem(
                        station.id,
                        station.name,
                        isFavorited,
                        stationItemClick,
                        stationFavoriteClick
                    )
                }
            allStationsSection.update(items)
            listOf(allStationsExpandable)
        }

        displayItems = CombinedLiveData(
            favoriteItems,
            stationItems
        ) { favItems, staItems -> favItems.orEmpty() + staItems.orEmpty() }
    }

    fun refresh() {
        viewModelScope.launch(ioDispatcher) {
            isRefreshing.postValue(true)
            val response = bartApi.getAllStationsSuspend()
            val stationList = response.root.stations.stationList
            stationsDao.upsertList(stationList)
            isRefreshing.postValue(false)
        }
    }

    fun onFavoriteMove(from: FavoriteStationItem, target: FavoriteStationItem) {
        val newFavorites = arrayOf(*favorites.value.orEmpty())
        val fromIndex = favoritesSection.getPosition(from)
        val toIndex = favoritesSection.getPosition(target)

        val swap = newFavorites[fromIndex]
        newFavorites[fromIndex] = newFavorites[toIndex]
        newFavorites[toIndex] = swap

        favoriteStationsPreference.setValue(newFavorites)
    }

    @AssistedInject.Factory
    interface Factory : ViewModelAssistedFactory<StationsViewModel>
}