package com.example.dogskotlin.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import  com.example.dogskotlin.R
import com.example.dogskotlin.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private val dogsListAdapter = DogListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // instance of our view model inside our fragment.
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)

        //this is going to call our refresh methods it's going to generate some dog objects.
        //A dog list and it's going to update the variables here.
        viewModel.refresh()

        //to retrieve that information here.
        //1- instantiate our dogs list.
        dogsList.apply {
            //it allows the system to order our elements sequentially in a linear fashion from top to bottom
            layoutManager = LinearLayoutManager(context)

            //2- we attach the adapter
            adapter = dogsListAdapter
        }
        refreshLayout.setOnRefreshListener {
            dogsList.visibility = View.GONE
            listError.visibility = View.GONE
            loadingView.visibility = View.VISIBLE
            viewModel.refreshByPassCache()
            refreshLayout.isRefreshing = false
        }

        observeViewModel()
    }

    // this method is going to use the variables that we've created
    //  in the ListViewModel to update the layout based on the values that we get
    private fun observeViewModel() {
        viewModel.dogs.observe(viewLifecycleOwner, Observer { dogs ->
            dogs?.let {
                dogsList.visibility = View.VISIBLE
                dogsListAdapter.updateDogsList(dogs)
            }
        })

        viewModel.dogsLoadError.observe(viewLifecycleOwner, Observer { isError ->
            isError?.let {
                listError.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading?.let {
                loadingView.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    listError.visibility = View.GONE
                    dogsList.visibility = View.GONE
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.actionSettings -> {
                view?.let { Navigation.findNavController(it).navigate(ListFragmentDirections.actionSettings()) }
            }
        }

        return super.onOptionsItemSelected(item)
    }
}