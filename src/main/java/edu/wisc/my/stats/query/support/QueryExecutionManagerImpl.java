/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.query.support;

import java.util.Set;

import org.springframework.beans.factory.annotation.Required;

import edu.wisc.my.stats.domain.Fact;
import edu.wisc.my.stats.domain.QueryInformation;
import edu.wisc.my.stats.query.QueryExecutionManager;
import edu.wisc.my.stats.query.QueryInformationProvider;
import edu.wisc.my.stats.query.QueryRunner;
import edu.wisc.my.stats.util.DefaultValueSortedKeyTable;
import edu.wisc.my.stats.util.Table;
import edu.wisc.my.stats.web.command.QueryCommand;
import edu.wisc.my.stats.web.command.QueryParameters;


/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class QueryExecutionManagerImpl implements QueryExecutionManager<Long, Fact, Double> {
    private QueryInformationProvider queryInformationProvider;
    private QueryRunner<Long, Fact, Double> queryRunner;
    
    /**
     * @return the queryInformationProvider
     */
    public QueryInformationProvider getQueryInformationProvider() {
        return this.queryInformationProvider;
    }
    /**
     * @param queryInformationProvider the queryInformationProvider to set
     */
    @Required
    public void setQueryInformationProvider(QueryInformationProvider queryInformationProvider) {
        this.queryInformationProvider = queryInformationProvider;
    }
    /**
     * @return the queryRunner
     */
    public QueryRunner<Long, Fact, Double> getQueryRunner() {
        return this.queryRunner;
    }
    /**
     * @param queryRunner the queryRunner to set
     */
    @Required
    public void setQueryRunner(QueryRunner<Long, Fact, Double> queryRunner) {
        this.queryRunner = queryRunner;
    }

    /**
     * @see edu.wisc.my.stats.query.QueryExecutionManager#executeQuery(edu.wisc.my.stats.web.command.QueryParameters)
     */
    public Table<Long, Fact, Double> executeQuery(QueryCommand queryCommand) {
        final QueryParameters queryParameters = queryCommand.getQueryParameters();
        //Get the queries for the requested facts
        final Set<Fact> facts = queryParameters.getFacts();
        final Set<QueryInformation> queryInfoSet = this.queryInformationProvider.getQueryInformation(facts);
        
        final Table<Long, Fact, Double> results = new DefaultValueSortedKeyTable<Long, Fact, Double>(0.0);
        for (final QueryInformation queryInformation : queryInfoSet) {
            final Table<Long, Fact, Double> queryResults = this.queryRunner.runQuery(queryCommand, queryInformation);
            results.putAll(queryResults);
            //TODO more inteligent merging (perhaps via some interface)
        }
        
        return results;
    }
}