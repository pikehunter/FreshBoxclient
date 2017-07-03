package com.synerit.freshboxclient.app;


/**
 * Created by jinne on 08.06.2017.
 */

public class Sellers {

    private String seller_name, seller_image;

    public Sellers(){

    }

    public Sellers(String seller_name, String seller_image){

        this.seller_name = seller_name;
        this.seller_image = seller_image;

    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getSeller_image() {
        return seller_image;
    }

    public void setSeller_image(String seller_image) {
        this.seller_image = seller_image;
    }

}
