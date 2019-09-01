package com.deviange.bart.stations

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.deviange.bart.base.ListFragment
import com.deviange.bart.dagger.viewmodel.ViewModelFactory
import javax.inject.Inject

class StationsFragment : ListFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    val viewModel: StationsViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.stations.observe(viewLifecycleOwner) { stations ->

        }
    }

    override fun onRefresh() {
        viewModel.refresh()
    }


}