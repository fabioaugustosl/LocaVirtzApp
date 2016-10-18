package br.com.virtz.www.locavirtzapp.beans;


import java.io.Serializable;

public class EventoBean implements Serializable {

    private String id;
    private String beacon;
    private String dono;
    private Double distanciaMinima;
    private String tipoEvento;
    private String texto;


    public EventoBean() {
    }


    public EventoBean(String id, String dono, String beacon, Double distanciaMinima, String tipoEvento, String texto) {
        this.id = id;
        this.dono = dono;
        this.beacon = beacon;
        this.distanciaMinima = distanciaMinima;
        this.tipoEvento = tipoEvento;
        this.texto = texto;
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
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}

