package com.codepath.apps.restclienttemplate.utils;

import android.widget.ImageView;
import android.widget.TextView;

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

    @BindingAdapter({"bind:likesCount"})
    public static void getLikesCount(TextView view, String likes) {
        view.setText((Integer.parseInt(likes) > 0) ? likes : "");
    }

    @BindingAdapter({"bind:retweetCount"})
    public static void getRetweetCount(TextView view, String retweets) {
        view.setText((Integer.parseInt(retweets) > 0) ? retweets : "");
    }
}
