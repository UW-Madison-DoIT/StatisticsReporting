/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Represents information about a column in a SQL query
 * 
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class ColumnInformation {
    private static int idGen = 0;
    
    private int id = idGen++;
    private String fullName;
    private String alias;
    
    /**
     * @return the id
     */
    public int getId() {
        return this.id;
    }
    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the alias
     */
    public String getAlias() {
        return this.alias;
    }
    /**
     * @param alias the alias to set
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }
    
    /**
     * @return the fullName
     */
    public String getFullName() {
        return this.fullName;
    }
    /**
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    
    /**
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof ColumnInformation)) {
            return false;
        }
        ColumnInformation rhs = (ColumnInformation)object;
        return new EqualsBuilder()
            .append(this.fullName, rhs.fullName)
            .append(this.alias, rhs.alias)
            .isEquals();
    }
    
    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder(304145885, 850228319)
            .append(this.fullName)
            .append(this.alias)
            .toHashCode();
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this)
            .appendSuper(super.toString())
            .append("fullName", this.fullName)
            .append("alias", this.alias)
            .toString();
    }
}
