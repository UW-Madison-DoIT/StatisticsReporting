/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.query.support;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;

import edu.wisc.my.stats.dao.QueryInformationDao;
import edu.wisc.my.stats.domain.Fact;
import edu.wisc.my.stats.domain.QueryInformation;
import edu.wisc.my.stats.query.FactProvider;
import edu.wisc.my.stats.query.QueryInformationProvider;


/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class QueryInformationProviderImpl implements QueryInformationProvider, FactProvider {
    private QueryInformationDao queryInformationDao;
    
    /**
     * @return the queryInformationDao
     */
    public QueryInformationDao getQueryInformationDao() {
        return this.queryInformationDao;
    }
    /**
     * @param queryInformationDao the queryInformationDao to set
     */
    @Required
    public void setQueryInformationDao(QueryInformationDao queryInformationDao) {
        this.queryInformationDao = queryInformationDao;
    }

    /**
     * @see edu.wisc.my.stats.query.QueryInformationProvider#getQueryInformation(java.util.Set)
     */
    public Set<QueryInformation> getQueryInformation(final Set<Fact> facts) {
        final Set<QueryInformation> queryInfoForFacts = new HashSet<QueryInformation>();
        
        for (final Fact fact : facts) {
            final Map<Fact, Set<QueryInformation>> queryInformationByFactMap = this.queryInformationDao.getQueryInformationByFactMap();
            final Set<QueryInformation> queryInfoForFact = queryInformationByFactMap.get(fact);
            queryInfoForFacts.addAll(queryInfoForFact);
        }
        
        //TODO logic to remove QueryInformation objects that aren't needed
        
        return queryInfoForFacts;
    }

    /**
     * @see edu.wisc.my.stats.query.FactProvider#getFactById(long)
     */
    public Fact getFactById(long id) {
        final Map<Long, Fact> getfactByIdMap = this.queryInformationDao.getfactByIdMap();
        return getfactByIdMap.get(id);
    }

    /**
     * @see edu.wisc.my.stats.query.FactProvider#getFacts()
     */
    public Set<Fact> getFacts() {
        final Map<Fact, Set<QueryInformation>> queryInformationByFactMap = this.queryInformationDao.getQueryInformationByFactMap();
        return queryInformationByFactMap.keySet();
    }
}
