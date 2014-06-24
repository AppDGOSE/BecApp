package mx.unam.becapp.app;

import android.util.Log;

import java.util.ArrayList;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONException;

/**
 * Lista de eventos
 */
public class Event {
    public URL link;
    public String title;
    public String description;
    public URL image;
    public String place_name;
    public String place_location;
    public String email;
}
