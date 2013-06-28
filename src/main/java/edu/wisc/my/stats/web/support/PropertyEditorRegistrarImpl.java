/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.web.support;

import java.beans.PropertyEditor;
import java.util.Map;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;

/**
 * Registers all of the mapped {@link PropertyEditor}s when {@link #registerCustomEditors(PropertyEditorRegistry)}
 * is called.
 * 
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class PropertyEditorRegistrarImpl implements PropertyEditorRegistrar {
    private Map<Class, PropertyEditor> propertyEditors;
    
    /**
     * @return the propertyEditors
     */
    public Map<Class, PropertyEditor> getPropertyEditors() {
        return this.propertyEditors;
    }
    /**
     * @param propertyEditors the propertyEditors to set
     */
    public void setPropertyEditors(Map<Class, PropertyEditor> propertyEditors) {
        this.propertyEditors = propertyEditors;
    }

    /**
     * @see org.springframework.beans.PropertyEditorRegistrar#registerCustomEditors(org.springframework.beans.PropertyEditorRegistry)
     */
    public void registerCustomEditors(PropertyEditorRegistry registry) {
        for (final Map.Entry<Class, PropertyEditor> editorEntry : this.propertyEditors.entrySet()) {
            final Class key = editorEntry.getKey();
            final PropertyEditor value = editorEntry.getValue();
            registry.registerCustomEditor(key, value);
        }
    }
}
