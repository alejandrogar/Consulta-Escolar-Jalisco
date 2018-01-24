package mx.gob.jalisco.edu.consultaescolar.fragments.tabs;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mx.gob.jalisco.edu.consultaescolar.Assignment;
import mx.gob.jalisco.edu.consultaescolar.Inscription;
import mx.gob.jalisco.edu.consultaescolar.R;
import mx.gob.jalisco.edu.consultaescolar.adapters.CardsAdapter;
import mx.gob.jalisco.edu.consultaescolar.objects.Card;
import mx.gob.jalisco.edu.consultaescolar.services.AsyncResponse;
import mx.gob.jalisco.edu.consultaescolar.services.DataSender;

public class ServicesFragment extends Fragment implements AsyncResponse{

    List<Card> items;
    CardsAdapter adapter;
    RecyclerView recycler;
    RecyclerView.LayoutManager lManager;
    //FirebaseRemoteConfig config;

    DataSender sender;

    public String PRE_INSC = "preinscripciones";
    public static Boolean show_pre_insc = true;

    public ServicesFragment() {
        // Required empty public constructor
    }

    public static ServicesFragment newInstance(String param1, String param2) {
        ServicesFragment fragment = new ServicesFragment();

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
        items = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_services, container, false);
        recycler = (RecyclerView) view.findViewById(R.id.recycler);
        lManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(lManager);

        JSONObject json = new JSONObject();
        sender = new DataSender(getContext() , "", json, "http://escuelatransparente.se.jalisco.gob.mx/app-config/remote-config.json", DataSender.GET);
        sender.delegate = this;
        sender.send();

        return view;
    }

    public void reloadItems(){
        sender.send();
        items = new ArrayList<>();
        Log.d("RELOAD ITEMS", "true");
    }

    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void processFinish(String output, String type, String param) {
        try{
            JSONObject config = new JSONObject(output);
            JSONObject params = config.getJSONObject("params");

            //if(Boolean.valueOf(params.getString("INSCRIPTIONS"))){
                Card preCard = new Card(Card.ACTIVITY, "NOT_USE_URL","Preinscripciones 2018-2019", "Ahora puedes preinscribir más fácil y rápido", R.drawable.banner_insc);
                preCard.setIntent(Inscription.class);
                items.add(preCard);
            //}

            if(Boolean.valueOf(params.getString("CONSULT_ASIGN"))){
                preCard = new Card(Card.ACTIVITY ,"NOT_USE_URL","Consulta de Asignación 2018-2019", "Conoce la escuela dónde está asignado su hijo(a)", R.drawable.consulta_asignacion);
                preCard.setIntent(Assignment.class);
                items.add(preCard);
            }
            //items.add(new Card("http://inscripciones.jalisco.gob.mx/inscribe/","Asignación de Estudiantes ciclo escolar 2016-2017",R.drawable.asignacion_baja));
            items.add(new Card(Card.WEB_VIEW, "http://consultaescolar.jalisco.gob.mx/escolar/login","Consulta de calificaciones", "Mantente al tanto del desempeño de tu hijo(a)", R.drawable.consulta_calificaciones_baja));
            items.add(new Card(Card.BROWSER, "http://consultaescolar.jalisco.gob.mx/escolar/certificado","Descarga tu certificado", "Descarga de manera rápida y sencilla", R.drawable.imprime_certificado_baja));

            adapter = new CardsAdapter(items, getContext());
            recycler.setAdapter(adapter);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
