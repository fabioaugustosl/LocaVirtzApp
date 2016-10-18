package br.com.virtz.www.locavirtzapp.mapper;


import android.database.Cursor;

import java.util.ArrayList;
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
            cursor.moveToFirst();
        } else {
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


    public String[] getColunas() {
        return colunas;
    }
}
