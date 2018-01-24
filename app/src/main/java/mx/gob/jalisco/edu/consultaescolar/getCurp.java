package mx.gob.jalisco.edu.consultaescolar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class getCurp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_curp);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                getCurp("GARCIA","GUTIERREZ","MANUEL ALEJANDRO", "H", "21/07/1996", "MC");
            }
        });
    }


    public static final String SEND_URL = "http://escuelatransparente.se.jalisco.gob.mx:3000/update_data";
    OkHttpClient mClient = new OkHttpClient();

    public void getCurp(final String paternal_surname, final String mothers_maiden_name, final String names, final String sex, final String birthdate, final String entity_birth) {

        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    JSONObject root = new JSONObject();

                    root.put("name", names);
                    //root.put("token", "1o5utNtl66aAL2baapvLql6amMQ2");

                    String result = post(root.toString());
                    Log.d("ANY TAG", "Result: " + result);
                    return result;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                Log.i("RESULT", "--"+result+"--");
                try {
                    JSONObject resultJson = new JSONObject(result);
                    String status = resultJson.getString("status");
                    Toast.makeText(getCurp.this, "Status: " + status, Toast.LENGTH_LONG).show();
                }catch (JSONException E){
                    E.printStackTrace();
                    Toast.makeText(getCurp.this, "Message Failed, Unknown error occurred.", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    String post(String bodyString) throws IOException {

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), bodyString);

        Request request = new Request.Builder()
                .url(SEND_URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "token=" + "__1o5utNtl66aAL2baapvLql6amMQ2")
                //.addHeader("Host", "token=" + "dceade403b9dda272e2cf3d54da7138d")
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }

}
