/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.query;

import java.util.List;

import edu.wisc.my.stats.domain.ExtraParameter;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public interface ExtraParameterValuesProvider {
    /**
     * Gets a List of possible values for the specified ExtraParameter. <code>null</code>
     * implies that any value is valid.
     */
    public List<String> getPossibleValues(ExtraParameter extraParameter);
}
