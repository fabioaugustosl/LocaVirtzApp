package br.com.virtz.www.locavirtzapp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import br.com.virtz.www.locavirtzapp.service.VirtzBeaconConsumer;

public class LocaVirtzSuperActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public void initServiceBeacon(View view) {
        try {
            Intent myIntent = new Intent(this.getApplicationContext(), VirtzBeaconConsumer.class);
            this.startService(myIntent);
        }catch (Exception e){
            Log.d("LocaVirtzSuperActivity" , "Erro ao INICIAR o serviço de beacon consumer em background");
        }
    }


    public void stopServiceBeacon(View view) {
        try{
            Intent intentService = new Intent(this.getApplicationContext(), VirtzBeaconConsumer.class);
            stopService(intentService);
        }catch (Exception e){
            Log.d("LocaVirtzSuperActivity" , "Erro ao PARAR o serviço de beacon consumer em background");
        }
    }

}
