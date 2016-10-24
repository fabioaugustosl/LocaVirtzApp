package br.com.virtz.www.locavirtzapp.async;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.virtz.www.locavirtzapp.beans.EventoBean;
import br.com.virtz.www.locavirtzapp.db.EventoDAO;
import br.com.virtz.www.locavirtzapp.rest.ConsumeRest;
import br.com.virtz.www.locavirtzapp.util.JsonUtil;

/**
 *
 * os voids abaixo são generics q significam:
 * Void (entrada no metodo doInBackground), Void(parametro dos metodos de progresso), Void (saida no metodo onPostExecute)
 *
 */
public class EventoListagemTask extends AsyncTask<Void, Void, List<EventoBean>> {

    private Context mContext;
    private ArrayAdapter adapter = null;

    public EventoListagemTask(Context context, ArrayAdapter adapter){
        mContext = context;
        this.adapter = adapter;
    }


    @Override
    protected List<EventoBean> doInBackground(Void... params) {

        EventoDAO eventoDAO = new EventoDAO(mContext);

        List<EventoBean> eventosBanco = eventoDAO.listarTodos("virtz");
        if(eventosBanco == null){
            eventosBanco = new ArrayList<EventoBean>();
        }

        return eventosBanco;
    }


    @Override
    protected void onPostExecute(List<EventoBean> eventos) {
        adapter.clear();
        adapter.addAll(eventos);
        adapter.notifyDataSetChanged();
    }


}
