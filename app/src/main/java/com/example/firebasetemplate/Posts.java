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

    private HashMap<String, Boolean> likes=new HashMap<>(); //el usuario y si le ha hecho fav al post

    public Posts(){
    }

    public Posts(String content, String authorName, String authorEmail, String datePost, String urlDescarga, String authorIcono) {//usuario con foto
        this.content = content;
        this.authorName = authorName;
        this.authorEmail = authorEmail;
        this.datePost = datePost;
        this.urlDescarga = urlDescarga;
        this.authorIcono = authorIcono;
        this.likes=new HashMap<>();
    }

    public Posts(String content, String authorName, String authorPhoto, String datePost, String urlDescarga) {//usuario sin foto
        this.content = content;
        this.authorName = authorName;
        this.authorEmail = authorPhoto;
        this.datePost = datePost;
        this.urlDescarga = urlDescarga;
        this.likes=new HashMap<>();
    }


    public Posts(String content, String authorName, String authorPhoto, String datePost) {//usuario con foto sin fotos en el post
        this.content = content;
        this.authorName = authorName;
        this.authorEmail = authorPhoto;
        this.datePost = datePost;
        this.likes=new HashMap<>();
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
