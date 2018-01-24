package mx.gob.jalisco.edu.consultaescolar.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 28/06/16.
 */
public class Card {

    public static String ACTIVITY = "activity";
    public static String WEB_VIEW = "web_view";
    public static String BROWSER = "borwser";

    private String type;
    private String url;
    private String text, subtitle;
    private int image;
    public static List<Card> Card = new ArrayList<>();
    private Class intent;


    public Card(String type, String url, String text, String subtitle, int image) {
        this.type = type;
        this.url = url;
        this.text = text;
        this.subtitle = subtitle;
        this.image = image;
    }

    public void setIntent(Class ActivityIntent){
        this.intent = ActivityIntent;
    }

    public Class getIntent(){
        return intent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getSubtitle() {
        return subtitle;
    }
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
