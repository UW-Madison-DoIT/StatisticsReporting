/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.query;

import java.util.Set;

import edu.wisc.my.stats.domain.Fact;
import edu.wisc.my.stats.domain.QueryInformation;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public interface QueryInformationProvider {
    /**
     * Gets a Set of QueryInformation objects that can provide results for the Set of Facts
     * that were passed.
     * 
     * @param facts A Set of Facts to get QueryInformation objects for.
     * @return A Set of QueryInformation objects that can provider results for the Facts.
     */
    public Set<QueryInformation> getQueryInformation(Set<Fact> facts);
}
