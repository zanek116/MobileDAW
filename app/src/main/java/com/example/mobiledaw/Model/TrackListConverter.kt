package com.example.mobiledaw.Database

import androidx.room.TypeConverter
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


object TrackListConverter {
    @TypeConverter
    fun fromByteArray(byteArray: ByteArray): ArrayList<Array<Pair<Int, Int>>?> {
        ObjectInputStream(ByteArrayInputStream(byteArray)).use { ois ->
            return ois.readObject() as ArrayList<Array<Pair<Int, Int>>?>
        }
    }

    @TypeConverter
    fun toByteArray(trackList: ArrayList<Array<Pair<Int, Int>>?>): ByteArray {
        ByteArrayOutputStream().use { bos ->
            ObjectOutputStream(bos).use { oos ->
                oos.writeObject(trackList)
            }
            return bos.toByteArray()
        }
    }
}
