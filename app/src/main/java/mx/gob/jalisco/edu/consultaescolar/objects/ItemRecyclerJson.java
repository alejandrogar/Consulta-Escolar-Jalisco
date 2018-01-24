package mx.gob.jalisco.edu.consultaescolar.objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Manuel on 22/06/17.
 */

public class ItemRecyclerJson {

    private JSONObject data;

    public  ItemRecyclerJson(JSONObject data){
        this.data = data;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public String getString(String key) {
        String value = "";
        try{
            value = data.getString(key);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return value;
    }

    public int getInt(String key) {
        int value = 0;
        try{
            value = data.getInt(key);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return value;
    }

}
