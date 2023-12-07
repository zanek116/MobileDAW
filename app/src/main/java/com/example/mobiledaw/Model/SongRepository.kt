package com.example.mobiledaw.Database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow


class SongRepository(private val songDao: SongDao) {

//    val allSongs: LiveData<List<Song>> = songDao.getAllSongs()

    //inserts new geoPhoto
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertSong(song: Song): Long {
        return songDao.insertSong(song)
    }

    suspend fun getSong(songId:Long): Song {
        return songDao.getSong(songId)
    }

//    fun getAllSongs(): LiveData<List<Song>> {
//        return songDao.getAllSongs()
//    }
    //updates geoPhotos
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(song: Song) {
        songDao.update(song)
    }

    //deletes geoPhoto
    suspend fun delete(song: Song){
        songDao.deleteSong(song)
    }

}