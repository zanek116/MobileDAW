package com.example.mobiledaw.Database

// DataDao.kt
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TrackDao {
    @Insert
    suspend fun insert(trackData: TrackData)

    @Query("SELECT * FROM data_table")
    suspend fun getAllData(): List<TrackData>

    @Query("SELECT third FROM data_table ORDER BY id DESC LIMIT 1")
    suspend fun getNextId(): Int

    @Query("SELECT DISTINCT third FROM data_table")
    suspend fun getTitles(): List<Int>
}
