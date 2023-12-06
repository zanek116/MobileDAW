package com.example.mobiledaw.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data_table")
data class TrackData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "first") val first: Int,
    @ColumnInfo(name = "second") val second: Int,
    @ColumnInfo(name = "third") val track: Int


)
