package com.example.dogskotlin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext


//routines are basically a way for us to access the database from a separate thread as we have
//talked about previously the database cannot be accessed from the main thread.
//Android does not allow us to do that because that will block the UI.
//And that will cause a bad experience for our users.
//So every time every single time we access the database it needs to be from a background threat.
//And the easiest way to access this to achieve this is with Coroutine
abstract class BaseViewModel(application: Application): AndroidViewModel(application), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}