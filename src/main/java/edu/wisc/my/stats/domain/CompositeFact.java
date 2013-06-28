/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.domain;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class CompositeFact extends Fact {
    private Fact baseFact;
    
    public CompositeFact() {
    }
    
    public CompositeFact(Fact baseFact, String compositeName) {
        this.baseFact = baseFact;
        super.setName(compositeName);
    }

    /**
     * @return the baseFact
     */
    public Fact getBaseFact() {
        return this.baseFact;
    }
    /**
     * @param baseFact the baseFact to set
     */
    public void setBaseFact(Fact baseFact) {
        this.baseFact = baseFact;
    }
    
    /**
     * @see edu.wisc.my.stats.domain.Fact#getName()
     */
    @Override
    public String getName() {
        return this.baseFact.getName() + " - " + super.getName();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this)
            .appendSuper(super.toString())
            .append("baseFact", this.baseFact)
            .toString();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder(1382715189, -115202965)
            .appendSuper(super.hashCode())
            .append(this.baseFact)
            .toHashCode();
    }

    /**
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof CompositeFact)) {
            return false;
        }
        CompositeFact rhs = (CompositeFact)object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(this.baseFact, rhs.baseFact)
            .isEquals();
    }

    /**
     * @see java.lang.Comparable#compareTo(Object)
     */
    public int compareTo(final Fact myClass) {
        if (!(myClass instanceof CompositeFact)) {
            return -1;
        }
        CompositeFact rhs = (CompositeFact)myClass;
        
        return new CompareToBuilder()
            .appendSuper(super.compareTo(myClass))
            .append(this.baseFact, rhs.baseFact)
            .toComparison();
    }
}
