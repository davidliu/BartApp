package com.deviange.bart.stations

import android.view.View
import com.deviange.bart.R
import com.deviange.bart.preferences.FavoriteStation
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.station_meta_item.view.*

class FavoriteStationItem(
    private val station: FavoriteStation,
    private val onClick: View.OnClickListener?
) : Item<ViewHolder>() {
    override fun getLayout(): Int = R.layout.station_meta_item

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.root.name.text = station.id
        viewHolder.root.setOnClickListener(onClick)
    }

    override fun isSameAs(other: Item<*>?): Boolean {
        return other is FavoriteStationItem &&
                station.id == other.station.id
    }
}
