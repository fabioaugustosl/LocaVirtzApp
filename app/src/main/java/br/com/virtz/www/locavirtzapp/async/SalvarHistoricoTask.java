package br.com.virtz.www.locavirtzapp.async;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.virtz.www.locavirtzapp.beans.BeaconBean;
import br.com.virtz.www.locavirtzapp.beans.BeaconHistoricoBean;
import br.com.virtz.www.locavirtzapp.db.BeaconDAO;
import br.com.virtz.www.locavirtzapp.db.BeaconHistoricoDAO;
import br.com.virtz.www.locavirtzapp.rest.ConsumeRest;
import br.com.virtz.www.locavirtzapp.rest.ParamRest;
import br.com.virtz.www.locavirtzapp.util.JsonUtil;

/**
 * os voids abaixo são generics q significam:
 * Void (entrada no metodo doInBackground), Void(parametro dos metodos de progresso), Void (saida no metodo onPostExecute)
 *
 */
public class SalvarHistoricoTask extends AsyncTask<BeaconHistoricoBean,Void,Void> {

    private Context mContext;
    private ConsumeRest consume  = null;

    public SalvarHistoricoTask(Context context){
        mContext = context;
        consume = new ConsumeRest();
    }


    @Override
    protected Void doInBackground(BeaconHistoricoBean... params) {

        BeaconHistoricoDAO historicoDAO = new BeaconHistoricoDAO(mContext);
        JsonUtil jsonUtil = new JsonUtil();

        BeaconHistoricoBean hist = params[0];

        List<ParamRest> parametros = new ArrayList<ParamRest>();
        parametros.add(new ParamRest("nomeBeacon",hist.getNomeBeacon(), false));
        parametros.add(new ParamRest("dono","virtz",false));
        if(hist.getDistanciaBeacon() != null ){
            parametros.add(new ParamRest("distancia",hist.getDistanciaBeacon().toString(),false));
        }

        try {
            consume.doPost("https://beaconbkendvirtz.appspot.com/_ah/api/beaconHistoricoService/v1/salvarHistorico", parametros);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //historicoDAO.novoHistorico(hist.getNomeBeacon(), new Date(), hist.getDistanciaBeacon());

        return null;
    }
}
