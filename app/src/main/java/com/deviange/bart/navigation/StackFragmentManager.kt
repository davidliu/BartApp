package com.deviange.bart.navigation

import androidx.fragment.app.FragmentManager
import com.deviange.bart.base.fragment.StackFragment

private const val STACK_PREFIX = "stack_index_key_"

private fun getKeyForIndex(index: Int) = "${STACK_PREFIX}_${index}"

fun FragmentManager.getKeyForPushFragment() = getKeyForIndex(backStackEntryCount)

fun FragmentManager.getTopFragment() =
    findFragmentByTag(getKeyForIndex(backStackEntryCount - 1)) as? StackFragment
        ?: findFragmentByTag("root") as? StackFragment