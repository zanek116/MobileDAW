

package com.example.mobiledaw.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.lang.reflect.Type


@Entity(tableName = "songs")
data class Song(
    @PrimaryKey(autoGenerate = true) val id: Int?,

    //Title - string
    @ColumnInfo(name = "songTitle") var songTitle: String,

    //numTracks - string
    @ColumnInfo(name = "numTracks") var numTracks: Int,

    //numTracks - string
    @ColumnInfo(name = "trackList") var trackList: String,


//    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
//    val tracks: List<Pair<Int, Int>>
)
class plzWork {
    @TypeConverter
    fun fromString(value: String?): ArrayList<Array<Pair<Int, Int>>?> {
        if (value == null) {
            return ArrayList()
        }

        val type: Type = object : TypeToken<ArrayList<Array<Pair<Int, Int>>?>?>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun toString(trackList: ArrayList<Array<Pair<Int, Int>>?>): String {
        return Gson().toJson(trackList)
    }
}

