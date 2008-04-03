package net.paguo.trafshow.backend.snmp.summary.model;

import java.util.Date;

/**
 * @author Reyentenko
 */
public interface DateRoller {
    void setStartDate(Date date);
    void setEnd(Date date);
    boolean hasNextDate();
    Date getNextDate();
    Date getCurrentDate();
}
