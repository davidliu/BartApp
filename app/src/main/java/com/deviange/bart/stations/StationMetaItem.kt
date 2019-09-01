package com.deviange.bart.stations

import android.view.View
import com.davidliu.bartapi.stations.StationMeta
import com.deviange.bart.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

import kotlinx.android.synthetic.main.station_meta_item.view.*

class StationMetaItem(
    private val station: StationMeta,
    private val onClick: View.OnClickListener?
) : Item<ViewHolder>() {
    override fun getLayout(): Int = R.layout.station_meta_item

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.root.name.text = station.name
        viewHolder.root.setOnClickListener(onClick)
    }
}
