/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.query.support;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import edu.wisc.my.stats.domain.ExtraParameter;
import edu.wisc.my.stats.query.ExtraParameterValuesProvider;

/**
 * Associates a simple SQL query to an {@link ExtraParameter}. When the values are needed for the ExtraParameter
 * the query is run and the contents of the first column of each row in the result set are returned as a List of
 * Strings.
 * 
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class JdbcExtraParameterValuesProvider extends JdbcDaoSupport implements ExtraParameterValuesProvider {
    private static final SimpleRowMapper SIMPLE_ROW_MAPPER = new SimpleRowMapper();
    
    private Map<ExtraParameter, String> parameterSqlMap = new HashMap<ExtraParameter, String>();
    
    /**
     * @return the parameterSqlMap
     */
    public Map<ExtraParameter, String> getParameterSqlMap() {
        return this.parameterSqlMap;
    }
    /**
     * @param parameterSqlMap the parameterSqlMap to set
     */
    public void setParameterSqlMap(Map<ExtraParameter, String> parameterSqlMap) {
        this.parameterSqlMap = parameterSqlMap;
    }


    /**
     * @see edu.wisc.my.stats.query.ExtraParameterValuesProvider#getPossibleValues(edu.wisc.my.stats.domain.ExtraParameter)
     */
    @SuppressWarnings("unchecked")
    public List<String> getPossibleValues(ExtraParameter extraParameter) {
        final String parameterSql = this.parameterSqlMap.get(extraParameter);
        
        if (parameterSql != null) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Executing '" + parameterSql + "' to get values List for parameter '" + extraParameter + "'");
            }
            
            final JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
            final List<String> values = jdbcTemplate.query(parameterSql, SIMPLE_ROW_MAPPER);
            return values;
        }
        else {
            this.logger.warn("Parameter '" + extraParameter + "' has not associated SQL");

            return Collections.emptyList();
        }
    }
    
    private static class SimpleRowMapper implements RowMapper {

        /**
         * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
         */
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getString(1);
        }
    }
}
