package mx.gob.jalisco.edu.consultaescolar.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import mx.gob.jalisco.edu.consultaescolar.Inscription;
import mx.gob.jalisco.edu.consultaescolar.R;
import mx.gob.jalisco.edu.consultaescolar.utils.Utils;

public class DomData extends Fragment {
    private OnFragmentInteractionListener mListener;

    TextInputLayout calle, n_exterior, n_interior, e_calle, y_calle, c_postal, colonia;
    Spinner municipios;
    TextView error_mun;
    int itemSelected = 0;

    public DomData() {
    }

    public static DomData newInstance(String param1, String param2) {
        DomData fragment = new DomData();
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
        Inscription.ADRESS_DATA = new HashMap<>();
        View view = inflater.inflate(R.layout.fragment_dom, container, false);

        calle = (TextInputLayout) view.findViewById(R.id.calle);
        n_exterior = (TextInputLayout) view.findViewById(R.id.numero_exterior);
        n_interior = (TextInputLayout) view.findViewById(R.id.numero_interior);
        e_calle = (TextInputLayout) view.findViewById(R.id.entre_calle);
        y_calle = (TextInputLayout) view.findViewById(R.id.y_calle);
        c_postal = (TextInputLayout) view.findViewById(R.id.c_postal);
        colonia = (TextInputLayout) view.findViewById(R.id.colonia);

        error_mun = (TextView) view.findViewById(R.id.error_mun);

        municipios = (Spinner) view.findViewById(R.id.municipios);

        String json = Inscription.JSON;
        String Mun = "";

        try {
            if (json != null) {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObjectC = jsonArray.getJSONObject(i);
                    /*if(var.indexOf("&quot;") > 0 ){
                        var = var.replaceAll("&quot;", "\"");
                    }*/
                    calle.getEditText().setText((!Objects.equals(jsonObjectC.getString("CALLE"), "")) ? jsonObjectC.getString("CALLE") : "");
                    n_exterior.getEditText().setText((!Objects.equals(jsonObjectC.getString("NUMEXT"), "")) ? jsonObjectC.getString("NUMEXT") : "");
                    n_interior.getEditText().setText((!Objects.equals(jsonObjectC.getString("NUMINT"), "")) ? jsonObjectC.getString("NUMINT") : "");
                    e_calle.getEditText().setText((!Objects.equals(jsonObjectC.getString("ENTRECALLE"), "")) ? jsonObjectC.getString("ENTRECALLE") : "");
                    y_calle.getEditText().setText((!Objects.equals(jsonObjectC.getString("YCALLE"), "")) ? jsonObjectC.getString("YCALLE") : "");
                    c_postal.getEditText().setText((!Objects.equals(jsonObjectC.getString("CP"), "")) ? jsonObjectC.getString("CP") : "");
                    colonia.getEditText().setText((!Objects.equals(jsonObjectC.getString("COLONIA"), "")) ? jsonObjectC.getString("COLONIA") : "");
                    Mun = (!Objects.equals(jsonObjectC.getString("DESMUNICIPIO"), "")) ? jsonObjectC.getString("DESMUNICIPIO") : "";
                    Log.i("MUNICIPIO", Mun);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.municipios, android.R.layout.simple_spinner_item);

        final String[] mun_opcs = getResources().getStringArray(R.array.municipios);

        for (int i = 0;i<mun_opcs.length;i++) {
            //Log.i("MUNICIPIO: " +i,mun_opcs[i]);
            if(Objects.equals(Mun, Utils.replaceSpecialCharacter(mun_opcs[i].toUpperCase()))) itemSelected = i;
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        municipios.setAdapter(adapter);
        municipios.setSelection(itemSelected);
        municipios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0) {
                    itemSelected = i;
                    Inscription.ADRESS_DATA.put("cvemunicipio",i+"");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //String json = getArguments().getString("JSON");


        return view;
    }

    public boolean validations(){
        setErrors(calle.getEditText().getText().toString().matches("^$"), n_exterior.getEditText().getText().toString().matches("^$"), colonia.getEditText().getText().toString().matches("^$"), itemSelected==0, c_postal.getEditText().getText().toString().matches("^$"));
        return (calle.getEditText().getText().toString().matches("^$") ||
                n_exterior.getEditText().getText().toString().matches("^$") ||
                c_postal.getEditText().getText().toString().matches("^$") ||
                colonia.getEditText().getText().toString().matches("^$") || itemSelected==0);
    }

    public void setErrors(boolean calle_val, boolean n_exterior_val, boolean colonia_val, boolean mun_val, boolean c_postal_val){
        if(calle_val) calle.setError(getString(R.string.requiered)); else calle.setError(null);
        if(n_exterior_val) n_exterior.setError(getString(R.string.requiered)); else n_exterior.setError(null);
        if(colonia_val) colonia.setError(getString(R.string.requiered)); else colonia.setError(null);
        error_mun.setVisibility((mun_val)?View.VISIBLE:View.GONE);
        c_postal.setError((c_postal_val)?getString(R.string.requiered):null);
    }


    public void getEditText(){
        Inscription.ADRESS_DATA.put("calle",calle.getEditText().getText().toString());
        Inscription.ADRESS_DATA.put("numext",n_exterior.getEditText().getText().toString());
        Inscription.ADRESS_DATA.put("numt",n_interior.getEditText().getText().toString());
        Inscription.ADRESS_DATA.put("entrecalle",e_calle.getEditText().getText().toString());
        Inscription.ADRESS_DATA.put("ycalle",y_calle.getEditText().getText().toString());
        Inscription.ADRESS_DATA.put("cp", c_postal.getEditText().getText().toString());
        Inscription.ADRESS_DATA.put("colonia",colonia.getEditText().getText().toString());

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
