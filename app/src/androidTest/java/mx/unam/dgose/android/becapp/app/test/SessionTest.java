package mx.unam.dgose.android.becapp.app.test;

import junit.framework.TestCase;
import mx.unam.dgose.android.becapp.app.Session;
import android.test.suitebuilder.annotation.SmallTest;

public class SessionTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    String API_URL = "http://api-dgose.herokuapp.com";

    @SmallTest
    public void testInvalidSignIn() {
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

