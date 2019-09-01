package com.deviange.bart.stations.estimates

import android.os.Parcelable
import com.davidliu.bartapi.estimated.EstimateDepartureTime
import com.davidliu.bartapi.estimated.EstimatedRoute
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RouteDeparture(
    val route: EstimatedRoute,
    val departure: EstimateDepartureTime
) : Parcelable