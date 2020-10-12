package com.example.dogskotlin.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


//this model class from line 11-41. It stores the information
// it retrieves the information from the back backend API and also it manages
//the names for the database columns where this entity will be stored.
@Entity
data class DogBreed(
    @ColumnInfo(name="breed_id")
    @SerializedName("id")
    val breedId: String?,

    @ColumnInfo(name="dog_name")
    @SerializedName("name")
    val dogBreed:String?,

    @ColumnInfo(name = "life_span")
    @SerializedName("life_span")
    val lifeSpan:String?,

    @ColumnInfo(name = "breed_group")
    @SerializedName("breed_group")
    val breedGroup:String?,


    @ColumnInfo(name = "bred_for")
    @SerializedName("bred_for")
    val breedFor:String?,


    @SerializedName("temperament")
    val temperament:String?,

    @ColumnInfo(name = "dog_url")
    @SerializedName("url")
    val imageUrl:String?
){

    //every time the above entity will be put inside the database the room library
    //will generate a primary key for us and store it in the database under you I.D..
    //And that will allow us to easily retrieve this particular dog breed from the database using this unique
    //identifier as the key for searching inside the database.
    @PrimaryKey(autoGenerate = true)
    var uuid:Int=0
}

data class DogPalette(var color:Int)

data class SmsInfo(
    var to: String,
    var text: String,
    var imageUrl: String?
)