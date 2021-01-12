package com.example.nasa_mars_api_service.ui.recycler_views.delegates

import android.app.Activity
import android.graphics.Color
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.nasa_mars_api_service.R
import com.example.nasa_mars_api_service.core.buildUTIFromURL
import com.example.nasa_mars_api_service.core.getBaseRequestOptions
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


    fun marsPhotoListDelegate(
            marsPhotoItemClickListener: (MarsPhoto) -> Unit,
            marsPhotoImageLongClickListener: (MarsPhoto) -> Unit,
            marsPhotoAddToFavouritesClickListener: (MarsPhoto) -> Unit
    ) =
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

                binding.materialCardViewLayout.setOnClickListener {
                    marsPhotoItemClickListener(item)
                }

                binding.imageView.setOnClickListener {
                    marsPhotoItemClickListener(item)
                }

                binding.imageView.setOnLongClickListener {
                    marsPhotoImageLongClickListener(item)
                    true
                }

                binding.addToFavoriteImage.setOnClickListener {
                    when(item.isFavourite) {
                        true -> {
                            binding.addToFavoriteImage.setImageResource(R.drawable.ic_star)
                        }
                        false -> {
                            binding.addToFavoriteImage.setImageResource(R.drawable.ic_star__filled)
                        }
                    }
                    marsPhotoAddToFavouritesClickListener(item)
                }

                val uri = buildUTIFromURL(item.imageSrc)
                val options = getBaseRequestOptions()
                Glide.with(binding.imageView)
                    .applyDefaultRequestOptions(options)
                    .load(uri)
                    .transition(withCrossFade())
                    .into(binding.imageView)
            }

            onViewRecycled {
                if ((binding.root.context as? Activity)?.isDestroyed?.not() == true) {
                    Glide.with(binding.root).clear(binding.imageView)
                }
            }
        }
}