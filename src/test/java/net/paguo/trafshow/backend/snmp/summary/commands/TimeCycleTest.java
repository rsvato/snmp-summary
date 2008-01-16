package net.paguo.trafshow.backend.snmp.summary.commands;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * User: sreentenko
 * Date: 16.01.2008
 * Time: 2:44:24
 */
public class TimeCycleTest {
    @Test
    public void testCycleDate(){
       Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 10);

        Date begin = cal.getTime();

        cal.roll(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 10);

        Date end = cal.getTime();
        int cycles = cycle(begin, end);
        Assert.assertEquals(24, cycles);
    }

    private int cycle(Date start, Date end){
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        cal.roll(Calendar.HOUR, 1);
        Date media = cal.getTime();
        int cycles = 0;
        while(media.before(end)){
            cycles++;
            start = new Date(media.getTime());
            cal.setTime(media);
            cal.roll(Calendar.HOUR_OF_DAY, 1);
            media = cal.getTime();
            if (media.before(start)){
                cal.roll(Calendar.DAY_OF_MONTH, 1);
                cal.roll(Calendar.SECOND, -1);
                media = cal.getTime();
            }

        }
        System.out.println(start);
        return cycles;
    }

}
