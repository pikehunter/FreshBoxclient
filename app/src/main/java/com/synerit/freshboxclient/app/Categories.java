package com.synerit.freshboxclient.app;


/**
 * Created by jinne on 08.06.2017.
 */

public class Categories {

    private String name, image;

    public Categories(){

    }

    public Categories(String name, String image){

        this.name = name;
        this.image = image;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
