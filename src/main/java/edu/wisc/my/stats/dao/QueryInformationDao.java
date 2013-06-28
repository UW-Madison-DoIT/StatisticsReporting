/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.dao;

import java.util.Map;
import java.util.Set;

import edu.wisc.my.stats.domain.Fact;
import edu.wisc.my.stats.domain.QueryInformation;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public interface QueryInformationDao {
    /**
     * @return The Map of Facts to Sets of QueryInformation that can provide values for the Fact
     */
    public Map<Fact, Set<QueryInformation>> getQueryInformationByFactMap();
    
    /**
     * @return A Map of ID numbers to Facts
     */
    public Map<Long, Fact> getfactByIdMap();
    
    /**
     * @param queryInformation Adds and stores the QueryInformation.
     */
    public void addQueryInformation(QueryInformation queryInformation);
}
