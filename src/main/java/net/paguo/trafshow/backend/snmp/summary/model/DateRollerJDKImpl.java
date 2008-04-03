package net.paguo.trafshow.backend.snmp.summary.model;

import java.util.Date;
import java.util.Calendar;

/**
 * @author Reyentenko
 * @Deprecated
 */
public class DateRollerJDKImpl implements DateRoller {
    private Date start;
    private Date end;

    private Date medianDate;

    public DateRollerJDKImpl(Date startDate, Date endDate){
        this.start = startDate;
        this.end = endDate;
        this.medianDate = null;
    }

    public DateRollerJDKImpl(DateParameters parameters){
        this(parameters.getStartDate(), parameters.getEndDate());
    }

    public void setStartDate(Date date) {
        this.start = date;
    }

    public void setEnd(Date date) {
        this.end = date;
    }

    public boolean hasNextDate() {
        if (null == medianDate){
           medianDate = start;
        }
        return medianDate.before(end);
    }

    protected Date rollMedianDate(){
        if (medianDate != null){
            start = new Date(medianDate.getTime());
        }
        medianDate = upOneHour();
        if (medianDate.before(start)){ // Day changed
           Calendar cal = Calendar.getInstance();
           cal.setTime(medianDate);
           cal.roll(Calendar.DAY_OF_MONTH, 1);
           cal.roll(Calendar.SECOND, -1);
           medianDate = cal.getTime();
        }

        return medianDate;
    }

    protected Date upOneHour() {
        long startTime = start.getTime();
        Date median = new Date(startTime + 3600 * 1000);
        return median;
    }

    public Date getNextDate() {
        return rollMedianDate();
    }

    public Date getCurrentDate() {
        return start;
    }

    public Date getEndDate() {
        return end;
    }
}
