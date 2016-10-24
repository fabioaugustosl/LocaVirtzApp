package br.com.virtz.www.locavirtzapp;


import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.Collection;

import br.com.virtz.www.locavirtzapp.async.SalvarHistoricoTask;
import br.com.virtz.www.locavirtzapp.async.SincronizarBeaconTask;
import br.com.virtz.www.locavirtzapp.async.SincronizarEventoTask;
import br.com.virtz.www.locavirtzapp.beans.BeaconHistoricoBean;
import br.com.virtz.www.locavirtzapp.notifications.Notification;
import br.com.virtz.www.locavirtzapp.notifications.ShowNotifications;
import br.com.virtz.www.locavirtzapp.service.VirtzBeaconConsumer;


public class LocaApplication extends Application implements BootstrapNotifier {

    private static final String TAG = "BeaconReferenceApp";
    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private boolean haveDetectedBeaconsSinceBoot = false;
    private MonitoramentoBeaconActivity monitoringActivity = null;
    private boolean estouRastreando = false;

    private BeaconManager beaconManager = null;
    private ShowNotifications notifications = null;

    private VirtzBeaconConsumer consumer = null;

    public void onCreate() {
        super.onCreate();
        this.estouRastreando = false;
        notifications = new ShowNotifications();
        beaconManager = BeaconManager.getInstanceForApplication(this);

        // By default the AndroidBeaconLibrary will only find AltBeacons.  If you wish to make it
        // find a different type of beacon, you must specify the byte layout for that beacon's
        // advertisement with a line like below.  The example shows how to find a beacon with the
        // same byte layout as AltBeacon but with a beaconTypeCode of 0xaabb.  To find the proper
        // layout expression for other beacon types, do a web search for "setBeaconLayout"
        // including the quotes.
        //
        beaconManager.getBeaconParsers().clear();
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));

        beaconManager.setForegroundScanPeriod(10000l);
        beaconManager.setBackgroundBetweenScanPeriod(8000l);
        beaconManager.setBackgroundScanPeriod(10000l);

        Region region = new Region("backgroundRegion", null, null, null);

        Log.d(TAG, "setting up background monitoring for beacons and power saving");
        // wake up the app when a beacon is seen

        regionBootstrap = new RegionBootstrap(this, region);


        //Intent service = new Intent(this,VirtzBeaconConsumer.class);
        //this.startService(service);

        // simply constructing this class and holding a reference to it in your custom Application
        // class will automatically cause the BeaconLibrary to save battery whenever the application
        // is not visible.  This reduces bluetooth power usage by about 60%
        backgroundPowerSaver = new BackgroundPowerSaver(this);

        // If you wish to test beacon detection in the Android Emulator, you can use code like this:
        // BeaconManager.setBeaconSimulator(new TimedBeaconSimulator() );
        // ((TimedBeaconSimulator) BeaconManager.getBeaconSimulator()).createTimedSimulatedBeacons();


        //INICIAR SINCRONIZADORES
        SincronizarBeaconTask beaconTask = new SincronizarBeaconTask(this);
        beaconTask.execute();

        SincronizarEventoTask eventoTask = new SincronizarEventoTask(this);
        eventoTask.execute();


        // EXEMPLO DE COMO SALVAR UM HISTORICO
       /* SalvarHistoricoTask histTask = new SalvarHistoricoTask(this);
        BeaconHistoricoBean h = new BeaconHistoricoBean();
        h.setNomeBeacon("003e8c80-ea01-4ebb-b888-78da19df9e55");

        histTask.execute(h);*/
    }



    @Override
    public void didEnterRegion(Region arg0) {
        // In this example, this class sends a notification to the user whenever a Beacon
        // matching a Region (defined above) are first seen.
        Log.d(TAG, "did enter region.");
        /*if (!haveDetectedBeaconsSinceBoot) {
            Log.d(TAG, "auto launching MainActivity");

            // The very first time since boot that we detect an beacon, we launch the
            // MainActivity
            if(monitoringActivity == null && !isEstouRastreando()){
                Intent intent = new Intent(this, MonitoramentoBeaconActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // Important:  make sure to add android:launchMode="singleInstance" in the manifest
                // to keep multiple copies of this activity from getting created if the user has
                // already manually launched the app.
                this.startActivity(intent);
            }

            haveDetectedBeaconsSinceBoot = true;
        } else {
            if (monitoringActivity != null) {
                // If the Monitoring Activity is visible, we log info about the beacons we have
                // seen on its display

                monitoringActivity.logToDisplay("Eu vejo um beacon de novo" );
            } else */

            if(!isEstouRastreando()) {
                // If we have already seen beacons before, but the monitoring activity is not in
                // the foreground, we send a notification to the user on subsequent detections.

                //Log.d(TAG, "Sending notification.");
                //sendNotification();

                Log.d(TAG, "start beacon consumer background");

                Intent service = new Intent(this.getApplicationContext(), VirtzBeaconConsumer.class);
                this.startService(service);
            }
//        }

    }


    @Override
    public void didExitRegion(Region region) {
       /* if (monitoringActivity != null) {
            monitoringActivity.logToDisplay("Eu não vejo nenhum beacon.");
        }*/
    }

    @Override
    public void didDetermineStateForRegion(int state, Region region) {
      /*  if (monitoringActivity != null) {
            this.estouRastreando = true;
            monitoringActivity.logToDisplay("Opa! Acabei de identificar uma alteração na quantidade de beacons encontrados: " + state);
        }*/
    }

    /**
     * Envia notificação que existe um beacon por perto.
     */
    private void sendNotification() {
        Intent intent = new Intent(this, MonitoramentoBeaconActivity.class);
        Notification notification = new Notification("Loca Virtz", "Um beacon está por perto.", intent, this);
        ShowNotifications show = new ShowNotifications();
        show.sendNotification(notification);
    }


    public void setMonitoringActivity(MonitoramentoBeaconActivity activity) {
        this.monitoringActivity = activity;
    }


    public boolean isEstouRastreando() {
        return estouRastreando;
    }

    public void setEstouRastreando(boolean estouRastreando) {
        this.estouRastreando = estouRastreando;
    }


}
