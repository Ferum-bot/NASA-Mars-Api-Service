package com.example.nasa_mars_api_service.ui.recycler_views.delegates

import android.graphics.Color
import com.example.nasa_mars_api_service.R
import com.example.nasa_mars_api_service.core.models.MarsPhoto
import com.example.nasa_mars_api_service.databinding.FragmentSearchListItemBinding
import com.example.nasa_mars_api_service.ui.recycler_views.models.ListItem
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

/**
 * Created by Matvey Popov.
 * Date: 03.01.2021
 * Time: 23:49
 * Project: NASA-Mars-API-Service
 */
object SearchListDelegates {


    fun marsPhotoListDelegate() =
        adapterDelegateViewBinding<MarsPhoto, ListItem, FragmentSearchListItemBinding>(
            { inflater, container -> FragmentSearchListItemBinding.inflate(inflater, container, false) }
        ) {

            binding.addToFavoriteImage.setImageResource(R.drawable.ic_star)

            bind {
                binding.earthDateTextView.text = item.earthDate
                binding.marsSolTextView.text = item.solDate.toString()
                binding.roverTextView.text = item.roverName
                when(item.isFavourite) {
                    true -> {
                        binding.addToFavoriteImage.setImageResource(R.drawable.ic_star__filled)
                    }
                    false -> {
                        binding.addToFavoriteImage.setImageResource(R.drawable.ic_star)
                    }
                }

                // Add Glide
                binding.imageView.setBackgroundColor(Color.LTGRAY)
            }
        }

}