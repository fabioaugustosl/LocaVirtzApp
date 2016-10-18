package br.com.virtz.www.locavirtzapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import br.com.virtz.www.locavirtzapp.beans.BeaconBean;
import br.com.virtz.www.locavirtzapp.beans.EventoBean;
import br.com.virtz.www.locavirtzapp.mapper.BeaconMapper;
import br.com.virtz.www.locavirtzapp.mapper.EventoMapper;

public class EventoDAO extends LocaDBHelper {

    public static final String EVENTOS = "eventos";

    public EventoDAO(Context context) {
        super(context);
    }

    public boolean novoEvento(Integer idBeacon, String beacon, String dono, String tipo, String texto, Double distanciaMinima){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues content = new ContentValues();

        content.put("id_beacon", idBeacon);
        content.put("beacon", beacon);
        content.put("dono", dono);
        content.put("tipo", tipo);
        content.put("texto", texto);
        content.put("distancia_minima", distanciaMinima.toString());

        long resultado = db.insert(EVENTOS, null, content);

        db.close();
        if(resultado != -1 ){
            return true;
        }else{
            return false;
        }
    }


    public List<EventoBean> listarTodos(String dono){
        SQLiteDatabase db = this.getReadableDatabase();
        EventoMapper mapper = new EventoMapper();

        Cursor c = db.query(
                EVENTOS,  // The table to query
                mapper.getColunas(),                    // The columns to return
                "dono = ?",                             // The columns for the WHERE clause
                new String[]{"virtz"},                  // The values for the WHERE clause
                null,                                   // don't group the rows
                null,                                   // don't filter by row groups
                null                                 // The sort order
        );

        //Cursor cursor = db.rawQuery("SELECT _id, nome, dono FROM beacons",null);

        List<EventoBean> beacons = mapper.cursorParaEvento(c);

        db.close();

        return beacons;
    }


    public void remover(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        String where =  "id  LIKE ?";
        String[] whereArgs = { String.valueOf(id) };
        db.delete(EVENTOS,where,whereArgs);
        db.close();

    }

}
