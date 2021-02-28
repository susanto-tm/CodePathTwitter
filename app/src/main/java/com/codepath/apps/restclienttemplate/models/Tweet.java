package com.codepath.apps.restclienttemplate.models;

import com.codepath.apps.restclienttemplate.utils.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Tweet {

    public String body;
    public String createdAt;
    public User user;
    public String relativeTime;
    public String detailDate;
    public String detailTime;
    public String replyingTo;
    public long id;

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.id = jsonObject.getLong("id");
        tweet.relativeTime = TimeFormatter.getTimeDifference(tweet.createdAt);
        tweet.detailDate = Tweet.getDetailDate(Tweet.getTwitterDate(tweet.createdAt));
        tweet.detailTime = Tweet.getDetailTime(Tweet.getTwitterDate(tweet.createdAt));
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
