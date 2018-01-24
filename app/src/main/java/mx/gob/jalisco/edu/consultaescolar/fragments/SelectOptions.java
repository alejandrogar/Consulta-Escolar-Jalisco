package mx.gob.jalisco.edu.consultaescolar.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import mx.gob.jalisco.edu.consultaescolar.Inscription;
import mx.gob.jalisco.edu.consultaescolar.R;
import mx.gob.jalisco.edu.consultaescolar.adapters.CCTAdapter;
import mx.gob.jalisco.edu.consultaescolar.objects.CCT;
import mx.gob.jalisco.edu.consultaescolar.services.AsyncResponse;
import mx.gob.jalisco.edu.consultaescolar.services.GetServerData;
import mx.gob.jalisco.edu.consultaescolar.utils.Utils;

public class SelectOptions extends Fragment implements AsyncResponse, TextWatcher {

    EditText o1, o2, o3, o4, o5,search_name, search_mun;

    ArrayList<CCT> search_result;
    RecyclerView recyclerView_result;
    RecyclerView.LayoutManager layoutManager;
    CCTAdapter adapter;

    TextView nombre1, nombre2, nombre3, nombre4, nombre5;
    TextView municipio1, municipio2, municipio3, municipio4, municipio5,help_copy;

    Button search_action;

    ProgressDialog progressDialog;

    String Mun ="";

    Spinner municipios;
    public SelectOptions() {
        // Required empty public constructor
    }

    public static SelectOptions newInstance(String param1, String param2) {
        SelectOptions fragment = new SelectOptions();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_options, container, false);
        /**/
        nombre1 = (TextView) view.findViewById(R.id.nombre1);
        nombre2 = (TextView) view.findViewById(R.id.nombre2);
        nombre3 = (TextView) view.findViewById(R.id.nombre3);
        nombre4 = (TextView) view.findViewById(R.id.nombre4);
        nombre5 = (TextView) view.findViewById(R.id.nombre5);

        municipio1 = (TextView) view.findViewById(R.id.municipio1);
        municipio2 = (TextView) view.findViewById(R.id.municipio2);
        municipio3 = (TextView) view.findViewById(R.id.municipio3);
        municipio4 = (TextView) view.findViewById(R.id.municipio4);
        municipio5 = (TextView) view.findViewById(R.id.municipio5);

        o1 = (EditText) view.findViewById(R.id.textOpc_1);
        o1.addTextChangedListener(this);

        o2 = (EditText) view.findViewById(R.id.textOpc_2);
        o2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence editable, int start, int before, int count) {
                if(editable.length() == 10) {
                    getcctData(editable.toString().toUpperCase().replaceAll("\\s", ""), 2);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        o3 = (EditText) view.findViewById(R.id.textOpc_3);
        o3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence editable, int start, int before, int count) {
                if(editable.length() == 10) {
                    getcctData(editable.toString().toUpperCase().replaceAll("\\s", ""), 3);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        o4 = (EditText) view.findViewById(R.id.textOpc_4);
        o4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence editable, int start, int before, int count) {
                if(editable.length() == 10) {
                    getcctData(editable.toString().toUpperCase().replaceAll("\\s", ""), 4);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        o5 = (EditText) view.findViewById(R.id.textOpc_5);
        o5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence editable, int start, int before, int count) {
                if(editable.length() == 10) {
                    getcctData(editable.toString().toUpperCase().replaceAll("\\s", ""), 5);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        try {
            if (Inscription.JSON != null) {
                JSONArray jsonArray = new JSONArray(Inscription.JSON);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObjectC = jsonArray.getJSONObject(i);
                    /*if(var.indexOf("&quot;") > 0 ){
                        var = var.replaceAll("&quot;", "\"");
                    }*/
                    o1.setText((!Objects.equals(jsonObjectC.getString("OP1"), "") || !Objects.equals(jsonObjectC.getString("OP1"), "null") || !Objects.equals(jsonObjectC.getString("OP1"), null))?jsonObjectC.getString("OP1"):"");
                    o2.setText((!Objects.equals(jsonObjectC.getString("OP2"), "") || !Objects.equals(jsonObjectC.getString("OP2"), "null") || !Objects.equals(jsonObjectC.getString("OP2"), null))?jsonObjectC.getString("OP2"):"");
                    o3.setText((!Objects.equals(jsonObjectC.getString("OP3"), "") || !Objects.equals(jsonObjectC.getString("OP3"), "null") || !Objects.equals(jsonObjectC.getString("OP3"), null))?jsonObjectC.getString("OP3"):"");
                    o4.setText((!Objects.equals(jsonObjectC.getString("OP4"), "") || !Objects.equals(jsonObjectC.getString("OP4"), "null") || !Objects.equals(jsonObjectC.getString("OP4"), null))?jsonObjectC.getString("OP4"):"");
                    o4.setText((!Objects.equals(jsonObjectC.getString("OP4"), "") || !Objects.equals(jsonObjectC.getString("OP4"), "null") || !Objects.equals(jsonObjectC.getString("OP4"), null))?jsonObjectC.getString("OP4"):"");
                    o5.setText((!Objects.equals(jsonObjectC.getString("OP5"), "") || !Objects.equals(jsonObjectC.getString("OP5"), "null") || !Objects.equals(jsonObjectC.getString("OP5"), null))?jsonObjectC.getString("OP5"):"");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.municipios, android.R.layout.simple_spinner_item);

        final String[] mun_opcs = getResources().getStringArray(R.array.municipios);
        municipios = (Spinner) view.findViewById(R.id.municipios);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        municipios.setAdapter(adapter);
        municipios.setSelection(0);
        municipios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0) {
                    Mun = mun_opcs[i];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        recyclerView_result = (RecyclerView) view.findViewById(R.id.search_result);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        help_copy = (TextView) view.findViewById(R.id.help_copy_to_clipboard);
        search_name = (EditText) view.findViewById(R.id.searchCCT);
        //search_mun = (EditText) view.findViewById(R.id.searchCCT_mun);
        search_action = (Button) view.findViewById(R.id.search_action);
        search_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Objects.equals(search_name.getText().toString(), "") || Objects.equals(Mun, "")){
                    Toast.makeText(getContext(), "Llenar datos faltantes", Toast.LENGTH_SHORT).show();
                }else{
                    getSearchCCT(search_name.getText().toString(), Mun);
                }
            }
        });
        return view;
    }

    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void processFinish(String output, String type, final String cct_param) {
        if(Objects.equals(type, "GET_CCT_OPTION")){
            if(output.contains("[]")){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(false)
                        .setMessage(R.string.cct_error)
                        .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                if(Objects.equals(cct_param.toUpperCase(), o1.getText().toString().toUpperCase())){
                                    o1.setText("");
                                    Inscription.CCT_OPTIONS[0] = "";
                                }
                                if(Objects.equals(cct_param.toUpperCase(), o2.getText().toString().toUpperCase())){
                                    o2.setText("");
                                    Inscription.CCT_OPTIONS[1] = "";
                                }
                                if(Objects.equals(cct_param.toUpperCase(), o3.getText().toString().toUpperCase())){
                                    o3.setText("");
                                    Inscription.CCT_OPTIONS[2] = "";
                                }
                                if(Objects.equals(cct_param.toUpperCase(), o4.getText().toString().toUpperCase())){
                                    o4.setText("");
                                    Inscription.CCT_OPTIONS[3] = "";
                                }
                                if(Objects.equals(cct_param.toUpperCase(), o5.getText().toString().toUpperCase())){
                                    o5.setText("");
                                    Inscription.CCT_OPTIONS[4] = "";
                                }
                            }
                        });
                builder.create().show();
            }else{
                try {
                    JSONArray jsonArray = new JSONArray(output);
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObjectC = jsonArray.getJSONObject(i);

                        if(Objects.equals(cct_param.toUpperCase(), o1.getText().toString().toUpperCase())){
                            nombre1.setVisibility(View.VISIBLE);
                            municipio1.setVisibility(View.VISIBLE);
                            nombre1.setText(jsonObjectC.getString("NOMBRE"));
                            municipio1.setText(jsonObjectC.getString("DESMUNICIPIO"));
                            Inscription.CCT_OPTIONS[0] = o1.getText().toString().toUpperCase();
                        }
                        if(Objects.equals(cct_param.toUpperCase(), o2.getText().toString().toUpperCase())){
                            nombre2.setVisibility(View.VISIBLE);
                            municipio2.setVisibility(View.VISIBLE);
                            nombre2.setText(jsonObjectC.getString("NOMBRE"));
                            municipio2.setText(jsonObjectC.getString("DESMUNICIPIO"));
                            Inscription.CCT_OPTIONS[1] = o2.getText().toString().toUpperCase();
                        }
                        if(Objects.equals(cct_param.toUpperCase(), o3.getText().toString().toUpperCase())){
                            nombre3.setVisibility(View.VISIBLE);
                            municipio3.setVisibility(View.VISIBLE);
                            nombre3.setText(jsonObjectC.getString("NOMBRE"));
                            municipio3.setText(jsonObjectC.getString("DESMUNICIPIO"));
                            Inscription.CCT_OPTIONS[2] = o3.getText().toString().toUpperCase();
                        }
                        if(Objects.equals(cct_param.toUpperCase(), o4.getText().toString().toUpperCase())){
                            nombre4.setVisibility(View.VISIBLE);
                            municipio4.setVisibility(View.VISIBLE);
                            nombre4.setText(jsonObjectC.getString("NOMBRE"));
                            municipio4.setText(jsonObjectC.getString("DESMUNICIPIO"));
                            Inscription.CCT_OPTIONS[3] = o4.getText().toString().toUpperCase();
                        }
                        if(Objects.equals(cct_param.toUpperCase(), o5.getText().toString().toUpperCase())){
                            nombre5.setVisibility(View.VISIBLE);
                            municipio5.setVisibility(View.VISIBLE);
                            nombre5.setText(jsonObjectC.getString("NOMBRE"));
                            municipio5.setText(jsonObjectC.getString("DESMUNICIPIO"));
                            Inscription.CCT_OPTIONS[4] = o5.getText().toString().toUpperCase();
                        }
                        if(Objects.equals(jsonObjectC.getString("OBSERVACIONES"),"TC")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            LayoutInflater factory = LayoutInflater.from(getContext());
                            final View viewDialog = factory.inflate(R.layout.poll, null);
                            builder.setView(viewDialog);
                            builder.setCancelable(false);
                            builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dlg, int sumthin) {
                                }
                            });

                            RadioGroup p1 = (RadioGroup) viewDialog.findViewById(R.id.radio_p_1);
                            p1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                    if(i == R.id.padre_1) Inscription.TC_POLL[0] = 1;
                                    if(i == R.id.madre_1) Inscription.TC_POLL[0] = 2;
                                    if(i == R.id.ambos_1) Inscription.TC_POLL[0] = 3;
                                    if(i == R.id.tutor_1) Inscription.TC_POLL[0] = 4;

                                    Log.i("PREGUNTA 1", Inscription.TC_POLL[0]+"");
                                }
                            });

                            RadioGroup p2 = (RadioGroup) viewDialog.findViewById(R.id.radio_p_2);
                            p2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                    if(i == R.id.padre_2) Inscription.TC_POLL[1] = 1;
                                    if(i == R.id.madre_2) Inscription.TC_POLL[1] = 2;
                                    if(i == R.id.ambos_2) Inscription.TC_POLL[1] = 3;
                                    if(i == R.id.tutor_2) Inscription.TC_POLL[1] = 4;
                                    Log.i("PREGUNTA 2", Inscription.TC_POLL[1]+"");
                                }
                            });

                            RadioGroup p3 = (RadioGroup) viewDialog.findViewById(R.id.radio_p_3);
                            p3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                    if(i == R.id.poll_si) Inscription.TC_POLL[2] = 1;
                                    if(i == R.id.poll_no) Inscription.TC_POLL[2] = 2;
                                    Log.i("PREGUNTA 3", Inscription.TC_POLL[2]+"");
                                }
                            });

                            RadioGroup p4 = (RadioGroup) viewDialog.findViewById(R.id.radio_p_4);
                            p4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                    if(i == R.id.poll_2_si) Inscription.TC_POLL[3] = 1;
                                    if(i == R.id.poll_2_no) Inscription.TC_POLL[3] = 2;
                                    Log.i("PREGUNTA 4", Inscription.TC_POLL[3]+"");
                                }
                            });

                            RadioGroup p5 = (RadioGroup) viewDialog.findViewById(R.id.radio_p_5);
                            p5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                    if(i == R.id.poll_5_1) Inscription.TC_POLL[4] = 1;
                                    if(i == R.id.poll_5_2) Inscription.TC_POLL[4] = 2;
                                    if(i == R.id.poll_5_3) Inscription.TC_POLL[4] = 3;
                                    if(i == R.id.poll_5_4) Inscription.TC_POLL[4] = 4;
                                    if(i == R.id.poll_5_5) Inscription.TC_POLL[4] = 5;
                                    Log.i("PREGUNTA 5", Inscription.TC_POLL[4]+"");
                                }
                            });
                            TextView download_letter = (TextView) viewDialog.findViewById(R.id.download_letter);
                            download_letter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://inscripciones.jalisco.gob.mx/inscribe/media/downloads/carta_compromiso_2018.pdf"));
                                    startActivity(browserIntent);
                                }
                            });
                            builder.show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else if(Objects.equals(type, "SEARCH_CCT")){
            progressDialog.dismiss();
            help_copy.setVisibility(View.VISIBLE);
            search_result = new ArrayList<>();
            //Log.i("OUTPUT_SEARCH",output+"");
            if(!Objects.equals(output, "[]")){
                try {
                    JSONArray jsonArray = new JSONArray(output);
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        //String clavecct, String domicilio, String nombrect, String n_municipi
                        search_result.add(new CCT(jsonObject.getString("CLAVECCT"),jsonObject.getString("DOMICILIO"),jsonObject.getString("NOMBRECT"),jsonObject.getString("N_MUNICIPI")));
                    }
                    recyclerView_result.setVisibility(View.VISIBLE);
                    recyclerView_result.setLayoutManager(layoutManager);
                    adapter = new CCTAdapter(search_result, getContext(), SelectOptions.this);
                    recyclerView_result.setAdapter(adapter);
                    //Log.i("outputDataCCT", outputDataCCT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(getContext(), "No se encontraron resultados", Toast.LENGTH_LONG).show();
            }

        }

    }

    @Override
    public void beforeTextChanged(CharSequence editable, int i0, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence editable, int i0, int i1, int i2) {
        if(editable.length() == 10) {
            getcctData(editable.toString().toUpperCase().replaceAll("\\s", ""), 1);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        /*if (editable.toString().length() < 10) {
            o1.setError("Mínimo 10 caracteres");
        }*/
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void getcctData(String cct, int opc) {
        GetServerData getData = new GetServerData();
        getData.delegate = this;
        getData.execute("http://escuelatransparente.se.jalisco.gob.mx:3000/get_cct_data/"+opc+"/"+cct+"/"+"1"+Inscription.NIVEL, "GET_CCT_OPTION",cct);
    }

    public void getSearchCCT(String name, String mun){

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Por favor espere...");
        progressDialog.show();

        GetServerData getSearch = new GetServerData();
        getSearch.delegate = this;
        String url = "http://escuelatransparente.se.jalisco.gob.mx/sites/all/themes/transparente/includes/getcct_per_name.php?name=" + Utils.replaceSpecialCharacter(name.trim())+ "&mun="+Utils.replaceSpecialCharacter(mun.trim()+"&nivel="+Inscription.NIVEL);
        url = url.replace(" ","%20");
        getSearch.execute(url, "SEARCH_CCT");
    }

    public void selectAs(int opc, String cct){
        if(opc == 1) o1.setText(cct);
        if(opc == 2) o2.setText(cct);
        if(opc == 3) o3.setText(cct);
        if(opc == 4) o4.setText(cct);
        if(opc == 5) o5.setText(cct);
        Toast.makeText(getContext(), "Opción " + opc + " como " + cct, Toast.LENGTH_SHORT).show();
    }
}
