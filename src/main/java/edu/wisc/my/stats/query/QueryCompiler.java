/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.query;

import edu.wisc.my.stats.domain.QueryInformation;
import edu.wisc.my.stats.web.command.QueryCommand;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public interface QueryCompiler {
    /**
     * @return A compiled Query object that is ready to be executed for the specified parameters and information.
     */
    public String compileQuery(QueryCommand queryCommand, QueryInformation queryInformation);
}
