package net.paguo.trafshow.backend.snmp.summary.model;

import java.util.Date;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;

/**
 * @author Reyentenko
 */
public class DateParameters {

    private static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private Date startDate;
    private Date endDate;

    private DateParameters(Date start, Date end) {
        this.startDate = start;
        this.endDate = end;
    }

    public Date getStartDate() {
        return (Date) startDate.clone();
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return (Date) endDate.clone();
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Returns parameters for input as "YYYY-MM-DD"
     *
     * @param dateStr input parameters
     * @return DateParameters instance
     * @throws net.paguo.trafshow.backend.snmp.summary.model.BadParameterException
     *          when parameter is badly formatted
     */
    public static DateParameters get(String dateStr) throws BadParameterException {
        Date date = FORMAT.parse(dateStr, new ParsePosition(0));
        if (date != null && FORMAT.format(date).equals(dateStr)) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 10);

            Date begin = cal.getTime();

            cal.roll(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 10);
            if (cal.getTime().before(begin)){
                cal.roll(Calendar.MONTH, 1);
            }
            Date end = cal.getTime();
            return new DateParameters(begin, end);
        } else {
            throw new BadParameterException("dateStr");
        }
    }
}
