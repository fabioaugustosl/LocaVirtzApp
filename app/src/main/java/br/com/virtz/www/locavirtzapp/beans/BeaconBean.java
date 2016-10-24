package br.com.virtz.www.locavirtzapp.beans;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BeaconBean implements Serializable{

    @SerializedName("id")
    private String id;

    @SerializedName("nome")
    private String nome;

    @SerializedName("dono")
    private String dono;


    private Double distanciaAtual;


    public BeaconBean() {
    }

    public BeaconBean(String id, String nome, String dono) {
        this.id = id;
        this.nome = nome;
        this.dono = dono;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDono() {
        return dono;
    }

    public void setDono(String dono) {
        this.dono = dono;
    }

    public Double getDistanciaAtual() {
        return distanciaAtual;
    }

    public void setDistanciaAtual(Double distanciaAtual) {
        this.distanciaAtual = distanciaAtual;
    }
}
