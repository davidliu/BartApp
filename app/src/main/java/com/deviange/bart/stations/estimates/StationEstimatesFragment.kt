package com.deviange.bart.stations

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import com.deviange.bart.base.ListFragment
import com.deviange.bart.stations.estimates.StationEstimatesViewModel
import kotlinx.android.synthetic.main.list_fragment.*
import javax.inject.Inject

class StationEstimatesFragment : ListFragment() {

    @Inject
    lateinit var viewModelFactory: StationEstimatesViewModel.Factory

    val viewModel: StationEstimatesViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory(this, null) {
            override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
                @Suppress("UNCHECKED_CAST")
                return viewModelFactory.create(
                    arguments!!.getString(STATION_NAME_KEY)!!,
                    handle
                ) as? T
                    ?: throw IllegalArgumentException("Unknown viewmodel class!")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.displayItems
            .observe(viewLifecycleOwner) { items -> adapter.update(items) }

        viewModel.isRefreshing
            .observe(viewLifecycleOwner) { refreshing -> swipe_refresh.isRefreshing = refreshing }
    }

    override fun onRefresh() {
        viewModel.refresh()
    }

    companion object {
        private const val STATION_NAME_KEY = "station"
        fun newInstance(station: String): StationEstimatesFragment {
            val fragment = StationEstimatesFragment()
            fragment.arguments = Bundle().apply {
                putString(STATION_NAME_KEY, station)
            }

            return fragment
        }
    }
}