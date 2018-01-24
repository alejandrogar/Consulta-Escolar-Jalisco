package mx.gob.jalisco.edu.consultaescolar.fragments.tabs;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mx.gob.jalisco.edu.consultaescolar.R;
import mx.gob.jalisco.edu.consultaescolar.adapters.NotificationAdapter;
import mx.gob.jalisco.edu.consultaescolar.objects.Notification;
import mx.gob.jalisco.edu.consultaescolar.services.AsyncResponse;
import mx.gob.jalisco.edu.consultaescolar.services.GetServerData;
import mx.gob.jalisco.edu.consultaescolar.utils.NetworkUtils;

public class NotificationsFragment extends Fragment implements AsyncResponse{

    List<Notification> items;

    NotificationAdapter adapter;
    RecyclerView recycler;
    RecyclerView.LayoutManager lManager;
    NetworkUtils utils;

    String node_title;
    String cuerpo;
    String imagen;
    String id;
    String publicado;


    public NotificationsFragment() {
        // Required empty public constructor
    }

    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        recycler = (RecyclerView) view.findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);

        lManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(lManager);

        utils = new NetworkUtils(getActivity());
        if(utils.isConnectingToInternet()){
            GetServerData getNotices = new GetServerData();
            getNotices.delegate = this;
            getNotices.execute("http://edu.jalisco.gob.mx/consulta-escolar/services/avisos/views/services_avisos", "SIMPLE");
        }

        return view;
    }

    @Override
    public void processFinish(String output, String type, String param) {
        items = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(output);

            for (int i=0;i<jsonArray.length();i++) {
                JSONObject jsonObjectNotification = jsonArray.getJSONObject(i);

                node_title = jsonObjectNotification.getString("node_title");
                cuerpo = jsonObjectNotification.getString("cuerpo");
                id = jsonObjectNotification.getString("id");
                publicado = jsonObjectNotification.getString("publicado");

                items.add(new Notification(node_title,publicado,cuerpo));

            }
            adapter = new NotificationAdapter(items, getContext());
            recycler.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
