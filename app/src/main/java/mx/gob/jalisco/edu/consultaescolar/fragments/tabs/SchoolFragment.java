package mx.gob.jalisco.edu.consultaescolar.fragments.tabs;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mx.gob.jalisco.edu.consultaescolar.R;
import mx.gob.jalisco.edu.consultaescolar.adapters.CardsAdapter;
import mx.gob.jalisco.edu.consultaescolar.objects.Card;


public class SchoolFragment extends Fragment {

        List<Card> items;
        CardsAdapter adapter;
        RecyclerView recycler;
        RecyclerView.LayoutManager lManager;

    public SchoolFragment() {
            // Required empty public constructor
        }

        public static SchoolFragment newInstance(String param1, String param2) {
            SchoolFragment fragment = new SchoolFragment();

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

            View view = inflater.inflate(R.layout.fragment_school, container, false);

            recycler = (RecyclerView) view.findViewById(R.id.recycler);

            lManager = new LinearLayoutManager(getActivity());
            recycler.setLayoutManager(lManager);
            items.add(new Card(Card.WEB_VIEW, "http://escuelatransparente.se.jalisco.gob.mx","Búsqueda de escuelas", "", R.drawable.busqueda_esc_baja));
            items.add(new Card(Card.WEB_VIEW, "http://estudiaen.jalisco.gob.mx/suma-por-la-paz/","Reporte casos acoso escolar", "",R.drawable.reporte_acoso_baja));

            adapter = new CardsAdapter(items, getContext());
            recycler.setAdapter(adapter);

            return view;
        }

        public interface OnFragmentInteractionListener {
            // TODO: Update argument type and name
            void onFragmentInteraction(Uri uri);
        }
    }
