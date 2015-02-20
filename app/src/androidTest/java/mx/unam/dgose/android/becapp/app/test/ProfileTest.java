package mx.unam.dgose.android.becapp.app.test;

import junit.framework.TestCase;
import mx.unam.dgose.android.becapp.app.Session;
import mx.unam.dgose.android.becapp.app.Profile;
import android.test.suitebuilder.annotation.SmallTest;

public class ProfileTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    String API_URL = "http://api-dgose.herokuapp.com";

    @SmallTest
    public void testNoSignIn () {
		Session s = new Session(API_URL);
		s.signIn("072101030", "wrongpass");

		Profile p = new Profile(s);
        p.getData();
        assertEquals("401", s.getStatus());
    }

    @SmallTest
    public void testProfileFound () {
        Session s = new Session(API_URL);
        s.signIn("072101030", "21101956");
        Profile p = new Profile(s);
        p.getData();
        assertEquals("200", s.getStatus());
        assertEquals("072101030", p.student_number);
        assertEquals("JAIME GARCIA ESPINOSA", p.fullname);
        assertEquals("jaimege@hotmail.com", p.unam_email);
        assertEquals("jaimege@hotmail.com", p.com_email);
        assertEquals("", p.curp);
        assertEquals("55819081", p.phone_number);
        assertEquals("0445536645855", p.mobile_number);
        assertEquals("FACULTAD DE PSICOLOGIA", p.school);
        assertEquals("PSICOLOGIA", p.major);
        assertEquals("PRONABES", p.scholarship);
        assertEquals("Becario Activo", p.scholarship_status);
        assertEquals("2013-2014", p.current_cycle);
    }
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
