package mx.gob.jalisco.edu.consultaescolar.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import mx.gob.jalisco.edu.consultaescolar.Inscription;
import mx.gob.jalisco.edu.consultaescolar.R;

public class PersonalData extends Fragment {
    private OnFragmentInteractionListener mListener;

    TextInputLayout nombre,cel,tel,email;

    public PersonalData() {
        // Required empty public constructor
    }

    public static PersonalData newInstance(String param1, String param2) {
        PersonalData fragment = new PersonalData();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_data, container, false);

        nombre = (TextInputLayout) view.findViewById(R.id.name);
        cel = (TextInputLayout) view.findViewById(R.id.cel);
        tel = (TextInputLayout) view.findViewById(R.id.tel);
        email = (TextInputLayout) view.findViewById(R.id.email);

        try {
            if (Inscription.JSON != null) {
                JSONArray jsonArray = new JSONArray(Inscription.JSON);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObjectC = jsonArray.getJSONObject(i);
                    /*if(var.indexOf("&quot;") > 0 ){
                        var = var.replaceAll("&quot;", "\"");
                    }*/
                    nombre.getEditText().setText((!Objects.equals(jsonObjectC.getString("AUX10"), "") || !Objects.equals(jsonObjectC.getString("AUX10"), "null")) ? jsonObjectC.getString("AUX10") : "");
                    cel.getEditText().setText((!Objects.equals(jsonObjectC.getString("TELEFONOCEL"), "") || !Objects.equals(jsonObjectC.getString("TELEFONOCEL"), "null")) ? jsonObjectC.getString("TELEFONOCEL") : "");
                    tel.getEditText().setText((!Objects.equals(jsonObjectC.getString("TELEFONO"), "") || !Objects.equals(jsonObjectC.getString("TELEFONO"), "null")) ? jsonObjectC.getString("TELEFONO") : "");
                    email.getEditText().setText((!Objects.equals(jsonObjectC.getString("EMAIL"), "") || !Objects.equals(jsonObjectC.getString("EMAIL"), "null")) ? jsonObjectC.getString("EMAIL") : "");

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public boolean validations(){

        String patternEmail = "([\\w-\\.]+)@((?:[\\w]+\\.)+)([a-zA-Z]{2,4})";
        String patternTel = "([0-9]{10})";

        setErrors(nombre.getEditText().getText().toString().matches("^$"), !cel.getEditText().getText().toString().matches(patternTel), !tel.getEditText().getText().toString().matches(patternTel), !email.getEditText().getText().toString().matches(patternEmail));
        return (nombre.getEditText().getText().toString().matches("^$") ||
                !cel.getEditText().getText().toString().matches(patternTel) ||
                !tel.getEditText().getText().toString().matches(patternTel) ||
                !email.getEditText().getText().toString().matches(patternEmail));
    }

    public void setErrors(boolean nombre_val, boolean cel_val, boolean tel_val, boolean email_val){
        if(nombre_val) nombre.setError(getString(R.string.requiered)); else nombre.setError(null);
        if(cel_val) cel.setError(getString(R.string.requiered)); else cel.setError(null);
        if(tel_val) tel.setError(getString(R.string.requiered)); else tel.setError(null);
        if(email_val) email.setError(getString(R.string.requiered)); else email.setError(null);
    }

    public void getEditText(){
        Inscription.PERSONAL_DATA = new HashMap<>();
        Inscription.PERSONAL_DATA.put("name", nombre.getEditText().getText().toString());
        Inscription.PERSONAL_DATA.put("tel", tel.getEditText().getText().toString());
        Inscription.PERSONAL_DATA.put("cel", cel.getEditText().getText().toString());
        Inscription.PERSONAL_DATA.put("email", email.getEditText().getText().toString());
    }
}
