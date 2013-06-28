/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.web.filter;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import edu.wisc.my.stats.web.support.WritableHttpServletRequestWrapper;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class RelativeDateQueryFilter implements Filter {
    private Set<String> dateParameters;
    private DateFormat dateFormat;
    
    /**
     * @return the dateFormat
     */
    public DateFormat getDateFormat() {
        return this.dateFormat;
    }
    /**
     * @param dateFormat the dateFormat to set
     */
    @Required
    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * @return the dateParameters
     */
    public Set<String> getDateParameters() {
        return this.dateParameters;
    }
    /**
     * @param dateParameters the dateParameters to set
     */
    @Required
    public void setDateParameters(Set<String> dateParameters) {
        this.dateParameters = dateParameters;
    }

    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)) {
            throw new ClassCastException("RelativeDateQueryFilter can only filter HttpServletRequests");
        }
        
        boolean useWrapper = false;
        final WritableHttpServletRequestWrapper wrappedRequestWrapper = new WritableHttpServletRequestWrapper((HttpServletRequest)request);
        for (final String dateParameter : this.dateParameters) {
            final String value = request.getParameter(dateParameter);
            
            if (StringUtils.isBlank(value)) {
                continue;
            }
            
            try {
                final int dayOffset = Integer.parseInt(value);
                final Calendar now = Calendar.getInstance();
                now.add(Calendar.DAY_OF_YEAR, -1 * dayOffset);
                final String newValue = this.dateFormat.format(now.getTime());
                
                useWrapper = true;
                wrappedRequestWrapper.putParameter(dateParameter, newValue);
            }
            catch (NumberFormatException nfe) {
                //Isn't a single number, assume it is a valid Date and just ignore it.
            }
        }
        
        if (useWrapper) {
            chain.doFilter(wrappedRequestWrapper, response);
        }
        else {
            chain.doFilter(request, response);
        }
    }
    
    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig config) throws ServletException {
    }
    
    /**
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy() {
    }
}
