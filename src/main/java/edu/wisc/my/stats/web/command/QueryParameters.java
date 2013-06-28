/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.web.command;

import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.wisc.my.stats.domain.Fact;
import edu.wisc.my.stats.domain.TimeResolution;


/**
 * Bean representing a query from the user
 * 
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class QueryParameters {
    private Date start;
    private Date end;
    private TimeResolution resolution;
    private Set<Fact> facts;
    
    /**
     * @return the end
     */
    public Date getEnd() {
        return this.end;
    }
    /**
     * @param end the end to set
     */
    public void setEnd(Date end) {
        this.end = end;
    }
    
    /**
     * @return the start
     */
    public Date getStart() {
        return this.start;
    }
    /**
     * @param start the start to set
     */
    public void setStart(Date start) {
        this.start = start;
    }
    
    /**
     * @return the facts
     */
    public Set<Fact> getFacts() {
        return this.facts;
    }
    /**
     * @param facts the facts to set
     */
    public void setFacts(Set<Fact> facts) {
        this.facts = facts;
    }
    
    /**
     * @return the resolution
     */
    public TimeResolution getResolution() {
        return this.resolution;
    }
    /**
     * @param resolution the resolution to set
     */
    public void setResolution(TimeResolution resolution) {
        this.resolution = resolution;
    }
    
    /**
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof QueryParameters)) {
            return false;
        }
        QueryParameters rhs = (QueryParameters)object;
        return new EqualsBuilder()
            .append(this.resolution, rhs.resolution)
            .append(this.facts, rhs.facts)
            .append(this.start, rhs.start)
            .append(this.end, rhs.end)
            .isEquals();
    }
    
    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder(-1760502265, -161919481)
            .append(this.resolution)
            .append(this.facts)
            .append(this.start)
            .append(this.end)
            .toHashCode();
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this)
            .appendSuper(super.toString())
            .append("resolution", this.resolution)
            .append("facts", this.facts)
            .append("start", this.start)
            .append("end", this.end)
            .toString();
    }
}
