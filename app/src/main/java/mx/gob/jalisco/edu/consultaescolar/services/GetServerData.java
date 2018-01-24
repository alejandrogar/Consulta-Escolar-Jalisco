package mx.gob.jalisco.edu.consultaescolar.services;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GetServerData extends AsyncTask<String, Void, String> {

    public AsyncResponse delegate = null;
    public String PARAMCCT = "";
    public String TYPE = "";
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(String... params) {
        HttpURLConnection connection = null;

        TYPE = params[1];
        if (Objects.equals(TYPE, "GET_CCT_OPTION")) PARAMCCT = params[2];
        try {
            URL u = new URL(params[0]);
            Log.i("URL REQUEST", u+"");
            connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-length", "0");
            connection.setUseCaches(false);
            connection.setAllowUserInteraction(false);
            connection.setConnectTimeout(100000);
            connection.setReadTimeout(100000);
            connection.connect();
            int status = connection.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String json) {
        delegate.processFinish(json, TYPE, PARAMCCT);
    }
}