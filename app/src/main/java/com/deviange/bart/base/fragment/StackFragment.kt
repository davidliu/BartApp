package com.deviange.bart.base.fragment

import dagger.android.support.DaggerFragment

abstract class StackFragment : DaggerFragment() {
    abstract fun getTitle(): String
}