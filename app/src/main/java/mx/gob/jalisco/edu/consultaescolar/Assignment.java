package mx.gob.jalisco.edu.consultaescolar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

import mx.gob.jalisco.edu.consultaescolar.adapters.JsonObjectAdapter;
import mx.gob.jalisco.edu.consultaescolar.services.AsyncResponse;
import mx.gob.jalisco.edu.consultaescolar.services.DataSender;
import mx.gob.jalisco.edu.consultaescolar.utils.NetworkUtils;
import mx.gob.jalisco.edu.consultaescolar.utils.Utils;

public class Assignment extends AppCompatActivity implements AsyncResponse {

    Spinner niveles;

    TextInputLayout matricula;

    TextInputLayout clavect, apellidos, names;

    TextView help_matricula;
    ViewFlipper field_content, content_container;
    Boolean SHOW_CCT_FIELDS = false;
    String matricula_pattern ="\\b\\d{8}\\b";
    String cct_patter = "\\b\\w{10}\\b";
    DataSender dataSender;
    Button send, see_maps;
    ProgressDialog progressDialog;

    TextView name_result, school_name, cct_result, turno_result;
    RecyclerView escuelas_top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_action_close_white);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        niveles = (Spinner) findViewById(R.id.niveles);
        matricula = (TextInputLayout) findViewById(R.id.matricula);
        field_content = (ViewFlipper) findViewById(R.id.fields_content);
        content_container = (ViewFlipper) findViewById(R.id.content_container);
        help_matricula = (TextView) findViewById(R.id.help_matricula);
        send = (Button) findViewById(R.id.send);

        turno_result = (TextView) findViewById(R.id.turno_result);
        clavect = (TextInputLayout) findViewById(R.id.cct);
        apellidos = (TextInputLayout) findViewById(R.id.last_name);
        names = (TextInputLayout) findViewById(R.id.name);

        name_result = (TextView) findViewById(R.id.name_result);
        school_name = (TextView) findViewById(R.id.school_name);
        cct_result = (TextView) findViewById(R.id.cct_result);

        escuelas_top = (RecyclerView) findViewById(R.id.escuelas_top);

        help_matricula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                field_content.showNext();
                SHOW_CCT_FIELDS = true;
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    public void validate(){

        Log.i("Selected item", niveles.getSelectedItemPosition()+"");

        NetworkUtils internet = new NetworkUtils(this);
        if(internet.isConnectingToInternet()){
            if(SHOW_CCT_FIELDS){
                if(clavect.getEditText().getText().toString().matches(cct_patter) && niveles.getSelectedItemPosition() != 0 && names.getEditText().getText().toString() != ""){
                    send();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Assignment.this);
                    builder.setCancelable(false)
                            .setMessage("Revise que los datos estén correctos he intente de nuevo.")
                            .setPositiveButton(R.string.cast_tracks_chooser_dialog_ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                }
            }else{
                if(matricula.getEditText().getText().toString().matches(matricula_pattern) && niveles.getSelectedItemPosition() != 0){
                    send();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Assignment.this);
                    builder.setCancelable(false)
                            .setMessage("Revise que los datos estén correctos he intente de nuevo.")
                            .setPositiveButton(R.string.cast_tracks_chooser_dialog_ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                }
            }
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false)
                    .setMessage(R.string.alert_no_internet)
                    .setPositiveButton(R.string.cast_tracks_chooser_dialog_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            finish();
                        }
                    });
            builder.create().show();
        }
    }

    public void send(){
        String url = "http://escuelatransparente.se.jalisco.gob.mx/";
        if(SHOW_CCT_FIELDS){
            url += "asignacion2/1"+niveles.getSelectedItemPosition()+"/"+ clavect.getEditText().getText().toString().toUpperCase()+"/"+ apellidos.getEditText().getText().toString().toUpperCase() +"/"+ names.getEditText().getText().toString().toUpperCase();
        }else{
            url += "asignacion1/1"+niveles.getSelectedItemPosition()+"/"+ matricula.getEditText().getText().toString().toUpperCase();
        }

        dataSender = new DataSender(Assignment.this,"", new JSONObject(), url, DataSender.GET);
        dataSender.delegate = this;
        dataSender.send();

        progressDialog = new ProgressDialog(Assignment.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Por favor espere...");
        progressDialog.show();
    }

    @Override
    public void processFinish(String output, String type, String param) {
        progressDialog.hide();
        Log.i("OUT PUT ASSIGN", output+"");
        if(Objects.equals(output, "[]") || Objects.equals(output, "null")){
            AlertDialog.Builder builder = new AlertDialog.Builder(Assignment.this);
            builder.setCancelable(false)
                    .setTitle("Oops!")
                    .setMessage("No hemos encontrado registros, revise que los datos sean correctos")
                    .setPositiveButton(R.string.try_again, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }else{
            try{

                final String schoolName;
                final JSONObject data = new JSONArray(output).getJSONObject(0);

                if(Objects.equals(data.getString("ASIGNADO").toString(), "") || Objects.equals(data.getString("OP1").toString(), "")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Assignment.this);
                    builder.setCancelable(false)
                            .setTitle("Oops!")
                            .setMessage("Su hijo(a) no está asignado a ninguna escuela o no se realizó trámite, favor de llamar al número 3030-7550 para cualquier duda o aclaración.")
                            .setPositiveButton(R.string.cast_tracks_chooser_dialog_ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                }else{
                    Log.i("ASUGNADO", "true");
                    content_container.showNext();
                }

                String name = data.getString("NOMBREASPIRANTE") + " "
                        + data.getString("APEPAT") + " "
                        + data.getString("APEMAT");

                name_result.setText("Nombre: " +Utils.toTitleCase(name));

                schoolName = Utils.toTitleCase(data.getString("NOMBREESCUELA"));
                school_name.setText("Nombre: " + schoolName);
                cct_result.setText("Clave: " + data.getString("ASIGNADO"));
                String turno = (Objects.equals(data.getString("TURNO"), "1"))?"Matutino":"Vespertino";
                turno_result.setText("Turno: " + turno);
            }catch (JSONException e){
                e.printStackTrace();
                final String lat, lng;
                final String schoolName;
                try{
                    final JSONObject data = new JSONArray(output).getJSONObject(0);
                    String name = data.getString("NOMBREASPIRANTE") + " "
                            + data.getString("APEPAT") + " "
                            + data.getString("APEMAT");

                    schoolName = Utils.toTitleCase(data.getString("NOMBREESCASIG"));
                    school_name.setText("Nombre: " + schoolName);
                    cct_result.setText("Clave: " + data.getString("ASIGNADO"));
                    String turno = (Objects.equals(data.getString("TURNO"), "1"))?"Matutino":"Vespertino";
                    turno_result.setText("Turno: " + turno);
                }catch (JSONException E){
                    E.printStackTrace();
                }
            }

            try{
                final String lat, lng;
                final JSONObject data = new JSONArray(output).getJSONObject(0);
                lat = data.getString("LATITUD");
                lng = data.getString("LONGITUD");

                /*see_maps.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double latitude = Double.valueOf(lat);
                        double longitude = Double.valueOf(lng);
                        String label = "";
                        String uriBegin = "geo:" + latitude + "," + longitude;
                        String query = latitude + "," + longitude + "(" + label + ")";
                        String encodedQuery = Uri.encode(query);
                        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                        Uri uri = Uri.parse(uriString);
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });*/
            }catch (JSONException LocationE){
                LocationE.printStackTrace();
            }

            /*final ArrayList<JSONObject> items = new ArrayList<>();

            try {
                items.add(new JSONObject().put("name", "Manuel Alejandro").put("age",22));
                items.add(new JSONObject().put("name", "Juan Pepe chuy").put("age",44));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectAdapter adapter = new JsonObjectAdapter<ViewHolder>(
                    items,
                    getApplicationContext(),
                    R.layout.item_esc_top,
                    ViewHolder.class) {
                @Override
                protected void populateViewHolder(ViewHolder viewHolder, int position) {
                    try {
                        viewHolder.setName(items.get(position).getString("name"));
                        viewHolder.setAge(items.get(position).getInt("age"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            escuelas_top.setLayoutManager(new LinearLayoutManager(this));
            escuelas_top.setAdapter(adapter);*/
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, age;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            age = (TextView) v.findViewById(R.id.age);
        }

        public void setName(String value) {
            name.setText(value);
        }

        public void setAge(int value) {
            age.setText(value+"");
        }
    }
}
