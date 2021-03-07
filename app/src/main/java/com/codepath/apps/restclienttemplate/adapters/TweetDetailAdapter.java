package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.databinding.ItemTweetReplyBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

import okhttp3.Headers;

public class TweetDetailAdapter extends RecyclerView.Adapter<TweetDetailAdapter.ViewHolder> {

    Context context;
    List<Tweet> tweetReplies;

    public TweetDetailAdapter(Context context, List<Tweet> tweetReplies) {
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

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemTweetReplyBinding binding;
        Button likeButton;
        TwitterClient client;

        public ViewHolder(@NonNull ItemTweetReplyBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
            likeButton = this.binding.likeButton;
            client = TwitterApp.getRestClient(context);
        }

        public void bind(final Tweet tweet) {
            binding.setTweet(tweet);
            binding.executePendingBindings();

            likeButton.setSelected(tweet.liked);

            likeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (!tweet.liked) {
                        client.postLike(tweet.id, new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.i("TweetsAdapter", "onSuccess from Posting Like");
                                tweet.liked = true;
                                tweet.favorites_count += 1;
                                likeButton.setSelected(true);
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.e("TweetsAdapter", "onFailure from Posting Like", throwable);
                            }
                        });
                    }
                    else {
                        client.destroyLike(tweet.id, new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.i("TweetsAdapter", "onSuccess Destroy Like");
                                tweet.liked = false;
                                tweet.favorites_count -= 1;
                                likeButton.setSelected(false);
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.e("TweetsAdapter", "onFailure Destroy Like", throwable);
                            }
                        });
                    }
                }
            });

        }
    }


}
