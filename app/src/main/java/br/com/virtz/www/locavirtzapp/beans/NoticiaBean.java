package br.com.virtz.www.locavirtzapp.beans;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NoticiaBean implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("dono")
    private String dono;


    @SerializedName("titulo")
    private String titulo;

    @SerializedName("texto")
    private String texto;

    @SerializedName("textoAdicional")
    private String textoAdicional;

    @SerializedName("urlImagem")
    private String urlImagem;



    public NoticiaBean() {
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDono() {
        return dono;
    }

    public void setDono(String dono) {
        this.dono = dono;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getTextoAdicional() {
        return textoAdicional;
    }

    public void setTextoAdicional(String textoAdicional) {
        this.textoAdicional = textoAdicional;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }
}

