package mx.unam.becapp.app;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import org.json.JSONObject;
import android.util.Log;

public class Session {
    private String url = null;
    private String action = null;
    private int status;
    private String message = null;

    /**
     * Constructor
     */
    public Session(String url, String action) {
        this.url = url;
        this.action = action;
    }

    public int getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }

    /**
     * Envia la solicitud de login a la API.
     *
     * @param number Número de cuenta como String.
     * @param password Contraseña.
     */
    public void signIn(String number, String password) { 
        JSONObject response = null;
        InputStream is = null;

        try {
            URL url = new URL( this.url + this.action );

            // Configuración de la conexión.
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setRequestProperty("Accept", "application/json; charset=utf8");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf8");

            // Constuir el objeto JSON.
            JSONObject jsonUser = new JSONObject()
                .put("account_number", number)
                .put("password", password);
            JSONObject jsonObject = new JSONObject()
                .put("user", jsonUser);

            // Poner el objeto JSON en el cuerpo del request.
            OutputStreamWriter wr= new OutputStreamWriter(conn.getOutputStream());
            wr.write(jsonObject.toString());

            // Enviar
            conn.connect();
            int status = conn.getResponseCode();
            Log.d("SIGNIN", "The response is: " + status);

            // Parsear la respuesta.
            is = conn.getInputStream();
            response = new JSONObject(is.toString());
            is.close();
            this.status = response.getInt("status");
            this.message = response.getString("message");

        } catch (Exception e) {
            Log.d("SIGNIN", e.getMessage());
        }
    }

}
