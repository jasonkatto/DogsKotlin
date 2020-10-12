package com.example.dogskotlin.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.dogskotlin.model.DogBreed
import com.example.dogskotlin.model.DogDatabase
import com.example.dogskotlin.model.DogsApiService
import com.example.dogskotlin.util.NotificationsHelper
import com.example.dogskotlin.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.lang.NumberFormatException

class ListViewModel(application: Application) : BaseViewModel(application) {

    private var prefHelper = SharedPreferencesHelper(getApplication())
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L

    private val dogService = DogsApiService()

    //the disposable will allow us to observe the observable that the
    // API gives us the single and not having to worry
    // too much about actually getting rid of it.
    //So that will allow us to root to avoid any memory leaks
    // that might come with waiting for an observable
    //while still our while our application has been destroyed.
    private val disposable = CompositeDisposable()

    //liveData variables
    //this will provide the information for the
    // actual list of dogs that we retrieve from all data source
    // which can be the backend API can.
    // or can be a local database or any other data source that we have
    val dogs = MutableLiveData<List<DogBreed>>()

    //This Life data will notify whoever is
    //listening to this new model will notify that there's an error
    // It will just specify that there is a generic error with the retrieval of the data.
    val dogsLoadError = MutableLiveData<Boolean>()

    // this live data will tell whoever's listening that the system is
    //loading so the data has not yet arrived.
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        checkCacheDuration()
        val updateTime = prefHelper.getUpdateTime()
        if(updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            fetchFromDataBase()
        } else {
            fetchFromRemote()
        }
    }

    private fun checkCacheDuration() {
        val cachePreference = prefHelper.getCacheDuration()

        try {
            val cachePreferenceInt = cachePreference?.toInt() ?: 5 * 60
            refreshTime = cachePreferenceInt.times(1000 * 1000 * 1000L)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    fun refreshByPassCache(){
        fetchFromRemote()
    }



    private fun fetchFromDataBase() {
        loading.value=true
        launch {
            val dogs=DogDatabase(getApplication()).dogDao().getAllDogs()
            dogsRetrieve(dogs)
            Toast.makeText(getApplication(), "Dogs Retrieved from database", Toast.LENGTH_SHORT).show()
        }
    }

    //will basically return the data from the remote endpoint
    private fun fetchFromRemote() {
         loading.value=true

        disposable.add(
            //this will return us the single that we have defined in DogsApiService
            //first thing we do is we set that call to be on
            // a separate thread rather than on the API thread.
            //The reason for that is that we don't want this call to be made
            // on the API on the main thread because
            //that will basically block our application while the call is retrieved.
            //And that will basically block the user for
            // however long and the communication with the backing API happens.
            dogService.getDogs()
                // we pass this call to the endpoint on a background threat.
                .subscribeOn(Schedulers.newThread())

                //However the result of this process needs
                // to be processed on the main thread of our application.
                //So in order to actually display it we need to have
                // it on the main thread and not still on the background thread.
                .observeOn(AndroidSchedulers.mainThread())
//here we need to pass the Observer that we want to observe this single.
// the single observer will basically get a list of dog breeds.
                .subscribeWith(object :DisposableSingleObserver<List<DogBreed>>(){
                    //onSuccess we actually get the list of dog breeds
                    //we update our mutable live date
                    override fun onSuccess(dogList: List<DogBreed>) {
                        storeDogsLocally(dogList)
                        Toast.makeText(getApplication(), "Dogs Retrieved from endpoint", Toast.LENGTH_SHORT).show()
                        NotificationsHelper(getApplication()).createNotification()

                    }

                    //onError We get a an error message.
                    override fun onError(e: Throwable) {
                        dogsLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }

                })
        )


    }

    private fun dogsRetrieve(dogList: List<DogBreed>) {
        dogs.value = dogList
        dogsLoadError.value = false
        loading.value = false

    }

    private fun storeDogsLocally(list: List<DogBreed>) {
        //to run the coroutine on the separate threat
        launch {
            val dao = DogDatabase(getApplication()).dogDao()
            dao.deleteAllDogs()
            val result = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i < list.size) {
                list[i].uuid = result[i].toInt()
            }
            dogsRetrieve(list)

        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}