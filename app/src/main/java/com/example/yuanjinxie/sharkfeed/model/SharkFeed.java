package com.example.yuanjinxie.sharkfeed.model;

import java.util.ArrayList;
import java.util.List;

public class SharkFeed {
    private List<SharkFeedEntity> feedsList;

    public SharkFeed() {
        feedsList = new ArrayList<>();
    }

    public void setFeedsList(List<SharkFeedEntity> feedsList) {
        this.feedsList = feedsList;
    }

    public List<SharkFeedEntity> addFeed(List<SharkFeedEntity> addList) {
        for (int i = 0; i < addList.size(); i++) {
            feedsList.add(addList.get(i));
        }
        return feedsList;
    }

    public List<SharkFeedEntity> getFeedsList() {
        return this.feedsList;
    }
}
