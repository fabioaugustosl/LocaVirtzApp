package br.com.virtz.www.locavirtzapp;

import android.app.Activity;
import android.content.Intent;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;

import br.com.virtz.www.locavirtzapp.db.LocaDBHelper;
import br.com.virtz.www.locavirtzapp.service.VirtzBeaconConsumer;

public class InfoBeaconActivity extends Activity implements BeaconConsumer {

    protected static final String TAG = "InfoBeaconActivity";
    private BeaconManager beaconManager = null;

    private LocaDBHelper helper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_beacon);
        try{
            Intent service = new Intent(this.getApplicationContext(),VirtzBeaconConsumer.class);
            this.stopService(service);
        }catch (Exception e){
            e.printStackTrace();
        }
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.bind(this);

        helper = new LocaDBHelper(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
        ((LocaApplication) this.getApplicationContext()).setEstouRastreando(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((LocaApplication) this.getApplicationContext()).setEstouRastreando(false);
        if (beaconManager.isBound(this)) {
            beaconManager.setBackgroundMode(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((LocaApplication) this.getApplicationContext()).setEstouRastreando(true);
        if (beaconManager.isBound(this)) {
            beaconManager.setBackgroundMode(false);
        }
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    Beacon firstBeacon = beacons.iterator().next();

                    logToDisplay(firstBeacon.toString(), firstBeacon.getDistance() );
                } else {
                    Log.d(TAG,"Não rolou nenhum beacon");
                }
            }

        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("virtzRegionID", null, null, null));
        } catch (RemoteException e) {   }
    }

    private void logToDisplay(final String nome, final Double distancia) {
        runOnUiThread(new Runnable() {
            public void run() {

            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2);

            TextView editText = (TextView)InfoBeaconActivity.this.findViewById(R.id.txtNomeBeacon);
            editText.setText("Beacon: "+nome);

            TextView dist = (TextView)InfoBeaconActivity.this.findViewById(R.id.txtDistanciaBeacon);
            dist.setText(nf.format(distancia));


            String txtLocal = "Cozinha";
            if(distancia > 1.5){
                txtLocal = "Sala";
            }
            TextView nome = (TextView)InfoBeaconActivity.this.findViewById(R.id.txtNomeLocal);
            nome.setText(txtLocal);
            }
        });
    }

}
