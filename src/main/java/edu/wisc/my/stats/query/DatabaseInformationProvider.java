/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import edu.wisc.my.stats.domain.ColumnInformation;
import edu.wisc.my.stats.domain.ExtraParameter;
import edu.wisc.my.stats.domain.TimeResolution;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public interface DatabaseInformationProvider {
    /**
     * Gets a correctly formatted string of resolution columns for the specified TimeResolution
     * to be appened to the SELECT portion of the query.
     */
    public String getSelectResolutionsString(TimeResolution resolution);
    
    /**
     * Gets a correctly formatted string of resolution columns for the specified TimeResolution
     * to be appened to the GROUP BY portion of the query.
     */
    public String getGroupByString(TimeResolution resolution);
    
    /**
     * Gets a correctly formatted string of resolution columns for the specified TimeResolution
     * to be appened to the ORDER BY portion of the query.
     */
    public String getOrderByString(TimeResolution resolution);
    
    /**
     * Gets a correctly formatted string restricting the query to between the specified Dates
     * to be appended to the WHERE portion of the query.
     */
    public String getDateConstraintString(Date start, Date end);
    
    public String getExtraParameterConstraint(ExtraParameter extraParameter, ColumnInformation columnInformation, List<String> values);

    /**
     * Gets a timestamp (milliseconds from epoch) from the ResultSet for the specified TimeResolution
     */
    public long getTimeStamp(ResultSet rs, TimeResolution resolution) throws SQLException;
}
