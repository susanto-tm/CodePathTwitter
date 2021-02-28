package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.databinding.ItemTweetReplyBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetRepliesAdapter extends RecyclerView.Adapter<TweetRepliesAdapter.ViewHolder> {

    Context context;
    List<Tweet> tweetReplies;

    public TweetRepliesAdapter(Context context, List<Tweet> tweetReplies) {
        this.context = context;
        this.tweetReplies = tweetReplies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        ItemTweetReplyBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_tweet_reply, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet tweet = tweetReplies.get(position);

        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweetReplies.size();
    }

    public void addAll(List<Tweet> tweetList) {
        tweetReplies.addAll(tweetList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemTweetReplyBinding binding;

        public ViewHolder(@NonNull ItemTweetReplyBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public void bind(Tweet tweet) {
            binding.setTweet(tweet);
            binding.executePendingBindings();
        }
    }
}
