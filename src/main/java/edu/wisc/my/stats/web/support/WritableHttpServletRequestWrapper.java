/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.web.support;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.collections.iterators.IteratorEnumeration;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class WritableHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private final Map<String, String[]> parameters = new HashMap<String, String[]>();
    
    /**
     * @param request
     */
    @SuppressWarnings("unchecked")
    public WritableHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.parameters.putAll(request.getParameterMap());
    }
    
    public void putParameter(String name, String value) {
        this.putParameter(name, new String[] { value });
    }
    
    public void putParameter(String name, String[] values) {
        this.parameters.put(name, values);
    }
    
    public void removeParameter(String name) {
        this.parameters.remove(name);
    }

    /**
     * @see javax.servlet.ServletRequestWrapper#getParameter(java.lang.String)
     */
    @Override
    public String getParameter(String name) {
        final String[] values = this.getParameterValues(name);

        if (values != null && values.length > 0) {
            return values[0];
        }
        else {
            return null;
        }
    }

    /**
     * @see javax.servlet.ServletRequestWrapper#getParameterMap()
     */
    @Override
    public Map getParameterMap() {
        return this.parameters;
    }

    /**
     * @see javax.servlet.ServletRequestWrapper#getParameterNames()
     */
    @Override
    public Enumeration getParameterNames() {
        final Set<String> parameterNames = this.parameters.keySet();
        return new IteratorEnumeration(parameterNames.iterator());
    }

    /**
     * @see javax.servlet.ServletRequestWrapper#getParameterValues(java.lang.String)
     */
    @Override
    public String[] getParameterValues(String name) {
        return this.parameters.get(name);
    }
}
