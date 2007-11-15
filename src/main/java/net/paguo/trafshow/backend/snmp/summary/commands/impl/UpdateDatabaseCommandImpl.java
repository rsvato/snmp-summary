package net.paguo.trafshow.backend.snmp.summary.commands.impl;

import net.paguo.trafshow.backend.snmp.summary.commands.UpdateDatabaseCommand;
import net.paguo.trafshow.backend.snmp.summary.database.DBProxy;
import net.paguo.trafshow.backend.snmp.summary.database.DBProxyFactory;
import net.paguo.trafshow.backend.snmp.summary.model.TrafficCollector;
import net.paguo.trafshow.backend.snmp.summary.model.RouterSummaryTraffic;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;

/**
 * @author Reyentenko
 */
public class UpdateDatabaseCommandImpl implements UpdateDatabaseCommand<TrafficCollector> {
    public static final Log log = LogFactory.getLog(UpdateDatabaseCommandImpl.class);
    public void doUpdate(TrafficCollector commandObject) {
        log.debug("doUpdate() <<<<");
        DBProxy proxy = DBProxyFactory.getDBProxy();
        Connection con = null;
        try{
            con = proxy.getConnection();
            PreparedStatement ipst = con.prepareStatement("insert into aggreg(dat, cisco, iface, t_in, t_out)" +
                    " values(?, ?, ?, ?, ?)");
            PreparedStatement upst = con.prepareStatement("update aggreg set t_in = ?, t_out = ? where a_id = ?");
            SearchTrafficDataCommandImpl search = new SearchTrafficDataCommandImpl();
            boolean inserts = false;
            boolean updates = false;
            int icount = 0;
            int ucount = 0;
            for (RouterSummaryTraffic o : commandObject.getTraffic().values()) {
                Long id = search.findRecordId(o);
                if (id != null){
                    upst.setLong(3, id);
                    upst.setLong(1, o.getTotalInput());
                    upst.setLong(2, o.getTotalOutput());
                    upst.addBatch();
                    updates = true;
                    ucount++;
                }else{
                    ipst.setDate(1, new Date(o.getDate().getTime()));
                    ipst.setString(2, o.getRouter());
                    ipst.setString(3, o.getIface());
                    ipst.setLong(4, o.getTotalInput());
                    ipst.setLong(5, o.getTotalOutput());
                    ipst.addBatch();
                    inserts = true;
                    icount++;
                }

            }
            log.debug("Assuming " + icount + " inserts and " + ucount + " updates" );
            search.closeConnection();
            if (inserts){
                int[] i = ipst.executeBatch();
                log.debug(i.length + " records inserted");
            }
            if (updates){
                int[] i = upst.executeBatch();
                log.debug(i.length + " records updated");
            }
            ipst.close();
            upst.close();
        } catch (SQLException e) {
            log.error(e);
        } finally {
            if (con != null){
                try {
                    con.close();
                } catch (SQLException e) {
                    log.error(e);
                }
            }
        }
        log.debug("doUpdate() >>>>");
    }
}
