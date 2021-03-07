package com.codepath.apps.restclienttemplate.models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CustomViewModel extends ViewModel {

    public MutableLiveData<Tweet> tweetMutableLiveData = new MutableLiveData<>();
    private Tweet tweet;

    public CustomViewModel() {

    }


}
