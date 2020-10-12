package com.example.dogskotlin.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


//this dog database is basically the object or the class that will access the database for us.
@Database(entities = arrayOf(DogBreed::class),version = 1)
abstract class DogDatabase:RoomDatabase() {

    abstract fun dogDao():DogDao

    companion object{
        @Volatile private var instance:DogDatabase?=null
        private  val LOCK=Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance?:buildDatabase(context).also{
                instance=it
            }
        }

        private fun buildDatabase(context: Context)=Room.databaseBuilder(
            context.applicationContext,
            DogDatabase::class.java,
            "dogDatabase"
        ).build()
    }
}