package mx.unam.becapp.app.test;

import junit.framework.TestCase;
import mx.unam.becapp.app.Session;
import android.test.suitebuilder.annotation.SmallTest;
import java.util.Date;

public class SessionTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    String API_URL = "http://api-dgose.herokuapp.com";
    String ACTION = "/users/sign_in/";

    @SmallTest
    public void testURL() {
        Session s = new Session(API_URL, ACTION);
        assertEquals(API_URL+ACTION, s.getFullURL());
    }

    @SmallTest
    public void testInvalidSignIn () {
        Session s = new Session(API_URL, ACTION);
        s.signIn("072101030", "wrongpass");
        assertEquals("401", s.getStatus());
    }

    @SmallTest
    public void testSuccessfulSignIn () {
        Session s = new Session(API_URL, ACTION);
        s.signIn("072101030", "21101956");
        assertEquals("200", s.getStatus());
    }

    @SmallTest
    public void testDateObject () {
        Session s = new Session(API_URL, ACTION);
        s.signIn("072101030", "21101956");
        Date date = null;
        assertSame(date, s.getLastSignIn());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}

