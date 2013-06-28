/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.query.support;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.wisc.my.stats.domain.ColumnInformation;
import edu.wisc.my.stats.domain.ExtraParameter;
import edu.wisc.my.stats.domain.TimeResolution;
import edu.wisc.my.stats.query.DatabaseInformationProvider;

/**
 * TODO make this more configurable (perhaps even via the DB?) 
 * 
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class UWDataWareHouseInformationProvider implements DatabaseInformationProvider {
    protected final Log logger = LogFactory.getLog(this.getClass());
    
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private final Map<Integer, String> fieldMap = new HashMap<Integer, String>();
    
    public UWDataWareHouseInformationProvider() {
        this.fieldMap.put(Calendar.YEAR, "DD.CALENDAR_YEAR_NUMBER");
        this.fieldMap.put(Calendar.MONTH, "DD.CALENDAR_MONTH_NUMBER");
        this.fieldMap.put(Calendar.DAY_OF_MONTH, "DD.CALENDAR_DAY_NUMBER");
    }
    
    /**
     * @see edu.wisc.my.stats.query.DatabaseInformationProvider#getDateConstraintString(java.util.Date, java.util.Date)
     */
    public String getDateConstraintString(Date start, Date end) {
        final Calendar startCal = Calendar.getInstance();
        startCal.setTime(start);

        final Calendar endCal = Calendar.getInstance();
        endCal.setTime(end);

        
        final StringBuilder dateConstraint = new StringBuilder();
        
        dateConstraint.append("DD.FULL_DATE ");
        
        if (startCal.get(Calendar.YEAR) == endCal.get(Calendar.YEAR) && startCal.get(Calendar.MONTH) == endCal.get(Calendar.MONTH) && startCal.get(Calendar.DAY_OF_MONTH) == endCal.get(Calendar.DAY_OF_MONTH)) {
            dateConstraint.append("= ");
            this.addDateConstraint(dateConstraint, start);
        }
        else {
            dateConstraint.append("BETWEEN ");
            this.addDateConstraint(dateConstraint, start);
            dateConstraint.append(" AND ");
            this.addDateConstraint(dateConstraint, end);
        }
        
        return dateConstraint.toString();
    }
    
    protected void addDateConstraint(StringBuilder buffer, Date date) {
        buffer.append("TO_DATE('");
        final String dateString = this.dateFormat.format(date);
        buffer.append(dateString);
        buffer.append("', 'yyyy/mm/dd')");
    }
    

    /**
     * @see edu.wisc.my.stats.query.DatabaseInformationProvider#getGroupByString(edu.wisc.my.stats.domain.TimeResolution)
     */
    public String getGroupByString(TimeResolution resolution) {
        return this.getSelectResolutionsString(resolution);
    }

    /**
     * @see edu.wisc.my.stats.query.DatabaseInformationProvider#getOrderByString(edu.wisc.my.stats.domain.TimeResolution)
     */
    public String getOrderByString(TimeResolution resolution) {
        return this.getSelectResolutionsString(resolution);
    }

    /**
     * @see edu.wisc.my.stats.query.DatabaseInformationProvider#getSelectResolutionsString(edu.wisc.my.stats.domain.TimeResolution)
     */
    public String getSelectResolutionsString(TimeResolution resolution) {
        final StringBuilder resolutionBuilder = new StringBuilder();
        
        switch (resolution) {
            case MINUTE:
                resolutionBuilder.append("TD.MINUTE");
            case FIVE_MINUTE:
                if (resolutionBuilder.length() > 0) {
                    resolutionBuilder.insert(0, ", ");
                }
                resolutionBuilder.insert(0, "TD.FIVE_MINUTE_INCREMENT");
            case HOUR:
                if (resolutionBuilder.length() > 0) {
                    resolutionBuilder.insert(0, ", ");
                }
                resolutionBuilder.insert(0, "TD.HOUR");
            case DAY:
                if (resolutionBuilder.length() > 0) {
                    resolutionBuilder.insert(0, ", ");
                }
                resolutionBuilder.insert(0, "DD.CALENDAR_DAY_NUMBER");
            case WEEK:
                if (resolutionBuilder.length() > 0) {
                    resolutionBuilder.insert(0, ", ");
                }
                resolutionBuilder.insert(0, "DD.CALENDAR_WEEK_NUMBER");
            case MONTH:
                if (resolutionBuilder.length() > 0) {
                    resolutionBuilder.insert(0, ", ");
                }
                resolutionBuilder.insert(0, "DD.CALENDAR_MONTH_NUMBER");
            case YEAR:
                if (resolutionBuilder.length() > 0) {
                    resolutionBuilder.insert(0, ", ");
                }
                resolutionBuilder.insert(0, "DD.CALENDAR_YEAR_NUMBER");
        }
        
        return resolutionBuilder.toString();
    }

    public long getTimeStamp(ResultSet rs, TimeResolution resolution) throws SQLException {
        final Calendar timeStampCal = Calendar.getInstance();
        timeStampCal.clear();
        
        switch (resolution) {
            case MINUTE:
                final int minutes = rs.getInt("MINUTE");
                timeStampCal.set(Calendar.MINUTE, minutes);
            case FIVE_MINUTE:
                if (!timeStampCal.isSet(Calendar.MINUTE)) {
                    final int fiveMinutes = rs.getInt("FIVE_MINUTE_INCREMENT");
                    timeStampCal.set(Calendar.MINUTE, (fiveMinutes - 1) * 5);
                }
            case HOUR:
                final int hours = rs.getInt("HOUR");
                timeStampCal.set(Calendar.HOUR, hours);
            case DAY:
                final int days = rs.getInt("CALENDAR_DAY_NUMBER");
                timeStampCal.set(Calendar.DAY_OF_MONTH, days);
            case WEEK:
                final int week = rs.getInt("CALENDAR_WEEK_NUMBER");
                timeStampCal.set(Calendar.WEEK_OF_YEAR, week);
            case MONTH:
                final int months = rs.getInt("CALENDAR_MONTH_NUMBER");
                timeStampCal.set(Calendar.MONTH, months - 1); //Dang 0 based months
            case YEAR:
                final int years = rs.getInt("CALENDAR_YEAR_NUMBER");
                timeStampCal.set(Calendar.YEAR, years);
        }
        
        return timeStampCal.getTimeInMillis();
    }

    public String getExtraParameterConstraint(ExtraParameter extraParameter, ColumnInformation columnInformation, List<String> values) {
        final StringBuilder extraParamBuilder = new StringBuilder();
        
        extraParamBuilder.append(columnInformation.getAlias());
        extraParamBuilder.append(" ");
        if (values == null || values.size() == 0) {
            extraParamBuilder.append("IS NULL");
        }
        else if (values.size() == 1) {
            extraParamBuilder.append("= '");
            extraParamBuilder.append(values.get(0));
            extraParamBuilder.append("'");
        }
        else {
            extraParamBuilder.append("IN ('");
            for (final Iterator<String> valueItr = values.iterator(); valueItr.hasNext();) {
                final String value = valueItr.next();
                extraParamBuilder.append(value);
                
                if (valueItr.hasNext()) {
                    extraParamBuilder.append("', '");
                }
            }
            extraParamBuilder.append("')");
        }
        
        return extraParamBuilder.toString();
    }
}
