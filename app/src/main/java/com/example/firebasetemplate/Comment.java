package com.example.firebasetemplate;

public class Comment {
    private String contenido;
    private String nombreUsuario;
   private String photo;

    public Comment(String contenido, String nombreUsuario, String photo) {
        this.contenido = contenido;
        this.nombreUsuario = nombreUsuario;
        this.photo = photo;
    }

    public Comment() {

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
}
