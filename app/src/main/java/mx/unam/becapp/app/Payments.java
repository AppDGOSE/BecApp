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
public class Payments {
    public static String path = "/payments/";
    private Session session;
    private String status;
    private String message;

    /* NOTE: Todas las siguientes propiedades
     * son de sólo lectura. Lo que implica que
     * deberían ser privadas y tener getters.
     * Sin embargo: http://goo.gl/H9yjXh
     *
     * NOTE: Los correos y números se guardan como
     * cadenas y no como tales porque el usuario
     * no va a llamar o mandar correo a si mismo.
     */

    public class Payment {
        public String month;
        public String amount;
        public String done;
        public String status;

        Payment(String month, String amount, String done, String status) {
            this.month = month;
            this.amount = amount;
            this.done = done;
            this.status = status;
        }
    }
    public String bank_name;
    public String bank_account;

    public ArrayList<Payment> calendar;

    public Payments (Session s) {
        session = s;
    }

    public void getData() {

        this.status = "0";
        this.message = "Failure";

        try {

            JSONObject result = session.send(path, "GET");

            status = result.getString("status");
            message = result.getString("message");

            if (status.equals("200")) {
                JSONObject payments = result.getJSONObject("payments");

                bank_name = payments.getJSONObject("bank").getString("name");
                bank_account = payments.getJSONObject("bank").getString("account");

                JSONArray months = payments.getJSONArray("calendar");
                calendar = new ArrayList<Payment>(months.length());
                for (int i = 0; i < months.length(); i++) {
                    JSONObject temp = months.getJSONObject(i);
                    String month = temp.getString("month");
                    String amount = temp.getString("amount");
                    String done = temp.getString("done");
                    String status = temp.getString("status");

                    calendar.add(i, new Payment(month, amount, done, status));
                }
            }

        } catch (JSONException e) {
        //} finally {
        //    dummyData();
        }
    }

    public boolean success() {
        return status.equals("200");
    }

    public String getStatus() {
        return status;
    }

    public void dummyData() {
        /**
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

            status = "200";
         **/
    }
}
