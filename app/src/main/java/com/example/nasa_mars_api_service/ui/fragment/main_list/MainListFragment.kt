package com.example.nasa_mars_api_service.ui.fragment.main_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.nasa_mars_api_service.R
import com.example.nasa_mars_api_service.core.Variables
import com.example.nasa_mars_api_service.database.db.MainDatabase
import com.example.nasa_mars_api_service.databinding.FragmentMainListBinding
import com.example.nasa_mars_api_service.network.api.MarsPhotosApi
import com.example.nasa_mars_api_service.preferences.implementations.AppPreferences
import com.example.nasa_mars_api_service.preferences.interfaces.BaseApplicationPreferences
import com.example.nasa_mars_api_service.repository.implementations.MainRepository
import com.example.nasa_mars_api_service.repository.interfaces.BaseRepository
import com.example.nasa_mars_api_service.ui.recycler_views.adapters.MainListAdapter

class MainListFragment: Fragment() {

    private lateinit var viewModel: MainListViewModel

    private lateinit var binding: FragmentMainListBinding

    private lateinit var mainListAdapter: MainListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_list, container, false)
        binding.lifecycleOwner = this

        getViewModel()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAllClickListeners()
        setAllObservers()
        setAdapters()
        requirePhotosOrIssue()
    }

    private fun getViewModel() {
        val context = requireContext().applicationContext
        val database = MainDatabase.getInstance(context)
        val preferences: BaseApplicationPreferences = AppPreferences.getInstance(context)
        val remoteSource = MarsPhotosApi.marsPhotosService

        val repository: BaseRepository = MainRepository.getInstance(database.marsPhotoDao, database.favoritePhotoDao, remoteSource, preferences)
        val factory = MainListViewModelFactory(repository)

        viewModel = ViewModelProvider(this, factory).get(MainListViewModel::class.java)
    }

    private fun setAllClickListeners() {

        binding.appBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.title) {
                getString(R.string.refresh) -> {
                    viewModel.refreshAllPhotos()
                    true
                }
                getString(R.string.search) -> {
                    findNavController().navigate(MainListFragmentDirections.actionMainListFragmentToSearchMarsPhotoFragment())
                    true
                }
                else -> false
            }
        }

    }

    private fun setAdapters() {
        mainListAdapter = MainListAdapter()
        binding.mainRecyclerView.adapter = mainListAdapter
    }

    private fun setAllObservers() {
        viewModel.resultListForMainListAdapter.observe(viewLifecycleOwner, Observer { newList ->
            mainListAdapter.items = newList
        })

    }

    private fun requirePhotosOrIssue() {

        viewModel.getResultList()

    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}