package com.example.nasa_mars_api_service.ui.fragment.main_list.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.nasa_mars_api_service.R
import com.example.nasa_mars_api_service.core.models.MarsPhoto
import com.example.nasa_mars_api_service.databinding.FragmentMainListItemBinding
import com.example.nasa_mars_api_service.ui.fragment.main_list.MainListViewModel

class ItemViewHolder private constructor(private val binding: FragmentMainListItemBinding,
                                         private val viewModel: MainListViewModel,
                                         private val marsPhoto: MarsPhoto): RecyclerView.ViewHolder(binding.root) {

    init {
        binding.marsPhoto = marsPhoto
        binding.executePendingBindings()
    }

    fun bind(marsPhoto: MarsPhoto) {
        binding.marsPhoto = marsPhoto
        binding.executePendingBindings()
    }

    companion object {
        fun getViewHolder(parent: ViewGroup, viewModel: MainListViewModel): ItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding: FragmentMainListItemBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_list_item, parent, false)
            return ItemViewHolder(binding, viewModel, MarsPhoto())
        }
    }

}