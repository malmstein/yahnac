package com.malmstein.hnews.model;

import com.firebase.client.Firebase;

public class HNewsFirebase {

    public Firebase itemFirebase(){
        return new Firebase("https://hacker-news.firebaseio.com/v0/item/");
    }

}
