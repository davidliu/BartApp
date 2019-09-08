@file:Suppress("unused")

package com.deviange.bart.base.util

fun Unit.safe() = this
fun Nothing?.safe() = this
fun Any?.safe() = this