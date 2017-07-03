package com.synerit.freshboxclient.app;

import java.util.ArrayList;

/**
 * Created by jinne on 14.06.2017.
 */

public class DataHolder {

    private String categoryId;


    private static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance() {
        return holder;
    }


    public String getCategory() {
        return categoryId;
    }
    public void setCategory(String categoryId) {
        this.categoryId = categoryId;
    }

}