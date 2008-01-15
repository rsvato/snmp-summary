package net.paguo.trafshow.backend.snmp.summary;

import net.paguo.trafshow.backend.snmp.summary.commands.DatabaseCommand;
import net.paguo.trafshow.backend.snmp.summary.commands.UpdateDatabaseCommand;
import net.paguo.trafshow.backend.snmp.summary.commands.impl.GetTrafficDataCommand;
import net.paguo.trafshow.backend.snmp.summary.commands.impl.UpdateDatabaseCommandImpl;
import net.paguo.trafshow.backend.snmp.summary.model.TrafficCollector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Main application class
 */
public class App {
    private static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final Log log = LogFactory.getLog(App.class);
    public static void main(String[] args) {
        if (args.length < 1){
            System.err.println("Usage: net.paguo.trafshow.backend.snmp.summary.App YYYY-MM-DD");
            System.exit(1);
        }
        long now = System.currentTimeMillis();
        String arg = args[0];
        Date date = FORMAT.parse(arg, new ParsePosition(0));  
        if (date != null && FORMAT.format(date).equals(arg)){
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 10);

            Date begin = cal.getTime();

            cal.roll(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 10);

            Date end = cal.getTime();

            DatabaseCommand<TrafficCollector> command = new GetTrafficDataCommand(begin, end);
            UpdateDatabaseCommand<TrafficCollector> updateDatabaseCommand
                    = new UpdateDatabaseCommandImpl();
            final TrafficCollector collector = command.getData();
            updateDatabaseCommand.doUpdate(collector);
            log.debug("Time of processing: " + (System.currentTimeMillis() - now));
        }else{
            final String errorMessage = new StringBuilder().append("Invalid date: ").
                    append(arg).toString();
            log.error(errorMessage);
            System.err.println(errorMessage);

        }
    }
}
