package mx.unam.becapp.app;

import org.json.JSONObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.HttpResponse;

public class Session {
    private String app_url = "http://api-dgose.herokuapp.com";
    private String action_url = "/users/sign_in/";

    /**
     * Envia la solicitud de login a la API.
     *
     * @param number Número de cuenta como String.
     * @param password Contraseña.
     */
    public String logIn(String number, String password) {
        InputStream is = null;
        String result = null;


        try {
            // Constuir el objeto JSON.
            JSONObject jsonUser = new JSONObject()
                .put("account_number", number)
                .put("password", password);
            JSONObject jsonObject = new JSONObject()
                .put("user", jsonUser);

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(app_url+action_url);

            StringEntity se = new StringEntity(jsonObject.toString());

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            is = httpResponse.getEntity().getContent();

            if(is != null) {
                result = convertInputStreamToString(is);
                is.close();
            } else {
                result = "Did not work!";
            }

        } catch (Exception e) {
            result = e.getMessage();
        }

        return result;
    }

    /**
     * Envía el mensaje de fin de sesión a la API.
     *
     */
    public boolean logOut() {
        return false;
    }

    /**
     * Extrae el contenido de la respuesta de la API como String.
     */
    private static String convertInputStreamToString(InputStream stream)
        throws IOException {

        BufferedReader reader = new BufferedReader( new InputStreamReader(stream));
        String line = "";
        String result = "";
        while((line = reader.readLine()) != null)
            result += line;

        stream.close();
        return result;

    } 
}
