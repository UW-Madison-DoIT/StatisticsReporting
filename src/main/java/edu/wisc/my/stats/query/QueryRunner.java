/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.query;

import edu.wisc.my.stats.domain.QueryInformation;
import edu.wisc.my.stats.util.Table;
import edu.wisc.my.stats.web.command.QueryCommand;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public interface QueryRunner<R, C, V> {
    /**
     * @return The results for the specified QueryParameters and Query 
     */
    public Table<R, C, V> runQuery(QueryCommand queryCommand, QueryInformation queryInformation);
}
