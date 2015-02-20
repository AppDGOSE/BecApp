package mx.unam.dgose.android.becapp.app.test;

import junit.framework.TestCase;
import mx.unam.dgose.android.becapp.app.Session;
import mx.unam.dgose.android.becapp.app.Payments;
import android.test.suitebuilder.annotation.SmallTest;

public class PaymentsTest extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    String API_URL = "http://api-dgose.herokuapp.com";

    @SmallTest
    public void testNoSignIn () {
        Session s = new Session(API_URL);
        s.signIn("072101030", "wrongpass");

        Payments p = new Payments(s);
        p.getData();
        assertEquals("401", s.getStatus());
    }

    @SmallTest
    public void testPaymentsFound () {
        Session s = new Session(API_URL);
        s.signIn("072101030", "21101956");
        Payments p = new Payments(s);
        p.getData();
        assertEquals("200", s.getStatus());
        assertEquals("BANAMEX", p.bank_name);
        assertEquals("8548730346581998", p.bank_account);

        // January
        assertEquals("Enero", p.calendar.get(0).month);
        assertEquals("0", p.calendar.get(0).amount);
        assertEquals("3 de febrero", p.calendar.get(0).done);
        assertEquals("Sin realizar", p.calendar.get(0).status);

        //February
        assertEquals("Febrero", p.calendar.get(1).month);
        assertEquals("0", p.calendar.get(1).amount);
        assertEquals("3 de marzo", p.calendar.get(1).done);
        assertEquals("Sin realizar", p.calendar.get(1).status);

        //March
        assertEquals("Marzo", p.calendar.get(2).month);
        assertEquals("0", p.calendar.get(2).amount);
        assertEquals("1 de abril", p.calendar.get(2).done);
        assertEquals("Sin realizar", p.calendar.get(2).status);

        //April
        assertEquals("Abril", p.calendar.get(3).month);
        assertEquals("0", p.calendar.get(3).amount);
        assertEquals("2 de mayo", p.calendar.get(3).done);
        assertEquals("Sin realizar", p.calendar.get(3).status);

        //May
        assertEquals("Mayo", p.calendar.get(4).month);
        assertEquals("0", p.calendar.get(4).amount);
        assertEquals("2 de junio", p.calendar.get(4).done);
        assertEquals("Sin realizar", p.calendar.get(4).status);

        //June
        assertEquals("Junio", p.calendar.get(5).month);
        assertEquals("0", p.calendar.get(5).amount);
        assertEquals("1 de julio", p.calendar.get(5).done);
        assertEquals("Sin realizar", p.calendar.get(5).status);

        //July
        assertEquals("Julio", p.calendar.get(6).month);
        assertEquals("0", p.calendar.get(6).amount);
        assertEquals("4 de agosto", p.calendar.get(6).done);
        assertEquals("Sin realizar", p.calendar.get(6).status);

        //August
        assertEquals("Agosto", p.calendar.get(7).month);
        assertEquals("0", p.calendar.get(7).amount);
        assertEquals("1 de septiembre", p.calendar.get(7).done);
        assertEquals("Sin realizar", p.calendar.get(7).status);

        //September
        assertEquals("Septiembre", p.calendar.get(8).month);
        assertEquals("750", p.calendar.get(8).amount);
        assertEquals("1 de octubre", p.calendar.get(8).done);
        assertEquals("Realizado", p.calendar.get(8).status);

        //October
        assertEquals("Octubre", p.calendar.get(9).month);
        assertEquals("750", p.calendar.get(9).amount);
        assertEquals("1 de noviembre", p.calendar.get(9).done);
        assertEquals("Realizado", p.calendar.get(9).status);

        //November
        assertEquals("Noviembre", p.calendar.get(10).month);
        assertEquals("750", p.calendar.get(10).amount);
        assertEquals("2 de diciembre", p.calendar.get(10).done);
        assertEquals("Realizado", p.calendar.get(10).status);

        //December
        assertEquals("Diciembre", p.calendar.get(11).month);
        assertEquals("0", p.calendar.get(11).amount);
        assertEquals("20 de diciembre", p.calendar.get(11).done);
        assertEquals("Sin realizar", p.calendar.get(11).status);
    }
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
