package com.example.yuanjinxie.sharkfeed.model;

import java.io.Serializable;

public class SharkFeedEntity implements Serializable {
    private String title;
    private String thumbnail;
    private String hq_thumbnail;
    private String ID;


    public String getTitle() {
        return title;
    }

    public String getID() {
        return ID;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setID(String Id) {
        this.ID = Id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getHQ_Thumbnail() {
        return hq_thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setHigh_QThumbnail(String High_qthumbnail) {
        this.hq_thumbnail = High_qthumbnail;
    }

}