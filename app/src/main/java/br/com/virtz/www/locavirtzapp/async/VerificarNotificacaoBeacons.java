package br.com.virtz.www.locavirtzapp.async;

import android.content.Context;
import android.os.AsyncTask;

import org.altbeacon.beacon.Beacon;
import org.json.JSONException;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.virtz.www.locavirtzapp.MonitoramentoBeaconActivity;
import br.com.virtz.www.locavirtzapp.beans.BeaconBean;
import br.com.virtz.www.locavirtzapp.beans.BeaconHistoricoBean;
import br.com.virtz.www.locavirtzapp.beans.EventoBean;
import br.com.virtz.www.locavirtzapp.db.BeaconDAO;
import br.com.virtz.www.locavirtzapp.db.BeaconHistoricoDAO;
import br.com.virtz.www.locavirtzapp.db.EventoDAO;
import br.com.virtz.www.locavirtzapp.notifications.Notification;
import br.com.virtz.www.locavirtzapp.notifications.ShowNotifications;
import br.com.virtz.www.locavirtzapp.rest.ConsumeRest;
import br.com.virtz.www.locavirtzapp.rest.ParamRest;
import br.com.virtz.www.locavirtzapp.util.JsonUtil;

/**
 * Classe para abrir outra thread que recupera os beacons cadastrados e seus respectivos eventos.
 *
 * os voids abaixo são generics q significam:
 * Void (entrada no metodo doInBackground), Void(parametro dos metodos de progresso), Void (saida no metodo onPostExecute)
 *
 */
public class VerificarNotificacaoBeacons {

    private Context mContext;
    private ConsumeRest consume  = null;

    public VerificarNotificacaoBeacons(Context context){
        mContext = context;
        consume = new ConsumeRest();
    }


    public List<EventoBean> recuperarEventosCompativeis(BeaconBean... params) {

        BeaconDAO beaconDAO = new BeaconDAO(mContext);
        EventoDAO eventoDAO = new EventoDAO(mContext);

        // recupera o beacon mais proximo
        BeaconBean beacon = identificaBeaconMaisProximo(params);


        List<BeaconBean> beaconsBanco = beaconDAO.listarTodos(beacon.getDono());

        if(beaconsBanco == null){
            beaconsBanco = recuperarBeaconsRest();
            if(beaconsBanco == null){
             return null;
            }
        }
        // verificar se o beacon está cadastrado para aquele lugar (dono)
        List<BeaconBean> beaconsValidados = new ArrayList<BeaconBean>();
        for(BeaconBean b : beaconsBanco){
            if(b.getNome().equals(beacon.getNome())){
                beaconsValidados.add(beacon);
            }
        }

        if(!beaconsValidados.isEmpty()){
            List<EventoBean> eventos =  eventoDAO.listarTodos(beacon.getDono());
            List<EventoBean> eventosValidos = new ArrayList<EventoBean>();
            EventoBean evento = null;
            for(BeaconBean b : beaconsValidados){
                evento = recuperarEvento(eventos, b);
                if(evento != null && !eventosValidos.contains(evento)){
                    eventosValidos.add(evento);
                }
            }
            return eventosValidos;
        }
        return null;
    }


    private BeaconBean identificaBeaconMaisProximo(BeaconBean[] params) {
        BeaconBean beacon = null;
        for(int i = 0; i < params.length; i++){
            if(beacon == null) {
                beacon = params[i];
                continue;
            }

            BeaconBean b = params[i];
            if(beacon.getDistanciaAtual() > b.getDistanciaAtual()){
                beacon = b;
            }
        }
        return beacon;
    }

    private  List<BeaconBean> recuperarBeaconsRest() {
        JsonUtil jsonUtil = new JsonUtil();
        String respostaJson = consume.doGet("https://beaconbkendvirtz.appspot.com/_ah/api/beaconService/v1/dono/virtz");
        List<BeaconBean> beaconsAtuais = null;
        try {
            beaconsAtuais = jsonUtil.jsonToBeacon(respostaJson);
        } catch (JSONException e) {
            beaconsAtuais = new ArrayList<BeaconBean>();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return beaconsAtuais;
    }


    private EventoBean recuperarEvento(List<EventoBean> eventos, BeaconBean beacon) {
        for(EventoBean e : eventos){
            if(e.getBeacon().equals(beacon.getNome()) && e.getDistanciaMinima() >= beacon.getDistanciaAtual()){
                return e;
            }
        }
        return null;
    }


}
