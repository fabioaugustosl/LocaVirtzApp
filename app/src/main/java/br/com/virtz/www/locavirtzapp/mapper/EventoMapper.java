package br.com.virtz.www.locavirtzapp.mapper;


import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.virtz.www.locavirtzapp.beans.BeaconBean;
import br.com.virtz.www.locavirtzapp.beans.EventoBean;

public class EventoMapper {


    private String[] colunas = {
            "_id",
            "id_beacon",
            "beacon",
            "dono",
            "tipo",
            "texto",
            "distancia_minima"
    };

    public List<EventoBean> cursorParaEvento(Cursor cursor){

        List<EventoBean> eventos = new ArrayList<EventoBean>();
        if(cursor!=null){
            cursor.moveToFirst();
        } else {
            return null;
        }

        do {
            String id = cursor.getString(0);
            String beacon = cursor.getString(2);
            String dono = cursor.getString(3);
            String tipo = cursor.getString(4);
            String texto = cursor.getString(5);
            double distanciaMinima = cursor.getDouble(6);

            EventoBean e = new EventoBean(id, dono, beacon, distanciaMinima, tipo, texto);
            eventos.add(e);
        } while (cursor.moveToNext());
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return eventos;
    }


    public String[] getColunas() {
        return colunas;
    }
}
