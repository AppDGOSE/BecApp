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
        Session s = new Session();
        String response = s.signIn("123456789", "apidgosepassword");
        assertEquals("Lorem ipsum", "{\"message\":[\"Authentication correct!\"]}", response);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
