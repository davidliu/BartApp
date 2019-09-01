package com.deviange.bart.stations.estimates

import android.view.View
import com.davidliu.bartapi.estimated.EstimateDepartureTime
import com.deviange.bart.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.station_meta_item.view.*

class EstimateItem(
    private val estimate: EstimateDepartureTime,
    private val onClick: View.OnClickListener?
) : Item<ViewHolder>() {
    override fun getLayout(): Int = R.layout.station_meta_item

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.root.name.text = estimate.minutes.toString()
        viewHolder.root.setOnClickListener(onClick)
    }
}
