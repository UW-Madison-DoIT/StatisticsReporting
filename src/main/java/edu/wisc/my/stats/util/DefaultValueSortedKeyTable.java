/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.util;



/**
 * Ensures that no get(R, C) (or any other method of accessing a value) returns null and
 * there are no holes in the table based on the row and column keys. Missing values are never
 * actually populated in the table.
 * 
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class DefaultValueSortedKeyTable<R, C, V> extends SortedKeyTable<R, C, V> {
    private final V defaultValue;
    
    public DefaultValueSortedKeyTable(final V defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * @see edu.wisc.my.stats.util.SortedKeyTable#getEntry(edu.wisc.my.stats.util.SortedKeyTable.HashingRowColumnKey)
     */
    @Override
    protected edu.wisc.my.stats.util.Table.Entry<R, C, V> getEntry(edu.wisc.my.stats.util.SortedKeyTable.HashingRowColumnKey<R, C> key) {
        final Entry<R, C, V> entry = super.getEntry(key);
        if (entry == null) {
            final HashingTableEntry<R, C, V> tempEntry = new HashingTableEntry<R, C, V>(key, this.defaultValue);
            return tempEntry;
        }
        else {
            if (entry.getValue() == null) {
                entry.setValue(this.defaultValue);
            }

            return entry;
        }
    }
    
}
