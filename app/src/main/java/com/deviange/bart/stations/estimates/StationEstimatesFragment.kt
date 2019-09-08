package com.deviange.bart.stations.estimates

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.deviange.bart.base.fragment.ListFragment
import com.deviange.bart.dagger.viewmodel.viewModelByFactory
import com.deviange.bart.stations.StationsFragment
import dagger.Binds
import kotlinx.android.synthetic.main.list_fragment.*
import javax.inject.Inject

class StationEstimatesFragment : ListFragment() {

    @Inject
    lateinit var viewModelFactory: StationEstimatesViewModel.Factory

    val viewModel: StationEstimatesViewModel by viewModelByFactory {
        viewModelFactory.create(arguments!!.getString(STATION_NAME_KEY)!!, it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    @dagger.Module
    interface Module {
        @Binds
        fun fragment(fragment: StationsFragment): Fragment
    }
}