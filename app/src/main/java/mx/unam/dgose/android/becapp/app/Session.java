package mx.unam.dgose.android.becapp.app;

import java.io.Serializable;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import org.json.JSONObject;

import android.os.NetworkOnMainThreadException;
import android.util.Log;

import java.lang.StringBuilder;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

import java.io.IOException;
import org.json.JSONException;

public class Session implements Serializable {
    private String url;

    private static String signInPath = "/users/sign_in/";
    private static String signOutPath = "/users/sign_out/";
    private static String DFORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private String status = "";
    private String message = "";
    private Date last_signin;
    private Boolean new_events = false;
    /**
     * Constructor
     */
    public Session(String url) {
        this.url = url;
    }

    public String getURL() {
        return this.url;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMessage() {
        return this.message;
    }
    public Date getLastSignIn() {
        return this.last_signin;
    }
    public Boolean getNewEvents() {
        return this.new_events;
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
            //Log.d("SIGNIN", e.getMessage());
        }

        JSONObject result = send(signInPath, "POST", jsonObject);
        try {
            this.status = result.getString("status");
            this.message = result.getString("message");

            if (this.status.equals("200")) {

                this.new_events = result.getJSONObject("user")
                        .getBoolean("new_events");

                SimpleDateFormat dparse = new SimpleDateFormat(DFORMAT);
                this.last_signin = dparse.parse(result
                                                .getJSONObject("user")
                                                .getString("last_login"));

            }
        } catch (JSONException e) {
        } catch (ParseException e) {
        } catch (NullPointerException e) {
        }

    }

    /**
     * Manda el mensaje a la API para cerrar la sesión.
     */
    public void signOut() {
        try {
            JSONObject result = send(signOutPath, "DELETE");
            this.status = result.getString("status");
            this.message = result.getString("message");
        } catch (JSONException e) {
        } catch (NullPointerException e) {
            this.status = "";
            this.message = "";
        }
    }

    /**
     * Envía una petición sin datos a la dirección indicada
     * @param path      Ruta de la API. E.g. /users/sign_in/
     * @param method    Método de la solicitud HTTP.
     * @return          Objeto JSON con la respuesta de la API
     *                  según la acción de la API.
     */
    public JSONObject send(String path, String method) {
        URL url;
        HttpURLConnection conn = null;

        int status = 0;
        // Preparación y configuración del mensaje.
        try {
            url = new URL( this.url + path );
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoInput(true);

            conn.setRequestProperty("Content-Type", "application/json; charset=utf8");
            conn.connect();
            status = conn.getResponseCode();
        } catch (IOException e) {
        } catch (NetworkOnMainThreadException e) {
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
        } catch (JSONException e) {
        } catch (NetworkOnMainThreadException e) {
        } catch (NullPointerException e) {
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
    public JSONObject send(String path, String method, JSONObject data) {
        URL url;
        HttpURLConnection conn = null;

        // Preparación y configuración del mensaje.
        try {
            url = new URL( this.url + path );
            conn = (HttpURLConnection) url.openConnection();
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
        }

        // Enviar mensaje
        int status = 0;
        try {
            conn.connect();
            status = conn.getResponseCode();
        } catch (IOException e) {
        } catch (NullPointerException e) {
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
        } catch (JSONException e) {
        } catch (NullPointerException e) {
        }

        return result;
    }

    /**
     * Construye el objeto JSON dentro de un Input Stream.
     * @param istream Objeto InputStream que contiene una cadena JSON.
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
        }
        return new JSONObject(sbuilder.toString());
    }
}
