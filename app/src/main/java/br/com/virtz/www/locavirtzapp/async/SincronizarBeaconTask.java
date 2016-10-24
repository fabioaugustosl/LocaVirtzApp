package br.com.virtz.www.locavirtzapp.async;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
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

        List<BeaconBean> beaconsBanco = beaconDAO.listarTodos("virtz");
        if(beaconsBanco == null){
            beaconsBanco = new ArrayList<BeaconBean>();
        }


        String respostaJson = consume.doGet("https://beaconbkendvirtz.appspot.com/_ah/api/beaconService/v1/dono/virtz");
        List<BeaconBean> beaconsAtuais = null;
        try {
            beaconsAtuais = jsonUtil.jsonToBeacon(respostaJson);
        } catch (JSONException e) {
            beaconsAtuais = new ArrayList<BeaconBean>();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        // beaconsAtuais.add(new BeaconBean("1", "id1: 003e8c80-ea01-4ebb-b888-78da19df9e55 id2: 768 id3: 1117", "virtz"));


        // percorre os beacons que existem no banco e não existem mais na nuvem
        for(BeaconBean b : beaconsBanco){
            boolean deveSerApagado = true;
            for(BeaconBean atual : beaconsAtuais){
                if(atual.getNome().equals(b.getNome())){
                    deveSerApagado = false; // não precisa ser apagado pq existe na nuvem tambem
                    break;
                }
            }
            if(deveSerApagado){
                beaconsRemover.add(b);
            }
        }

        // percorre os beacons que existem na nuvem e não existem mais no banco
        for(BeaconBean atual : beaconsAtuais){
            boolean deveSerInserido = true;
            for(BeaconBean b : beaconsBanco){
                if(atual.getNome().equals(b.getNome())){
                    deveSerInserido = false; // Já existe no banco então não precisa ser inserido novamento
                    break;
                }
            }
            if(deveSerInserido){
                beaconsInserir.add(atual);
            }
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
