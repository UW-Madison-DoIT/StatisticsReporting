/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.web.command;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class AdvancedOptions {
    private int width = 1024;
    private int height = 768;
    /**
     * @return the height
     */
    public int getHeight() {
        return this.height;
    }
    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }
    /**
     * @return the width
     */
    public int getWidth() {
        return this.width;
    }
    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }
}
