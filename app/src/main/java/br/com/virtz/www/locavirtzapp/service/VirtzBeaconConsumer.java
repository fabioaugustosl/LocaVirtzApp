package br.com.virtz.www.locavirtzapp.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.virtz.www.locavirtzapp.MonitoramentoBeaconActivity;
import br.com.virtz.www.locavirtzapp.db.LocaDBHelper;
import br.com.virtz.www.locavirtzapp.notifications.Notification;
import br.com.virtz.www.locavirtzapp.notifications.ShowNotifications;

/**
 * Esse cara fica monitorando os beacons em um determinado local.
 * Ele é quem fornece informações dos beacons, como: quais, quantos, e a distancia.
 *
 * O Loca Application fica observando a regiao.. se encontra algum beacon dispara
 * esse consumer para identificar quais beacons estão na área.
 *
 */
public class VirtzBeaconConsumer extends Service implements BeaconConsumer {

    private ShowNotifications notifications = null;
    private BeaconManager beaconManager = null;


    private List<String> beaconsEncontrados = null;


    @Override
    public void onCreate() {
        super.onCreate();
        beaconsEncontrados = new ArrayList<String>();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(beaconManager == null) {
            beaconManager = BeaconManager.getInstanceForApplication(this);
        }
        beaconManager.bind(this);
        notifications = new ShowNotifications();

        onBeaconServiceConnect();

        return Service.START_STICKY;
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
            if (beacons.size() > 0) {
                Beacon firstBeacon = beacons.iterator().next();

                if(!beaconsEncontrados.contains(firstBeacon.toString())) {
                    NumberFormat nb = NumberFormat.getInstance();
                    nb.setMaximumFractionDigits(2);

                    String txt = "Aproveite que você está passando perto do acougue! Promoção da alcatra, apenas R$19,90!  (" + firstBeacon.getDistance()+")";
                    MonitoramentoBeaconActivity m = new MonitoramentoBeaconActivity();
                    notifications.sendNotification(new Notification("Virtz Loca", txt, m, getApplicationContext()));
                    beaconsEncontrados.add(firstBeacon.toString());
                }

            } else {
                Log.d(this.getClass().toString(),"Não rolou nenhum beacon");
            }
            }

        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("backgroundRegion", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public Context getApplicationContext() {
        return this;
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        Log.d(this.getClass().getName(), "unbindService muito loco");
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return false;
    }


}
