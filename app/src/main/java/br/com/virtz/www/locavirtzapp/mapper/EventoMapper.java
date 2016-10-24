package br.com.virtz.www.locavirtzapp.mapper;


import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.virtz.www.locavirtzapp.beans.BeaconBean;
import br.com.virtz.www.locavirtzapp.beans.EventoBean;

public class EventoMapper {


    private String[] colunas = {
            "_id",
            "beacon",
            "dono",
            "tipo",
            "titulo",
            "texto",
            "textoExtra",
            "distancia_minima"
    };

    public List<EventoBean> cursorParaEvento(Cursor cursor){

        List<EventoBean> eventos = new ArrayList<EventoBean>();
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
            String beacon = cursor.getString(1);
            String dono = cursor.getString(2);
            String tipo = cursor.getString(3);
            String titulo = cursor.getString(4);
            String texto = cursor.getString(5);
            String textoExtra = cursor.getString(6);
            double distanciaMinima = cursor.getDouble(7);

            EventoBean e = new EventoBean(id, dono, beacon, distanciaMinima, tipo, titulo, texto, textoExtra);
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
