package mx.unam.becapp.app;

import android.util.Log;

import org.json.JSONObject;
import org.json.JSONException;

/**
 * Clase para contener la información
 * del prefil de un becario.
 */
public class Profile {
    public static String path = "/profile/";
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
    public String student_number;
    public String fullname;
    public String school;
    public String major;
    public String phone_number;
    public String mobile_number;
    public String unam_email;
    public String com_email;
    public String scholarship;

    public Profile (Session s) {
        this.session = s;
    }

    public void getData() {
        JSONObject result = session.send(path, "GET");

        try {
            JSONObject profile = result.getJSONObject("profile");
            this.student_number = profile.getString("student_number");
            this.fullname = profile.getString("name");
            this.school = profile.getString("school");
            this.major = profile.getString("major");
            this.phone_number = profile.getJSONObject("phone")
                .getString("direct");
            this.mobile_number = profile.getJSONObject("phone")
                .getString("mobile");
            this.unam_email = profile.getJSONObject("email")
                .getString("unam");
            this.unam_email = profile.getJSONObject("email")
                .getString("com");
            this.scholarship = profile.getJSONObject("scholarship")
                .getString("name");
        } catch (JSONException e) {
            Log.d("SIGNIN", e.getMessage());
        }
    }
}
