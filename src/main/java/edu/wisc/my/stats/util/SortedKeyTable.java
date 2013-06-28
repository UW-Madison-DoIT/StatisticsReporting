/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;



/**
 * If a Comparator is not provided for the row and/or column key(s) the objects for that key must
 * implement Comparable.
 * 
 * Provides O(1) access to data in the table and is a sparce representation, having 100 row keys and 100
 * column keys won't result in 10000 objects in the table. By default keys are in their natural ordering,
 * {@link Comparator}s can be specified for the row and column keys.
 * 
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class SortedKeyTable<R, C, V> implements Table<R, C, V> {
    private final Map<HashingRowColumnKey<R, C>, Entry<R, C, V>> values = new HashMap<HashingRowColumnKey<R, C>, Entry<R, C, V>>();
    private final SortedMap<R, Integer> rowKeys;
    private final SortedMap<C, Integer> columnKeys;
    
    //Unmodifiable views into the key sets, prevents un-needed object creation for each get*Keys call
    private final Set<R> readOnlyRowKeysView;
    private final Set<C> readOnlyColumnKeysView;
    
    
    /**
     * Same as calling SortedKeyTable(null, null)
     */
    public SortedKeyTable() {
        this(null, null);
    }
    
    /**
     * @param rowKeyComparator Comparator to order the row keys with, if null the natural ordering is used.
     * @param columnKeyComparator Comparator to order the column keys with, if null the natural ordering is used.
     */
    public SortedKeyTable(final Comparator<R> rowKeyComparator, final Comparator<C> columnKeyComparator) {
        if (rowKeyComparator != null) {
            this.rowKeys = new TreeMap<R, Integer>(rowKeyComparator);
        }
        else {
            this.rowKeys = new TreeMap<R, Integer>();
        }
        
        if (columnKeyComparator != null) {
            this.columnKeys = new TreeMap<C, Integer>(columnKeyComparator);
        }
        else {
            this.columnKeys = new TreeMap<C, Integer>();
        }
        
        this.readOnlyRowKeysView = Collections.unmodifiableSet(this.rowKeys.keySet());
        this.readOnlyColumnKeysView = Collections.unmodifiableSet(this.columnKeys.keySet());
    }
    
    /**
     * @see edu.wisc.my.stats.util.Table#get(java.lang.Object, java.lang.Object)
     */
    public V get(R rowKey, C columnKey) {
        final Entry<R, C, V> entry = this.getEntry(rowKey, columnKey);
        if (entry == null) {
            return null;
        }
        else {
            return entry.getValue();
        }
    }

    /**
     * TODO operations on the iterator should be valid and affect the whole table
     * TODO return a SortedSet
     * @see edu.wisc.my.stats.util.Table#getColumnKeys()
     */
    public Iterable<C> getColumnKeys() {
        return this.readOnlyColumnKeysView;
    }

    /**
     * TODO operations on the iterator should be valid and affect the whole table
     * TODO return a SortedSet
     * @see edu.wisc.my.stats.util.Table#getRowKeys()
     */
    public Iterable<R> getRowKeys() {
        return this.readOnlyRowKeysView;
    }
    
    /**
     * TODO operations on the iterator should be valid and affect the whole table
     * TODO return a SortedSet
     * @see edu.wisc.my.stats.util.Table#getColumnEntries(java.lang.Object)
     */
    public Iterable<Entry<R, C, V>> getColumnEntries(C columnKey) {
        return new RowSetIterable(columnKey, this.readOnlyRowKeysView);
    }

    /**
     * TODO operations on the iterator should be valid and affect the whole table
     * TODO return a SortedSet
     * @see edu.wisc.my.stats.util.Table#getRowEntries(java.lang.Object)
     */
    public Iterable<Entry<R, C, V>> getRowEntries(R rowKey) {
        return new ColumnSetIterable(rowKey, this.readOnlyColumnKeysView);
    }

    /**
     * @see edu.wisc.my.stats.util.Table#getSize()
     */
    public long getSize() {
        return this.values.size();
    }

    /**
     * @see edu.wisc.my.stats.util.Table#put(java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public V put(R rowKey, C columnKey, V value) {
        final HashingTableEntry<R, C, V> entry = new HashingTableEntry<R, C, V>(rowKey, columnKey, value);
        
        Integer rowEntryCount = this.rowKeys.get(rowKey);
        rowEntryCount = (rowEntryCount != null ? rowEntryCount + 1 : 1);
        this.rowKeys.put(rowKey, rowEntryCount);
        
        Integer columnEntryCount = this.columnKeys.get(columnKey);
        columnEntryCount = (columnEntryCount != null ? columnEntryCount + 1 : 1);
        this.columnKeys.put(columnKey, columnEntryCount);
        
        final Entry<R, C, V> oldEntry = this.values.put(entry.getKey(), entry);
        if (oldEntry != null) {
            return oldEntry.getValue();
        }
        else {
            return null;
        }
    }
    
    /**
     * @see edu.wisc.my.stats.util.Table#putAll(edu.wisc.my.stats.util.Table)
     */
    public void putAll(Table<R, C, V> sourceTable) {
        if (sourceTable.getSize() == 0) {
            return;
        }

        for (final R rowKey : sourceTable.getRowKeys()) {
            for (final C columnKey : sourceTable.getColumnKeys()) {
                final V value = sourceTable.get(rowKey, columnKey);
                this.put(rowKey, columnKey, value);
            }
        }
    }

    /**
     * @see edu.wisc.my.stats.util.Table#remove(java.lang.Object, java.lang.Object)
     */
    public V remove(R rowKey, C columnKey) {
        final Entry<R, C, V> entry = this.removeEntry(rowKey, columnKey);
        
        if (entry != null) {
            Integer rowEntryCount = this.rowKeys.get(rowKey);
            rowEntryCount = (rowEntryCount != null ? rowEntryCount - 1 : 1);
            if (rowEntryCount > 0) {
                this.rowKeys.put(rowKey, rowEntryCount);
            }
            else {
                this.rowKeys.remove(rowKey);
            }
            
            Integer columnEntryCount = this.columnKeys.get(columnKey);
            columnEntryCount = (columnEntryCount != null ? columnEntryCount - 1 : 1);
            if (columnEntryCount > 0) {
                this.columnKeys.put(columnKey, columnEntryCount);
            }
            else {
                this.columnKeys.remove(columnKey);
            }
            
            return entry.getValue();
        }
        else {
            return null;
        }
    }
    
    /**
     * @see SortedSet#comparator()
     */
    public Comparator<? super R> getRowKeyComparator() {
        return this.rowKeys.comparator();
    }
    
    /**
     * @see SortedSet#comparator()
     */
    public Comparator<? super C> getColumnKeyComparator() {
        return this.columnKeys.comparator();
    }

    /**
     * Gets an Entry for a rowKey and columnKey
     */
    protected Entry<R, C, V> getEntry(R rowKey, C columnKey) {
        final HashingRowColumnKey<R, C> key = new HashingRowColumnKey<R, C>(rowKey, columnKey);
        return this.getEntry(key);
    }
    
    /**
     * Gets an Entry for a rowKey and columnKey
     */
    protected Entry<R, C, V> getEntry(HashingRowColumnKey<R, C> key) {
        return this.values.get(key);
    }

    /**
     * Remove an Entry for a rowKey and columnKey
     */
    protected Entry<R, C, V> removeEntry(R rowKey, C columnKey) {
        final HashingRowColumnKey<R, C> key = new HashingRowColumnKey<R, C>(rowKey, columnKey);
        return this.values.remove(key);
    }
    
    /**
     * Iterates over a set of values in a specific column based on the row headers in the specified rowSet
     */
    private class RowSetIterable extends EntrySet<R> {
        private final C columnKey;
        
        public RowSetIterable(final C columnKey, final Set<R> rowSet) {
            super(rowSet);
            this.columnKey = columnKey;
        }

        @Override
        protected HashingRowColumnKey<R, C> getValueKey(R rowKey) {
            return new HashingRowColumnKey<R, C>(rowKey, this.columnKey);
        }
    }
    
    /**
     * Iterates over a set of values in a specific row based on the column headers in the specified columnSet
     */
     private class ColumnSetIterable extends EntrySet<C> {
        private final R rowKey;
        
        public ColumnSetIterable(final R rowKey, final Set<C> columnSet) {
            super(columnSet);
            this.rowKey = rowKey;
        }

        @Override
        protected HashingRowColumnKey<R, C> getValueKey(C columnKey) {
            return new HashingRowColumnKey<R, C>(this.rowKey, columnKey);
        }
    }
    
    /**
     * Base class for iterating over a Set and using the set values to get a key to get the
     * Entry from the values Map.
     */
    private abstract class EntrySet<ESK> implements SortedSet<Entry<R, C, V>> {
        private final Set<ESK> keySet;
        
        public EntrySet(final Set<ESK> entrySet) {
            this.keySet = entrySet;
        }

        public final Iterator<edu.wisc.my.stats.util.Table.Entry<R, C, V>> iterator() {
            return new EntrySetIterator();
        }
        
        protected abstract HashingRowColumnKey<R, C> getValueKey(ESK rowKey);
        
        private class EntrySetIterator implements Iterator<edu.wisc.my.stats.util.Table.Entry<R, C, V>> {
            private final Iterator<ESK> entrySetIterator;
            
            public EntrySetIterator() {
                this.entrySetIterator = EntrySet.this.keySet.iterator();
            }

            public boolean hasNext() {
                return this.entrySetIterator.hasNext();
            }

            public edu.wisc.my.stats.util.Table.Entry<R, C, V> next() {
                final ESK entrySetKey = this.entrySetIterator.next();
                final HashingRowColumnKey<R, C> key = EntrySet.this.getValueKey(entrySetKey);
                return getEntry(key);
            }

            public void remove() {
                this.entrySetIterator.remove();
            }
        }
        
        public Comparator<? super edu.wisc.my.stats.util.Table.Entry<R, C, V>> comparator() {
            throw new UnsupportedOperationException();
        }
        public edu.wisc.my.stats.util.Table.Entry<R, C, V> first() {
            throw new UnsupportedOperationException();
        }
        public SortedSet<edu.wisc.my.stats.util.Table.Entry<R, C, V>> headSet(edu.wisc.my.stats.util.Table.Entry<R, C, V> toElement) {
            throw new UnsupportedOperationException();
        }
        public edu.wisc.my.stats.util.Table.Entry<R, C, V> last() {
            throw new UnsupportedOperationException();
        }
        public SortedSet<edu.wisc.my.stats.util.Table.Entry<R, C, V>> subSet(edu.wisc.my.stats.util.Table.Entry<R, C, V> fromElement, edu.wisc.my.stats.util.Table.Entry<R, C, V> toElement) {
            throw new UnsupportedOperationException();
        }
        public SortedSet<edu.wisc.my.stats.util.Table.Entry<R, C, V>> tailSet(edu.wisc.my.stats.util.Table.Entry<R, C, V> fromElement) {
            throw new UnsupportedOperationException();
        }
        public boolean add(edu.wisc.my.stats.util.Table.Entry<R, C, V> o) {
            throw new UnsupportedOperationException();
        }
        public boolean addAll(Collection<? extends edu.wisc.my.stats.util.Table.Entry<R, C, V>> c) {
            throw new UnsupportedOperationException();
        }
        public void clear() {
            throw new UnsupportedOperationException();
        }
        public boolean contains(Object o) {
            throw new UnsupportedOperationException();
        }
        public boolean containsAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        public boolean isEmpty() {
            throw new UnsupportedOperationException();
        }
        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }
        public int size() {
            throw new UnsupportedOperationException();
        }
        public Object[] toArray() {
            throw new UnsupportedOperationException();
        }
        public <T> T[] toArray(T[] a) {
            throw new UnsupportedOperationException();
        }
    }
    
    
    /**
     * Represents an entry in the table that has references to the row and column keys used to store
     * the entry.
     */
    static class HashingTableEntry<R, C, V> implements Table.Entry<R, C, V> {
        private final HashingRowColumnKey<R, C> key;
        private V value;
        
        public HashingTableEntry(final R rowKey, final C columnKey, final V value) {
            this(new HashingRowColumnKey<R, C>(rowKey, columnKey), value);
        }
        
        public HashingTableEntry(final HashingRowColumnKey<R, C> key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        HashingRowColumnKey<R, C> getKey() {
            return this.key;
        }
        
        /**
         * @see edu.wisc.my.stats.util.Table.Entry#getColumnKey()
         */
        public C getColumnKey() {
            return this.key.getColumnKey();
        }

        /**
         * @see edu.wisc.my.stats.util.Table.Entry#getRowKey()
         */
        public R getRowKey() {
            return this.key.getRowKey();
        }

        /**
         * @see edu.wisc.my.stats.util.Table.Entry#getValue()
         */
        public V getValue() {
            return this.value;
        }
        /**
         * @see edu.wisc.my.stats.util.Table.Entry#setValue(java.lang.Object)
         */
        public void setValue(V value) {
            this.value = value;
        }

        /**
         * @see java.lang.Object#equals(Object)
         */
        public boolean equals(final Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof HashingTableEntry)) {
                return false;
            }
            HashingTableEntry rhs = (HashingTableEntry)object;
            return new EqualsBuilder()
                .append(this.key, rhs.key)
                .append(this.value, rhs.value)
                .isEquals();
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        public int hashCode() {
            return new HashCodeBuilder(-720255699, -209255433)
                .append(this.key)
                .append(this.value)
                .toHashCode();
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {
            return "[K=" + this.key + ", V=" + this.value + "]";
        }
        
        
    }
    
    /**
     * Represents a row/column key pair, a reference to an entry in the table.
     */
    static class HashingRowColumnKey<RK, CK> {
        private final RK rowKey;
        private final CK columnKey;
        
        public HashingRowColumnKey(final RK rowKey, final CK columnKey) {
            this.rowKey = rowKey;
            this.columnKey = columnKey;
        }
        
        /**
         * @return the value1
         */
        public RK getRowKey() {
            return this.rowKey;
        }

        /**
         * @return the value2
         */
        public CK getColumnKey() {
            return this.columnKey;
        }

        /**
         * @see java.lang.Object#equals(Object)
         */
        public boolean equals(final Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof HashingRowColumnKey)) {
                return false;
            }
            HashingRowColumnKey rhs = (HashingRowColumnKey)object;
            return new EqualsBuilder()
                .append(this.rowKey, rhs.rowKey)
                .append(this.columnKey, rhs.columnKey)
                .isEquals();
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        public int hashCode() {
            return new HashCodeBuilder(1078254737, 749664183)
                .append(this.rowKey)
                .append(this.columnKey)
                .toHashCode();
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString() {
            return "[RK=" + this.rowKey + ", CK=" + this.columnKey + "]";
        }
    }

    /**
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof SortedKeyTable)) {
            return false;
        }
        SortedKeyTable rhs = (SortedKeyTable)object;
        return new EqualsBuilder()
            .append(this.values, rhs.values)
            .append(this.columnKeys, rhs.columnKeys)
            .append(this.rowKeys, rhs.rowKeys)
            .isEquals();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder(-1532894747, 1592941045)
            .append(this.values)
            .append(this.columnKeys)
            .append(this.rowKeys)
            .toHashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        final StringBuilder tableString = new StringBuilder();
        boolean firstRun = true;
        
        tableString.append("[");
        for (final Iterator<R> rowKeyItr = this.rowKeys.keySet().iterator(); rowKeyItr.hasNext();) {
            final R rowKey = rowKeyItr.next();
            
            tableString.append(rowKey);
            tableString.append("=[");
            for (final Iterator<C> columnKeyItr = this.columnKeys.keySet().iterator(); columnKeyItr.hasNext();) {
                final C columnKey = columnKeyItr.next();
                
                if (firstRun) {
                    firstRun = false;
                    tableString.append(columnKey);
                }
                else {
                    final V value = this.get(rowKey, columnKey);
                    tableString.append(value);
                }
                
                if (columnKeyItr.hasNext()) {
                    tableString.append(", ");
                }
            }
            tableString.append("]");
            
            if (rowKeyItr.hasNext()) {
                tableString.append(", ");
            }
        }
        tableString.append("]");
        
        return tableString.toString();
    }
}