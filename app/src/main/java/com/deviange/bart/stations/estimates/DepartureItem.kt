package com.deviange.bart.stations.estimates

import android.graphics.Color
import android.view.View
import com.deviange.bart.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.departure_item.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DepartureItem(
    private val departure: RouteDeparture,
    private val onClick: View.OnClickListener?
) : Item() {
    override fun getLayout(): Int = R.layout.departure_item

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val context = viewHolder.root.context
        viewHolder.destination.text = departure.route.destination
        when (departure.departure.minutes) {
            "Leaving" -> {
                viewHolder.estimate.text = context.getString(R.string.leaving)
                viewHolder.time.visibility = View.GONE
            }
            else -> {
                viewHolder.estimate.text = context.getString(R.string.departure_in_minutes, departure.departure.minutes)

                val minutes = departure.departure.minutes.toLong()
                val departureTime = Date(Date().time + TimeUnit.MILLISECONDS.convert(minutes, TimeUnit.MINUTES))
                viewHolder.time.visibility = View.VISIBLE
                viewHolder.time.text = dateFormatter.format(departureTime)
            }
        }
        viewHolder.color_indicator.setBackgroundColor(Color.parseColor(departure.departure.hexcolor))
        viewHolder.root.setOnClickListener(onClick)
    }


    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        return other is DepartureItem &&
                departure == other.departure
    }

    companion object {
        val dateFormatter = SimpleDateFormat("h:mm", Locale.US)
    }
}
