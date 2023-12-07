package com.example.mobiledaw.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


import androidx.room.TypeConverter

@Dao
interface SongDao {
    @Insert
    suspend fun insertSong(song: Song): Long

    @Query("SELECT * FROM songs")
    fun getAllSongs(): LiveData<List<Song>>

    @Query("SELECT * FROM songs WHERE id=:id")
    suspend fun getSong(id: Long): Song

    //Insert a single song
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(song: Song)

    //make another delete by ID
    @Delete
    suspend fun deleteSong(song: Song)

    //Update a single geoPhoto
    @Update
    suspend fun update(song: Song):Int
}