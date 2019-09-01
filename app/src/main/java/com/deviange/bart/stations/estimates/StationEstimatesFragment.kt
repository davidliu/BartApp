package com.deviange.bart.stations

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import com.deviange.bart.R
import com.deviange.bart.base.ListFragment
import com.deviange.bart.base.ui.HeaderItem
import com.deviange.bart.stations.estimates.EstimateItem
import com.deviange.bart.stations.estimates.StationEstimatesViewModel
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.list_fragment.*
import javax.inject.Inject

class StationEstimatesFragment : ListFragment() {

    @Inject
    lateinit var viewModelFactory: StationEstimatesViewModel.Factory

    val viewModel: StationEstimatesViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory(requireActivity(), null) {
            override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
                @Suppress("UNCHECKED_CAST")
                return viewModelFactory.create(
                    arguments!!.getString(STATION_NAME_KEY)!!,
                    handle,
                    key,
                    requireActivity().savedStateRegistry
                ) as? T
                    ?: throw IllegalArgumentException("Unknown viewmodel class!")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.estimates.observe(viewLifecycleOwner) { estimatesPair ->
            val (northEstimates, southEstimates) = estimatesPair
            val northSection = Section()
            northEstimates?.let {
                northSection.setHeader(HeaderItem(R.string.northbound))
                northEstimates.forEach { estimate ->
                    val item = EstimateItem(estimate, null)
                    northSection.add(item)
                }
            }


            val southSection = Section()
            southEstimates?.let {
                southSection.setHeader(HeaderItem(R.string.southbound))
                southEstimates.forEach { estimate ->
                    val item = EstimateItem(estimate, null)
                    southSection.add(item)
                }
            }
            adapter.update(listOf(northSection, southSection))
        }
    }

    override fun onRefresh() {
        viewModel.refresh {
            swipe_refresh.isRefreshing = false
        }
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