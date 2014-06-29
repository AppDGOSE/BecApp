package mx.unam.becapp.app.test;

import junit.framework.TestCase;
import mx.unam.becapp.app.Session;
import mx.unam.becapp.app.Events;
import android.test.suitebuilder.annotation.SmallTest;

public class EventsTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    String API_URL = "http://api-dgose.herokuapp.com";

    @SmallTest
    public void testNoSignIn () {
        Session s = new Session(API_URL);
        s.signIn("072101030", "wrongpass");

        Events e = new Events(s);
        e.getData();
        assertEquals("401", s.getStatus());
    }

    @SmallTest
    public void testEventsFound () {
        Session s = new Session(API_URL);
        s.signIn("072101030", "21101956");
        Events e = new Events(s);

        e.getData();

        assertEquals("200", s.getStatus());

        assertEquals("ENCUENTRO EN MEXICO 2010 CONTRUYENDO FUTUROS",
                    e.events.get(0).title);
        assertEquals("", e.events.get(0).summary);
        assertEquals("2010-10-19 18:00:00", e.events.get(0).start);
        assertEquals("2010-10-22 00:00:00", e.events.get(0).end);
        assertEquals("CENTRO CULTURAL UNIVERSITARIO TLATELOLCO DE LA UNAM",
                    e.events.get(0).place_name);
        assertEquals("", e.events.get(0).place_location);
        assertEquals("www.becarios.unam.mx/Sistemas/Eventos/encuentromexico/confirmados2.php",
                    e.events.get(0).url);
        assertEquals("registro.php", e.events.get(0).alias);
        assertEquals("", e.events.get(0).email);
    }
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
