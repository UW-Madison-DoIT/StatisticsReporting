/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.query.support;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import edu.wisc.my.stats.domain.ColumnInformation;
import edu.wisc.my.stats.domain.ExtraParameter;
import edu.wisc.my.stats.domain.QueryInformation;
import edu.wisc.my.stats.domain.TimeResolution;
import edu.wisc.my.stats.query.DatabaseInformationProvider;
import edu.wisc.my.stats.query.QueryCompiler;
import edu.wisc.my.stats.web.command.QueryCommand;
import edu.wisc.my.stats.web.command.QueryParameters;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class QueryCompilerImpl implements QueryCompiler {
    protected final Log logger = LogFactory.getLog(this.getClass());
    
    private DatabaseInformationProvider databaseInformationProvider;
    
    /**
     * @return the databaseInformationProvider
     */
    public DatabaseInformationProvider getDatabaseInformationProvider() {
        return this.databaseInformationProvider;
    }
    /**
     * @param databaseInformationProvider the databaseInformationProvider to set
     */
    @Required
    public void setDatabaseInformationProvider(DatabaseInformationProvider databaseInformationProvider) {
        this.databaseInformationProvider = databaseInformationProvider;
    }

    /**
     * @see edu.wisc.my.stats.query.QueryCompiler#compileQuery(edu.wisc.my.stats.web.command.QueryCommand, edu.wisc.my.stats.domain.QueryInformation)
     */
    public String compileQuery(QueryCommand queryCommand, QueryInformation queryInformation) {
        final StringBuilder queryBuilder = new StringBuilder();

        final QueryParameters queryParameters = queryCommand.getQueryParameters();
        final TimeResolution resolution = queryParameters.getResolution();

        //SELECT
        final String baseSelectSql = queryInformation.getBaseSelectSql();
        queryBuilder.append(baseSelectSql);
        final String selectResolutionString = this.databaseInformationProvider.getSelectResolutionsString(resolution);
        if (selectResolutionString != null) {
            queryBuilder.append(", ");
            queryBuilder.append(selectResolutionString);
        }
        queryBuilder.append(" \n");
        
        //FROM
        final String fromSql = queryInformation.getFromSql();
        queryBuilder.append(fromSql);
        queryBuilder.append(" \n");
        
        //WHERE
        final String baseWhereSql = queryInformation.getBaseWhereSql();
        queryBuilder.append(baseWhereSql);
        
        final Date start = queryParameters.getStart();
        final Date end = queryParameters.getEnd();
        final String dateConstraintString = this.databaseInformationProvider.getDateConstraintString(start, end);
        
        queryBuilder.append(" AND ");
        queryBuilder.append(dateConstraintString);
        
        final Map<ExtraParameter, ColumnInformation> extraParameters = queryInformation.getExtraParameters();
        if (extraParameters != null) {
            final Map<String, List<String>> extraParameterValues = queryCommand.getExtraParameterValues();
            
            for (final ExtraParameter extraParameter : extraParameters.keySet()) {
                final String extraParameterName = extraParameter.getName();
                
                final ColumnInformation columnInformation = extraParameters.get(extraParameter);
                final List<String> values = extraParameterValues.get(extraParameterName);
                
                final String extraParameterConstraint = this.databaseInformationProvider.getExtraParameterConstraint(extraParameter, columnInformation, values);
                
                queryBuilder.append(" AND ");
                queryBuilder.append(extraParameterConstraint);
            }
        }
        
        queryBuilder.append(" \n");
        
        //GROUP BY
        final StringBuilder groupByStringBuilder = new StringBuilder();
        final String groupByString = this.databaseInformationProvider.getGroupByString(resolution);
        if (groupByString != null) {
            groupByStringBuilder.append(groupByString);
        }
        if (extraParameters != null) {
            for (final ColumnInformation columnInformation : extraParameters.values()) {
                if (groupByStringBuilder.length() > 0) {
                    groupByStringBuilder.append(", ");
                }
                groupByStringBuilder.append(columnInformation.getAlias());
            }
        }
        
        if (groupByStringBuilder.length() > 0) {
            queryBuilder.append("GROUP BY ");
            queryBuilder.append(groupByStringBuilder);
            queryBuilder.append(" \n");
        }
        
        //ORDER BY
        final StringBuilder orderByStringBuilder = new StringBuilder();
        final String orderByString = this.databaseInformationProvider.getOrderByString(resolution);
        if (orderByString != null) {
            orderByStringBuilder.append(orderByString);
        }
        if (extraParameters != null) {
            for (final ColumnInformation columnInformation : extraParameters.values()) {
                if (orderByStringBuilder.length() > 0) {
                    orderByStringBuilder.append(", ");
                }
                orderByStringBuilder.append(columnInformation.getAlias());
            }
        }
        
        if (orderByStringBuilder.length() > 0) {
            queryBuilder.append("ORDER BY ");
            queryBuilder.append(orderByStringBuilder);
            queryBuilder.append(" \n");
        }
        
        final String queryString = queryBuilder.toString();
        this.logger.debug("Generated SQL for QueryInformation='" + queryInformation + "'\n" + queryString);
        
        return queryString;
    }
}
