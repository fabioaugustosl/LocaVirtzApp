package br.com.virtz.www.locavirtzapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class LocaDBHelper extends SQLiteOpenHelper {

    private static final String BANCO_DADOS = "db_loca";
    private static  int VERSAO = 1;

    public LocaDBHelper(Context context){
        super(context, BANCO_DADOS, null, VERSAO);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //beacons
        db.execSQL("CREATE TABLE beacons (_id INTEGER PRIMARY KEY, nome TEXT, dono TEXT);");

        // beacons vistos - serve como histórico
        db.execSQL("CREATE TABLE beacons_historico (_id INTEGER PRIMARY KEY, data DATE DEFAULT (date('now','localtime')), " +
                " id_beacon Integer, nome_beacon TEXT, distancia_beacon REAL, nome_beacon_secundario TEXT, distancia_beacon_secundario REAL,"+
                " nome_beacon_secundario2 TEXT, distancia_beacon_secundario2, " +
                " FOREIGN KEY(id_beacon) REFERENCES beacons(_id));");

        // eventos
        db.execSQL("CREATE TABLE eventos (_id INTEGER PRIMARY KEY, id_beacon Integer, beacon TEXT, " +
                " dono TEXT, tipo TEXT, texto TEXT, distancia_minima REAL," +
                " FOREIGN KEY(id_beacon) REFERENCES beacons(_id));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("ALTER TABLE gasto ADD COLUMN pessoa TEXT");
    }
}
