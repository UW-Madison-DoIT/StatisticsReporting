/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.domain;

import java.util.Map;
import java.util.Set;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 * 
 */
public class QueryInformation {
    private static long idGen = 0;
    
    private long id = idGen++;
    private String baseSelectSql;
    private String fromSql;
    private String baseWhereSql;
    private Map<Fact, Set<ColumnInformation>> factsToColumns;
    private Map<ExtraParameter, ColumnInformation> extraParameters;

    /**
     * @return the baseSelectSql
     */
    public String getBaseSelectSql() {
        return this.baseSelectSql;
    }
    /**
     * @param baseSelectSql the baseSelectSql to set
     */
    public void setBaseSelectSql(String baseSelectSql) {
        this.baseSelectSql = baseSelectSql;
    }
    /**
     * @return the baseWhereSql
     */
    public String getBaseWhereSql() {
        return this.baseWhereSql;
    }
    /**
     * @param baseWhereSql the baseWhereSql to set
     */
    public void setBaseWhereSql(String baseWhereSql) {
        this.baseWhereSql = baseWhereSql;
    }
    /**
     * @return the factsToColumns
     */
    public Map<Fact, Set<ColumnInformation>> getFactsToColumns() {
        return this.factsToColumns;
    }
    /**
     * @param factsToColumns the factsToColumns to set
     */
    public void setFactsToColumns(Map<Fact, Set<ColumnInformation>> factsToColumns) {
        this.factsToColumns = factsToColumns;
    }
    /**
     * @return the fromSql
     */
    public String getFromSql() {
        return this.fromSql;
    }
    /**
     * @param fromSql the fromSql to set
     */
    public void setFromSql(String fromSql) {
        this.fromSql = fromSql;
    }
    /**
     * @return the id
     */
    public long getId() {
        return this.id;
    }
    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }
    /**
     * @return the extraParameters
     */
    public Map<ExtraParameter, ColumnInformation> getExtraParameters() {
        return this.extraParameters;
    }
    /**
     * @param extraParameters the extraParameters to set
     */
    public void setExtraParameters(Map<ExtraParameter, ColumnInformation> extraParameters) {
        this.extraParameters = extraParameters;
    }
}