package br.com.virtz.www.locavirtzapp.util;



import com.google.gson.Gson;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import br.com.virtz.www.locavirtzapp.beans.BeaconBean;
import br.com.virtz.www.locavirtzapp.beans.EventoBean;

public class JsonUtil {


    public List<BeaconBean> jsonToBeacon(String json) throws JSONException, IOException {
        List<BeaconBean> lista = new ArrayList<BeaconBean>();

        JsonFactory jsonfactory = new JsonFactory();
        JsonParser jsonParser = jsonfactory.createJsonParser(json);

        JsonNode jsonObj = null;
        ObjectMapper mapper = new ObjectMapper();
        jsonObj = mapper.readTree(jsonParser);

        Gson gson = new Gson();
        for(int i = 0; i< jsonObj.get("items").size(); i++){
            BeaconBean b = gson.fromJson(jsonObj.get("items").get(i).toString(), BeaconBean.class);
            lista.add(b);
        }

        return lista;
    }


    public List<EventoBean> jsonToEvento(String json) throws JSONException, IOException {
        List<EventoBean> lista = new ArrayList<EventoBean>();

        JsonFactory jsonfactory = new JsonFactory();
        JsonParser jsonParser = jsonfactory.createJsonParser(json);

        JsonNode jsonObj = null;
        ObjectMapper mapper = new ObjectMapper();
        jsonObj = mapper.readTree(jsonParser);

        Gson gson = new Gson();
        for(int i = 0; i< jsonObj.get("items").size(); i++){
            EventoBean b = gson.fromJson(jsonObj.get("items").get(i).toString(), EventoBean.class);
            lista.add(b);
        }

        return lista;
    }

}
