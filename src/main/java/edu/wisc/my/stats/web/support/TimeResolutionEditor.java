/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.web.support;

import java.beans.PropertyEditorSupport;

import org.springframework.util.StringUtils;

import edu.wisc.my.stats.domain.TimeResolution;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class TimeResolutionEditor extends PropertyEditorSupport {
    /**
     * @see java.beans.PropertyEditorSupport#getAsText()
     */
    @Override
    public String getAsText() {
        final TimeResolution value = (TimeResolution)this.getValue();
        if (value != null) {
            return value.getCode();
        }
        else {
            return null;
        }
    }

    /**
     * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (!StringUtils.hasText(text)) { //TODO replace with commons-lang
            this.setValue(null);
        }
        else {
            final TimeResolution resolution = TimeResolution.valueOf(text);
            this.setValue(resolution);
        }
    }
}
