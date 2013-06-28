/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.tag;

import java.util.Date;

import edu.wisc.my.stats.util.Table;
import edu.wisc.my.stats.util.Table.Entry;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class Functions {
    public static Date toDate(long timestamp) {
        return new Date(timestamp);
    }
    
    public static <R, C, V> Iterable<Entry<R, C, V>> rowEntries(Table<R, C, V> table, R rowKey) {
        return table.getRowEntries(rowKey);
    }
}
