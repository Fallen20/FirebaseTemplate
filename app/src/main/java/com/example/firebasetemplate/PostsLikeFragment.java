package com.example.firebasetemplate;

import com.google.firebase.firestore.Query;

public class PostsLikeFragment extends PostsHomeFragment{

    Query setQuery(){
        return db.collection("posts").whereEqualTo("likes."+auth.getUid(), true);
    }
}
