/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.util;

/**
 * A table looks a lot like a multi-key list with the added ability of being able to iterate over
 * the row and column keys.
 * 
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public interface Table<R, C, V> {
    public V put(R rowKey, C columnKey, V value);
    
    public V get(R rowKey, C columnKey);
    
    public V remove(R rowKey, C columnKey);
    
    public Iterable<C> getColumnKeys();
    
    public Iterable<R> getRowKeys();
    
    public Iterable<Entry<R, C, V>> getRowEntries(R rowKey);
    
    public Iterable<Entry<R, C, V>> getColumnEntries(C columnKey);
    
    public void putAll(Table<R, C, V> sourceTable);
    
    public long getSize();
    
    /**
     * An entry in the table, with references to its Keys and Value
     */
    public interface Entry<R, C, V> {
        public R getRowKey();
        
        public C getColumnKey();
        
        public V getValue();
        
        public void setValue(V value);
    }
}
