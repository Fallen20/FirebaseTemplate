package com.example.firebasetemplate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;

public class PostsMyFragment extends PostsHomeFragment{
    //solo los mios



    @Override
    Query setQuery(){
        return db.collection("posts").whereEqualTo("authorName", auth.getCurrentUser().getDisplayName());
    }
}
