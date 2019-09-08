package com.deviange.bart.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.deviange.bart.R
import com.deviange.bart.base.fragment.StackFragment
import com.deviange.bart.base.util.safe
import javax.inject.Inject

class StackNavigator
@Inject constructor(
    private val fragment: Fragment,
    private val fragmentManager: FragmentManager
) {

    fun onNavEvent(navEvent: NavEvent) {
        when (navEvent) {
            is FragmentNavEvent -> pushFragment(navEvent.fragment)
            is PopNavEvent -> pop()
        }.safe()
    }

    private fun pushFragment(newFragment: StackFragment) {
        fragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
            .hide(fragment)
            .add(R.id.root, newFragment, fragmentManager.getKeyForPushFragment())
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    private fun pop() = fragmentManager.popBackStack()
}

sealed class NavEvent
class FragmentNavEvent(val fragment: StackFragment) : NavEvent()
class PopNavEvent : NavEvent()