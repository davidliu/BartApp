package com.deviange.bart.stations.estimates

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.deviange.bart.base.fragment.ListFragment
import com.deviange.bart.dagger.viewmodel.viewModelByFactory
import com.deviange.bart.notifications.departnotification.DepartureNotificationService
import com.deviange.bart.stations.StationsFragment
import dagger.Binds
import kotlinx.android.synthetic.main.list_fragment.*
import javax.inject.Inject
import kotlin.time.ExperimentalTime
import kotlin.time.minutes

class StationEstimatesFragment : ListFragment() {

    @Inject
    lateinit var viewModelFactory: StationEstimatesViewModel.Factory

    val viewModel: StationEstimatesViewModel by viewModelByFactory {
        viewModelFactory.create(arguments!!.getString(STATION_ID_KEY)!!, it)
    }

    @ExperimentalTime
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.displayItems
            .observe(viewLifecycleOwner) { items -> adapter.update(items) }
        viewModel.isRefreshing
            .observe(viewLifecycleOwner) { refreshing -> swipe_refresh.isRefreshing = refreshing }
        viewModel.clickEvents
            .observe(viewLifecycleOwner) { item ->
                val context = requireContext()
                val intent = DepartureNotificationService
                    .createIntent(
                        context,
                        item.creationTime + item.departure.departure.minutes.minutes.toLongMilliseconds()
                    )

                ContextCompat.startForegroundService(context, intent)
            }
    }

    override fun onRefresh() {
        viewModel.refresh()
    }

    override fun getTitle() = arguments!!.getString(STATION_NAME_KEY)!!

    companion object {
        private const val STATION_ID_KEY = "station_id"
        private const val STATION_NAME_KEY = "station_key"
        fun newInstance(id: String, name: String): StationEstimatesFragment {
            val fragment = StationEstimatesFragment()
            fragment.arguments = Bundle().apply {
                putString(STATION_ID_KEY, id)
                putString(STATION_NAME_KEY, name)
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