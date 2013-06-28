/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.dao.support;

import java.util.Set;

import edu.wisc.my.stats.domain.QueryInformation;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class MapQueryInformationDao extends AbstractMapQueryInformationDao {
    private Set<QueryInformation> queryInfoSet;
    
    /**
     * @return the queryInfoSet
     */
    public Set<QueryInformation> getQueryInfoSet() {
        return this.queryInfoSet;
    }
    /**
     * @param queryInfoSet the queryInfoSet to set
     */
    public void setQueryInfoSet(Set<QueryInformation> queryInfoSet) {
        this.writeLock.lock();
        try {
            this.queryInfoSet = queryInfoSet;
            this.queryInfoByFactMap.clear();
            this.factByIdMap.clear();
            for (final QueryInformation queryInformation : queryInfoSet) {
                this.addQueryInformationInternal(queryInformation);
            }
        }
        finally {
            this.writeLock.unlock();
        }
    }
}
