package com.example.dogskotlin.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.dogskotlin.R


//we define a function that will give us a little spinner
//that we can display on the image while we're waiting for it to be downloaded.

val PERMISSION_SEND_SMS =234
fun getProgressDrawable(context:Context):CircularProgressDrawable{
    return CircularProgressDrawable(context).apply {
        strokeWidth= 10f
        centerRadius=50f
        start()
    }
}

//extension function for the image view element.
fun ImageView.loadImage(uri:String?, progressDrawable:CircularProgressDrawable){

    val option=RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_dogs_icon)
    Glide.with(context)
        .setDefaultRequestOptions(option)
        .load(uri)
        .into(this)
}

@BindingAdapter("android:imageUrl")
fun loadImage(view:ImageView, url:String?){
    view.loadImage(url, getProgressDrawable(view.context))
}