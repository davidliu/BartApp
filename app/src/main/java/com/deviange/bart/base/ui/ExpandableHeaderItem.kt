package com.deviange.bart.base.ui

import androidx.annotation.StringRes
import com.deviange.bart.R
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.expandable_header_item.*


class ExpandableHeaderItem(
    @StringRes private val titleResId: Int
) : Item(), ExpandableItem {

    private lateinit var expandableGroup: ExpandableGroup

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.item_expandable_header_title.setText(titleResId)
        viewHolder.item_expandable_header_icon.setImageResource(getRotatedIconResId())

        viewHolder.root.setOnClickListener {
            expandableGroup.onToggleExpanded()
            viewHolder.item_expandable_header_icon.setImageResource(getRotatedIconResId())
        }
    }

    override fun getLayout() = R.layout.expandable_header_item

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        expandableGroup = onToggleListener
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        return this == other
    }

    override fun equals(other: Any?): Boolean {
        return other is ExpandableHeaderItem && other.titleResId == titleResId
    }

    private fun getRotatedIconResId() =
        if (expandableGroup.isExpanded)
            R.drawable.ic_keyboard_arrow_down_black_24dp
        else
            R.drawable.ic_keyboard_arrow_up_black_24dp
}
