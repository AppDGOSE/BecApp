package mx.unam.becapp.app;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import org.json.JSONObject;
import android.util.Log;
import java.lang.StringBuilder;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

import java.io.IOException;
import org.json.JSONException;

public class Session {
    private String url = null;
    private String action = null;
    private String status = null;
    private String message = null;
    private static String DFORMAT = "yyy-MM-dd HH:mm:ss Z";
    private Date last_signin = null;

    /**
     * Constructor
     */
    public Session(String url, String action) {
        this.url = url;
        this.action = action;
    }

    public String getFullURL() {
        return this.url + this.action;
    }

    public String getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }

    public Date getLastSignIn() {
        return this.last_signin;
    }

    /**
     * Envia la solicitud de login a la API.
     *
     * @param number Número de cuenta como String.
     * @param password Contraseña.
     */
    public void signIn(String number, String password) { 
        JSONObject response = null;
        InputStream istream = null;
        String raw = null;
        int s;

        try {
            URL url = new URL( getFullURL() );

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
            wr.flush();

            // Enviar
            conn.connect();
            s = conn.getResponseCode();
            Log.d("SIGNIN", "Response status: " + s);

            // NOTE: HttpURLConnection pone el acceso al contenido de
            // la respuesta en otro método cuando se trata de un error.
            if (s == 200) {
                istream = conn.getInputStream();
            } else {
                istream = conn.getErrorStream();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(istream));
            String line;
            StringBuilder sbuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sbuilder.append(line);
            }
            istream.close();
            reader.close();
            JSONObject result = new JSONObject(sbuilder.toString());

            this.status = result.getString("status");
            this.message = result.getString("message");

            SimpleDateFormat dparse = new SimpleDateFormat(DFORMAT);
            this.last_signin = dparse.parse(result
                    .getJSONObject("user")
                    .getString("last_login"));

        } catch (JSONException e) {
            Log.d("SIGNIN", e.getMessage());
        } catch (IOException e) {
            Log.d("SIGNIN", e.getMessage());
        } catch (ParseException e) {
            Log.d("SIGNIN", e.getMessage());
        }
    }
}
