/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.dao.support;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.wisc.my.stats.dao.QueryInformationDao;
import edu.wisc.my.stats.domain.Fact;
import edu.wisc.my.stats.domain.QueryInformation;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractMapQueryInformationDao implements QueryInformationDao {
    protected final Log logger = LogFactory.getLog(this.getClass());
    
    protected final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    protected final Lock readLock = this.readWriteLock.readLock();
    protected final Lock writeLock = this.readWriteLock.writeLock();
    
    protected final Map<Fact, Set<QueryInformation>> queryInfoByFactMap = new ConcurrentHashMap<Fact, Set<QueryInformation>>();
    protected final Map<Fact, Set<QueryInformation>> unmodifiableQueryInfoByFactMap = Collections.unmodifiableMap(this.queryInfoByFactMap);
    protected final Map<Long, Fact> factByIdMap = new ConcurrentHashMap<Long, Fact>();
    protected final Map<Long, Fact> unmodifiableFactByIdMap = Collections.unmodifiableMap(this.factByIdMap);

    
    /**
     * @see edu.wisc.my.stats.dao.QueryInformationDao#addQueryInformation(edu.wisc.my.stats.domain.QueryInformation)
     */
    public void addQueryInformation(QueryInformation queryInformation) {
        this.addQueryInformationInternal(queryInformation);
    }

    /**
     * @see edu.wisc.my.stats.dao.QueryInformationDao#getQueryInformationByFactMap()
     */
    public Map<Fact, Set<QueryInformation>> getQueryInformationByFactMap() {
        this.readLock.lock();
        try {
            return this.unmodifiableQueryInfoByFactMap;
        }
        finally {
            this.readLock.unlock();
        }
    }

    /**
     * @see edu.wisc.my.stats.dao.QueryInformationDao#getfactByIdMap()
     */
    public Map<Long, Fact> getfactByIdMap() {
        this.readLock.lock();
        try {
            return this.unmodifiableFactByIdMap;
        }
        finally {
            this.readLock.unlock();
        }
    }

    /**
     * @param queryInformation Adds a QueryInformation object to the Set of QueryInformation
     */
    protected void addQueryInformationInternal(QueryInformation queryInformation) {
        this.writeLock.lock();
        try {
            for (final Fact fact : queryInformation.getFactsToColumns().keySet()) {
                Set<QueryInformation> queryInfoByFactSet = this.queryInfoByFactMap.get(fact);
                if (queryInfoByFactSet == null) {
                    queryInfoByFactSet = new HashSet<QueryInformation>();
                    this.queryInfoByFactMap.put(fact, queryInfoByFactSet);
                }
                queryInfoByFactSet.add(queryInformation);
    
                this.factByIdMap.put(fact.getId(), fact);
            }
        }
        finally {
            this.writeLock.unlock();
        }
    }
}
