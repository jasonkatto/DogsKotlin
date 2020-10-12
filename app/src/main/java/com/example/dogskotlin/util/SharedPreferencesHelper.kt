package com.example.dogskotlin.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.core.content.edit


//Now shared preferences are a way to store key value pairs in our Android applications.
//It's a very simple system where we just store a key and a value and we use that to we can use it to
//retrieve that information later or updated or whatever we need to do.
class SharedPreferencesHelper {

    companion object {

        private const val PREF_TIME = "Pref time"
        private var prefs: SharedPreferences? = null

        @Volatile private var instance: SharedPreferencesHelper? = null
        private val LOCK = Any()

        operator fun invoke(context: Context): SharedPreferencesHelper = instance ?: synchronized(LOCK) {
            instance ?: buildHelper(context).also {
                instance = it
            }
        }

        private fun buildHelper(context: Context) : SharedPreferencesHelper {
            prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return SharedPreferencesHelper()
        }
    }

    fun saveUpdateTime(time: Long) {
        prefs?.edit(commit = true) {putLong(PREF_TIME, time)}
    }

    //this is a function to retrieve the data
    // from the shared preferences that we stored earlier
    fun getUpdateTime() = prefs?.getLong(PREF_TIME, 0)

    fun getCacheDuration()= prefs?.getString("pref_cache_duration", "")

 }
