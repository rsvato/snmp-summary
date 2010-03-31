package net.paguo.trafshow.backend.snmp.summary.commands.impl.util;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: sreentenko
 * Date: 01.04.2010
 * Time: 0:40:10
 */
public interface ResultsetCommand<T> {
    T process(ResultSet rs) throws SQLException;
}
