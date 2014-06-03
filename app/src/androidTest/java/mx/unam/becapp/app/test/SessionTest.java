package mx.unam.becapp.app.test;

import junit.framework.TestCase;
import mx.unam.becapp.app.Session;
import android.test.suitebuilder.annotation.SmallTest;

public class SessionTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @SmallTest
    public void testURL() {
        String url = "http://api-dgose.herokuapp.com";
        String url2 = "http://dgose.herokuapp.com";
        String action = "/users/sign_in/";
        Session s = new Session(url2, action);
        assertEquals(url2+action, s.getFullURL());
        s.setURL(url);
        assertEquals(url+action, s.getFullURL());
    }

    @SmallTest
    public void testSuccessfulSignIn () {
        Session s = new Session("http://api-dgose.herokuapp.com", "/users/sign_in/");
        s.signIn("072101030", "21101956");
        assertEquals("200", s.getStatus());
    }

    @SmallTest
    public void testInvalidSignIn () {
        Session s = new Session("http://api-dgose.herokuapp.com", "/users/sign_in/");
        s.signIn("072101030", "wrongpass");
        assertEquals("401", s.getStatus());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}

