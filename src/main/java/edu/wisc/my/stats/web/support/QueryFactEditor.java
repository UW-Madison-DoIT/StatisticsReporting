/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.web.support;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang.StringUtils;

import edu.wisc.my.stats.domain.Fact;
import edu.wisc.my.stats.query.FactProvider;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class QueryFactEditor extends PropertyEditorSupport {
    private FactProvider factProvider;

    /**
     * @return the factProvider
     */
    public FactProvider getFactProvider() {
        return this.factProvider;
    }
    /**
     * @param factProvider the factProvider to set
     */
    public void setFactProvider(FactProvider factProvider) {
        this.factProvider = factProvider;
    }

    /**
     * @see java.beans.PropertyEditorSupport#getAsText()
     */
    @Override
    public String getAsText() {
        final Fact value = (Fact)this.getValue();
        if (value != null) {
            return Long.toString(value.getId());
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
        if (!StringUtils.isNotBlank(text)) {
            this.setValue(null);
        }
        else {
            final long id = Long.parseLong(text);
            final Fact fact = this.factProvider.getFactById(id);
            this.setValue(fact);
        }
    }
}
