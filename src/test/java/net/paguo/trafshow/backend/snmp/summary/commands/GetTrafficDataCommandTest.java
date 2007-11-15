package net.paguo.trafshow.backend.snmp.summary.commands;

import net.paguo.trafshow.backend.snmp.summary.commands.impl.GetTrafficDataCommand;
import net.paguo.trafshow.backend.snmp.summary.commands.impl.UpdateDatabaseCommandImpl;
import net.paguo.trafshow.backend.snmp.summary.model.TrafficCollector;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class GetTrafficDataCommandTest {
    @Test
    public void testGetData() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 10);

        Date begin = cal.getTime();

        cal.roll(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        Date end = cal.getTime();

        GetTrafficDataCommand command = new GetTrafficDataCommand(begin, end);
        TrafficCollector result = command.getData();
        Assert.assertNotNull(result);
        UpdateDatabaseCommand<TrafficCollector> up = new UpdateDatabaseCommandImpl();
        up.doUpdate(result);
    }
}
