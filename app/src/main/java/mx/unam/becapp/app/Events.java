package mx.unam.becapp.app;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Clase para contener la información
 * del prefil de un becario.
 */
public class Events extends Information {
    public static String path = "/events/";
    private Session session;

    /* NOTE: Todas las siguientes propiedades
     * son de sólo lectura. Lo que implica que
     * deberían ser privadas y tener getters.
     * Sin embargo: http://goo.gl/H9yjXh
     *
     * NOTE: Los correos y números se guardan como
     * cadenas y no como tales porque el usuario
     * no va a llamar o mandar correo a si mismo.
     */

    public class Event {
        public String title;
        public String summary;
        public String start;
        public String end;
        public String place_name;
        public String place_location;
        public String url;
        public String alias;
        public String email;
    }

    public ArrayList<Event> events;

    public Events(Session session) {
        this.session = session;
    }

    public void getData() {

		status = "0";
		message = "Failure";
		
        try {
			JSONObject result = session.send(path, "GET");
			
			status = result.getString("status");
			message = result.getString("message");

            if (session.getStatus().equals("200")) {
                JSONArray events = result.getJSONArray("events");
                this.events = new ArrayList<Event>(events.length());
                for (int i = 0; i < events.length(); i++) {
                    JSONObject temp = events.getJSONObject(i);

                    Event event = new Event();
                    this.events.add(i, event);

                    event.title = temp.getString("title");
                    event.summary= temp.getString("abstract");
                    event.start = temp.getJSONObject("date").getString("start");
                    event.end = temp.getJSONObject("date").getString("end");
                    event.place_name = temp.getJSONObject("place").getString("name");
                    event.place_location = temp.getJSONObject("place").getString("location");
                    event.url = temp.getString("url");
                    event.alias = temp.getString("alias");
                    event.email = temp.getString("email");
                }

        }

        } catch (JSONException e) {
        } catch (NullPointerException e) {
        }
    }
}
