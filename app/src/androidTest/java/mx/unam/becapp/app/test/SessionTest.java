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
    public void testSessionSignIn () {
        Session s = new Session("http://api-dgose.herokuapp.com", "/users/sign_in/");
        s.signIn("123456789", "apidgosepassword");
        //assertEquals(200, s.getStatus());
        assertEquals("Authentication correct!", s.getMessage());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}

