package com.deviange.bart.stations

import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.deviange.bart.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.station_meta_item.*

data class StationMetaItem(
    val stationId: String,
    val name: String,
    val isFavorited: Boolean,
    private val onClick: (StationMetaItem) -> Unit,
    private val onFavoriteClick: (StationMetaItem) -> Unit
) : Item() {
    override fun getLayout(): Int = R.layout.station_meta_item

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.root.setOnClickListener { onClick(this) }
        viewHolder.name.text = name

        val resources = viewHolder.root.context.resources
        val drawable = when (isFavorited) {
            true -> resources.getDrawable(R.drawable.ic_star_24px, null)
                .also {
                    DrawableCompat.setTint(it, ResourcesCompat.getColor(resources, R.color.gold, null))
                }
            else -> resources.getDrawable(R.drawable.ic_star_border_24px, null)
                .also {
                    DrawableCompat.setTint(it, ResourcesCompat.getColor(resources, R.color.light_grey, null))
                }
        }
        viewHolder.favorite.setImageDrawable(drawable)
        viewHolder.favorite.setOnClickListener {
            onFavoriteClick(this)
            viewHolder.favorite.setOnClickListener(null) // disable until next bind
        }
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        return other is StationMetaItem
                && this.stationId == other.stationId
    }
}
