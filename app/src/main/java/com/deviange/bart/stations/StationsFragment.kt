package com.deviange.bart.stations

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import com.deviange.bart.R
import com.deviange.bart.base.ListFragment
import com.deviange.bart.dagger.viewmodel.ViewModelFactory
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.list_fragment.*
import javax.inject.Inject

class StationsFragment : ListFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    val viewModel: StationsViewModel by activityViewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.stations.observe(viewLifecycleOwner) { stations ->
            val section = Section()
            stations.forEach { station ->
                val item = StationMetaItem(station, View.OnClickListener {
                    val fragment = StationEstimatesFragment.newInstance(station.id)
                    val fm = requireFragmentManager()
                    fm.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                        .hide(this)
                        .add(R.id.content, fragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss()
                })
                section.add(item)
            }
            adapter.update(listOf(section))
        }

        viewModel.isRefreshing.observe(viewLifecycleOwner) { refreshing ->
            swipe_refresh.isRefreshing = refreshing
        }
    }

    override fun onRefresh() {
        viewModel.refresh()
    }


}