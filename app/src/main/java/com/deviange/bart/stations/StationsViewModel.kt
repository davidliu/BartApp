package com.deviange.bart.stations

import android.view.View
import androidx.lifecycle.*
import com.davidliu.bartapi.BartApi
import com.deviange.bart.base.livedata.CombinedLiveData
import com.deviange.bart.base.livedata.SingleLiveEvent
import com.deviange.bart.dagger.modules.DispatchersModule
import com.deviange.bart.dagger.viewmodel.ViewModelAssistedFactory
import com.deviange.bart.navigation.FragmentNavEvent
import com.deviange.bart.navigation.NavEvent
import com.deviange.bart.preferences.FavoriteStationsPreference
import com.deviange.bart.stations.estimates.StationEstimatesFragment
import com.deviange.bartdb.stations.StationDao
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
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
        favoriteItems.addSource(favorites) { stations ->
            val section = Section()
            stations.forEach { station ->
                val item = FavoriteStationItem(station, View.OnClickListener {
                    val fragment = StationEstimatesFragment.newInstance(station.id)
                    navEvent.postValue(FragmentNavEvent(fragment))
                })
                section.add(item)
            }
            favoriteItems.postValue(listOf(section))
        }

        val stationItems = MediatorLiveData<List<Group>>()
        stationItems.addSource(stations) { stations ->
            val section = Section()
            stations.forEach { station ->
                val item = StationMetaItem(station, View.OnClickListener {
                    val fragment = StationEstimatesFragment.newInstance(station.id)
                    navEvent.postValue(FragmentNavEvent(fragment))
                })
                section.add(item)
            }
            stationItems.postValue(listOf(section))
        }

        displayItems = CombinedLiveData<List<Group>, List<Group>, List<Group>>(
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