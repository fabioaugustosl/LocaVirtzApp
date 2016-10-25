package br.com.virtz.www.locavirtzapp.async;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import br.com.virtz.www.locavirtzapp.beans.EventoBean;
import br.com.virtz.www.locavirtzapp.beans.NoticiaBean;
import br.com.virtz.www.locavirtzapp.db.EventoDAO;
import br.com.virtz.www.locavirtzapp.rest.ConsumeRest;
import br.com.virtz.www.locavirtzapp.util.JsonUtil;

/**
 *
 * os voids abaixo são generics q significam:
 * Void (entrada no metodo doInBackground), Void(parametro dos metodos de progresso), Void (saida no metodo onPostExecute)
 *
 */
public class NoticiaListagemTask extends AsyncTask<Void, Void, List<NoticiaBean>> {

    private Context mContext;
    private ConsumeRest consume  = null;
    private ArrayAdapter adapter = null;

    public NoticiaListagemTask(Context context, ArrayAdapter adapter){
        mContext = context;
        this.adapter = adapter;
        this.consume = new ConsumeRest();
    }


    @Override
    protected List<NoticiaBean> doInBackground(Void... params) {

        JsonUtil jsonUtil = new JsonUtil();
        String respostaJson = consume.doGet("https://beaconbkendvirtz.appspot.com/_ah/api/noticiaService/v1/dono/virtz");

        List<NoticiaBean> noticias = null;
        try {
            noticias = jsonUtil.jsonToNotocia(respostaJson);
        } catch (Exception e) {
            e.printStackTrace();
            noticias = new ArrayList<NoticiaBean>();
        }
        return noticias;
    }


    @Override
    protected void onPostExecute(List<NoticiaBean> noticias) {
        adapter.clear();
        adapter.addAll(noticias);
        adapter.notifyDataSetChanged();
    }


}
