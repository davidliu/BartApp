package com.deviange.bart.stations

import com.deviange.bart.R
import com.deviange.bart.preferences.FavoriteStation
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.station_meta_item.view.*

class FavoriteStationItem(
    val station: FavoriteStation,
    private val onClick: (FavoriteStationItem) -> Unit
) : Item() {
    override fun getLayout(): Int = R.layout.favorite_station_item

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.root.name.text = station.name
        viewHolder.root.setOnClickListener { onClick(this) }
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        return other is FavoriteStationItem &&
                this.station.id == other.station.id
    }
}
