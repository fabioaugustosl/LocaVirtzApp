package br.com.virtz.www.locavirtzapp.service;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import br.com.virtz.www.locavirtzapp.beans.BeaconBean;
import br.com.virtz.www.locavirtzapp.db.BeaconDAO;
import br.com.virtz.www.locavirtzapp.db.BeaconHistoricoDAO;

public class BeaconHistoricoService extends Service{

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        BeaconHistoricoDAO historicoDAO = new BeaconHistoricoDAO(this);

       /* String idBeacon = intent.getStringExtra("idBeacon");
        String nomeBeacon = intent.getStringExtra("nomeBeacon");
        historicoDAO.novoHistorico(idBeacon, nomeBeacon);*/

        stopSelf();

        return Service.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
