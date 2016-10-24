package br.com.virtz.www.locavirtzapp.beans;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EventoBean implements Serializable {

    public static final String TIPO_EVENTO_NOTIFICACAO = "NOTIFICACAO";
    public static final String TIPO_EVENTO_VIDEO = "VIDEO";
    public static final String TIPO_EVENTO_IMAGEM = "IMAGEM";
    public static final String TIPO_EVENTO_SITE = "SITE";



    @SerializedName("id")
    private String id;

    @SerializedName("beacon")
    private String beacon;

    @SerializedName("dono")
    private String dono;

    @SerializedName("distanciaMinima")
    private Double distanciaMinima;

    @SerializedName("tipoEvento")
    private String tipoEvento;

    @SerializedName("titulo")
    private String titulo;

    @SerializedName("urlImagem")
    private String urlImagem;

    @SerializedName("urlVideo")
    private String urlVideo;

    @SerializedName("urlSite")
    private String urlSite;

    @SerializedName("textoNotificacao")
    private String textoNotificacao;

    @SerializedName("textoExtra")
    private String textoExtra;

    private String texto;


    public EventoBean() {
    }


    public EventoBean(String id, String dono, String beacon, Double distanciaMinima, String tipoEvento, String titulo, String texto, String textoExtra) {
        this.id = id;
        this.dono = dono;
        this.beacon = beacon;
        this.distanciaMinima = distanciaMinima;
        this.tipoEvento = tipoEvento;
        this.titulo = titulo;
        this.texto = texto;
        this.textoExtra = textoExtra;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBeacon() {
        return beacon;
    }

    public void setBeacon(String beacon) {
        this.beacon = beacon;
    }

    public String getDono() {
        return dono;
    }

    public void setDono(String dono) {
        this.dono = dono;
    }

    public Double getDistanciaMinima() {
        return distanciaMinima;
    }

    public void setDistanciaMinima(Double distanciaMinima) {
        this.distanciaMinima = distanciaMinima;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getTexto() {
        if(texto == null){
            if(TIPO_EVENTO_IMAGEM.equals(getTipoEvento())){
                return getUrlImagem();
            } else if(TIPO_EVENTO_VIDEO.equals(getTipoEvento())){
                return getUrlVideo();
            } else if(TIPO_EVENTO_SITE.equals(getTipoEvento())){
                return getUrlSite();
            } else{
                return getTextoNotificacao();
            }
        }
        return texto;
    }

    public String getTextoDescricao() {
        if(texto == null){
            if(!TIPO_EVENTO_NOTIFICACAO.equals(getTipoEvento())){
                return getTextoExtra();
            } else {
                return getTextoNotificacao();
            }
        }
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public String getUrlSite() {
        return urlSite;
    }

    public void setUrlSite(String urlSite) {
        this.urlSite = urlSite;
    }

    public String getTextoNotificacao() {
        return textoNotificacao;
    }

    public void setTextoNotificacao(String textoNotificacao) {
        this.textoNotificacao = textoNotificacao;
    }

    public String getTextoExtra() {
        return textoExtra;
    }

    public void setTextoExtra(String textoExtra) {
        this.textoExtra = textoExtra;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}

