package com.deviange.bartdb.stations

import androidx.lifecycle.LiveData
import androidx.room.*
import com.davidliu.bartapi.stations.StationMeta

@Dao
abstract class StationDao {

    @Query("SELECT * FROM StationMeta")
    abstract fun getAllStations(): List<StationMeta>

    @Query("SELECT * FROM StationMeta")
    abstract fun observeAllStations(): LiveData<List<StationMeta>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract fun insert(entities: List<StationMeta>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    protected abstract fun update(entities: List<StationMeta>)

    @Transaction
    open fun upsertList(entities: List<StationMeta>) {
        insert(entities)
        update(entities)
    }
}