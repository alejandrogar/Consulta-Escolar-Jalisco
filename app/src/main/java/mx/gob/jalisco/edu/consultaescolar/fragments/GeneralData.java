package mx.gob.jalisco.edu.consultaescolar.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import mx.gob.jalisco.edu.consultaescolar.Inscription;
import mx.gob.jalisco.edu.consultaescolar.R;

public class GeneralData extends Fragment {
    private OnFragmentInteractionListener mListener;

    TextInputLayout cct,f_nacimiento,e_nacimiento;
    TextView nombre;
    Spinner grado, genero;
    LinearLayout cct_container, e_nacimiento_container, grado_container;

    public GeneralData() {
    }

    public static GeneralData newInstance(String param1, String param2) {
        GeneralData fragment = new GeneralData();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_general_data, container, false);
        cct = (TextInputLayout) view.findViewById(R.id.cct);
        grado = (Spinner) view.findViewById(R.id.grado);
        nombre = (TextView) view.findViewById(R.id.name);
        f_nacimiento = (TextInputLayout) view.findViewById(R.id.f_nacimiento);
        e_nacimiento = (TextInputLayout) view.findViewById(R.id.e_nacimiento);
        genero = (Spinner) view.findViewById(R.id.gender);
        cct_container = (LinearLayout) view.findViewById(R.id.cct_container);
        e_nacimiento_container = (LinearLayout) view.findViewById(R.id.e_nacimiento_container);

        //String json = getArguments().getString("JSON");
        String json = Inscription.JSON;
        int gr = 0,ge = 0;

        //2012 => 3ro
        //2013 => 2do

        grado_container = (LinearLayout) view.findViewById(R.id.grado_container);

        try {
            if(json != null) {
                JSONArray jsonArray = new JSONArray(json);
                Inscription.CANDIDATE_DATA = new HashMap<>();
                for (int i=0;i<jsonArray.length();i++) {
                    JSONObject jsonObjectC = jsonArray.getJSONObject(i);

                    Inscription.CANDIDATE_DATA.put("idaspirante", jsonObjectC.getString("IDASPIRANTE"));

                    if(Inscription.NIVEL == 1) {
                        cct_container.setVisibility(View.GONE);
                        e_nacimiento_container.setVisibility(View.GONE);
                        Inscription.CANDIDATE_DATA.put("apepat", jsonObjectC.getString("APEPAT"));
                        Inscription.CANDIDATE_DATA.put("apemat", jsonObjectC.getString("APEMAT"));
                        Inscription.CANDIDATE_DATA.put("nombre", jsonObjectC.getString("NOMBRE"));
                        Inscription.CANDIDATE_DATA.put("genero", jsonObjectC.getString("GENERO"));
                        Inscription.CANDIDATE_DATA.put("fechanac", jsonObjectC.getString("FECHANAC"));
                        //Inscription.CANDIDATE_DATA.put("grado", jsonObjectC.getString("GRADO"));
                    }

                    if(Inscription.NIVEL != 1){
                        cct.getEditText().setText(jsonObjectC.getString("CCTFUENTE"));
                        gr = Integer.parseInt(jsonObjectC.getString("GRADO"));
                        e_nacimiento.getEditText().setText(jsonObjectC.getString("DESENTIDAD"));
                    }

                    nombre.setText(jsonObjectC.getString("NOMBRE") + " " + jsonObjectC.getString("APEPAT") + " " + jsonObjectC.getString("APEMAT"));
                    f_nacimiento.getEditText().setText(jsonObjectC.getString("FECHANAC"));
                    ge = (Objects.equals(jsonObjectC.getString("GENERO"), "H"))?1:2;
                    /*if(var.indexOf("&quot;") > 0 ){
                        var = var.replaceAll("&quot;", "\"");
                    }*/
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<CharSequence> adapterGr = ArrayAdapter.createFromResource(getContext(),R.array.grados_primaria, android.R.layout.simple_spinner_item);
        adapterGr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        grado.setAdapter(adapterGr);
        grado.setEnabled(false);
        if(Inscription.NIVEL != 1){
            grado.setSelection(gr - 1);
        }else{
            grado.setSelection(gr+1);
        }
        grado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    //itemSelected = i;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if(Inscription.NIVEL == 1) grado_container.setVisibility(View.GONE);

        ArrayAdapter<CharSequence> adapterGe = ArrayAdapter.createFromResource(getContext(),R.array.genero, android.R.layout.simple_spinner_item);
        adapterGe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genero.setAdapter(adapterGe);
        genero.setSelection(ge);
        genero.setEnabled(false);
        genero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    //itemSelected = i;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
