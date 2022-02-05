package net.paguo.trafshow.backend.snmp.summary;

import net.paguo.trafshow.backend.snmp.summary.commands.DatabaseCommand;
import net.paguo.trafshow.backend.snmp.summary.commands.UpdateDatabaseCommand;
import net.paguo.trafshow.backend.snmp.summary.commands.impl.GetTrafficDataCommand;
import net.paguo.trafshow.backend.snmp.summary.commands.impl.UpdateDatabaseCommandImpl;
import net.paguo.trafshow.backend.snmp.summary.model.BadParameterException;
import net.paguo.trafshow.backend.snmp.summary.model.DateParameters;
import net.paguo.trafshow.backend.snmp.summary.model.TrafficCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main application class
 */
public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);
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
            log.error("Invalid date {}", e.getBadParameter());
        }
    }
}
