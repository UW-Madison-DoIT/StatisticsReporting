/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.web.command.validation;

import java.util.Date;
import java.util.Set;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import edu.wisc.my.stats.domain.Fact;
import edu.wisc.my.stats.web.command.QueryCommand;
import edu.wisc.my.stats.web.command.QueryParameters;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class QueryCommandValidator implements PageValidator {
    /**
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    public boolean supports(Class clazz) {
        return QueryCommand.class.isAssignableFrom(clazz);
    }

    /**
     * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
     */
    public void validate(Object target, Errors errors) {
        this.validatePage(0, target, errors);
        this.validatePage(1, target, errors);
        this.validatePage(2, target, errors);
    }
    
    /**
     * @see edu.wisc.my.stats.web.command.validation.PageValidator#validatePage(int, java.lang.Object, org.springframework.validation.Errors)
     */
    public void validatePage(int page, Object target, Errors errors) {
        final QueryCommand queryCommand = (QueryCommand)target;
        
        switch (page) {
            case 0: {
                this.validateDates(queryCommand, errors);
                this.validateResolution(queryCommand, errors);
                this.validateFacts(queryCommand, errors);
                //TODO ensure the number of data points isn't too large
            } break;
            case 1: {
                //TODO ensure all nessesary ExtraParameters have values
            }
            case 2: {
                //TODO ensure graph options are valid
            }
        }
    }

    private void validateDates(QueryCommand queryCommand, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "queryParameters.start", "portlet.queryinfo.error.start.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "queryParameters.end", "portlet.queryinfo.error.end.required");
        
        final QueryParameters queryParameters = queryCommand.getQueryParameters();
        final Date start = queryParameters.getStart();
        final Date end = queryParameters.getEnd();
        if (start != null && end != null && start.after(end)) {
            errors.rejectValue("queryParameters.start", "portlet.queryinfo.error.start.greater_than_end");
        }
    }
    
    private void validateResolution(QueryCommand queryCommand, Errors errors) {
        final QueryParameters queryParameters = queryCommand.getQueryParameters();
        if (queryParameters.getResolution() == null) {
            errors.rejectValue("queryParameters.resolution", "portlet.queryinfo.error.resolution.required");
        }
    }
    
    private void validateFacts(QueryCommand queryCommand, Errors errors) {
        final QueryParameters queryParameters = queryCommand.getQueryParameters();
        final Set<Fact> facts = queryParameters.getFacts();
        if (facts == null || facts.size() < 1) {
            errors.rejectValue("queryParameters.facts", "portlet.queryinfo.error.facts.required");
        }
    }
}
