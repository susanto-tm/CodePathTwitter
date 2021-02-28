package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.apps.restclienttemplate.DetailTweetActivity;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.databinding.ItemTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    Context context;
    List<Tweet> tweets;

    // Pass in context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // For each row, inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);

        ItemTweetBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_tweet, parent, false);

//        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(binding);
    }

    // Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data at position
        Tweet tweet = tweets.get(position);

        // Bind the tweet with the ViewHolder
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Tweet> tweetList) {
        tweets.addAll(tweetList);
        notifyDataSetChanged();
    }

    // Define a ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        RelativeLayout rvTweetContainer;

        private final ItemTweetBinding binding;

        public ViewHolder(@NonNull ItemTweetBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

            ivProfileImage = this.binding.ivProfileImage;
            tvBody = this.binding.tvBody;
            tvScreenName = this.binding.tvScreenName;
            rvTweetContainer = this.binding.rvTweetContainer;

        }

        public void bind(final Tweet tweet) {
            binding.setTweet(tweet);
            binding.executePendingBindings();
            rvTweetContainer.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailTweetActivity.class);
                    intent.putExtra("tweetId", tweet.id);
                    context.startActivity(intent);
                }
            });
        }
    }


}
