package com.codepath.apps.restclienttemplate.utils;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;

public class BindingUtil {
    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext())
                .load(url)
                .circleCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(view);
    }
}
