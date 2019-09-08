package com.deviange.bart.stations

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.deviange.bart.R
import com.deviange.bart.base.fragment.ListFragment
import com.deviange.bart.dagger.viewmodel.activityViewModelByFactory
import com.deviange.bart.navigation.StackNavigator
import dagger.Binds
import kotlinx.android.synthetic.main.list_fragment.*
import javax.inject.Inject

class StationsFragment : ListFragment() {

    @Inject
    lateinit var viewModelFactory: StationsViewModel.Factory

    @Inject
    lateinit var navigator: StackNavigator

    val viewModel: StationsViewModel by activityViewModelByFactory {
        viewModelFactory.create(it)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.displayItems.observe(viewLifecycleOwner) { adapter.update(it) }
        viewModel.isRefreshing.observe(viewLifecycleOwner) { swipe_refresh.isRefreshing = it }
        viewModel.navEvent.observe(viewLifecycleOwner) { navigator.onNavEvent(it) }
    }

    override fun onRefresh() {
        viewModel.refresh()
    }

    override fun getTitle(): String = getString(R.string.all_stations)

    @dagger.Module
    interface Module {
        @Binds
        fun fragment(fragment: StationsFragment): Fragment
    }
}