package mx.unam.becapp.app.models;

import org.json.JSONObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
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
    public boolean logIn(String number, String password) {
        InputStream is = null;
        String result = null;

        // FIXME: I mean, just look at it :-\
        JSONObject jsonUser = new JSONObject()
            .put("account_number", number)
            .put("password", password);
        JSONObject jsonObject = new JSONObjects()
            .put("user", jsonUser);

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            StringEntity se = new StringEntity(jsonObject.toString());

            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            is = httpResponse.getEntity().getContent();

            if(is != null)
                result = convertInputStreamToString(is);
            else
                result = "Did not work!";

        } catch (Exception e) {
            result = e.getMessage();

        } finally {
            if (is != null) {
                is.close();
            } 
        }

        return result;
        return false;
    }

    /**
     * Envía el mensaje de fin de sesión a la API.
     *
     */
    public boolean logOut() {
        return false;
    }
}
