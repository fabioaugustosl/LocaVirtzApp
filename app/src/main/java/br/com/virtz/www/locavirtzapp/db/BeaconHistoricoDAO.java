package br.com.virtz.www.locavirtzapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;
import java.util.List;

import br.com.virtz.www.locavirtzapp.beans.BeaconBean;
import br.com.virtz.www.locavirtzapp.beans.BeaconHistoricoBean;
import br.com.virtz.www.locavirtzapp.mapper.BeaconHistoricoMapper;
import br.com.virtz.www.locavirtzapp.mapper.BeaconMapper;

public class BeaconHistoricoDAO extends LocaDBHelper {

    public static final String BEACONS_HISTORICO = "beacons_historico";

    public BeaconHistoricoDAO(Context context) {
        super(context);
    }

    public boolean novoHistorico(String nomeBeacon, Date data, Double distancia){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues content = new ContentValues();

        content.put("nome_beacon", nomeBeacon);
        content.put("data", data.getTime());
        if(distancia != null) {
            content.put("distancia_beacon", distancia.toString());
        }

        long resultado = db.insert(BEACONS_HISTORICO, null, content);

        db.close();
        if(resultado > 0l ){
            return true;
        }else{
            return false;
        }
    }


    public List<BeaconHistoricoBean> listarTodos(String nomeBeacon){
        SQLiteDatabase db = this.getReadableDatabase();
        BeaconHistoricoMapper mapper = new BeaconHistoricoMapper();

        Cursor c = db.query(
                BEACONS_HISTORICO,  // The table to query
                mapper.getColunas(),                    // The columns to return
                "nome_beacon = ?",                            // The columns for the WHERE clause
                new String[]{nomeBeacon},                  // The values for the WHERE clause
                null,                                   // don't group the rows
                null,                                   // don't filter by row groups
                null                                 // The sort order
        );

      //  Cursor cursor = db.rawQuery("SELECT _id, nome, dono, distancia_minima FROM beacons",null);

        List<BeaconHistoricoBean> beacons = mapper.cursorParaBeaconsHistorico(c);

        db.close();

        return beacons;
    }


    public void remover(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        String where =  "id LIKE ?";
        String[] whereArgs = { String.valueOf(id) };
        db.delete(BEACONS_HISTORICO,where,whereArgs);
        db.close();

    }

}
