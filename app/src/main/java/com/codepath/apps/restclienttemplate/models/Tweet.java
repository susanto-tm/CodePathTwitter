package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.codepath.apps.restclienttemplate.utils.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Parcel
@Entity(foreignKeys = @ForeignKey(entity=User.class, parentColumns="id", childColumns="userId"))
public class Tweet {

    @ColumnInfo
    @PrimaryKey
    public long id;

    @ColumnInfo
    public String body;

    @ColumnInfo
    public String createdAt;

    @ColumnInfo
    public long userId;

    @Ignore
    public User user;

    @ColumnInfo
    public String relativeTime;

    @ColumnInfo
    public String detailDate;

    @ColumnInfo
    public String detailTime;

    @ColumnInfo
    public String replyingTo;

    @ColumnInfo
    public int favorites_count;

    @ColumnInfo
    public int retweets_count;

    @ColumnInfo
    public boolean liked;

    private static final String TAG = "TweetObject";

    // empty constructor needed by Parceler library
    public Tweet() { }

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.id = jsonObject.getLong("id");
        tweet.relativeTime = TimeFormatter.getTimeDifference(tweet.createdAt);
        tweet.detailDate = Tweet.getDetailDate(Tweet.getTwitterDate(tweet.createdAt));
        tweet.detailTime = Tweet.getDetailTime(Tweet.getTwitterDate(tweet.createdAt));
        tweet.favorites_count = jsonObject.getInt("favorite_count");
        tweet.retweets_count = jsonObject.getInt("retweet_count");
        tweet.liked = jsonObject.getBoolean("favorited");

        User user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.user = user;
        tweet.userId = user.id;

        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray, String screenName) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            Tweet tweet = fromJson(jsonArray.getJSONObject(i));

            tweet.replyingTo = screenName;
            tweets.add(tweet);
        }
        return tweets;
    }

    public static Date getTwitterDate(String createdAt) {
        final String DATE_FORMAT = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        sf.setLenient(true);
        try {
            return sf.parse(createdAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Calendar.getInstance().getTime();
    }

    public static String getDetailDate(Date date) {
        SimpleDateFormat sf = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        return sf.format(date);
    }

    public static String getDetailTime(Date date) {
        SimpleDateFormat sf = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
        return sf.format(date);
    }
}
