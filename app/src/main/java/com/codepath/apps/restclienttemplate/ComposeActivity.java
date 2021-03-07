package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.databinding.ActivityComposeBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    EditText etCompose;
    Button btnTweet;
    public static final int MAX_TWEET_LENGTH = 140;
    TwitterClient client;
    public final String TAG = "ComposeActivity";
    ActivityComposeBinding binding;
    Snackbar snackbarEmpty;
    Snackbar snackbarLong;
    TextView tvCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_compose);

        binding.setMaxCharCount(MAX_TWEET_LENGTH);
        binding.setCharCount(0);

        etCompose = binding.etCompose;
        btnTweet = binding.btnTweet;
        tvCounter = binding.tvCounter;

        client = TwitterApp.getRestClient(this);

        snackbarEmpty = Snackbar.make(binding.composeCoordinatorLayout, R.string.tweet_empty, Snackbar.LENGTH_SHORT);
        snackbarLong = Snackbar.make(binding.composeCoordinatorLayout, R.string.tweet_long, Snackbar.LENGTH_SHORT);

        etCompose.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.setVariable(BR.char_count, charSequence.length());

                if (charSequence.length() > MAX_TWEET_LENGTH) {
                    tvCounter.setTextColor(ContextCompat.getColor(ComposeActivity.this, R.color.holo_red_light));
                } else {
                    tvCounter.setTextColor(Color.BLACK);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Set click listener on button
        btnTweet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String tweetContent = etCompose.getText().toString();

                if (tweetContent.isEmpty()) {
                    snackbarEmpty.show();
                    return;
                }

                if (tweetContent.length() > MAX_TWEET_LENGTH) {
                    snackbarLong.show();
                    return;
                }

                // Make a call to Twitter API
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess to Publish Tweet");
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i(TAG, "Published tweet: " + tweet.body);
                            Intent intent = new Intent();
                            intent.putExtra("tweet", Parcels.wrap(tweet));
                            setResult(RESULT_OK, intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure to Publish Tweet", throwable);
                    }
                });
            }
        });
    }
}