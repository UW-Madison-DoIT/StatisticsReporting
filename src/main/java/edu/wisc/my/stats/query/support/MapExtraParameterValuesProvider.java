/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.query.support;

import java.util.List;
import java.util.Map;

import edu.wisc.my.stats.domain.ExtraParameter;
import edu.wisc.my.stats.query.ExtraParameterValuesProvider;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class MapExtraParameterValuesProvider implements ExtraParameterValuesProvider {
    private Map<ExtraParameter, List<String>> possibleValuesMapping;
    
    /**
     * @return the possibleValuesMapping
     */
    public Map<ExtraParameter, List<String>> getPossibleValuesMapping() {
        return this.possibleValuesMapping;
    }
    /**
     * @param possibleValuesMapping the possibleValuesMapping to set
     */
    public void setPossibleValuesMapping(Map<ExtraParameter, List<String>> possibleValuesMapping) {
        this.possibleValuesMapping = possibleValuesMapping;
    }

    /**
     * @see edu.wisc.my.stats.query.ExtraParameterValuesProvider#getPossibleValues(edu.wisc.my.stats.domain.ExtraParameter)
     */
    public List<String> getPossibleValues(ExtraParameter extraParameter) {
        return possibleValuesMapping.get(extraParameter);
    }
}
