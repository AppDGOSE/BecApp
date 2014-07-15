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
    public String curp;
    public String current_cycle;
    public String scholarship_status;
		
    public Profile (Session s) {
        session = new Session(s.getURL());
    }
	
    public void getData() {
		
        try {
            JSONObject result = session.send(path, "GET");

            if (session.getStatus().equals("200")) {
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
            }
			
        } catch (JSONException e) {
        } catch (NullPointerException e) {
        } finally {
            dummyData();
        }
    }

    public boolean success() {
        return session.getStatus().equals("200");
    }

    public String getStatus() {
        return session.getStatus();
    }

    public void dummyData() {
        student_number = "307048503";
        fullname = "José Emiliano Cabrera Blancas";
        unam_email = "user@unam.mx";
        com_email = "user@example.com";
        curp = "CABE910808MDASD";
        phone_number = "(12 32 13 12";
        mobile_number = "(55) 12 91 82 82 03";
        school = "Facultad de Ciencias";
        major = "Ciencias de la Computación";
        scholarship = "Pronabes";
        scholarship_status = "Activa";
        current_cycle = "2014 1";

        session.setStatus("200");
    }
}
