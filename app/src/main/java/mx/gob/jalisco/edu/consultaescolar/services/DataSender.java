package mx.gob.jalisco.edu.consultaescolar.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Manuel on 12/06/17.
 */

public class DataSender {

    public static String SERVER = "";
    public static String GET = "GET";
    public static String POST = "POST";

    public AsyncResponse delegate = null;
    private OkHttpClient mClient = new OkHttpClient();
    private String authToken;
    private JSONObject data;
    private Context context;

    private String url;
    private String type;
    String resultTask;

    public void SendData(){}

    public DataSender(Context context, String authToken, JSONObject data, String url, String type) {
        this.context = context;
        this.authToken = authToken;
        this.data = data;
        this.url = url;
        this.type = type;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String send() {

        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    String result = SendToServer(data.toString());
                    return result;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                resultTask = result;
                //Log.i("MESSAGE", result +"");
                delegate.processFinish(result, "", data.toString());
            }
        }.execute();

        return resultTask;
    }

    private String SendToServer(String bodyString) throws IOException {

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), bodyString);
        //Log.i("URL", SERVER + url);
        Request request = null;
        if(Objects.equals(type, POST)){
            request = new Request.Builder()
                    .url(SERVER + url)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + authToken)
                    .build();
        }else{
            request = new Request.Builder()
                    .url(SERVER + url)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + authToken)
                    .build();
        }

        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }


}



