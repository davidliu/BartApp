package com.deviange.bart.base.ui

import androidx.annotation.StringRes
import com.deviange.bart.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.header_item.*

class HeaderItem(@StringRes val titleResId: Int) : Item() {
    override fun getLayout(): Int = R.layout.header_item

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.header.setText(titleResId)
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        return this == other
    }

    override fun equals(other: Any?): Boolean {
        return other is HeaderItem && other.titleResId == titleResId
    }
}