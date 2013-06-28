/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.CompareToBuilder;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class Fact implements Comparable<Fact> {
    private static long idGen = 0;
    
    private long id = idGen++;
    private String name;
    
    public Fact() {
    }
    
    public Fact(long id, String name) {
        this.id = id;
        this.name = name;
    }
    
    /**
     * @return the id
     */
    public long getId() {
        return this.id;
    }
    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString())
            .append("id", this.id)
            .append("name", this.name)
            .toString();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder(1382715189, -115202965)
            .append(this.name)
            .toHashCode();
    }

    /**
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof Fact)) {
            return false;
        }
        Fact rhs = (Fact)object;
        return new EqualsBuilder()
            .append(this.name, rhs.name)
            .isEquals();
    }

    /**
     * @see java.lang.Comparable#compareTo(Object)
     */
    public int compareTo(final Fact myClass) {
        return new CompareToBuilder()
            .append(this.name, myClass.name)
            .toComparison();
    }
}
