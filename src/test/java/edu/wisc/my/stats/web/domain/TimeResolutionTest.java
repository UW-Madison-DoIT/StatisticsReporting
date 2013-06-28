/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.web.domain;

import java.util.Calendar;

import edu.wisc.my.stats.domain.TimeResolution;

import junit.framework.TestCase;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class TimeResolutionTest extends TestCase {
    private Calendar start;
    private Calendar end;
    
   /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        start = Calendar.getInstance();
        start.set(Calendar.YEAR, 2000);
        start.set(Calendar.MONTH, 1);
        start.set(Calendar.DAY_OF_MONTH, 1);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        
        end = (Calendar)start.clone();
    }
    
    public void testInstanceCountMinutes() {
       end.set(Calendar.MINUTE, 51); 
       final long count = TimeResolution.MINUTE.instanceCount(start.getTime(), end.getTime());
       assertEquals("Expected instance count is incorrect", 51, count);
    }

    public void testInstanceCountFiveMinutes() {
       end.set(Calendar.MINUTE, 51); 
       final long count = TimeResolution.FIVE_MINUTE.instanceCount(start.getTime(), end.getTime());
       assertEquals("Expected instance count is incorrect", 10, count);
    }
}
