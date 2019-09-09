package com.deviange.bart.stations

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.deviange.bart.R
import com.deviange.bart.base.fragment.ListFragment
import com.deviange.bart.dagger.viewmodel.activityViewModelByFactory
import com.deviange.bart.navigation.StackNavigator
import com.xwray.groupie.GroupAdapter
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


        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                if (viewHolder.itemViewType == R.layout.favorite_station_item) {
                    return makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0)
                } else {
                    return makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0)
                }
            }

            override fun canDropOver(recyclerView: RecyclerView, current: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return current::class == target::class
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                if (canDropOver(recyclerView, viewHolder, target)) {
                    val adapter = recyclerView.adapter as GroupAdapter
                    viewModel.onFavoriteMove(
                        adapter.getItem(viewHolder.adapterPosition) as FavoriteStationItem,
                        adapter.getItem(target.adapterPosition) as FavoriteStationItem
                    )
                    return true
                }
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) = Unit

        })

        itemTouchHelper.attachToRecyclerView(recycler_view)
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