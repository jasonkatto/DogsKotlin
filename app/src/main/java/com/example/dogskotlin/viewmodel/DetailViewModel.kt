package com.example.dogskotlin.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogskotlin.model.DogBreed
import com.example.dogskotlin.model.DogDatabase
import kotlinx.coroutines.launch
import java.util.*

class DetailViewModel(application: Application):BaseViewModel(application) {

    val dogLiveData= MutableLiveData<DogBreed>()

    fun fetch(uuid:Int){
        launch {
            val dog=DogDatabase(getApplication()).dogDao().getDog(uuid)
            dogLiveData.value=dog
        }



    }
}