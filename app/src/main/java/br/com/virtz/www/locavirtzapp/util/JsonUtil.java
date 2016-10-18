package br.com.virtz.www.locavirtzapp.util;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.virtz.www.locavirtzapp.beans.BeaconBean;

public class JsonUtil {

    public List<BeaconBean> jsonToBeacon(String json) throws JSONException {
        List<BeaconBean> lista = new ArrayList<BeaconBean>();

        JSONArray jsonArray = new JSONArray(json);
        for(int i = 0; i< jsonArray.length(); i++){
            JSONObject object = (JSONObject) jsonArray.get(i);
            String id = (String) object.get("id");
            String nome = (String) object.get("nome");
            String dono = (String) object.get("dono");

            lista.add(new BeaconBean(id,nome,dono));
        }

        return lista;
    }

}
