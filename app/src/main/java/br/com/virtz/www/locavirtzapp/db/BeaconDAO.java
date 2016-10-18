package br.com.virtz.www.locavirtzapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.virtz.www.locavirtzapp.beans.BeaconBean;
import br.com.virtz.www.locavirtzapp.mapper.BeaconMapper;

public class BeaconDAO extends LocaDBHelper {

    public static final String BEACONS = "beacons";

    public BeaconDAO(Context context) {
        super(context);
    }

    public boolean novoBeacon(String nome, String dono){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues content = new ContentValues();

        content.put("nome", nome);
        content.put("dono", dono);

        long resultado = db.insert(BEACONS, null, content);

        db.close();
        if(resultado != -1 ){
            return true;
        }else{
            return false;
        }
    }


    public List<BeaconBean> listarTodos(String dono){
        SQLiteDatabase db = this.getReadableDatabase();
        BeaconMapper mapper = new BeaconMapper();

        Cursor c = db.query(
                BEACONS,  // The table to query
                mapper.getColunas(),                    // The columns to return
                "dono = ?",                             // The columns for the WHERE clause
                new String[]{"virtz"},                  // The values for the WHERE clause
                null,                                   // don't group the rows
                null,                                   // don't filter by row groups
                null                                 // The sort order
        );

        Cursor cursor = db.rawQuery("SELECT _id, nome, dono FROM beacons",null);

        List<BeaconBean> beacons = mapper.cursorParaBeacons(cursor);

        db.close();

        return beacons;
    }


    public void remover(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        String where =  "id  LIKE ?";
        String[] whereArgs = { String.valueOf(id) };
        db.delete(BEACONS,where,whereArgs);
        db.close();

    }

}
