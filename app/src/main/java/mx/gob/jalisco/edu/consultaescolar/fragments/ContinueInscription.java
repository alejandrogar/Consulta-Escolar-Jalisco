package mx.gob.jalisco.edu.consultaescolar.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import mx.gob.jalisco.edu.consultaescolar.Inscription;
import mx.gob.jalisco.edu.consultaescolar.R;
import mx.gob.jalisco.edu.consultaescolar.getCurp;

public class ContinueInscription extends Fragment {

    private OnFragmentInteractionListener mListener;
    TextView help_secundaria_primaria,help_curp;
    LinearLayout container_preescolar,container_primaria_secundaria;

    TextInputLayout cct,matricula,password, curp;

    View view;
    int nivel;

    RadioButton segundo,tercero;

    public ContinueInscription() {
        // Required empty public constructor
    }

    public static ContinueInscription newInstance(String param1, String param2) {
        ContinueInscription fragment = new ContinueInscription();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_continue_inscription, container, false);

        help_secundaria_primaria = (TextView) view.findViewById(R.id.help_secundaria_primaria);
        help_secundaria_primaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater factory = LayoutInflater.from(getContext());
                final View viewDialog = factory.inflate(R.layout.help_secundaria_primaria, null);
                builder.setView(viewDialog);
                builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dlg, int sumthin) {

                    }
                });

                builder.show();
            }
        });

        segundo = (RadioButton) view.findViewById(R.id.radio_segundo);
        tercero = (RadioButton) view.findViewById(R.id.radio_tercero);

        help_curp = (TextView) view.findViewById(R.id.help_curp);
        help_curp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.renapo.gob.mx/swb/swb/RENAPO/consultacurp"));
                startActivity(browserIntent);
            }
        });

        container_preescolar = (LinearLayout) view.findViewById(R.id.container_preescolar);
        container_primaria_secundaria = (LinearLayout) view.findViewById(R.id.container_primaria_secundaria);

        nivel = Inscription.NIVEL;
        curp = (TextInputLayout) view.findViewById(R.id.curp);

        //DATOS DE PRUEBA
        if(Inscription.NIVEL == 1){
            //curp.getEditText().setText("PAOE120114HJCRCRA2");
        }else if(Inscription.NIVEL == 2){
            cct = (TextInputLayout) view.findViewById(R.id.cct);
            cct.getEditText().setText("14EJN0203D");

            matricula = (TextInputLayout) view.findViewById(R.id.matricula);
            matricula.getEditText().setText("16178873");

            password = (TextInputLayout) view.findViewById(R.id.password);
            password.getEditText().setText("10825");
        }else if(Inscription.NIVEL == 3){
            cct = (TextInputLayout) view.findViewById(R.id.cct);
            //cct.getEditText().setText("14EML0068O");

            matricula = (TextInputLayout) view.findViewById(R.id.matricula);
            //matricula.getEditText().setText("06030498");

            password = (TextInputLayout) view.findViewById(R.id.password);
            //password.getEditText().setText("59569");
        }

        container_preescolar.setVisibility((nivel!=1)?View.GONE:View.VISIBLE);

        container_primaria_secundaria.setVisibility((nivel!=1)?View.VISIBLE:View.GONE);


        curp.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().indexOf("12") == 5){
                    segundo.setEnabled(false);
                    tercero.setEnabled(true);
                }

                if(charSequence.toString().indexOf("13") == 5){
                    segundo.setEnabled(true);
                    tercero.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    public void getEditText(){
        if(Inscription.NIVEL == 1){
            Inscription.CURP = curp.getEditText().getText().toString().toUpperCase();
        }else{
            Inscription.CCT = cct.getEditText().getText().toString().toUpperCase();
            Inscription.M = matricula.getEditText().getText().toString();
            Inscription.PASSWORD = password.getEditText().getText().toString();
        }
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
