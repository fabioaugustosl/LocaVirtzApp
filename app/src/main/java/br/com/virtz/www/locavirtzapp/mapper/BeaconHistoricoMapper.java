package br.com.virtz.www.locavirtzapp.mapper;


import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.virtz.www.locavirtzapp.beans.BeaconBean;
import br.com.virtz.www.locavirtzapp.beans.BeaconHistoricoBean;

public class BeaconHistoricoMapper {

    private String[] colunas = {
            "_id",
            "data",
            "nome_beacon",
            "distancia_beacon"
    };

    public List<BeaconHistoricoBean> cursorParaBeaconsHistorico(Cursor cursor){

        List<BeaconHistoricoBean> beacons = new ArrayList<BeaconHistoricoBean>();
        if(cursor!=null){
            cursor.moveToFirst();
        } else {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY", Locale.getDefault());

        do {
            String id = cursor.getString(0);
            Date data = null;
            try {
                data = sdf.parse(cursor.getString(1));
            }catch (ParseException pe){}
            String nomeBeacon = cursor.getString(2);
            Double distanciaBeacon = cursor.getDouble(3);

            BeaconHistoricoBean b = new BeaconHistoricoBean(id, data, nomeBeacon, distanciaBeacon);
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
