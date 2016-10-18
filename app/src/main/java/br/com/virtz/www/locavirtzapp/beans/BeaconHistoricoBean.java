package br.com.virtz.www.locavirtzapp.beans;


import java.io.Serializable;
import java.util.Date;

public class BeaconHistoricoBean implements Serializable{

    private String id;
    private Date data;
    private String idBeacon;
    private String nomeBeacon;
    private Double distanciaBeacon;

    public BeaconHistoricoBean() {
    }

    public BeaconHistoricoBean(String id, Date data, String idBeacon, String nomeBeacon, Double distanciaBeacon) {
        this.id = id;
        this.data = data;
        this.idBeacon = idBeacon;
        this.nomeBeacon = nomeBeacon;
        this.distanciaBeacon = distanciaBeacon;

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getIdBeacon() {
        return idBeacon;
    }

    public void setIdBeacon(String idBeacon) {
        this.idBeacon = idBeacon;
    }

    public String getNomeBeacon() {
        return nomeBeacon;
    }

    public void setNomeBeacon(String nomeBeacon) {
        this.nomeBeacon = nomeBeacon;
    }

    public Double getDistanciaBeacon() {
        return distanciaBeacon;
    }

    public void setDistanciaBeacon(Double distanciaBeacon) {
        this.distanciaBeacon = distanciaBeacon;
    }
}
