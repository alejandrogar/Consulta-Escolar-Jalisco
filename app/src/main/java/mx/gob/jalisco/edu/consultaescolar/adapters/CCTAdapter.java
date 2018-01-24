package mx.gob.jalisco.edu.consultaescolar.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.gob.jalisco.edu.consultaescolar.R;
import mx.gob.jalisco.edu.consultaescolar.fragments.SelectOptions;
import mx.gob.jalisco.edu.consultaescolar.objects.CCT;

public class CCTAdapter extends RecyclerView.Adapter<CCTAdapter.ViewHolder>{

    List<CCT> items = new ArrayList<>();

    private final Context context;
    SelectOptions fragmentContext;

    public CCTAdapter(List<CCT> items, Context context, SelectOptions fragmentContext) {
        this.items = items;
        this.context = context;
        this.fragmentContext = fragmentContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView nombre, dom, cct,mun;
        public RelativeLayout select;

        public ViewHolder(View v, CCTAdapter root) {
            super(v);

            nombre = (TextView) v.findViewById(R.id.cct_name);
            dom = (TextView) v.findViewById(R.id.cct_dom);
            cct = (TextView) v.findViewById(R.id.cct_clave);
            mun = (TextView) v.findViewById(R.id.cct_mun);
            select = (RelativeLayout) v.findViewById(R.id.select);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = null;

        v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cct_card_item, viewGroup, false);
        return new ViewHolder(v,this);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        //viewHolder.text.setText(items.get(i).getText());

        viewHolder.nombre.setText(items.get(i).getNombrect());
        viewHolder.dom.setText(items.get(i).getDomicilio());
        viewHolder.cct.setText(items.get(i).getClavecct());
        viewHolder.mun.setText(items.get(i).getN_municipi());

        viewHolder.select.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                final CharSequence[] itemsOpc = {
                        "Opción 1", "Opción 2", "Opción 3", "Opción 4", "Opción 5"
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Seleccionar escuela como...");
                builder.setItems(itemsOpc, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        fragmentContext.selectAs(item+1, items.get(i).getClavecct());
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();


                /*ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("CCT", viewHolder.cct.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Copiado a portapapeles", Toast.LENGTH_SHORT).show();*/
                return false;
            }
        });

        final int item = i;

        /*viewHolder.to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });*/
        setFadeAnimation(viewHolder.itemView);
    }
    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1);
        view.startAnimation(anim);
    }
}
