/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.dao.xml;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;

import edu.wisc.my.stats.dao.support.AbstractMapQueryInformationDao;
import edu.wisc.my.stats.domain.Fact;
import edu.wisc.my.stats.domain.QueryInformation;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class XmlQueryInformationDao extends AbstractMapQueryInformationDao {
    private Set<QueryInformation> queryInfoSet;
    private boolean initialized = false;
    
    private String storeFile;
    
    /**
     * @return the storeFile
     */
    public String getStoreFile() {
        return this.storeFile;
    }
    /**
     * @param storeFile the storeFile to set
     */
    @Required
    public void setStoreFile(String storeFile) {
        this.storeFile = storeFile;
    }

    /**
     * @see edu.wisc.my.stats.dao.QueryInformationDao#getQueryInformationByFactMap()
     */
    public Map<Fact, Set<QueryInformation>> getQueryInformationByFactMap() {
        this.initialize();
        return super.getQueryInformationByFactMap();
    }

    /**
     * @see edu.wisc.my.stats.dao.QueryInformationDao#getfactByIdMap()
     */
    public Map<Long, Fact> getfactByIdMap() {
        this.initialize();
        return super.getfactByIdMap();
    }

    /**
     * @see edu.wisc.my.stats.dao.QueryInformationDao#addQueryInformation(edu.wisc.my.stats.domain.QueryInformation)
     */
    public void addQueryInformation(QueryInformation queryInformation) {
        this.initialize();
        
        this.writeLock.lock();
        try {
            this.addQueryInformationInternal(queryInformation);
            this.readLock.lock(); //Downgrade the lock so the changes can be persisted
        }
        finally {
            this.writeLock.unlock();
        }
        
        try {
            this.writeQueryInformationSet(this.queryInfoSet);
        }
        finally {
            this.readLock.unlock();
        }
    }
    
    /**
     * Initializes the DAO if needed by reading the queryInfoSet from the specified XML
     * file and configuring the maps
     */
    protected void initialize() {
        if (this.initialized) {
            return; //Already initialized, return immediatly
        }
        
        this.writeLock.lock();
        try {
            if (this.initialized) {
                return; //Already initialized, return immediatly
            }
        
            final Set<QueryInformation> queryInformationSet = this.readQueryInformationSet();
            for (final QueryInformation queryInformation : queryInformationSet) {
                this.addQueryInformationInternal(queryInformation);
            }
            
            this.queryInfoSet = queryInformationSet;
            this.initialized = true;
        }
        finally {
            this.writeLock.unlock();
        }
    }
    
    /**
     * @return Reads the Set of QueryInformation from the specified XML file and returns it.
     */
    @SuppressWarnings("unchecked")
    protected Set<QueryInformation> readQueryInformationSet() {
        final FileInputStream fis;
        try {
            fis = new FileInputStream(this.storeFile);
        }
        catch (FileNotFoundException fnfe) {
            final String errorMessage = "The specified storeFile='" + this.storeFile + "' could not be found.";
            this.logger.error(errorMessage, fnfe);
            throw new IllegalArgumentException(errorMessage, fnfe);
        }
        
        final BufferedInputStream bis = new BufferedInputStream(fis);
        final XMLDecoder xmlDecoder = new XMLDecoder(bis);
        try {
            return (Set<QueryInformation>)xmlDecoder.readObject();
        }
        finally {
            xmlDecoder.close();
        }
    }
    
    /**
     * @param queryInformationSet The Set of QueryInformation to persist to the specified XML file.
     */
    protected void writeQueryInformationSet(Set<QueryInformation> queryInformationSet) {
        final FileOutputStream fos;
        try {
            fos = new FileOutputStream(this.storeFile);
        }
        catch (FileNotFoundException fnfe) {
            final String errorMessage = "The specified storeFile='" + this.storeFile + "' could not be found.";
            this.logger.error(errorMessage, fnfe);
            throw new IllegalArgumentException(errorMessage, fnfe);
        }
        
        final BufferedOutputStream bos = new BufferedOutputStream(fos);
        final XMLEncoder xmlEncoder = new XMLEncoder(bos);
        try {
            xmlEncoder.writeObject(queryInformationSet);
            xmlEncoder.flush();
        }
        finally {
            xmlEncoder.close();
        }
    }
}
