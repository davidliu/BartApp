package com.deviange.bart.base.ui

import androidx.annotation.StringRes
import com.deviange.bart.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.header_item.view.*

class HeaderItem(@StringRes val titleResId: Int) : Item<ViewHolder>() {
    override fun getLayout(): Int = R.layout.header_item

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.root.header.setText(titleResId)
    }

    override fun isSameAs(other: Item<*>?): Boolean {
        return other is HeaderItem && other.titleResId == titleResId
    }


}