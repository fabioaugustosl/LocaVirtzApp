package br.com.virtz.www.locavirtzapp.mapper;


import android.database.Cursor;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.virtz.www.locavirtzapp.beans.BeaconBean;

public class BeaconMapper {

    private String[] colunas = {
            "_id",
            "nome",
            "dono"
    };

    public List<BeaconBean> cursorParaBeacons(Cursor cursor){

        List<BeaconBean> beacons = new ArrayList<BeaconBean>();
        if(cursor!=null){
            try {
                cursor.moveToFirst();
            }catch (Exception e){
                return null;
            }
        } else {
            return null;
        }

        // se não tiver nenhum no banco ainda
        if(!cursor.isFirst()){
            return null;
        }

        do {

            String id = cursor.getString(0);
            String nome = cursor.getString(1);
            String dono = cursor.getString(2);
           // double distanciaMinima = cursor.getDouble(3);

            BeaconBean b = new BeaconBean(id, nome, dono);
            beacons.add(b);
        } while (cursor.moveToNext());
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return beacons;
    }


    public List<BeaconBean> beaconParaBeaconsBean(List<Beacon> beacons){

        List<BeaconBean> beaconsNovos = new ArrayList<BeaconBean>();

        for(Beacon b : beacons){
            BeaconBean bNovo = new BeaconBean();
            bNovo.setDono("virtz");
            bNovo.setNome(b.getId1().toString());
            bNovo.setDistanciaAtual(b.getDistance());

            beaconsNovos.add(bNovo);
        }

        return beaconsNovos;
    }


    public String[] getColunas() {
        return colunas;
    }
}
