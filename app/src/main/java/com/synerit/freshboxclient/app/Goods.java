package com.synerit.freshboxclient.app;

/**
 * Created by jinne on 23.05.2017.
 */

public class Goods {

        private String item_desc, item_title, item_image, seller_id, category;
        private long item_cost, likes, visitors, orders;
//        private Date date;

        public Goods(){

        }

        public Goods(String item_desc, String item_title, String item_image, String seller_id, String user_name, String category, long item_cost, long likes, long visitors, long orders){

            this.item_desc = item_desc;
            this.item_title = item_title;
            this.item_image = item_image;
            this.seller_id = seller_id;
            this.category = category;
            this.item_cost = item_cost;
            this.likes = likes;
            this.visitors = visitors;
            this.orders = orders;

        }

    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }

    public String getItem_title() {
        return item_title;
    }

    public void setItem_title(String item_title) {
        this.item_title = item_title;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getItem_cost() {
        return item_cost;
    }

    public void setItem_cost(long item_cost) {
        this.item_cost = item_cost;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public long getVisitors() {
        return visitors;
    }

    public void setVisitors(long visitors) {
        this.visitors = visitors;
    }

    public long getOrders() {
        return orders;
    }

    public void setOrders(long orders) {
        this.orders = orders;
    }
}
