package com.deviange.bart.stations

import android.view.View
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
    favoriteStationsPreference: FavoriteStationsPreference,
    @Assisted private val handle: SavedStateHandle
) : ViewModel() {

    private val favorites = favoriteStationsPreference.liveData
    private val stations = stationsDao.observeAllStations()
    val displayItems: LiveData<List<Group>>
    val navEvent = SingleLiveEvent<NavEvent>()
    val isRefreshing = MutableLiveData<Boolean>(false)

    init {
        val favoriteItems = MediatorLiveData<List<Group>>()
        val favoritesExpandable = ExpandableGroup(ExpandableHeaderItem(R.string.favorites), true)
        val favoritesSection = Section()
        favoritesExpandable.add(favoritesSection)
        favoriteItems.addSource(favorites) { stations ->
            favoritesSection.clear()
            val items = stations.map { station ->
                FavoriteStationItem(station, View.OnClickListener {
                    val fragment = StationEstimatesFragment.newInstance(station.id)
                    navEvent.postValue(FragmentNavEvent(fragment))
                })
            }
            favoritesSection.update(items)
            favoriteItems.postValue(listOf(favoritesExpandable))
        }

        val allStationsExpandable = ExpandableGroup(ExpandableHeaderItem(R.string.all_stations), true)
        val allStationsSection = Section()
        allStationsExpandable.add(allStationsSection)
        val stationItems = CombinedLiveData(favorites, stations) { favorites, stations ->
            allStationsSection.clear()
            val items = stations.orEmpty()
                .map { station ->
                    val isFavorited = favorites.orEmpty()
                        .firstOrNull { favorite -> favorite.id == station.id } != null

                    StationMetaItem(
                        station.id,
                        station.name,
                        isFavorited,
                        View.OnClickListener {
                            val fragment = StationEstimatesFragment.newInstance(station.id)
                            navEvent.postValue(FragmentNavEvent(fragment))
                        },
                        View.OnClickListener {
                            if (!isFavorited) {
                                val newArray = arrayOf(*favorites.orEmpty(), FavoriteStation(station.id, station.name))
                                favoriteStationsPreference.setValue(newArray)
                            } else {
                                val newArray = favorites
                                    .orEmpty()
                                    .filter { favorite -> favorite.id != station.id }
                                    .toTypedArray()
                                favoriteStationsPreference.setValue(newArray)
                            }
                        })
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

    @AssistedInject.Factory
    interface Factory : ViewModelAssistedFactory<StationsViewModel>
}