/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public enum TimeResolution {
    MINUTE (1, Calendar.MINUTE),
    FIVE_MINUTE (5, MINUTE),
    HOUR (60, MINUTE, Calendar.HOUR),
    DAY (24, HOUR, Calendar.DAY_OF_MONTH),
    WEEK (7, DAY, Calendar.WEEK_OF_YEAR),
    MONTH (30, WEEK, Calendar.MONTH),
    YEAR (365, DAY, Calendar.YEAR);
    
    private final int minutes;
    private final List<Integer> calendarFields;

    
    private TimeResolution(int minutes, int... calendarFields) {
        this.minutes = minutes;
        
        final List<Integer> fields = new ArrayList<Integer>(calendarFields.length);
        for (final int field : calendarFields) {
            fields.add(field);
        }
        
        this.calendarFields = Collections.unmodifiableList(fields);
    }
    private TimeResolution(int count, TimeResolution base, int... calendarFields) {
        this(count * base.getMinutes(), calendarFields);
    }
    
    public String getName() {
        return this.name();
    }
    
    public String getCode() {
        return this.name();
    }
    
    /**
     * @return Returns the number of minutes in this TimeResolution.
     */
    public int getMinutes() {
        return this.minutes;
    }
    
    /**
     * @return Returns the calendarFields.
     */
    public List<Integer> getCalendarFields() {
        return this.calendarFields;
    }

    /**
     * A convience for {@link #instanceCount(long, long)} 
     */
    public long instanceCount(Date start, Date end) {
        return this.instanceCount(start.getTime(), end.getTime());
    }
    
    /**
     * Returns the number of instances of the resolution that occur between the two dates.
     * 
     * @param start The start date in milliseconds - inclusive. See {@link Date#getTime()}
     * @param end The end date in milliseconds - exclusive. See {@link Date#getTime()}
     * @return The number of instances between the two ex: 8/1/2006 12:00:00.000 AM to 8/2/2006 12:00:00.000 AM for {@link #HOUR} should return 24.
     */
    public long instanceCount(long start, long end) {
        if (end <= start) {
            throw new IllegalArgumentException("Start must be before end");
        }
        
        final long milliDiff = end - start;
        final long minuteDiff = milliDiff / DateUtils.MILLIS_PER_MINUTE;
        final long instances = minuteDiff / this.minutes;
        
        return instances;
    }
}
