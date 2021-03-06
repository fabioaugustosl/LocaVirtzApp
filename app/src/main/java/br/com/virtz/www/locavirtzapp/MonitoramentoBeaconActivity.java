package br.com.virtz.www.locavirtzapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.altbeacon.beacon.BeaconManager;

import java.util.ArrayList;

import br.com.virtz.www.locavirtzapp.adapters.EventoAdapter;
import br.com.virtz.www.locavirtzapp.adapters.NoticiaAdapter;
import br.com.virtz.www.locavirtzapp.async.EventoListagemTask;
import br.com.virtz.www.locavirtzapp.async.NoticiaListagemTask;
import br.com.virtz.www.locavirtzapp.beans.BeaconBean;
import br.com.virtz.www.locavirtzapp.beans.EventoBean;
import br.com.virtz.www.locavirtzapp.beans.NoticiaBean;
import br.com.virtz.www.locavirtzapp.dialog.AlertaEventoActivity;
import br.com.virtz.www.locavirtzapp.service.VirtzBeaconConsumer;
import br.com.virtz.www.locavirtzapp.service.VirtzBeaconService;

public class MonitoramentoBeaconActivity extends LocaVirtzSuperActivity {

    protected static final String TAG = "MonitoramentoBeaconActivity";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private NoticiaAdapter noticiaAdapter = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoramento_beacon);

        verifyBluetooth();

        TextView txtM = (TextView) findViewById(R.id.textoMonitorando);
        txtM.setText("Ande por todo o ambiente. Estamos monitorando coisas boas perto de você.");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Essa app precisa de localização.");
                builder.setMessage("Please grant location access so this app can detect beacons in the background.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @TargetApi(23)
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                PERMISSION_REQUEST_COARSE_LOCATION);
                    }

                });
                builder.show();
            }
        }


        ListView listaView = (ListView) findViewById(R.id.lista_noticias_monitoramento);
        noticiaAdapter = new NoticiaAdapter(this, new ArrayList<NoticiaBean>());
        listaView.setAdapter(noticiaAdapter);

        new NoticiaListagemTask(this, noticiaAdapter).execute();

    }

    @Override
    protected void onPause() {
        super.onPause();
        initServiceBeacon(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        initServiceBeacon(null);

  //      initServiceBeacon(null);

//        Intent service = new Intent(this.getApplicationContext(),VirtzBeaconConsumer.class);
//        this.startService(service);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Funcionamento limitado");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    public void irParaMonitamentoBeacons(View view) {
        Intent intent = new Intent(getApplicationContext(), AlertaEventoActivity.class);
        startActivity(intent);
        //Intent myIntent = new Intent(this, InfoBeaconActivity.class);
        //this.startActivity(myIntent);
    }



    @Override
    public void onResume() {
        super.onResume();
        //((LocaApplication) this.getApplicationContext()).setMonitoringActivity(this);
   //     stopServiceBeacon(null);
    }


    private void verifyBluetooth() {

        try {
            if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Bluetooth não habilitado");
                builder.setMessage("Por favor habilite o bluetooth e reinicie o app.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                        System.exit(0);
                    }
                });
                builder.show();
            }
        }
        catch (RuntimeException e) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth LE não disponível.");
            builder.setMessage("Sorry, this device does not support Bluetooth LE.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                    System.exit(0);
                }

            });
            builder.show();

        }

    }

    public void logToDisplay(final String line) {
        runOnUiThread(new Runnable() {
            public void run() {
                TextView text = (TextView)MonitoramentoBeaconActivity.this.findViewById(R.id.textoMonitorando);
                text.setText(line);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_monitoramento_beacon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
