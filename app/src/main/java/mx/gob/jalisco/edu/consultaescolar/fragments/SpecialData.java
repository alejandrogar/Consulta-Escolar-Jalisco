package mx.gob.jalisco.edu.consultaescolar.fragments;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Objects;
import mx.gob.jalisco.edu.consultaescolar.Inscription;
import mx.gob.jalisco.edu.consultaescolar.R;
import mx.gob.jalisco.edu.consultaescolar.services.AsyncResponse;
import mx.gob.jalisco.edu.consultaescolar.services.GetServerData;


public class SpecialData extends Fragment implements AsyncResponse{

    Spinner Dis_opcSpinner;
    LinearLayout answer_container_1, answer_container_2, answer_container_3;
    ProgressDialog progressDialog;

    EditText matricula, matricula_2, nombre, nombre_2, turno, grado, grupo;

    LinearLayout container_2,container_3;


    RadioButton si_p1, no_p1, si_p2, no_p2, si_p3, no_p3;

    private OnFragmentInteractionListener mListener;

    public SpecialData() {
        // Required empty public constructor
    }

    public static SpecialData newInstance() {
        SpecialData fragment = new SpecialData();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Inscription.WBRO_DATA = new HashMap<>();
        View view = inflater.inflate(R.layout.fragment_special_data, container, false);

        answer_container_1 = (LinearLayout) view.findViewById(R.id.answer_container_1);
        answer_container_2 = (LinearLayout) view.findViewById(R.id.answer_container_2);
        answer_container_3 = (LinearLayout) view.findViewById(R.id.answer_container_3);

        container_2 = (LinearLayout) view.findViewById(R.id.container_2);
        container_3 = (LinearLayout) view.findViewById(R.id.container_3);

        si_p1 = (RadioButton) view.findViewById(R.id.si_p1);
        no_p1 = (RadioButton) view.findViewById(R.id.no_p1);
        si_p2 = (RadioButton) view.findViewById(R.id.si_p2);
        no_p2 = (RadioButton) view.findViewById(R.id.no_p2);
        si_p3 = (RadioButton) view.findViewById(R.id.si_p3);
        no_p3 = (RadioButton) view.findViewById(R.id.no_p3);

        final String[] dis_opc_key = getResources().getStringArray(R.array.dis_opc_key);

        Dis_opcSpinner = (Spinner) view.findViewById(R.id.dis_opcSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.dis_opc, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Dis_opcSpinner.setAdapter(adapter);
        Dis_opcSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Inscription.CVEDEFSUF = dis_opc_key[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        matricula = (EditText) view.findViewById(R.id.matricula);
        nombre = (EditText) view.findViewById(R.id.name);
        turno = (EditText) view.findViewById(R.id.turno);
        grado = (EditText) view.findViewById(R.id.grado);
        grupo = (EditText) view.findViewById(R.id.grupo);
        nombre.setEnabled(false);
        turno.setEnabled(false);
        grado.setEnabled(false);
        grupo.setEnabled(false);

        matricula_2 = (EditText) view.findViewById(R.id.matricula_2);
        matricula_2.setHint((Inscription.NIVEL==1)?"CURP":"Mátricula");
        nombre_2 = (EditText) view.findViewById(R.id.name_2);
        nombre_2.setEnabled(false);

        matricula.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 8){
                    getBrother(charSequence.toString(), "HERMANO");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        matricula_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 8 && Inscription.NIVEL !=1 ){
                    getBrother(charSequence.toString(), "TRAMITE");
                    Log.i("TRAMITE HERMANO", charSequence.toString()+"--");
                }

                if(charSequence.length() == 18 && Inscription.NIVEL ==1){
                    getBrother(charSequence.toString(), "TRAMITE");
                }
            }


            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void processFinish(String output, String type, String param) {
        progressDialog.dismiss();


        Log.i("output",output);
        if(!output.contains("[]")){
            try {
                if (output != null) {
                    JSONArray jsonArray = new JSONArray(output);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if(Inscription.HE && !Inscription.TH){
                            Inscription.WBRO_DATA = new HashMap<>();
                            nombre.setText(jsonObject.getString("NOMBRE")+" "+jsonObject.getString("APEPAT")+" "+jsonObject.getString("APEMAT"));
                            turno.setText((Objects.equals(jsonObject.getString("TURNO"), "1"))?"Matutino":"Vespertino");
                            grado.setText("Grado: "+jsonObject.getString("GRADO"));
                            grupo.setText("Grupo: "+jsonObject.getString("GRUPO"));
                            Inscription.WBRO_DATA.put("matriculahermano", matricula.getText()+"");
                            Inscription.WBRO_DATA.put("idhermano", jsonObject.getString("IDALUMNO"));
                            Inscription.WBRO_DATA.put("nombreshermano", jsonObject.getString("APEPAT") + jsonObject.getString("APEMAT") + jsonObject.getString("NOMBRE"));
                            Inscription.WBRO_DATA.put("gradohermano", jsonObject.getString("GRADO"));
                            Inscription.WBRO_DATA.put("turnohermano", jsonObject.getString("TURNO"));
                        }

                        if(Inscription.TH && !Inscription.HE){
                            Inscription.WBRO_DATA = new HashMap<>();
                            nombre_2.setText(jsonObject.getString("NOMBRE")+" "+jsonObject.getString("APEPAT")+" "+jsonObject.getString("APEMAT"));
                            Inscription.WBRO_DATA.put("matriculahermano", matricula_2.getText()+"");
                            Inscription.WBRO_DATA.put("idhermano", jsonObject.getString("IDASPIRANTE"));
                            Inscription.WBRO_DATA.put("nombreshermano", jsonObject.getString("APEPAT") + " " + jsonObject.getString("APEMAT") +" "+ jsonObject.getString("NOMBRE"));
                            Inscription.WBRO_DATA.put("gradohermano", "");
                            Inscription.WBRO_DATA.put("turnohermano", "");
                        }
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getContext(), "No se encontraron resultados", Toast.LENGTH_LONG).show();
            matricula.setText("");
            nombre.setText("");
            turno.setText("");
            grado.setText("");
            grupo.setText("");
            matricula_2.setText("");
            nombre_2.setText("");
            if(Inscription.TH) Inscription.TH = false;
            if(Inscription.HE) Inscription.HE = false;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void setViewsAnswers(boolean... answers){
        answer_container_1.setVisibility((answers[0])?View.VISIBLE:View.GONE);
        answer_container_2.setVisibility((answers[1])?View.VISIBLE:View.GONE);
        answer_container_3.setVisibility((answers[2])?View.VISIBLE:View.GONE);
    }

    public void evalViewAnswers1(boolean a){
        container_3.setVisibility((a)?View.GONE:View.VISIBLE);
        if(a){
            matricula.setText("");
            nombre.setText("");
            turno.setText("");
            grado.setText("");
            grupo.setText("");
        }
    }

    public void evalViewAnswers2(boolean a){
        container_2.setVisibility((a)?View.GONE:View.VISIBLE);
        if(a){
            matricula_2.setText("");
            nombre_2.setText("");
        }
    }

    public boolean getRadioButtons(){
        if(si_p1.isChecked() || no_p1.isChecked() || si_p2.isChecked() || no_p2.isChecked() || si_p3.isChecked() || no_p3.isChecked()) return true;
        else return false;
    }

    public void getBrother(String m, String type){
        GetServerData getBrotherData = new GetServerData();
        getBrotherData.delegate = this;

        if(Objects.equals(type, "HERMANO")){
            getBrotherData.execute("http://escuelatransparente.se.jalisco.gob.mx:3000/get_buscar_hermanosescuela/"+m,"SIMPLE");
            Log.i("URL","http://escuelatransparente.se.jalisco.gob.mx:3000/get_buscar_hermanosescuela/"+m);
        }else{
            m = (Inscription.NIVEL==1)?m.toUpperCase():m;
            Log.i("LOG PREESCOLAR CURP","http://escuelatransparente.se.jalisco.gob.mx:3000/get_buscar_tramitehermano/"+m);
            getBrotherData.execute("http://escuelatransparente.se.jalisco.gob.mx:3000/get_buscar_tramitehermano/"+m,"SIMPLE");
        }

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Por favor espere...");
        progressDialog.show();
    }
}