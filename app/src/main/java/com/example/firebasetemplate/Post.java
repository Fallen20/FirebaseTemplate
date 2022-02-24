package com.example.firebasetemplate;

import java.util.HashMap;

public class Post {

    private String id;



    private String content;
    private String authorName;
    private String authorEmail;
    private String datePost;
    private String urlImagenPost;
    private String authorIcono;

    private HashMap<String, Boolean> likes=new HashMap<>(); //el usuario y si le ha hecho fav al post

    public Post(){
        this.content=null;
        this.authorName=null;
        this.authorEmail=null;
        this.datePost=null;
        this.urlImagenPost =null;
        this.authorIcono=null;
    }


    public String getAuthorEmail() {
        return authorEmail;
    }

    public HashMap<String, Boolean> getLikes() {
        return likes;
    }

    public void setLikes(HashMap<String, Boolean> likes) {
        this.likes = likes;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getAuthorIcono() {
        return authorIcono;
    }

    public void setAuthorIcono(String authorIcono) {
        this.authorIcono = authorIcono;
    }

    public String getUrlImagenPost() {
        return urlImagenPost;
    }

    public void setUrlImagenPost(String urlImagenPost) {
        this.urlImagenPost = urlImagenPost;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getDatePost() {
        return datePost;
    }

    public void setDatePost(String datePost) {
        this.datePost = datePost;
    }
}
