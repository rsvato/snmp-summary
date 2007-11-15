package net.paguo.trafshow.backend.snmp.summary.commands;

import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.paguo.trafshow.backend.snmp.summary.database.DBProxyFactory;
import net.paguo.trafshow.backend.snmp.summary.database.DBProxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class GetTrafficDataCommand implements DatabaseCommand<Object>{
        private static final String SQL = "select dt, cisco, interface, inoctets, outoctets, uptime from tr_with_uptime t where dt between ? and ? order by cisco, interface, dt";
        private static final Log log = LogFactory.getLog(GetTrafficDataCommand.class);
        private Date start;
        private Date end;

        public GetTrafficDataCommand(Date from, Date to){
                this.start = from;
                this.end = to;
        }

        public Object getData(){
                log.debug("getData(): <<<<");
                DBProxy proxy = DBProxyFactory.getDBProxy();
                Connection conn = null;
                try{
                        conn = proxy.getConnection();
                        PreparedStatement pst = conn.prepareStatement(SQL);
                        pst.setTimestamp(1, new Timestamp(start.getTime()));
                        pst.setTimestamp(2, new Timestamp(end.getTime()));
                        ResultSet rs = pst.executeQuery();
                        while(rs.next()){
                                ;
                        }
                        rs.close();
                        pst.close();
                }catch(SQLException e){
                        log.error(e);
                }finally{
                        if (conn != null){
                                try{
                                        conn.close();
                                }catch(SQLException e){
                                        log.error("Error when closing connection", e);
                                }
                        }
                }
                log.debug("getData(): >>>>");
                return null;
        }
}
