package mx.gob.jalisco.edu.consultaescolar.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import mx.gob.jalisco.edu.consultaescolar.Inscription;
import mx.gob.jalisco.edu.consultaescolar.R;
import mx.gob.jalisco.edu.consultaescolar.ViewPage;
import mx.gob.jalisco.edu.consultaescolar.objects.Card;
import mx.gob.jalisco.edu.consultaescolar.utils.NetworkUtils;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder>{

    List<Card> items = new ArrayList<>();

    private final Context context;

    public CardsAdapter(List<Card> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView title, subtitle;
        public ImageView img;
        public RelativeLayout to;

        public ViewHolder(View v, CardsAdapter padre) {
            super(v);

            img = (ImageView) v.findViewById(R.id.image);
            subtitle = (TextView) v.findViewById(R.id.subtitle);
            title = (TextView) v.findViewById(R.id.title);
            to = (RelativeLayout) v.findViewById(R.id.to);
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
                .inflate(R.layout.card_item, viewGroup, false);
        return new ViewHolder(v,this);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        //viewHolder.text.setText(items.get(i).getText());

        viewHolder.img.setImageResource(items.get(i).getImage());
        viewHolder.title.setText(items.get(i).getText());
        viewHolder.subtitle.setText(items.get(i).getSubtitle());

        final int item = i;

        viewHolder.to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NetworkUtils internet = new NetworkUtils(context);
                if(internet.isConnectingToInternet()){
                    if(Objects.equals(items.get(item).getType(), Card.ACTIVITY)){
                        Intent intent = new Intent(context, items.get(item).getIntent());
                        Bundle bundle = new Bundle();
                        bundle.putString("KEY","KEY_I");
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }

                    if(Objects.equals(items.get(item).getType(), Card.WEB_VIEW)){
                        Intent intent = new Intent(context, ViewPage.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("URL",items.get(item).getUrl());
                        bundle.putString("TITLE",items.get(item).getText());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }

                    if(Objects.equals(items.get(item).getType(), Card.BROWSER)){
                        String url = items.get(item).getUrl();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        context.startActivity(i);
                    }
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(false)
                            .setMessage(R.string.alert_no_internet)
                            .setPositiveButton(R.string.cast_tracks_chooser_dialog_ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                }

/*
                if(Objects.equals(items.get(item).getText(), "Descarga tu certificado")){
                    String url = items.get(item).getUrl();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);

                }else if(Objects.equals(items.get(item).getText(), "Preinscripciones 2017-2018")){
                    NetworkUtils internet = new NetworkUtils(context);
                    if(internet.isConnectingToInternet()){
                        Intent intent = new Intent(context, Inscription.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("KEY","KEY_I");
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setCancelable(false)
                                .setMessage(R.string.alert_no_internet)
                                .setPositiveButton(R.string.cast_tracks_chooser_dialog_ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                       dialog.dismiss();
                                    }
                                });
                        builder.create().show();
                    }

                }else if(Objects.equals(items.get(item).getText(), "Preinscripciones 2017-2018")){

                }else{
                    Intent intent = new Intent(context, ViewPage.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("URL",items.get(item).getUrl());
                    bundle.putString("TITLE",items.get(item).getText());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
                */
            }
        });
        setFadeAnimation(viewHolder.itemView);
    }
    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1);
        view.startAnimation(anim);
    }
}