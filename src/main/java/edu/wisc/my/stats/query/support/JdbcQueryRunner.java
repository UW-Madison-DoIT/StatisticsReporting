/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.query.support;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import edu.wisc.my.stats.domain.ColumnInformation;
import edu.wisc.my.stats.domain.CompositeFact;
import edu.wisc.my.stats.domain.ExtraParameter;
import edu.wisc.my.stats.domain.Fact;
import edu.wisc.my.stats.domain.QueryInformation;
import edu.wisc.my.stats.domain.TimeResolution;
import edu.wisc.my.stats.query.DatabaseInformationProvider;
import edu.wisc.my.stats.query.QueryCompiler;
import edu.wisc.my.stats.query.QueryRunner;
import edu.wisc.my.stats.util.SortedKeyTable;
import edu.wisc.my.stats.util.Table;
import edu.wisc.my.stats.web.command.QueryCommand;
import edu.wisc.my.stats.web.command.QueryParameters;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class JdbcQueryRunner extends JdbcDaoSupport implements QueryRunner {
    private QueryCompiler queryCompiler;
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
     * @return the queryCompiler
     */
    public QueryCompiler getQueryCompiler() {
        return this.queryCompiler;
    }
    /**
     * @param queryCompiler the queryCompiler to set
     */
    @Required
    public void setQueryCompiler(QueryCompiler queryCompiler) {
        this.queryCompiler = queryCompiler;
    }

    /**
     * @see edu.wisc.my.stats.query.QueryRunner#runQuery(edu.wisc.my.stats.web.command.QueryParameters, edu.wisc.my.stats.domain.QueryInformation)
     */
    @SuppressWarnings("unchecked")
    public Table runQuery(QueryCommand queryCommand, QueryInformation queryInformation) {
        final String sql = this.queryCompiler.compileQuery(queryCommand, queryInformation);
        
        final JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
        final DataPointRowMapper dataPointRowMapper = new DataPointRowMapper(queryCommand, queryInformation);
        final Table<Long, Fact, Double> results = (Table<Long, Fact, Double>)jdbcTemplate.query(sql, dataPointRowMapper);
        
        return results;
    }

    
    private class DataPointRowMapper implements ResultSetExtractor {
        private final QueryCommand queryCommand;
        private final QueryInformation queryInformation;
        
        public DataPointRowMapper(final QueryCommand queryCommand, final QueryInformation queryInformation) {
            this.queryCommand = queryCommand;
            this.queryInformation = queryInformation;
        }

        public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
            final Table<Long, Fact, Double> allResults = new SortedKeyTable<Long, Fact, Double>();
            
            final QueryParameters queryParameters = this.queryCommand.getQueryParameters();
            final TimeResolution resolution = queryParameters.getResolution();
            final Set<Fact> queryFacts = queryParameters.getFacts();
            final Map<Fact, Set<ColumnInformation>> factsToColumns = this.queryInformation.getFactsToColumns();
            
            //TODO prime the Facts in the table with the composites of Facts and ExtraParameter ColumnInformations 
            
            while (rs.next()) {
                final long timestamp = JdbcQueryRunner.this.databaseInformationProvider.getTimeStamp(rs, resolution);
                
                for (final Map.Entry<Fact, Set<ColumnInformation>> factsToColumnsEntry : factsToColumns.entrySet()) {
                    Fact fact = factsToColumnsEntry.getKey();
                    
                    if (queryFacts.contains(fact)) {
                        //TODO move this into another method or class
                        final Map<ExtraParameter, ColumnInformation> extraParametersMap = this.queryInformation.getExtraParameters();
                        if (extraParametersMap != null) {
                            final StringBuilder compositeFactName = new StringBuilder();
                            
                            for (final Iterator<ColumnInformation> columnInformationItr = extraParametersMap.values().iterator(); columnInformationItr.hasNext();) {
                                final ColumnInformation columnInformation = columnInformationItr.next();
                                final String factPart = rs.getString(columnInformation.getAlias());
                                compositeFactName.append(factPart);
                                
                                if (columnInformationItr.hasNext()) {
                                    compositeFactName.append(".");
                                }
                            }
                            
                            if (compositeFactName.length() > 0) {
                                fact = new CompositeFact(fact, compositeFactName.toString());
                            }
                        }
                    
                        double value = 0;
                        for (final ColumnInformation columnInformation : factsToColumnsEntry.getValue()) {
                            value += rs.getDouble(columnInformation.getAlias());
                        }
                        
                        allResults.put(timestamp, fact, value);
                    }
                }
            }
            
            return allResults;
        }
    }
}
