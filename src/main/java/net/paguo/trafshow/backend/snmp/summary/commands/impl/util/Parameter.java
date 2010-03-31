package net.paguo.trafshow.backend.snmp.summary.commands.impl.util;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: sreentenko
 * Date: 31.03.2010
 * Time: 23:18:06
 */
public abstract class Parameter<T> {
    private int position;
    private T data;

    public Parameter(int pos, T data){
        this.position = pos;
        this.data = data;
    }

    public abstract void apply(PreparedStatement pstmt) throws SQLException;

    public int getPosition() {
        return position;
    }

    public T getData() {
        return data;
    }

    public class StringParameter extends Parameter<String>{
        public StringParameter(int pos, String data) {
            super(pos, data);
        }

        @Override
        public void apply(PreparedStatement pstmt) throws SQLException {
            pstmt.setString(getPosition(), getData());
        }
    }

    public class IntParameter extends Parameter<Integer>{
        public IntParameter(int pos, Integer data) {
            super(pos, data);
        }

        @Override
        public  void apply(PreparedStatement pstmt) throws SQLException {
            pstmt.setInt(getPosition(), getData());
        }
    }

    public class DateParameter extends Parameter<java.sql.Date>{
        public DateParameter(int pos, Date data) {
            super(pos, data);
        }

        @Override
        public void apply(PreparedStatement pstmt) throws SQLException {
            pstmt.setDate(getPosition(), getData());
        }
    }

    public static class TimestampParameter extends Parameter<Timestamp>{
        public TimestampParameter(int pos, Timestamp data) {
            super(pos, data);
        }

        @Override
        public void apply(PreparedStatement pstmt) throws SQLException {
            pstmt.setTimestamp(getPosition(), getData());
        }
    }
}
