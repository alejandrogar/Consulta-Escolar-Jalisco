package mx.gob.jalisco.edu.consultaescolar.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class JsonObjectAdapter<VH extends  RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH>{

    List<JSONObject> items = new ArrayList<>();

    private final Context context;
    private final Class<VH> holder;
    private final int layout;

    public JsonObjectAdapter(List<JSONObject> items, Context context, int layout, Class<VH> holder) {
        this.items = items;
        this.context = context;
        this.holder = holder;
        this.layout = layout;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        try {
            Constructor<VH> constructor = holder.getConstructor(View.class);
            return constructor.newInstance(v);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onBindViewHolder(VH viewHolder, int position) {
        populateViewHolder(viewHolder, position);
    }

    protected abstract void populateViewHolder(VH viewHolder, int position);

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1);
        view.startAnimation(anim);
    }
}
