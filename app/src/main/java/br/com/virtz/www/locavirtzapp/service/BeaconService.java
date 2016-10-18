package br.com.virtz.www.locavirtzapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import br.com.virtz.www.locavirtzapp.beans.BeaconBean;
import br.com.virtz.www.locavirtzapp.db.BeaconDAO;


public class BeaconService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        BeaconDAO beaconDAO = new BeaconDAO(this);

        List<BeaconBean> beaconsRemover = new ArrayList<BeaconBean>();
        List<BeaconBean> beaconsInserir = new ArrayList<BeaconBean>();

        List<BeaconBean> beaconsAtuais = new ArrayList<BeaconBean>();
        beaconsAtuais.add(new BeaconBean("1", "id1: 003e8c80-ea01-4ebb-b888-78da19df9e55 id2: 768 id3: 1117", "virtz"));

        List<BeaconBean> beaconsBanco = beaconDAO.listarTodos("virtz");

        // percorre os beacons que existem no banco e não existem mais na nuvem
        for(BeaconBean b : beaconsBanco){
            for(BeaconBean atual : beaconsAtuais){
                if(atual.getNome().equals(b.getNome())){
                    continue;
                }
            }
            beaconsRemover.add(b);
        }

        // percorre os beacons que existem no banco e não existem mais na nuvem
        for(BeaconBean atual : beaconsAtuais){
            for(BeaconBean b : beaconsBanco){
                if(atual.getNome().equals(b.getNome())){
                    continue;
                }
            }
            beaconsInserir.add(atual);
        }

        // remove do banco os que não existem mais
        for(BeaconBean rem : beaconsRemover){
            beaconDAO.remover(rem.getId());
        }

        // insere no banco os novos
        for(BeaconBean ins : beaconsInserir){
            beaconDAO.novoBeacon(ins.getNome(), ins.getDono());
        }

        stopSelf();

        return Service.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
