package com.example.firebasetemplate;

public class Posts {

    private String content;
    private String authorName;
    private String authorEmail;
    private String datePost;
    private String urlDescarga;
    private String authorIcono;

    public Posts(){
    }

    public String getAuthorEmail() {
        return authorEmail;
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

    public Posts(String content, String authorName, String authorPhoto, String datePost, String urlDescarga) {
        this.content = content;
        this.authorName = authorName;
        this.authorEmail = authorPhoto;
        this.datePost = datePost;
        this.urlDescarga = urlDescarga;
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
