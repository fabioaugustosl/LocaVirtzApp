package br.com.virtz.www.locavirtzapp.service;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.virtz.www.locavirtzapp.DetalheEventoActivity;
import br.com.virtz.www.locavirtzapp.LocaApplication;
import br.com.virtz.www.locavirtzapp.MonitoramentoBeaconActivity;
import br.com.virtz.www.locavirtzapp.async.VerificarNotificacaoBeacons;
import br.com.virtz.www.locavirtzapp.beans.BeaconBean;
import br.com.virtz.www.locavirtzapp.beans.EventoBean;
import br.com.virtz.www.locavirtzapp.dialog.AlertaEvento;
import br.com.virtz.www.locavirtzapp.dialog.AlertaEventoActivity;
import br.com.virtz.www.locavirtzapp.mapper.BeaconMapper;
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


    private Map<String, Date> beaconsEncontrados = null;


    @Override
    public void onCreate() {
        super.onCreate();
        beaconsEncontrados = new HashMap<String, Date>();
        ((LocaApplication)getApplication()).setEstouRastreando(true);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((LocaApplication)getApplication()).setEstouRastreando(false);
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

                    List<Beacon> beaconsPermitidos = retiraBeaconsJaEncontrados(beacons);

                    if (beaconsPermitidos != null && !beaconsPermitidos.isEmpty()) {

                        boolean appEstahAberto = appAberto(getApplicationContext());

                        BeaconMapper mapper = new BeaconMapper();

                        List<BeaconBean> beaconsBean = mapper.beaconParaBeaconsBean(beaconsPermitidos);
                        VerificarNotificacaoBeacons notificacaoTask = new VerificarNotificacaoBeacons(getApplicationContext());
                        List<EventoBean> eventos = notificacaoTask.recuperarEventosCompativeis(beaconsBean.toArray(new BeaconBean[]{}));

                        if(eventos != null && !eventos.isEmpty()){
                            for(EventoBean evento: eventos){
                                beaconsEncontrados.put(evento.getBeacon(), new Date());

                                if(appEstahAberto){
                                    final EventoBean eventoAlerta = evento;

                                    Intent intent = new Intent(getApplicationContext(), AlertaEventoActivity.class);
                                    intent.putExtra("EVENTO", evento);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } else {
                                    String texto = "";
                                    Intent intent = null;
                                    if("NOTIFICACAO".equals(evento.getTipoEvento())) {
                                        texto = evento.getTexto();
                                        intent = new Intent(getApplicationContext(), DetalheEventoActivity.class);
                                        intent.putExtra("EVENTO", evento);
                                    } else {
                                        texto = evento.getTextoExtra();
                                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(evento.getTexto()));
                                        if("IMAGEM".equals(evento.getId())){
                                            intent.setType("image/*");
                                            intent.setAction(Intent.ACTION_GET_CONTENT);
                                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                                        }
                                    }

                                    Notification notification = new Notification(evento.getTitulo(), texto, intent, getApplicationContext());
                                    ShowNotifications show = new ShowNotifications();
                                    show.sendNotification(notification);
                                }
                            }
                        }

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



    private List<Beacon>  retiraBeaconsJaEncontrados(Collection<Beacon> beacons) {
        Iterator<Beacon> itBeacon = beacons.iterator();
        List<Beacon> retorno = new ArrayList<Beacon>();

        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, -1);

        while(itBeacon.hasNext()){
            Beacon b = itBeacon.next();
            Date dataEncontrado =  beaconsEncontrados.get(b.getId1().toString());

            if(dataEncontrado == null || c.getTime().after(dataEncontrado)) {
                retorno.add(b);
            }
        }
        return retorno;
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
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return false;
    }


    public boolean appAberto(Context ctx) {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
                return true;
        }

        return false;
    }


}
