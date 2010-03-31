package net.paguo.trafshow.backend.snmp.summary.commands.impl.util;

import net.paguo.trafshow.backend.snmp.summary.database.DBProxyFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: sreentenko
 * Date: 01.04.2010
 * Time: 0:34:23
 */
public class PreparedStatementHandler {

    private Connection c;

    public PreparedStatementHandler(){}

    public<T> T handle(String query, ResultsetCommand<T> command, Parameter... parameters) throws SQLException {
        if (c == null) c = DBProxyFactory.getDBProxy().getConnection();
        PreparedStatement pstmt = c.prepareStatement(query);
        if (parameters != null){
            for (Parameter parameter : parameters) {
                parameter.apply(pstmt);
            }
        }
        ResultSet rs = pstmt.executeQuery();
        T result = command.process(rs);
        rs.close();
        pstmt.close();
        return result;
    }

    public void closeConnection() throws SQLException {
        DBProxyFactory.getDBProxy().closeConnection(c);
    }
}
