package net.paguo.trafshow.backend.snmp.summary.commands.impl;

import net.paguo.trafshow.backend.snmp.summary.commands.DatabaseCommand;
import net.paguo.trafshow.backend.snmp.summary.database.DBProxy;
import net.paguo.trafshow.backend.snmp.summary.database.DBProxyFactory;
import net.paguo.trafshow.backend.snmp.summary.model.TrafficCollector;
import net.paguo.trafshow.backend.snmp.summary.model.TrafficRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.Calendar;
import java.util.Date;

public class GetTrafficDataCommand implements DatabaseCommand<TrafficCollector> {
    private static final String SQL = "select dt, cisco, interface, inoctets, outoctets, uptime from tr_with_uptime t where dt between ? and ? and (cisco, interface) in (select cisco, interface from cl) order by cisco, interface, dt";
    private static final Log log = LogFactory.getLog(GetTrafficDataCommand.class);
    private Date start;
    private Date end;

    public GetTrafficDataCommand(Date from, Date to) {
        this.start = from;
        this.end = to;
    }

    public TrafficCollector getData() {
        log.debug("getData(): <<<<");
        log.debug("Parameters are: " + start.toString() + " " + end.toString());
        DBProxy proxy = DBProxyFactory.getDBProxy();
        Connection conn = null;
        TrafficCollector collector = new TrafficCollector();
        try {
            conn = proxy.getConnection();
            PreparedStatement pst = conn.prepareStatement(SQL);
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);
            cal.roll(Calendar.HOUR, 1);
            Date media = cal.getTime();
            int processed = 0;
            while(media.before(end)) {
                log.debug("Setting end data to " + media);
                log.debug("Start date is " + start);
                pst.setTimestamp(1, new Timestamp(start.getTime()));
                pst.setTimestamp(2, new Timestamp(media.getTime()));
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    TrafficRecord record = new TrafficRecord();
                    record.setDatetime(new Date(rs.getTimestamp(1).getTime()));
                    record.setRouter(rs.getString(2));
                    record.setIface(rs.getString(3));
                    record.setInput(rs.getLong(4));
                    record.setOutput(rs.getLong(5));
                    record.setUptime(rs.getLong(6));
                    collector.addTrafficRecord(record);
                    processed++;
                }
                log.debug(processed + " records processed so far");
                rs.close();
                pst.clearParameters();
                start = (Date) media.clone();
                cal.roll(Calendar.HOUR_OF_DAY, 1);
                media = cal.getTime();
                if (media.before(start)){
                    cal.roll(Calendar.DAY_OF_MONTH, 1);
                    cal.roll(Calendar.SECOND, -1);
                    media = cal.getTime();
                }
            }
            pst.close();
        } catch (SQLException e) {
            log.error(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.error("Error when closing connection", e);
                }
            }
        }
        log.debug("Result size: " + collector.getTraffic().values().size());
        log.debug("getData(): >>>>");
        return collector;
    }
}
