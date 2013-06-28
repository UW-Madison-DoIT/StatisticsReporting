/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.web.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class QueryCommand {
    private QueryParameters queryParameters = new QueryParameters();
    private Map<String, List<String>> extraParameterValues = new HashMap<String, List<String>>();
    private AdvancedOptions advancedOptions = new AdvancedOptions();
    private boolean hasAdvancedOptions = false;
    
    /**
     * @return the advancedOptions
     */
    public AdvancedOptions getAdvancedOptions() {
        return this.advancedOptions;
    }
    /**
     * @param advancedOptions the advancedOptions to set
     */
    public void setAdvancedOptions(AdvancedOptions advancedOptions) {
        this.advancedOptions = advancedOptions;
    }
    
    /**
     * @return the extraParameterValues
     */
    public Map<String, List<String>> getExtraParameterValues() {
        return this.extraParameterValues;
    }
    /**
     * @param extraParameterValues the extraParameterValues to set
     */
    public void setExtraParameterValues(Map<String, List<String>> extraParameterValues) {
        this.extraParameterValues = extraParameterValues;
    }
    
    /**
     * @return the hasAdvancedOptions
     */
    public boolean isHasAdvancedOptions() {
        return this.hasAdvancedOptions;
    }
    /**
     * @param hasAdvancedOptions the hasAdvancedOptions to set
     */
    public void setHasAdvancedOptions(boolean hasAdvancedOptions) {
        this.hasAdvancedOptions = hasAdvancedOptions;
    }
    
    /**
     * @return the queryParameters
     */
    public QueryParameters getQueryParameters() {
        return this.queryParameters;
    }
    /**
     * @param queryParameters the queryParameters to set
     */
    public void setQueryParameters(QueryParameters queryParameters) {
        this.queryParameters = queryParameters;
    }
    
    
    /**
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof QueryCommand)) {
            return false;
        }
        QueryCommand rhs = (QueryCommand)object;
        return new EqualsBuilder()
            .append(this.extraParameterValues, rhs.extraParameterValues)
            .append(this.advancedOptions, rhs.advancedOptions)
            .append(this.hasAdvancedOptions, rhs.hasAdvancedOptions)
            .append(this.queryParameters, rhs.queryParameters)
            .isEquals();
    }
    
    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder(-1545574009, 1096542795)
            .append(this.extraParameterValues)
            .append(this.advancedOptions)
            .append(this.hasAdvancedOptions)
            .append(this.queryParameters)
            .toHashCode();
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this)
            .appendSuper(super.toString())
            .append("advancedOptions", this.advancedOptions)
            .append("queryParameters", this.queryParameters)
            .append("hasAdvancedOptions", this.hasAdvancedOptions)
            .append("extraParameterValues", this.extraParameterValues)
            .toString();
    }
}
