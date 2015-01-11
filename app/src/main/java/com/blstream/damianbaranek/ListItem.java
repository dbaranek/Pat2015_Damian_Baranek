package com.blstream.damianbaranek;

import android.graphics.Bitmap;

import org.json.JSONObject;

public class ListItem {
    public Bitmap imageBitmap;
    public String title;
    public String desc;
    public String imageURL;

    public ListItem(JSONObject jsonObject, Bitmap imageBitmap) {
        this.title = jsonObject.optString("title");
        this.desc = jsonObject.optString("desc");
        this.imageURL = jsonObject.optString("url");
        this.imageBitmap = imageBitmap;
    }
}
