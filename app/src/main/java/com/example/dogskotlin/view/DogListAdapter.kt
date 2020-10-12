package com.example.dogskotlin.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.dogskotlin.R
import com.example.dogskotlin.databinding.ItemDogBinding
import com.example.dogskotlin.model.DogBreed
import com.example.dogskotlin.util.getProgressDrawable
import com.example.dogskotlin.util.loadImage
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.android.synthetic.main.item_dog.view.*

class DogListAdapter(val dogsList: ArrayList<DogBreed>) : RecyclerView.Adapter<DogListAdapter.DogViewHolder>(), DogClickListener {


    fun updateDogsList(newDogList:List<DogBreed>){
        dogsList.clear()
        dogsList.addAll(newDogList)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val inflater :LayoutInflater= LayoutInflater.from(parent.context)
        val view =DataBindingUtil.inflate<ItemDogBinding>(inflater, R.layout.item_dog, parent, false)
        return DogViewHolder(view)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.view.dog=dogsList[position]
        holder.view.listener= this



    }

    override fun onDogClicked(v: View) {
        val uuid=v.dogId.text.toString().toInt()
        val action= ListFragmentDirections.actionDetailFragment()
            action.dogUuid=uuid
            Navigation.findNavController(v).navigate(action)
    }

    override fun getItemCount()=dogsList.size




    class DogViewHolder(var view: ItemDogBinding) : RecyclerView.ViewHolder(view.root)

}