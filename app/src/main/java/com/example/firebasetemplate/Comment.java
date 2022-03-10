package com.example.firebasetemplate;

import java.util.HashMap;

public class Comment {


    private String commentid;
    private String contenido;
    private String nombreUsuario;
   private String photo;
   private String email;
   private String postid;
    private HashMap<String, Boolean> likes=new HashMap<>(); //el usuario y si le ha hecho fav al post

    public Comment(String commentid, String contenido, String nombreUsuario, String photo, String email, String postid) {
        this.commentid = commentid;
        this.contenido = contenido;
        this.nombreUsuario = nombreUsuario;
        this.photo = photo;
        this.email = email;
        this.postid = postid;
    }

    public Comment() {

    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public HashMap<String, Boolean> getLikes() {
        return likes;
    }

    public void setLikes(HashMap<String, Boolean> likes) {
        this.likes = likes;
    }

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }
}
