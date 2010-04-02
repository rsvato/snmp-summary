package net.paguo.trafshow.backend.snmp.summary.model;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * @author Reyentenko
 */
public class DateRollerJodaImpl implements DateRoller{
    private DateTime start;
    private DateTime end;

    private DateTime medianDate;


    public void setStartDate(Date date) {
        this.start = new DateTime(date);
    }

    public void setEnd(Date date) {
        this.end = new DateTime(date);
    }

    /*
     * @return
     */
    public boolean hasNextDate() {
        if (null == medianDate) {
            medianDate = start;
        }
        medianDate = rollMedianDate();
        return medianDate.isBefore(end) || medianDate.isEqual(end);
    }


    public DateRollerJodaImpl(Date startDate, Date endDate) {
        this.start = new DateTime(startDate);
        this.end = new DateTime(endDate);
    }

    public DateRollerJodaImpl(DateParameters parameters) {
        this(parameters.getStartDate(), parameters.getEndDate());
    }

    protected DateTime upOneHour() {
        DateTime curDateTime = new DateTime(getCurrentDate());
        return curDateTime.plusHours(1);
    }

     public Date getNextDate() {
        return medianDate.toDate();
    }

    public Date getCurrentDate() {
        return start.toDate();
    }

    public Date getEndDate() {
        return end.toDate();
    }

    protected DateTime rollMedianDate(){
        if (medianDate != null){
            start = medianDate.toDateTime();
        }
        medianDate = upOneHour();
        return medianDate;
    }
}
