package com.assignment.brewapps.database

import android.content.Context
import androidx.room.*
import com.assignment.brewapps.pojo.Movies

@TypeConverters(DBTypeConverter::class)
@Database(entities = [Movies::class],version = 1)
abstract class MoviesDatabase: RoomDatabase() {
    abstract fun nowPlayingListDao():NowPlayingListDao
    abstract fun nowPlayingDetailDao():NowPlayingDetailDao

    companion object{
        @Volatile
        private var instance:MoviesDatabase?=null

        fun getInstance(context: Context)= instance?: synchronized(this){
            Room.databaseBuilder(context.applicationContext,MoviesDatabase::class.java
            ,"movie_database").build().also {
                instance=it
            }
        }
    }
}