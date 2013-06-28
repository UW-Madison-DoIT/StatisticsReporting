/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.web.command.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public interface PageValidator extends Validator {
    /**
     * Same as {@link Validator#validate(Object, Errors)} but only validates a portion of
     * the object based on the specified page number.
     * 
     * @param page The page number to validate the object for, likely does not validate the entire object.
     * @param target the object that is to be validated (can be <code>null</code>) 
     * @param errors contextual state about the validation process (never <code>null</code>) 
     * @see ValidationUtils
     */
    public void validatePage(int page, Object target, Errors errors);
}
