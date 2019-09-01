package com.deviange.bart.stations.estimates

import android.graphics.Color
import android.view.View
import com.deviange.bart.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.departure_item.view.*

class DepartureItem(
    private val departure: RouteDeparture,
    private val onClick: View.OnClickListener?
) : Item<ViewHolder>() {
    override fun getLayout(): Int = R.layout.departure_item

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val context = viewHolder.root.context
        viewHolder.root.destination.text = departure.route.destination
        viewHolder.root.estimate.text = when (departure.departure.minutes) {
            "Leaving" -> context.getString(R.string.leaving)
            else -> context.getString(R.string.departure_in_minutes, departure.departure.minutes)
        }
        viewHolder.root.color_indicator.setBackgroundColor(Color.parseColor(departure.departure.color))
        viewHolder.root.setOnClickListener(onClick)
    }

    override fun isSameAs(other: Item<*>?): Boolean {
        return other is DepartureItem &&
                departure == other.departure
    }
}
