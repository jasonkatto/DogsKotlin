package com.example.dogskotlin.model

import android.security.identity.AccessControlProfileId
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


//this is the interface through which we will access objects in the database.
//So this interface will define what kind of functions we can perform on our database.
@Dao
interface DogDao {

    // at insert annotation means that this function will insert data into the database.
    //it will insert parameters of type.
    // Dog breed and var ARG means that it's basically shorthand for valuable arguments or multiple arguments of type dogbreed
    //So we can pass to this function as many object of type. Dog breed as we like.
    //this function will return a list of long
    // this list of long is the primary key that we have defined as a list.
    //So as many dog breed objects we provide to this function just as many new ideas we will get as a return list
    //the reason for the suspend keyword here is that this function as with all function that access our
    //database will need to be done from a separate thread as we have discussed.
    //So we cannot access the database on the local main thread of our app.
    //Because that will cause our thread to be blocked while this operation finishes and Android really doesn't
    //like that and it will crash our application for us and actually it won't even allow us to compile our
    //code because Android Studio will detect that and will stop us.
    @Insert
    suspend fun insertAll(vararg  dogs:DogBreed):List<Long>


    //we want to retrive all the information that we stored in the database
    // so we will have another fucntion that we will annotate with the ass query annotation
    //we have suspend fun get all dogs okay. And this will return a list of dog breed.
    //how does the system know to get all the dogs. Well we need to provide a a query
    @Query("SELECT * From dogbreed")
    suspend fun getAllDogs():List<DogBreed>



    //how does the system know to get all the dogs. Well we need to provide a a query
    @Query("SELECT* FROM dogbreed WHERE uuid=:dogId")
    suspend fun getDog(dogId: Int):DogBreed




    @Query("Delete FROM dogbreed")
    suspend fun deleteAllDogs()



}