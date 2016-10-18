package br.com.virtz.www.locavirtzapp.async;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import br.com.virtz.www.locavirtzapp.beans.BeaconBean;
import br.com.virtz.www.locavirtzapp.db.BeaconDAO;
import br.com.virtz.www.locavirtzapp.rest.ConsumeRest;
import br.com.virtz.www.locavirtzapp.util.JsonUtil;

/**
 * Classe para abrir outra thread que recupere da nuvem os beacons cadastrados
 * e atualize o banco de dados sqlite.
 *
 * os voids abaixo são generics q significam:
 * Void (entrada no metodo doInBackground), Void(parametro dos metodos de progresso), Void (saida no metodo onPostExecute)
 *
 */
public class SincronizarBeaconTask extends AsyncTask<Void,Void,Void> {

    private Context mContext;
    private ConsumeRest consume  = null;

    public SincronizarBeaconTask (Context context){
        mContext = context;
        consume = new ConsumeRest();
    }


    @Override
    protected Void doInBackground(Void... params) {
        BeaconDAO beaconDAO = new BeaconDAO(mContext);
        JsonUtil jsonUtil = new JsonUtil();

        List<BeaconBean> beaconsRemover = new ArrayList<BeaconBean>();
        List<BeaconBean> beaconsInserir = new ArrayList<BeaconBean>();


        String respostaJson = consume.doGet("https://beaconbkendvirtz.appspot.com/_ah/api/beaconService/v1/dono/virtz");
        List<BeaconBean> beaconsAtuais = null;
        try {
            beaconsAtuais = jsonUtil.jsonToBeacon(respostaJson);
        } catch (JSONException e) {
            beaconsAtuais = new ArrayList<BeaconBean>();
        }

        // beaconsAtuais.add(new BeaconBean("1", "id1: 003e8c80-ea01-4ebb-b888-78da19df9e55 id2: 768 id3: 1117", "virtz"));

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

        return null;
    }
}
