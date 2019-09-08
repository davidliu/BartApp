package com.deviange.bart.stations

import android.view.View
import com.deviange.bart.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.station_meta_item.*

data class StationMetaItem(
    private val id: String,
    private val name: String,
    private val isFavorited: Boolean,
    private val onClick: View.OnClickListener?,
    private val onFavoriteClick: View.OnClickListener?
) : Item() {
    override fun getLayout(): Int = R.layout.station_meta_item

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.root.setOnClickListener(onClick)
        viewHolder.name.text = name
        viewHolder.favorite.setImageResource(
            when (isFavorited) {
                true -> R.drawable.ic_star_24px
                else -> R.drawable.ic_star_border_24px
            }
        )
        viewHolder.favorite.setOnClickListener(onFavoriteClick)
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        return other is StationMetaItem
                && this.id == other.id
    }
}
