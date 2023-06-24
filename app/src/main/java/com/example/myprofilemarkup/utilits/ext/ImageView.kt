package com.example.myprofilemarkup.utilits.ext

import android.widget.ImageView
import coil.load
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.example.myprofilemarkup.utilits.ImageLoader
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

private val imageLoader: ImageLoader = ImageLoader.Glide

fun ImageView.loadPhoto(url: String) {

    when (imageLoader) {
        ImageLoader.Glide -> {
            Glide.with(context)
                .load(url)
                .circleCrop()
                .into(this) //todo error
        }

        ImageLoader.Coil -> {
            load(url) { transformations(CircleCropTransformation()) }
        }

        ImageLoader.Picasso -> {
            Picasso.get().load(url).transform(CropCircleTransformation()).into(this)
        }
    }

}