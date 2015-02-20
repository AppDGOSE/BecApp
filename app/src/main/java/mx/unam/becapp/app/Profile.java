package mx.unam.becapp.app;

import android.util.Log;

import org.json.JSONObject;
import org.json.JSONException;

/**
 * Clase para contener la información
 * del prefil de un becario.
 */
public class Profile extends Information {
    public static String path = "/profile/";

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
    public String curp;
    public String current_cycle;
    public String scholarship_status;
    public String bank_name;
    public String bank_account;

    public Profile (Session session) {
        this.session = session;
    }

    public void getData() {

        status = "0";
        message = "Failure";

        try {
            JSONObject result = session.send(path, "GET");

            status = result.getString("status");
            message = result.getString("message");

            if (status.equals("200")) {
                JSONObject profile = result.getJSONObject("profile");

                student_number = profile.getString("student_number");
                fullname = profile.getString("name");
                unam_email = profile.getJSONObject("email").getString("unam");
                com_email = profile.getJSONObject("email").getString("com");
                curp = profile.getString("curp");
                phone_number = profile.getJSONObject("phone").getString("direct");
                mobile_number = profile.getJSONObject("phone").getString("mobile");
                school = profile.getString("school");
                major = profile.getString("major");
                scholarship = profile.getJSONObject("scholarship").getString("name");
                scholarship_status = profile.getJSONObject("scholarship").getString("status");
                current_cycle = profile.getString("current_cycle");
                bank_name = profile.getJSONObject("bank").getString("name");
                bank_account = profile.getJSONObject("bank").getString("account");
            }

        } catch (JSONException e) {
        } catch (NullPointerException e) {
        }
    }
}
