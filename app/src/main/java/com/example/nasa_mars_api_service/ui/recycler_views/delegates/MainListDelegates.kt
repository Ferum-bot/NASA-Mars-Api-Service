package com.example.nasa_mars_api_service.ui.recycler_views.delegates

import android.app.Activity
import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.nasa_mars_api_service.R
import com.example.nasa_mars_api_service.core.buildUTIFromURL
import com.example.nasa_mars_api_service.core.getBaseRequestOptions
import com.example.nasa_mars_api_service.core.models.FavouritePhoto
import com.example.nasa_mars_api_service.core.models.MarsPhoto
import com.example.nasa_mars_api_service.core.models.PictureOfDayPhoto
import com.example.nasa_mars_api_service.databinding.*
import com.example.nasa_mars_api_service.ui.recycler_views.adapters.FavouritePhotosHorizontalListAdapter
import com.example.nasa_mars_api_service.ui.recycler_views.adapters.GridMarsPhotoListAdapter
import com.example.nasa_mars_api_service.ui.recycler_views.models.GridListMarsPhotos
import com.example.nasa_mars_api_service.ui.recycler_views.models.HorizontalFavouritePhotosListRecycler
import com.example.nasa_mars_api_service.ui.recycler_views.models.ListItem
import com.example.nasa_mars_api_service.ui.recycler_views.models.PictureOfDayItem
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

/**
 * Created by Matvey Popov.
 * Date: 02.01.2021
 * Time: 22:15
 * Project: NASA-Mars-API-Service
 */
object MainListDelegates {

    fun favouritePhotosHorizontalListDelegate() =
        adapterDelegateViewBinding<HorizontalFavouritePhotosListRecycler, ListItem, FragmentHorizontalListBinding>(
            { inflater, container -> FragmentHorizontalListBinding.inflate(inflater, container, false) }
        ) {

            // OnCreateViewHolder
            val adapter = FavouritePhotosHorizontalListAdapter()
            binding.horizontalRecyclerView.adapter = adapter

            // OnBindViewHolder
            bind {
                binding.titleLabel.text = item.title
                adapter.apply {
                    items = item.listOfItems
                }
            }

            // OnViewRecycled
            onViewRecycled {

            }
        }

    fun pictureOfDayDelegate() =
        adapterDelegateViewBinding<PictureOfDayItem, ListItem, FragmentImageOfDayBinding>(
            { inflater, container -> FragmentImageOfDayBinding.inflate(inflater, container, false) }
        ) {

            // OnCreateViewHolder
            binding.pictureOfTheDayTextView.text = getString(R.string.picture_of_the_day)

            // OnBindViewHolder
            bind {
                val isFavourite = item.picture.isFavourite
                if (isFavourite) {
                    binding.addToFavoriteImage.setImageResource(R.drawable.ic_star__filled)
                }
                else {
                    binding.addToFavoriteImage.setImageResource(R.drawable.ic_star)
                }

                val uri = buildUTIFromURL(item.picture.imageSrc)
                val options = getBaseRequestOptions()
                Glide.with(binding.pictureOfTheDayImageView)
                    .applyDefaultRequestOptions(options)
                    .load(uri)
                    .transition(withCrossFade())
                    .into(binding.pictureOfTheDayImageView)

            }

            onViewRecycled {
                if ((binding.root.context as? Activity)?.isDestroyed?.not() == true) {
                    Glide.with(binding.root).clear(binding.pictureOfTheDayImageView)
                }
            }
        }

    fun gridMarsPhotosListDelegate() =
        adapterDelegateViewBinding<GridListMarsPhotos, ListItem, FragmentGridMarsPhotoListBinding>(
            { inflater, container -> FragmentGridMarsPhotoListBinding.inflate(inflater, container, false) }
        ) {

            val adapter = GridMarsPhotoListAdapter()
            binding.titleLabel.text = getString(R.string.new_mars_photos)
            binding.gridMarsPhotoRecyclerView.adapter = adapter

            bind {
                adapter.apply {
                    items = item.listOfPhotos
                }
            }
        }


    fun gridMarsPhotosListItemDelegate() =
        adapterDelegateViewBinding<MarsPhoto, ListItem, FragmentGridMarsPhotoItemBinding>(
            { inflater, container -> FragmentGridMarsPhotoItemBinding.inflate(inflater, container, false) }
        ) {

            bind {
                val uri = buildUTIFromURL(item.imageSrc)
                val options = getBaseRequestOptions()
                Glide.with(binding.marsPhotoImageView)
                    .applyDefaultRequestOptions(options)
                    .load(uri)
                    .transition(withCrossFade())
                    .into(binding.marsPhotoImageView)
            }

            onViewRecycled {
                if ((binding.root.context as? Activity)?.isDestroyed?.not() == true) {
                    Glide.with(binding.root).clear(binding.marsPhotoImageView)
                }
            }
        }


    fun favouritePhotosHorizontalListItemDelegate() =
        adapterDelegateViewBinding<FavouritePhoto, ListItem, FragmentHorizontalListItemBinding>(
            { inflater, container -> FragmentHorizontalListItemBinding.inflate(inflater, container, false) }
        ) {

            bind {
                binding.addToFavoriteImage.setImageResource(R.drawable.ic_star__filled)
                val photo = item.photo
                var uri: Uri? = null
                when(photo) {
                    is MarsPhoto -> {
                        binding.solTextView.text = getString(R.string.sol) + " " + photo.solDate
                        uri = buildUTIFromURL(photo.imageSrc)
                    }
                    is PictureOfDayPhoto -> {
                        binding.solTextView.text = getString(R.string.nasa)
                        uri = buildUTIFromURL(photo.imageSrc)
                    }
                }

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