package com.deviange.bart.dagger.modules

import android.content.Context
import androidx.room.Room
import com.deviange.bartdb.BartDatabase
import com.deviange.bartdb.stations.StationDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DatabaseModule {
    @Provides
    @Singleton
    @JvmStatic
    fun database(context: Context): BartDatabase =
        Room.databaseBuilder(context, BartDatabase::class.java, "bart_database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @JvmStatic
    fun stationDao(bartDatabase: BartDatabase): StationDao = bartDatabase.stationDao()
}