package com.example.yuanjinxie.sharkfeed.model;

import android.arch.lifecycle.ViewModel;

// Data Model which contains the feed object
public class FeedModel extends ViewModel {
    public SharkFeed sharkFeed;

    public FeedModel() {
        sharkFeed = new SharkFeed();
    }
}
