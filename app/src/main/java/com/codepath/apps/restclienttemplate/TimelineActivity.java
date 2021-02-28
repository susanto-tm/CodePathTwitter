package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import com.codepath.apps.restclienttemplate.adapters.TweetsAdapter;
import com.codepath.apps.restclienttemplate.databinding.ActivityTimelineBinding;
import com.codepath.apps.restclienttemplate.databinding.ItemTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    public static final String TAG = "TimelineActivity";
    TwitterClient client;
    RecyclerView rvTweets;
    List<Tweet> tweets;
    TweetsAdapter adapter;
    SwipeRefreshLayout swipeContainer;
    EndlessRecyclerViewScrollListener scrollListener;

    ActivityTimelineBinding binding;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_timeline);

        client = TwitterApp.getRestClient(this);

        // Find the recycler view
        rvTweets = binding.rvTweets;
        // Find Swipe Container
        swipeContainer = binding.swipeContainer;

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "fetching new data...");
                populateHomeTimeline();
            }
        });

        // Configure the refreshing color
        swipeContainer.setColorSchemeResources(R.color.holo_blue_bright,
                R.color.holo_green_light,
                R.color.holo_orange_light,
                R.color.holo_red_light);

        // Init the list of tweets and adapter
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this, tweets);
        // Recycler view setup: layout manager and the adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(layoutManager);
        rvTweets.setAdapter(adapter);
        rvTweets.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG, "onLoadMore: " + page);
                loadMoreData();
            }
        };

        // Adds the scroll listener to RecyclerView
        rvTweets.addOnScrollListener(scrollListener);

        // Populate Timeline with Tweets
        populateHomeTimeline();
    }

    private void loadMoreData() {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        client.getNextPageOfTweets(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess for loadMoreData " + json.toString());
                //  --> Deserialize and construct new model objects from the API response
                JSONArray jsonArray = json.jsonArray;
                try {
                    //  --> Append the new data objects to the existing set of items inside the array of items
                    //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
                    adapter.addAll(Tweet.fromJsonArray(jsonArray));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure for loadMoreData", throwable);
            }
        }, tweets.get(tweets.size() - 1).id);
    }

    private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess " + json.toString());
                JSONArray jsonArray = json.jsonArray;
                try {
                    adapter.clear();
                    adapter.addAll(Tweet.fromJsonArray(jsonArray));
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    Log.e(TAG, "Json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure " + response, throwable);
            }
        });
    }
}