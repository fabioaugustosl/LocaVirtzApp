package br.com.virtz.www.locavirtzapp.async;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.virtz.www.locavirtzapp.beans.BeaconBean;
import br.com.virtz.www.locavirtzapp.beans.EventoBean;
import br.com.virtz.www.locavirtzapp.db.BeaconDAO;
import br.com.virtz.www.locavirtzapp.db.EventoDAO;
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
public class SincronizarEventoTask extends AsyncTask<Void,Void,Void> {

    private Context mContext;
    private ConsumeRest consume  = null;

    public SincronizarEventoTask(Context context){
        mContext = context;
        consume = new ConsumeRest();
    }


    @Override
    protected Void doInBackground(Void... params) {


        EventoDAO eventoDAO = new EventoDAO(mContext);
        JsonUtil jsonUtil = new JsonUtil();

        List<EventoBean> eventosRemover = new ArrayList<EventoBean>();
        List<EventoBean> eventosInserir = new ArrayList<EventoBean>();

        List<EventoBean> eventosBanco = eventoDAO.listarTodos("virtz");
        if(eventosBanco == null){
            eventosBanco = new ArrayList<EventoBean>();
        }

        String respostaJson = consume.doGet("https://beaconbkendvirtz.appspot.com/_ah/api/eventoService/v1/dono/virtz");
        List<EventoBean> beaconsAtuais = null;
        try {
            beaconsAtuais = jsonUtil.jsonToEvento(respostaJson);
        } catch (Exception e) {
            e.printStackTrace();
            beaconsAtuais = new ArrayList<EventoBean>();
        }

        // beaconsAtuais.add(new BeaconBean("1", "id1: 003e8c80-ea01-4ebb-b888-78da19df9e55 id2: 768 id3: 1117", "virtz"));

        // percorre os beacons que existem no banco e não existem mais na nuvem
        for(EventoBean b : eventosBanco){
            boolean deveSerApagado = true;
            for(EventoBean atual : beaconsAtuais){
                if(atual.getId().equals(b.getId())){
                    deveSerApagado = false; // não precisa ser apagado pq existe na nuvem tambem
                    break;
                }
            }
            if(deveSerApagado){
                eventosRemover.add(b);
            }
        }

        // percorre os beacons que existem na nuvem e não existem mais no banco
        for(EventoBean atual : beaconsAtuais){
            boolean deveSerInserido = true;
            for(EventoBean b : eventosBanco){
                if(atual.getId().equals(b.getId())){
                    deveSerInserido = false; // Já existe no banco então não precisa ser inserido novamento
                    break;
                }
            }
            if(deveSerInserido){
                eventosInserir.add(atual);
            }
        }


        // remove do banco os que não existem mais
        for(EventoBean rem : eventosRemover){
            eventoDAO.remover(rem.getId());
        }

        // insere no banco os novos
        for(EventoBean ins : eventosInserir){
            eventoDAO.novoEvento(ins.getBeacon(), ins.getDono(), ins.getTipoEvento(), ins.getTitulo(), ins.getTexto(), ins.getTextoExtra(), ins.getDistanciaMinima());
        }

        return null;
    }
}
