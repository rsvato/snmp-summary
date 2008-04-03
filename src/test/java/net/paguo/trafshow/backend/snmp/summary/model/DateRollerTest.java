package net.paguo.trafshow.backend.snmp.summary.model;

import org.junit.Test;
import org.junit.Assert;

import java.util.Date;

/**
 * @author Reyentenko
 */
public class DateRollerTest {

    @Test
    public final void testDateRollerCount() throws BadParameterException {
        DateRollerJDKImpl dr = new DateRollerJDKImpl(DateParameters.get("2008-03-31"));
        int i = 0;
        Date d = null;
        while(dr.hasNextDate()){
            i++;
            d = dr.getNextDate();
            Assert.assertTrue("" + d + " " + dr.getCurrentDate(),
                    d.after(dr.getCurrentDate()));
        }
        Assert.assertEquals("" + d + " " + dr.getEndDate(), 24, i);
    }


    @Test
    public final void testJTDateRollerCount() throws BadParameterException {
        DateRollerJodaImpl dr = new DateRollerJodaImpl(DateParameters.get("2008-03-31"));
        int i = 0;
        Date d = null;
        while(dr.hasNextDate()){
            i++;
            d = dr.getNextDate();
            Assert.assertTrue("" + d + " " + dr.getCurrentDate(),
                    d.after(dr.getCurrentDate()));
        }
        Assert.assertEquals("" + d + " " + dr.getEndDate(), 24, i);
    }
}
