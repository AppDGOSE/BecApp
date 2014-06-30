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
import java.net.CookieManager;
import java.net.CookieHandler;

public class Session {
    private String url;

    private static String signInPath = "/users/sign_in/";
    private static String signOutPath = "/users/sign_out/";
    private static String DFORMAT = "yyyy-MM-dd HH:mm:ss Z";

    private String status;
    private String message;
    private Date last_signin;
    /**
     * Constructor
     */
    public Session(String url) {
        this.url = url;
        CookieHandler.setDefault(new CookieManager());
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
     * @param number Número de cuenta como String.
     * @param password Contraseña.
     */
    public void signIn(String number, String password) { 
        JSONObject jsonUser;
        JSONObject jsonObject = null;
        JSONObject response;

        // Constuir el objeto JSON.
        try {
            jsonUser = new JSONObject()
                .put("account_number", number)
                .put("password", password);
            jsonObject = new JSONObject()
                .put("user", jsonUser);
        } catch (JSONException e) {
            Log.d("SIGNIN", e.getMessage());
        }

        JSONObject result = send(signInPath, "POST", jsonObject);
        try {
            this.status = result.getString("status");
            this.message = result.getString("message");

            if (this.status == "200") {
                SimpleDateFormat dparse = new SimpleDateFormat(DFORMAT);
                this.last_signin = dparse.parse(result
                        .getJSONObject("user")
                        .getString("last_login"));
            }
        } catch (JSONException e) {
            Log.d("SIGNIN", e.getMessage());
        } catch (ParseException e) {
            Log.d("ParseException", e.getMessage());
        }

    }

    /**
     * Manda el mensaje a la API para cerrar la sesión.
     */
    public void signOut() {
        try {
            JSONObject result = send(this.signOutPath, "DELETE");
            this.status = result.getString("status");
            this.message = result.getString("message");
        } catch (JSONException e) {
            Log.d("JSONException", e.getMessage());
        }
    }

    /**
     * Envía una petición sin datos a la dirección indicada
     * @param path      Ruta de la API. E.g. /users/sign_in/
     * @param method    Método de la solicitud HTTP.
     * @return          Objeto JSON con la respuesta de la API
     *                  según la acción de la API.
     */
    public JSONObject send (String path, String method) {
        URL url;
        HttpURLConnection conn = null;
        int status = 0;
        // Preparación y configuración del mensaje.
        try {
            url = new URL( this.url + path );
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            //conn.setDoOutput(true);
            //conn.setRequestProperty("Accept", "application/json; charset=utf8");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf8");
            conn.connect();
            status = conn.getResponseCode();
        } catch (IOException e) {
            Log.d("IOException", e.getMessage());
        }

        InputStream istream;
        JSONObject result = null;
        try {
            // HttpURLConnection pone el acceso al contenido de
            // la respuesta en otro método cuando se trata de un error.
            if (status == 200) {
                istream = conn.getInputStream();
            } else {
                istream = conn.getErrorStream();
            }

            result = parseInputStream(istream);
            istream.close();
        } catch (IOException e) {
            Log.d("IOException", e.getMessage());
        } catch (JSONException e) {
            Log.d("JSONException", e.getMessage());
        }

        return result;
    }

    /**
     * Envia un objeto JSON a la dirección indicada 
     *
     * @param path      Ruta de la API. E.g. /users/sign_in/
     * @param method    Método de la solicitud HTTP.
     * @param data      Objeto JSON con la infomación según
     *                  lo requiera la acción de la API.
     *
     * @return          Objeto JSON con la respuesta de la API
     *                  según la acción de la API.
     */
    public JSONObject send (String path, String method, JSONObject data) {
        URL url;
        HttpURLConnection conn = null;

        // Preparación y configuración del mensaje.
        try {
            url = new URL( this.url + path );
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "application/json; charset=utf8");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf8");

            // Poner el objeto JSON en el cuerpo del request.
            OutputStreamWriter wr= new OutputStreamWriter(conn.getOutputStream());
            wr.write(data.toString());
            wr.flush();
        } catch (IOException e) {
            Log.d("APIConnection.send", e.getMessage());
        }

        // Enviar mensaje
        int status = 0;
        try {
            conn.connect();
            status = conn.getResponseCode();
        } catch (IOException e) {
            Log.d("APIConnection.send", e.getMessage());
        }

        InputStream istream;
        JSONObject result = null;
        try {
            // HttpURLConnection pone el acceso al contenido de
            // la respuesta en otro método cuando se trata de un error.
            if (status == 200) {
                istream = conn.getInputStream();
            } else {
                istream = conn.getErrorStream();
            }

            result = parseInputStream(istream);
            istream.close();
        } catch (IOException e) {
            Log.d("IOException", e.getMessage());
        } catch (JSONException e) {
            Log.d("JSONException", e.getMessage());
        }

        return result;
    }

    /**
     * Construye el objeto JSON dentro de un Input Stream.
     * @param stream Objeto InputStream que contiene una cadena JSON.
     * @return Objeto JSON.
     */
    private JSONObject parseInputStream (InputStream istream) throws JSONException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(istream));
        StringBuilder sbuilder = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sbuilder.append(line);
            }
        } catch (IOException e) {
            Log.d("ParseInputStream", e.getMessage());
        }
        return new JSONObject(sbuilder.toString());
    }
}
