package mx.unam.becapp.app.test;

import junit.framework.TestCase;
import java.net.CookieHandler;
import java.net.CookieManager;
import mx.unam.becapp.app.Session;
import android.test.suitebuilder.annotation.SmallTest;
import java.util.Date;

public class SessionTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    String API_URL = "http://api-dgose.herokuapp.com";

    @SmallTest
    public void testInvalidSignIn () {
        Session s = new Session(API_URL);
        s.signIn("072101030", "wrongpass");
        assertEquals("401", s.getStatus());
    }

    @SmallTest
    public void testSuccessfulSignIn () {
        Session s = new Session(API_URL);
        s.signIn("072101030", "21101956");
        assertEquals("200", s.getStatus());
        // FIXME: revisar la cookie
    }

    @SmallTest
    public void testSuccessfulSignOut () {
        CookieManager cookiemanager = new CookieManager();
        CookieHandler.setDefault(cookiemanager);

        Session s = new Session(API_URL);
        s.signIn("072101030", "21101956");
        s.signOut();
        assertEquals("Sesión cerrada", s.getMessage());
    }

    // TODO: Prueba que verifique que last_login se
    // parseó correctamente.

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}

