/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.query;

import java.util.Set;

import edu.wisc.my.stats.domain.Fact;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public interface FactProvider {
    /**
     * @return All available Facts in the system
     */
    public Set<Fact> getFacts();
    
    /**
     * @param id The id to get the Fact for.
     * @return The Fact for the specified id, null if no Fact exists for the specified ID.
     */
    public Fact getFactById(long id);
}
