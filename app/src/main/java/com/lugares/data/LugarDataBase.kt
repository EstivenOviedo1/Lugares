package com.lugares.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lugares.model.Lugar
import java.time.Instant


@Database(entities = [Lugar::class], version = 1, exportSchema = false)
abstract  class LugarDataBase: RoomDatabase() {

    abstract fun lugarDao(): LugarDao
    companion object{
        @Volatile
        private  var INSTANCE: LugarDataBase? = null

        fun getDatabase(context:  android.content.Context): LugarDataBase {
            val instance = INSTANCE
            if (instance != null) {

                return instance
            }
synchronized(this){

    val basedatos =  Room.databaseBuilder(
        context.applicationContext,
        LugarDataBase::class.java,
        "lugar_database"

    ).build()
    INSTANCE = basedatos
    return basedatos
}

        }
    }
}