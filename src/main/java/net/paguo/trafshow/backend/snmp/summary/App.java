package net.paguo.trafshow.backend.snmp.summary;

import net.paguo.trafshow.backend.snmp.summary.commands.DatabaseCommand;
import net.paguo.trafshow.backend.snmp.summary.commands.UpdateDatabaseCommand;
import net.paguo.trafshow.backend.snmp.summary.commands.impl.GetTrafficDataCommand;
import net.paguo.trafshow.backend.snmp.summary.commands.impl.UpdateDatabaseCommandImpl;
import net.paguo.trafshow.backend.snmp.summary.model.BadParameterException;
import net.paguo.trafshow.backend.snmp.summary.model.DateParameters;
import net.paguo.trafshow.backend.snmp.summary.model.TrafficCollector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

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

        try {
            DateParameters parameters = DateParameters.get(arg);
            DatabaseCommand<TrafficCollector> command = new GetTrafficDataCommand(parameters.getStartDate(),
                    parameters.getEndDate());
            UpdateDatabaseCommand<TrafficCollector> updateDatabaseCommand
                    = new UpdateDatabaseCommandImpl();
            final TrafficCollector collector = command.getData();
            updateDatabaseCommand.doUpdate(collector);
            log.debug("Time of processing: " + (System.currentTimeMillis() - now));
        } catch (BadParameterException e) {
            final String errorMessage = new StringBuilder().append("Invalid date: ").
                    append(e.getBadParameter()).toString();
            log.error(errorMessage);
            System.err.println(errorMessage);
        }
    }
}
