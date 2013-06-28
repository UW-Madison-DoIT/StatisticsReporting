/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.dao.support;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.CompositeMap;

import edu.wisc.my.stats.dao.QueryInformationDao;
import edu.wisc.my.stats.domain.Fact;
import edu.wisc.my.stats.domain.QueryInformation;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class ConsolidatingQueryInformationDao implements QueryInformationDao {
    private QueryInformationDao writableQueryInformationDao;
    private Set<QueryInformationDao> readableQueryInformationDaos;
    
    /**
     * @return the readableQueryInformationDaos
     */
    public Set<QueryInformationDao> getReadableQueryInformationDaos() {
        return this.readableQueryInformationDaos;
    }
    /**
     * @param readableQueryInformationDaos the readableQueryInformationDaos to set
     */
    public void setReadableQueryInformationDaos(Set<QueryInformationDao> readableQueryInformationDaos) {
        this.readableQueryInformationDaos = readableQueryInformationDaos;
    }

    /**
     * @return the writableQueryInformationDao
     */
    public QueryInformationDao getWritableQueryInformationDao() {
        return this.writableQueryInformationDao;
    }
    /**
     * @param writableQueryInformationDao the writableQueryInformationDao to set
     */
    public void setWritableQueryInformationDao(QueryInformationDao writableQueryInformationDao) {
        this.writableQueryInformationDao = writableQueryInformationDao;
    }

    /**
     * @see edu.wisc.my.stats.dao.QueryInformationDao#addQueryInformation(edu.wisc.my.stats.domain.QueryInformation)
     */
    public void addQueryInformation(QueryInformation queryInformation) {
        this.writableQueryInformationDao.addQueryInformation(queryInformation);
    }

    /**
     * @see edu.wisc.my.stats.dao.QueryInformationDao#getQueryInformationByFactMap()
     */
    @SuppressWarnings("unchecked")
    public Map<Fact, Set<QueryInformation>> getQueryInformationByFactMap() {
        final Set<Map<Fact, Set<QueryInformation>>> queryInformationByFactMaps = new HashSet<Map<Fact, Set<QueryInformation>>>();
        
        for (final QueryInformationDao queryInformationDao : this.readableQueryInformationDaos) {
            final Map<Fact, Set<QueryInformation>> queryInformationByFactMap = queryInformationDao.getQueryInformationByFactMap();
            queryInformationByFactMaps.add(queryInformationByFactMap);
        }
        
        final CompositeMap cm = new CompositeMap(queryInformationByFactMaps.toArray(new Map[queryInformationByFactMaps.size()]));
        return Collections.unmodifiableMap(cm); //will this work?
    }

    /**
     * @see edu.wisc.my.stats.dao.QueryInformationDao#getfactByIdMap()
     */
    @SuppressWarnings("unchecked")
    public Map<Long, Fact> getfactByIdMap() {
        final Set<Map<Long, Fact>> queryInformationByFactMaps = new HashSet<Map<Long, Fact>>();
        
        for (final QueryInformationDao queryInformationDao : this.readableQueryInformationDaos) {
            final Map<Long, Fact> queryInformationByFactMap = queryInformationDao.getfactByIdMap();
            queryInformationByFactMaps.add(queryInformationByFactMap);
        }
        
        final CompositeMap cm = new CompositeMap(queryInformationByFactMaps.toArray(new Map[queryInformationByFactMaps.size()]));
        return Collections.unmodifiableMap(cm); //will this work?
    }

}
