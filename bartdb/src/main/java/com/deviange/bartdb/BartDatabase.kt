package com.deviange.bartdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.davidliu.bartapi.stations.StationMeta
import com.deviange.bartdb.stations.StationDao

@Database(
    entities = [
        StationMeta::class
    ],
    version = 1
)
abstract class BartDatabase : RoomDatabase() {
    abstract fun stationDao(): StationDao
}