package net.paguo.trafshow.backend.snmp.summary.commands.impl;

import net.paguo.trafshow.backend.snmp.summary.database.DBProxy;
import net.paguo.trafshow.backend.snmp.summary.database.DBProxyFactory;
import net.paguo.trafshow.backend.snmp.summary.model.RouterSummaryTraffic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Reyentenko
 */
public class SearchTrafficDataCommandImpl {
    private static final Logger log = LoggerFactory.getLogger(SearchTrafficDataCommandImpl.class);
    private Connection connection;
    private static final String FINDER_SQL = "select a_id from aggreg where cisco = ? and" +
            " iface = ? and dat = ?";

    public SearchTrafficDataCommandImpl() {
    }


    public final Long findRecordId(RouterSummaryTraffic record) {
        if (connection == null) {
            openConnection();
        }
        Long result = null;
        try {
            PreparedStatement pst = connection.prepareStatement(FINDER_SQL);
            pst.setString(1, record.getRouter());
            pst.setString(2, record.getIface());
            pst.setDate(3, new java.sql.Date(record.getDate().getTime()));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                result = rs.getLong(1);
            }
            rs.close();
            pst.close();
        } catch (SQLException e) {
            log.error("Cannot find record id", e);
        }
        return result;
    }

    private void openConnection() {
        DBProxy proxy = DBProxyFactory.getDBProxy();
        try {
            connection = proxy.getConnection();
        } catch (SQLException e) {
            log.error("Error creating connection", e);
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("Error closing connection", e);
            }
        }
    }
}
