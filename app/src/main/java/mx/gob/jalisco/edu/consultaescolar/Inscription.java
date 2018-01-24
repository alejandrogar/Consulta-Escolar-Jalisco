package mx.gob.jalisco.edu.consultaescolar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import mx.gob.jalisco.edu.consultaescolar.fragments.ContinueInscription;
import mx.gob.jalisco.edu.consultaescolar.fragments.GeneralData;
import mx.gob.jalisco.edu.consultaescolar.fragments.DomData;
import mx.gob.jalisco.edu.consultaescolar.fragments.PersonalData;
import mx.gob.jalisco.edu.consultaescolar.fragments.SelectOptions;
import mx.gob.jalisco.edu.consultaescolar.fragments.SpecialData;
import mx.gob.jalisco.edu.consultaescolar.fragments.StartInscription;
import mx.gob.jalisco.edu.consultaescolar.services.AsyncResponse;
import mx.gob.jalisco.edu.consultaescolar.services.GetServerData;
import mx.gob.jalisco.edu.consultaescolar.utils.NetworkUtils;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class Inscription extends AppCompatActivity implements AsyncResponse {

    int counter = 0;
    FragmentTransaction transaction;

    public static int NIVEL;
    public static String JSON;
    public static boolean CONTINUE = false;
    public static String CCT, M, PASSWORD, CURP="", GRADO ="";
    public static String[] CCT_OPTIONS = {"", "", "", "", ""};
    public static int[] TC_POLL = {0, 0, 0, 0, 0};
    public static String CVEDEFSUF = "0N";
    public static Map<String,String> PERSONAL_DATA = new HashMap<>();
    public static Map<String,String> ADRESS_DATA = new HashMap<>();
    public static Map<String,String> WBRO_DATA = new HashMap<>();
    public static String MATRICULAHERMANO = "";
    public static Map<String,String> CANDIDATE_DATA = new HashMap<>();
    public static int TURNOESPECIAL = 0;
    public static boolean TH = false, HE = false;
    public static boolean TC = false;
    public static boolean[] special_data_answers = {false, false, false};
    public static String PDF_URL = "";

    Toolbar toolbar;
    Button next,back;
    ProgressDialog progressDialog;

    StartInscription startInscription;
    ContinueInscription continueInscription;
    GeneralData generalData;
    DomData domData;
    SelectOptions selectOptions;
    PersonalData personalData;
    SpecialData specialData;

    public static final String SEND_URL = "http://escuelatransparente.se.jalisco.gob.mx:3000/update_data";
    OkHttpClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Preinscripción 2018-2019");
            toolbar.setNavigationIcon(R.drawable.ic_action_close_white);
        }

        NIVEL = 0;
        JSON = "";
        CCT = "";
        M = "";
        PASSWORD= "";
        CURP="";
        GRADO ="";
        CCT_OPTIONS = new String[]{"", "", "", "", ""};
        TC_POLL = new int[]{0, 0, 0, 0, 0};
        CVEDEFSUF = "0N";
        PERSONAL_DATA = new HashMap<>();
        ADRESS_DATA = new HashMap<>();
        WBRO_DATA = new HashMap<>();
        CANDIDATE_DATA = new HashMap<>();
        TH = false;
        HE = false;
        TC = false;

        startInscription = new StartInscription();
        continueInscription = new ContinueInscription();
        generalData = new GeneralData();
        domData = new DomData();
        selectOptions = new SelectOptions();
        personalData = new PersonalData();
        specialData = new SpecialData();

        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, startInscription);
        transaction.addToBackStack(null);
        transaction.commit();
        counter++;

        next = (Button) findViewById(R.id.next);
        back = (Button) findViewById(R.id.back);
        next.setEnabled(false);
        next.setBackgroundTintList(getResources().getColorStateList(R.color.SecondaryText));

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.i("NIVEL", NIVEL+"//");
                //Log.i("STEP",counter+"");

                NetworkUtils internet = new NetworkUtils(Inscription.this);
                if(internet.isConnectingToInternet()){
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
                    if(counter == 1){
                        //Bundle bundle = new Bundle();
                        //bundle.putInt("NIVEL", NIVEL);
                        //continueInscription.setArguments(bundle);
                        transaction.replace(R.id.fragment_container, continueInscription);
                        transaction.commit();
                    }else if(counter == 2){
                        continueInscription.getEditText();
                        if(NIVEL == 1){
                            AlertDialog.Builder builder = new AlertDialog.Builder(Inscription.this);
                            builder.setCancelable(false)
                                    .setMessage(R.string.help_preescolar)
                                    .setPositiveButton(R.string.continuar, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                            Log.i(GRADO,CURP);
                                            if(!Objects.equals(GRADO, "") && !Objects.equals(CURP, "")) {
                                                PPRequest(CURP.toUpperCase(), GRADO);
                                            }else{
                                                Toast.makeText(Inscription.this, "Llenar datos faltantes", Toast.LENGTH_SHORT).show();
                                                counter = 2;
                                            }
                                        }
                                    })
                                    .setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                            finish();
                                        }
                                    });
                            builder.create().show();

                        }else{
                            if(!Objects.equals(M, "") && !Objects.equals(CCT,"") && !Objects.equals(PASSWORD,"")) {
                                PPRequest(CCT.toUpperCase(),M,PASSWORD);
                            }else{
                                Toast.makeText(Inscription.this, "Llenar datos faltantes", Toast.LENGTH_SHORT).show();
                                counter = 1;
                            }
                        }
                    }else if(counter == 3){
                        //Bundle bundle = new Bundle();
                        //bundle.putString("JSON", JSON);
                        //domData.setArguments(bundle);
                        transaction.replace(R.id.fragment_container, domData);
                        transaction.commit();
                    }else if(counter == 4){
                        //Log.i("VALIDATIONS", domData.validations()+"");
                        if(domData.validations()){
                            counter--;
                            Toast.makeText(Inscription.this, "Llenar datos obligatorios", Toast.LENGTH_SHORT).show();
                        }else{
                            domData.getEditText();
                            AlertDialog.Builder builder = new AlertDialog.Builder(Inscription.this);
                            builder.setCancelable(false)
                                    .setMessage(R.string.alert_data_empty)
                                    .setPositiveButton(R.string.correct_data, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            transaction.replace(R.id.fragment_container, selectOptions);
                                            transaction.commit();
                                        }
                                    })
                                    .setNegativeButton(R.string.review, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                            counter--;
                                        }
                                    });
                            builder.create().show();
                        }
                    }else if(counter == 5){
                        if(Objects.equals(CCT_OPTIONS[0], "")) {
                            Toast.makeText(Inscription.this, "Registra al menos una opción", Toast.LENGTH_LONG).show();
                            counter--;
                        }else{
                            transaction.replace(R.id.fragment_container, personalData);
                            transaction.commit();
                        }
                    }else if(counter == 6){
                        if(personalData.validations()){
                            counter--;
                            Toast.makeText(Inscription.this, "Llenar datos obligatorios", Toast.LENGTH_SHORT).show();
                        }else {
                            personalData.getEditText();
                            transaction.replace(R.id.fragment_container, specialData);
                            transaction.commit();
                        }
                    }else if(counter == 7){
                        Log.i("Hermano escuela", HE+"");
                        Log.i("Hermano escuela", (HE && WBRO_DATA.get("matriculahermano") != null )+"");

                        if(specialData.getRadioButtons()){
                            if(HE && WBRO_DATA.get("matriculahermano") == null){
                                counter = 6;
                                Toast.makeText(Inscription.this, "Por favor introduce la matricula del hermano", Toast.LENGTH_SHORT).show();
                            }else{
                                JSONObject root;
                                try {
                                    root = new JSONObject();
                                    root.put("idaspirante", CANDIDATE_DATA.get("idaspirante"));
                                    root.put("cctfuente", CCT.toUpperCase());
                                    root.put("nivel",NIVEL);
                                    root.put("matricula", M);
                                    root.put("calle", ADRESS_DATA.get("calle"));
                                    root.put("numext", ADRESS_DATA.get("numext")+"");
                                    root.put("numt", ADRESS_DATA.get("numt")+"");
                                    root.put("entrecalle", ADRESS_DATA.get("entrecalle").toUpperCase());
                                    root.put("ycalle", ADRESS_DATA.get("ycalle").toUpperCase());
                                    root.put("cp", Integer.parseInt(ADRESS_DATA.get("cp")));
                                    root.put("colonia", ADRESS_DATA.get("colonia").toUpperCase());
                                    root.put("cvemunicipio", Integer.parseInt(ADRESS_DATA.get("cvemunicipio")));
                                    root.put("op1", CCT_OPTIONS[0]);
                                    root.put("op2", CCT_OPTIONS[1]);
                                    root.put("op3", CCT_OPTIONS[2]);
                                    root.put("op4", CCT_OPTIONS[3]);
                                    root.put("op5", CCT_OPTIONS[4]);
                                    root.put("op5", CCT_OPTIONS[4]);
                                    root.put("cvedefsuf", CVEDEFSUF);
                                    root.put("tramitehermano",(TH)?"S":"N");
                                    root.put("hermanosenescuela",(HE)?"S":"N");

                                    root.put("matriculahermano", (WBRO_DATA.get("matriculahermano") == null || Objects.equals(WBRO_DATA.get("matriculahermano"), ""))?"":WBRO_DATA.get("matriculahermano"));
                                    root.put("idhermano", (WBRO_DATA.get("idhermano") == null)?"":WBRO_DATA.get("idhermano"));
                                    root.put("nombreshermano", (WBRO_DATA.get("nombreshermano") == null)?"":WBRO_DATA.get("nombreshermano"));
                                    root.put("gradohermano", (WBRO_DATA.get("gradohermano") == null || Objects.equals(WBRO_DATA.get("gradohermano"), ""))?0:Integer.parseInt(WBRO_DATA.get("gradohermano")));
                                    root.put("turnohermano", (WBRO_DATA.get("turnohermano") == null)?"":WBRO_DATA.get("turnohermano"));

                                    root.put("tramiteweb","APP");
                                    root.put("cvenivel", "1"+NIVEL);
                                    root.put("turnoespecial", TURNOESPECIAL);
                                    root.put("encuesta", "");
                                    root.put("folioreg", "");
                                    root.put("enc_etc1", (TC_POLL[0] == 1)?"PADRE":(TC_POLL[0] == 2)?"MADRE":(TC_POLL[0] == 3)?"AMBOS":(TC_POLL[0] == 4)?"TUTOR":"");
                                    root.put("enc_etc2", (TC_POLL[1] == 1)?"PADRE":(TC_POLL[1] == 2)?"MADRE":(TC_POLL[1] == 3)?"AMBOS":(TC_POLL[1] == 4)?"TUTOR":"");
                                    root.put("enc_etc3", (TC_POLL[2] == 1)?"SI":(TC_POLL[2] == 2)?"NO":"");
                                    root.put("enc_etc4", (TC_POLL[3] == 1)?"SI":(TC_POLL[3] == 2)?"NO":"");
                                    root.put("enc_etc5", TC_POLL[4]+"");
                                    root.put("TELEFONO", PERSONAL_DATA.get("tel"));
                                    root.put("TELEFONOCEL", PERSONAL_DATA.get("cel"));
                                    root.put("email", PERSONAL_DATA.get("email"));
                                    root.put("aux10", PERSONAL_DATA.get("name").toUpperCase());

                                    PDF_URL = "http://inscripciones.jalisco.gob.mx/inscribe/modules/print/ReporteSalida.php?id="+ CANDIDATE_DATA.get("idaspirante");

                                    if(NIVEL == 1){
                                        //root.put("grado", Integer.parseInt(CANDIDATE_DATA.get("grado")));
                                        //root.put("grado", GRADO);
                                        root.put("grado", (Objects.equals(GRADO, "tercero"))?3:2);
                                        root.put("curp", CURP);
                                        root.put("apepat", CANDIDATE_DATA.get("apepat"));
                                        root.put("apemat", CANDIDATE_DATA.get("apemat"));
                                        root.put("nombre", CANDIDATE_DATA.get("nombre"));
                                        root.put("genero", CANDIDATE_DATA.get("genero"));
                                        root.put("fechanac", CANDIDATE_DATA.get("fechanac"));
                                        root.put("fechanac", CANDIDATE_DATA.get("fechanac"));
                                        root.put("ciclo", "0");
                                        root.put("situacion", "0");
                                        root.put("cveacceso", PASSWORD);

                                    }else {
                                        root.put("cveacceso", PASSWORD);
                                    }

                                    mClient = new OkHttpClient();

                                    progressDialog = new ProgressDialog(Inscription.this);
                                    progressDialog.setIndeterminate(true);
                                    progressDialog.setCancelable(false);
                                    progressDialog.setCanceledOnTouchOutside(false);
                                    progressDialog.setMessage("Por favor espere...");
                                    progressDialog.show();

                                    sendData(root.toString());

                                    Log.i("OUTPUT ROOT",root.toString());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }else {
                            counter = 6;
                            Toast.makeText(Inscription.this, "Por favor conteste las preguntas", Toast.LENGTH_SHORT).show();
                        }
                    }
                    counter++;
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Inscription.this);
                    builder.setCancelable(false)
                            .setMessage(R.string.alert_no_internet)
                            .setPositiveButton(R.string.cast_tracks_chooser_dialog_ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                }


            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter-=2;
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.leave_right_out, R.anim.leave_right_in);
                if(counter == 0){
                    transaction.replace(R.id.fragment_container, startInscription);
                    transaction.commit();
                }else if(counter == 1){
                    //Bundle bundle = new Bundle();
                    //bundle.putInt("NIVEL", NIVEL);
                    //continueInscription.setArguments(bundle);
                    transaction.replace(R.id.fragment_container, continueInscription);
                    transaction.commit();
                }else if(counter == 2){
                    //Bundle bundle = new Bundle();
                    //bundle.putString("JSON", JSON);
                    //generalData.setArguments(bundle);
                    transaction.replace(R.id.fragment_container, generalData);
                    transaction.commit();
                }else if(counter == 3){
                    //Bundle bundle = new Bundle();
                    //bundle.putString("JSON", JSON);
                    //domData.setArguments(bundle);
                    transaction.replace(R.id.fragment_container, domData);
                    transaction.commit();
                }else if(counter == 4){
                    transaction.replace(R.id.fragment_container, selectOptions);
                    transaction.commit();
                }else if(counter == 5){
                    transaction.replace(R.id.fragment_container, personalData);
                    transaction.commit();
                }else if(counter == 6){
                    transaction.replace(R.id.fragment_container, specialData);
                    transaction.commit();
                }
                else{
                    finish();
                }
                counter++;
            }
        });
    }

    public void StartTarget(){
        next.setEnabled(true);
        next.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));

        new MaterialTapTargetPrompt.Builder(Inscription.this)
                .setTarget(next)
                .setAutoDismiss(false)
                .setPrimaryText("Continuar")
                .setSecondaryText("Toca aquí para continuar con el trámite")
                .setBackgroundColour(Color.parseColor("#257880"))
                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener(){
                    @Override
                    public void onHidePrompt(MotionEvent event, boolean tappedTarget){
                        //Do something such as storing a value so that this prompt is never shown again
                    }

                    @Override
                    public void onHidePromptComplete(){

                    }
                })
                .show();
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked


        switch(view.getId()) {
            case R.id.radio_segundo:
                if (checked)
                    GRADO = "segundo";
                break;
            case R.id.radio_tercero:
                if (checked)
                    GRADO = "tercero";
                break;
            case R.id.si_p1:
                special_data_answers[0] = true;
                specialData.setViewsAnswers(special_data_answers[0], special_data_answers[1], special_data_answers[2]);
                break;
            case R.id.no_p1:
                special_data_answers[0] = false;
                specialData.setViewsAnswers(special_data_answers[0], special_data_answers[1], special_data_answers[2]);
                break;
            case R.id.si_p2:
                special_data_answers[1] = true;
                Log.i("hermano escuela radio", special_data_answers[1]+"");
                HE = true;
                specialData.evalViewAnswers1(true);specialData.setViewsAnswers(special_data_answers[0], special_data_answers[1], special_data_answers[2]);
                break;
            case R.id.no_p2:
                special_data_answers[1] = false;
                Log.i("hermano escuela radio", special_data_answers[1]+"");
                HE = false;
                Inscription.WBRO_DATA.remove("matriculahermano");
                Inscription.WBRO_DATA.remove("idhermano");
                Inscription.WBRO_DATA.remove("nombreshermano");
                Inscription.WBRO_DATA.remove("gradohermano");
                Inscription.WBRO_DATA.remove("turnohermano");
                specialData.evalViewAnswers1(false);specialData.setViewsAnswers(special_data_answers[0], special_data_answers[1], special_data_answers[2]);
                break;
            case R.id.si_p3:
                special_data_answers[2] = true;
                TH = true;
                specialData.evalViewAnswers2(true);specialData.setViewsAnswers(special_data_answers[0], special_data_answers[1], special_data_answers[2]);
                break;
            case R.id.no_p3:
                special_data_answers[2] = false;
                TH = false;
                Inscription.WBRO_DATA.remove("matriculahermano");
                Inscription.WBRO_DATA.remove("idhermano");
                Inscription.WBRO_DATA.remove("nombreshermano");
                Inscription.WBRO_DATA.remove("gradohermano");
                Inscription.WBRO_DATA.remove("turnohermano");
                specialData.evalViewAnswers2(false);specialData.setViewsAnswers(special_data_answers[0], special_data_answers[1], special_data_answers[2]);
                break;
            case R.id.no_asiste:
                TURNOESPECIAL = 0;break;
            case R.id.matutino_q_1:
                TURNOESPECIAL = 1;break;
            case R.id.vespertino_q_1:
                TURNOESPECIAL = 2;break;
        }
    }

    public void PPRequest(String... data){
        GetServerData ppRequest = new GetServerData();
        progressDialog = new ProgressDialog(Inscription.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Por favor espere...");
        progressDialog.show();

        ppRequest.delegate = this;
        if(NIVEL == 1){
            ppRequest.execute("http://escuelatransparente.se.jalisco.gob.mx:3000/preescolar/"+ data[0] +"/"+ data[1], "SIMPLE");
            Log.i("Executing task","http://escuelatransparente.se.jalisco.gob.mx:3000/preescolar/"+ data[0] +"/"+ data[1]);
        }else{
            //ppRequest.execute("http://escuelatransparente.se.jalisco.gob.mx:3000/primaria_secundaria/14PPR1498I/10163818/29764");
            ppRequest.execute("http://escuelatransparente.se.jalisco.gob.mx:3000/primaria_secundaria/"+data[0]+"/"+data[1]+"/"+data[2], "SIMPLE");
        }
        //Log.i("URL","http://escuelatransparente.se.jalisco.gob.mx:3000/primaria_secundaria/"+data[0]+"/"+data[1]+"/"+data[2]);
    }

    @Override
    public void onBackPressed(){
        counter-=2;
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.leave_right_out, R.anim.leave_right_in);
        if(counter == 0){
            transaction.replace(R.id.fragment_container, startInscription);
            transaction.commit();
        }else if(counter == 1){
            //Bundle bundle = new Bundle();
            //bundle.putInt("NIVEL", NIVEL);
            //continueInscription.setArguments(bundle);
            transaction.replace(R.id.fragment_container, continueInscription);
            transaction.commit();
        }else if(counter == 2){
            //Bundle bundle = new Bundle();
            //bundle.putString("JSON", JSON);
            //generalData.setArguments(bundle);
            transaction.replace(R.id.fragment_container, generalData);
            transaction.commit();
        }else if(counter == 3){
            //Bundle bundle = new Bundle();
            //bundle.putString("JSON", JSON);
            //domData.setArguments(bundle);
            transaction.replace(R.id.fragment_container, domData);
            transaction.commit();
        }else if(counter == 4){
            transaction.replace(R.id.fragment_container, selectOptions);
            transaction.commit();
        }else if(counter == 5){
            transaction.replace(R.id.fragment_container, personalData);
            transaction.commit();
        }else if(counter == 6){
            transaction.replace(R.id.fragment_container, specialData);
            transaction.commit();
        }
        else{
            super.onBackPressed();
        }
        counter++;
    }

    @Override
    public void processFinish(String output, String type, String cct_param) {
        if(Objects.equals(type, "SIMPLE")){
            JSON = output;
            progressDialog.dismiss();
            if(JSON.contains("[]")){
                AlertDialog.Builder builder = new AlertDialog.Builder(Inscription.this);
                builder.setCancelable(false)
                        .setMessage(R.string.alert_data_empty)
                        .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                counter = 2;
                            }
                        });
                builder.create().show();
                JSON="";
            }else{
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
                //Bundle bundle = new Bundle();
                //bundle.putString("JSON", JSON);
                //generalData.setArguments(bundle);
                transaction.replace(R.id.fragment_container, generalData);
                transaction.commit();
            }
        }
    }

    public void sendData(final String data) {

        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    String result = post(data);
                    //Log.d("ANY TAG", "Result: " + result);
                    return result;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                Log.i("RESULT", result);
                progressDialog.dismiss();

                try {
                    JSONObject resultJson = new JSONObject(result);
                    int status = resultJson.getInt("status");
                    //Toast.makeText(Inscription.this, "Status: " + status, Toast.LENGTH_LONG).show();
                    if(status == 200){
                        AlertDialog.Builder builder = new AlertDialog.Builder(Inscription.this);
                        LayoutInflater factory = LayoutInflater.from(Inscription.this);
                        final View viewDialog = factory.inflate(R.layout.inscription_success, null);
                        builder.setView(viewDialog);
                        builder.setCancelable(false);

                        AppCompatButton download_pdf = (AppCompatButton) viewDialog.findViewById(R.id.download_pdf);
                        download_pdf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PDF_URL));
                                startActivity(browserIntent);

                                Toast.makeText(Inscription.this, "Descargando comprobante", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
                        builder.show();
                    }
                }catch (JSONException E){
                    E.printStackTrace();
                    Toast.makeText(Inscription.this, "Ha ocurrido un errro, intente de nuevo", Toast.LENGTH_LONG).show();
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
                .addHeader("Authorization", "token=" + "1o5utNtl66aAL2baapvLql6amMQ2")
                //.addHeader("Host", "token=" + "dceade403b9dda272e2cf3d54da7138d")
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }

}
