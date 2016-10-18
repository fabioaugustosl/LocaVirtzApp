package br.com.virtz.www.locavirtzapp.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;


public class VirtzBeaconService extends Service implements BeaconConsumer {
    public static final String SERVICE_BEACON = "ServiceBeacon";
    private BeaconManager beaconManager;
    private AudioManager audioManager;
    private SharedPreferences sharedPreferences;


    // oncreate eh chamada uma unica vez quando o serviço eh criado
    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.setBackgroundMode(true);
        beaconManager.getBeaconParsers().clear();
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
        //beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);
    }

    // chamado toda vez que start o service
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Serviço de pesquisa de beacons iniciado.", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Serviço foi destuido.", Toast.LENGTH_SHORT).show();
        beaconManager.unbind(this);
        super.onDestroy();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.d(SERVICE_BEACON, "I just saw an beacon for the first time!");
                Log.d(SERVICE_BEACON, "Region id - " + region.getUniqueId());

                if (audioManager.getRingerMode() != AudioManager.RINGER_MODE_VIBRATE) {
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    Log.d(SERVICE_BEACON, "RINGER_MODE_VIBRATE set");
                }
                boolean switchSoundOn = sharedPreferences.getBoolean("turn_sound_on", false);
                if (!switchSoundOn) {
                    stopSelf();
                }
            }

            @Override
            public void didExitRegion(Region region) {
                boolean switchSoundOn = sharedPreferences.getBoolean("turn_sound_on", false);
                if (switchSoundOn) {
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                }

                Log.d(SERVICE_BEACON, "I no longer see a beacon");
                Log.d(SERVICE_BEACON, "Region id - " + region.getUniqueId());
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
            }
        });

        try {
            Identifier identifier = Identifier.parse("E2C56DB5-DFFB-48D2-B060-D0F5A71096E0"); //mini beacon
            //Identifier identifier2 = Identifier.parse("F7826DA6-4FA2-4E98-8024-BC5B71E0893E"); //kontakt
            beaconManager.startMonitoringBeaconsInRegion(new Region("virtzRegionService", identifier, null, null));
            //beaconManager.startMonitoringBeaconsInRegion(new Region(Constants.REGION2_ID, identifier2, null, null));
        } catch (RemoteException e) {
            Log.e(SERVICE_BEACON, e.getMessage(), e);
        }
    }

}
