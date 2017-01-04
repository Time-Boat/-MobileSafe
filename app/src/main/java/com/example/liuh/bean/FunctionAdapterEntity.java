package com.example.liuh.bean;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016-09-19.
 */
public class FunctionAdapterEntity {

    private Bitmap pic;
    private String title;
    private String subhead;


    public Bitmap getPic() {
        return pic;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubhead() {
        return subhead;
    }

    public void setSubhead(String subhead) {
        this.subhead = subhead;
    }
}
