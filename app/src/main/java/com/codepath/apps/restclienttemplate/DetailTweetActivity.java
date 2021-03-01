package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.apps.restclienttemplate.adapters.TweetDetailAdapter;
import com.codepath.apps.restclienttemplate.databinding.ActivityDetailTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class DetailTweetActivity extends AppCompatActivity {

    private final String TAG = "DetailTweetActivity";
    ActivityDetailTweetBinding binding;
    TwitterClient client;
    TweetDetailAdapter adapter;
    List<Tweet> tweetReplies;

    RecyclerView rvDetailReplies;

    /* TODO -- Make embedded image and videos based on different layouts, determine type using
    *   extended entities field in json with media_url_https and get from Photo Media URL formatting for photos
    *   and use video_info variants for videos */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_tweet);

        client = TwitterApp.getRestClient(this);

        long tweetId = getIntent().getLongExtra("tweetId", 0);
        Log.i("DetailTweetActivity", "Tweet ID: " + tweetId);

        tweetReplies = new ArrayList<>();

        getTweetFromId(tweetId);

        rvDetailReplies = binding.rvDetailReplies;
        rvDetailReplies.setOverScrollMode(View.OVER_SCROLL_NEVER);

        adapter = new TweetDetailAdapter(this, tweetReplies);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvDetailReplies.setLayoutManager(layoutManager);
        rvDetailReplies.setAdapter(adapter);
        rvDetailReplies.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void populateTweetReplies(final long tweetId, final String screenName) {
        Log.i(TAG, "Screen Name: " + screenName);
        client.getTweetReplies(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess " + json.toString());
                JSONObject jsonObject = json.jsonObject;
                try {
                    adapter.addAll(Tweet.fromJsonArray(jsonObject.getJSONArray("statuses"), screenName));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG, "onFailure for Populate Tweet Replies", throwable);
            }
        }, tweetId, screenName);
    }

    private void getTweetFromId(final long tweetId) {
        Log.i(TAG, "Getting Tweet... " + tweetId);
        client.getTweetFromId(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i("DetailActivityTweet", "onSuccess for getTweetFromId " + json.toString());
                try {
                    Tweet tweet = Tweet.fromJson(json.jsonObject);
                    binding.setTweet(tweet);
                    populateTweetReplies(tweetId, tweet.user.screenName);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e("DetailActivityTweet", "onFailure for getTweetFromId", throwable);
            }
        }, tweetId);
    }
}