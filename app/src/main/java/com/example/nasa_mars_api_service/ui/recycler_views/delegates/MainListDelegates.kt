package com.example.nasa_mars_api_service.ui.recycler_views.delegates

import android.app.Activity
import android.net.Uri
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.nasa_mars_api_service.R
import com.example.nasa_mars_api_service.core.buildUTIFromURL
import com.example.nasa_mars_api_service.core.getBaseRequestOptions
import com.example.nasa_mars_api_service.core.models.FavouritePhoto
import com.example.nasa_mars_api_service.core.models.MarsPhoto
import com.example.nasa_mars_api_service.core.models.PictureOfDayPhoto
import com.example.nasa_mars_api_service.databinding.*
import com.example.nasa_mars_api_service.network.MarsApiStatus
import com.example.nasa_mars_api_service.ui.recycler_views.adapters.FavouritePhotosHorizontalListAdapter
import com.example.nasa_mars_api_service.ui.recycler_views.adapters.GridMarsPhotoListAdapter
import com.example.nasa_mars_api_service.ui.recycler_views.models.*
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

/**
 * Created by Matvey Popov.
 * Date: 02.01.2021
 * Time: 22:15
 * Project: NASA-Mars-API-Service
 */
object MainListDelegates {

    fun favouritePhotosHorizontalListDelegate(
            favouritePhotosListItemClickListener: (FavouritePhoto) -> Unit,
            favouritePhotosListItemLongClickListener: (FavouritePhoto) -> Unit,
            favouritePhotosListItemDeleteFromFavouritesClickListener: (FavouritePhoto) -> Boolean
    ) =
        adapterDelegateViewBinding<HorizontalFavouritePhotosListRecycler, ListItem, FragmentHorizontalListBinding>(
            { inflater, container -> FragmentHorizontalListBinding.inflate(inflater, container, false) }
        ) {

            // OnCreateViewHolder
            val adapter = FavouritePhotosHorizontalListAdapter(favouritePhotosListItemClickListener, favouritePhotosListItemLongClickListener, favouritePhotosListItemDeleteFromFavouritesClickListener)
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

    fun pictureOfDayDelegate(
            pictureOfDayPhotoItemClickListener: (PictureOfDayPhoto) -> Unit,
            pictureOfDayPhotoItemLongClickListener: (PictureOfDayPhoto) -> Unit,
            pictureOfDayPhotoItemAddToFavouritesClickListener: (PictureOfDayPhoto) -> Boolean
    ) =
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
                    .load(item.picture.imageSrc)
                    .transition(withCrossFade())
                    .into(binding.pictureOfTheDayImageView)

                binding.materialCardViewLayout.setOnClickListener {
                    pictureOfDayPhotoItemClickListener(item.picture)
                }

                binding.pictureOfTheDayImageView.setOnClickListener {
                    pictureOfDayPhotoItemClickListener(item.picture)
                }

                binding.pictureOfTheDayImageView.setOnLongClickListener {
                    pictureOfDayPhotoItemLongClickListener(item.picture)
                    true
                }

                binding.addToFavoriteImage.setOnClickListener {
                    if (item.picture.isFavourite) {
                        binding.addToFavoriteImage.setImageResource(R.drawable.ic_star)
                    }
                    else {
                        binding.addToFavoriteImage.setImageResource(R.drawable.ic_star__filled)
                    }
                    pictureOfDayPhotoItemAddToFavouritesClickListener(item.picture)
                }
            }

            onViewRecycled {
                if ((binding.root.context as? Activity)?.isDestroyed?.not() == true) {
                    Glide.with(binding.root).clear(binding.pictureOfTheDayImageView)
                }
            }
        }

    fun gridMarsPhotosListDelegate(
            marsPhotoItemClickListener: (MarsPhoto) -> Unit,
            marsPhotoItemLongClickListener: (MarsPhoto) -> Unit,
            marsPhotoItemAddToFavouritesClickListener: (MarsPhoto) -> Boolean,
    ) =
        adapterDelegateViewBinding<GridListMarsPhotos, ListItem, FragmentGridMarsPhotoListBinding>(
            { inflater, container -> FragmentGridMarsPhotoListBinding.inflate(inflater, container, false) }
        ) {

            val adapter = GridMarsPhotoListAdapter(marsPhotoItemClickListener, marsPhotoItemLongClickListener, marsPhotoItemAddToFavouritesClickListener)
            binding.titleLabel.text = getString(R.string.new_mars_photos)
            binding.gridMarsPhotoRecyclerView.adapter = adapter

            bind {
                adapter.apply {
                    items = item.listOfPhotos
                }
            }
        }


    fun gridMarsPhotosListItemDelegate(
            marsPhotoItemClickListener: (MarsPhoto) -> Unit,
            marsPhotoItemLongClickListener: (MarsPhoto) -> Unit,
            marsPhotoItemAddToFavouritesClickListener: (MarsPhoto) -> Boolean
    ) =
        adapterDelegateViewBinding<MarsPhoto, ListItem, FragmentGridMarsPhotoItemBinding>(
            { inflater, container -> FragmentGridMarsPhotoItemBinding.inflate(inflater, container, false) }
        ) {

            bind {
                val uri = buildUTIFromURL(item.imageSrc)
                val options = getBaseRequestOptions()
                Glide.with(binding.marsPhotoImageView)
                    .applyDefaultRequestOptions(options)
                    .load(item.imageSrc)
                    .transition(withCrossFade())
                    .into(binding.marsPhotoImageView)

                binding.marsPhotoImageView.setOnClickListener {
                    marsPhotoItemClickListener(item)
                }

                binding.marsPhotoImageView.setOnLongClickListener {
                    marsPhotoItemLongClickListener(item)
                    true
                }

                binding.addToFavoriteImage.setOnClickListener {
                    if(item.isFavourite) {
                        binding.addToFavoriteImage.setImageResource(R.drawable.ic_star)
                    }
                    else {
                        binding.addToFavoriteImage.setImageResource(R.drawable.ic_star__filled)
                    }
                    marsPhotoItemAddToFavouritesClickListener(item)
                }
            }

            onViewRecycled {
                if ((binding.root.context as? Activity)?.isDestroyed?.not() == true) {
                    Glide.with(binding.root).clear(binding.marsPhotoImageView)
                }
            }
        }


    fun favouritePhotosHorizontalListItemDelegate(
            favouritePhotosListItemClickListener: (FavouritePhoto) -> Unit,
            favouritePhotosListItemLongClickListener: (FavouritePhoto) -> Unit,
            favouritePhotosListItemDeleteFromFavouritesClickListener: (FavouritePhoto) -> Boolean
    ) =
        adapterDelegateViewBinding<FavouritePhoto, ListItem, FragmentHorizontalListItemBinding>(
            { inflater, container -> FragmentHorizontalListItemBinding.inflate(inflater, container, false) }
        ) {
            binding.addToFavoriteImage.setImageResource(R.drawable.ic_star__filled)

            bind {
                val photo = item.photo
                var uri: Uri? = null
                val src = when(photo) {
                    is MarsPhoto -> {
                        binding.solTextView.text = getString(R.string.sol) + " " + photo.solDate
                        uri = buildUTIFromURL(photo.imageSrc)
                        photo.imageSrc
                    }
                    is PictureOfDayPhoto -> {
                        binding.solTextView.text = getString(R.string.nasa)
                        uri = buildUTIFromURL(photo.imageSrc)
                        photo.imageSrc
                    }
                    else -> ""
                }

                val options = getBaseRequestOptions()
                Glide.with(binding.imageView)
                    .applyDefaultRequestOptions(options)
                    .load(src)
                    .transition(withCrossFade())
                    .into(binding.imageView)

                binding.materialCardViewLayout.setOnClickListener {
                    favouritePhotosListItemClickListener(item)
                }

                binding.imageView.setOnLongClickListener {
                    favouritePhotosListItemLongClickListener(item)
                    true
                }

                binding.imageView.setOnClickListener {
                    favouritePhotosListItemClickListener(item)
                }

                binding.addToFavoriteImage.setOnClickListener {
                    binding.addToFavoriteImage.setImageResource(R.drawable.ic_star)
                    favouritePhotosListItemDeleteFromFavouritesClickListener(item)
                }
            }

            onViewRecycled {
                if ((binding.root.context as? Activity)?.isDestroyed?.not() == true) {
                    Glide.with(binding.root).clear(binding.imageView)
                }
            }
        }


    fun loadMoreMarsPhotosDelegate(
            loadMorePhotosButtonClickListener: () -> Unit,
            resultStatus: LiveData<MarsApiStatus>,
            lifecycleOwner: LifecycleOwner
    ) =
        adapterDelegateViewBinding<LoadMoreMarsPhotos, ListItem, FragmentLoadMoreItemsBinding>(
            { inflater, container -> FragmentLoadMoreItemsBinding.inflate(inflater, container, false) }
        ) {
            binding.progressBar.visibility = View.GONE
            binding.loadMoreButton.visibility = View.VISIBLE
            binding.lifecycleOwner = lifecycleOwner

            bind {
                binding.progressBar.visibility = View.GONE
                binding.loadMoreButton.visibility = View.VISIBLE

                binding.loadMoreButton.setOnClickListener {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.loadMoreButton.visibility = View.GONE
                    loadMorePhotosButtonClickListener()
                }
                binding.lifecycleOwner?.let { it1 ->
                    resultStatus.observe(it1, Observer { newStatus->
                        when (newStatus) {
                            MarsApiStatus.ERROR -> {
                                binding.progressBar.visibility = View.GONE
                                binding.loadMoreButton.visibility = View.VISIBLE
                            }
                            MarsApiStatus.DONE -> {
                                binding.progressBar.visibility = View.GONE
                                binding.loadMoreButton.visibility = View.VISIBLE
                            }
                            MarsApiStatus.LOADING -> {
                                binding.loadMoreButton.visibility = View.GONE
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            else -> {
                                binding.progressBar.visibility = View.GONE
                                binding.loadMoreButton.visibility = View.VISIBLE
                            }
                        }
                    } )
                }
            }

        }
}