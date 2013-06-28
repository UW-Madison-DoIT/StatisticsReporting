/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.domain;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class ExtraParameter {
    private static long idGen = 0;
    
    private long id = idGen++;
    private String name;
    private boolean multivalued;

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
     * @return the multivalued
     */
    public boolean isMultivalued() {
        return this.multivalued;
    }
    /**
     * @param multivalued the multivalued to set
     */
    public void setMultivalued(boolean multivalued) {
        this.multivalued = multivalued;
    }
}
