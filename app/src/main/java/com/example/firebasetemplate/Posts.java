package com.example.firebasetemplate;

import java.util.HashMap;

public class Posts {

    private String id;



    private String content;
    private String authorName;
    private String authorEmail;
    private String datePost;
    private String urlDescarga;
    private String authorIcono;

    public HashMap<String, Boolean> likes; //el usuario y si le ha hecho fav al post

    public Posts(){
    }

    public Posts(String content, String authorName, String authorEmail, String datePost, String urlDescarga, String authorIcono) {
        this.content = content;
        this.authorName = authorName;
        this.authorEmail = authorEmail;
        this.datePost = datePost;
        this.urlDescarga = urlDescarga;
        this.authorIcono = authorIcono;
    }

    public Posts(String content, String authorName, String authorPhoto, String datePost, String urlDescarga) {
        this.content = content;
        this.authorName = authorName;
        this.authorEmail = authorPhoto;
        this.datePost = datePost;
        this.urlDescarga = urlDescarga;
    }

    public Posts(String content, String authorName, String authorPhoto, String datePost) {
        this.content = content;
        this.authorName = authorName;
        this.authorEmail = authorPhoto;
        this.datePost = datePost;
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

    public String getUrlDescarga() {
        return urlDescarga;
    }

    public void setUrlDescarga(String urlDescarga) {
        this.urlDescarga = urlDescarga;
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
