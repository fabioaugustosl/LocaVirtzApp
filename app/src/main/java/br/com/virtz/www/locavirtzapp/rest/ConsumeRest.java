package br.com.virtz.www.locavirtzapp.rest;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class ConsumeRest {

    // post https://beaconbkendvirtz.appspot.com/_ah/api/beaconHistoricoService/v1/salvarHistorico/003e8c80-ea01-4ebb-b888-78da19df9e55/virtz

    public String doGet(String urlString) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String linha;
            StringBuilder sb = new StringBuilder();
            while ((linha = reader.readLine()) != null) {
                sb.append(linha).append("\n");
            }

            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    /**
     * Invoca um POST.
     * Para isso recebe a url e um map de parametros. Onde chave é o nome do parametro e Valor é o proprio valor a ser passado.
     *
     * @param urlString
     * @param params
     * @throws ProtocolException
     * @throws IOException
     */
    public void doPost(String urlString, List<ParamRest> params) throws ProtocolException, IOException {
        HttpsURLConnection conn = null;
        urlString += getParansPath(params);
        //"https://beaconbkendvirtz.appspot.com/_ah/api/beaconHistoricoService/v1/salvarHistorico/003e8c80-ea01-4ebb-b888-78da19df9e55/virtz
        try {
            URL url = new URL(urlString);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            if (params != null && !params.isEmpty()) {
                String q = getQuery(params);
                if (q != null && !"".equals(q.trim())) {
                    writer.write(q);
                }
            }
            writer.flush();
            writer.close();
            os.close();

            conn.connect();

           /* StringBuilder sb = new StringBuilder();
            int HttpResult = conn.getResponseCode();

            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                System.out.println("" + sb.toString());
            } else {
                System.out.println(conn.getResponseMessage());
            }
*/

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }

        }

    }

    private String getParansPath( List<ParamRest> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (ParamRest p : params) {
            if(!p.isQueryString()) {
                result.append("/").append(p.getValor());
            }
        }

        return result.toString();
    }

    private String getQuery( List<ParamRest> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (ParamRest p : params) {
            if(p.isQueryString()) {

                if (first) {
                    first = false;
                } else {
                    result.append("&");
                }

                result.append(URLEncoder.encode(p.getChave(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(p.getValor(), "UTF-8"));
            }
        }

        return result.toString();
    }


}