/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.web.command.validation;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.springframework.validation.BeanPropertyBindingResult;

import edu.wisc.my.stats.domain.Fact;
import edu.wisc.my.stats.domain.TimeResolution;
import edu.wisc.my.stats.web.command.QueryCommand;

/**
 * Tests QueryParametersValidator
 * 
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class QueryCommandValidatorTest extends TestCase {
    private QueryCommand queryCommand;
    private QueryCommandValidator queryParametersValidator;
    private BeanPropertyBindingResult errors;
    
    @Override
    protected void setUp() throws Exception {
        this.queryCommand = new QueryCommand();
        this.queryParametersValidator = new QueryCommandValidator();
        this.errors = new BeanPropertyBindingResult(this.queryCommand, "queryParameters");
    }

    public void testEmptyBean() {
        this.queryParametersValidator.validate(this.queryCommand, this.errors);
        assertEquals(this.errors.toString(), 4, this.errors.getErrorCount());
    }

    public void testNoStartDate() {
        final Set<Fact> facts = new HashSet<Fact>();
        facts.add(new Fact(0, "Login"));
        this.queryCommand.getQueryParameters().setFacts(facts);
        this.queryCommand.getQueryParameters().setEnd(new Date());
        this.queryCommand.getQueryParameters().setResolution(TimeResolution.DAY);
        
        this.queryParametersValidator.validate(this.queryCommand, this.errors);
        assertEquals(this.errors.toString(), 1, this.errors.getErrorCount());
    }

    public void testNoEndDate() {
        final Set<Fact> facts = new HashSet<Fact>();
        facts.add(new Fact(0, "Login"));
        this.queryCommand.getQueryParameters().setFacts(facts);
        this.queryCommand.getQueryParameters().setStart(new Date());
        this.queryCommand.getQueryParameters().setResolution(TimeResolution.DAY);
        
        this.queryParametersValidator.validate(this.queryCommand, this.errors);
        assertEquals(this.errors.toString(), 1, this.errors.getErrorCount());
    }

    public void testNoFacts() {
        this.queryCommand.getQueryParameters().setStart(new Date(System.currentTimeMillis() - 1000));
        this.queryCommand.getQueryParameters().setEnd(new Date());
        this.queryCommand.getQueryParameters().setResolution(TimeResolution.DAY);
        
        this.queryParametersValidator.validate(this.queryCommand, this.errors);
        assertEquals(this.errors.toString(), 1, this.errors.getErrorCount());
    }

    public void testEmptyFacts() {
        this.queryCommand.getQueryParameters().setStart(new Date(System.currentTimeMillis() - 1000));
        this.queryCommand.getQueryParameters().setEnd(new Date());
        this.queryCommand.getQueryParameters().setFacts(new HashSet<Fact>());
        this.queryCommand.getQueryParameters().setResolution(TimeResolution.DAY);
        
        this.queryParametersValidator.validate(this.queryCommand, this.errors);
        assertEquals(this.errors.toString(), 1, this.errors.getErrorCount());
    }

    public void testEarlyStartDate() {
        final Set<Fact> facts = new HashSet<Fact>();
        facts.add(new Fact(0, "Login"));
        this.queryCommand.getQueryParameters().setFacts(facts);
        this.queryCommand.getQueryParameters().setEnd(new Date());
        this.queryCommand.getQueryParameters().setStart(new Date(System.currentTimeMillis() + 1000));
        this.queryCommand.getQueryParameters().setResolution(TimeResolution.DAY);
        
        this.queryParametersValidator.validate(this.queryCommand, this.errors);
        assertEquals(this.errors.toString(), 1, this.errors.getErrorCount());
    }

    public void testValidQueryInfoNoResolution() {
        final Set<Fact> facts = new HashSet<Fact>();
        facts.add(new Fact(0, "Login"));
        this.queryCommand.getQueryParameters().setFacts(facts);
        this.queryCommand.getQueryParameters().setStart(new Date(System.currentTimeMillis() - 1000));
        this.queryCommand.getQueryParameters().setEnd(new Date());
        this.queryCommand.getQueryParameters().setResolution(null);
        
        this.queryParametersValidator.validate(this.queryCommand, this.errors);
        assertEquals(this.errors.toString(), 1, this.errors.getErrorCount());
    }

    public void testValidQueryInfoWithResolution() {
        final Set<Fact> facts = new HashSet<Fact>();
        facts.add(new Fact(0, "Login"));
        this.queryCommand.getQueryParameters().setFacts(facts);
        this.queryCommand.getQueryParameters().setStart(new Date(System.currentTimeMillis() - 1000));
        this.queryCommand.getQueryParameters().setEnd(new Date());
        this.queryCommand.getQueryParameters().setResolution(TimeResolution.FIVE_MINUTE);
        
        this.queryParametersValidator.validate(this.queryCommand, this.errors);
        assertEquals(this.errors.toString(), 0, this.errors.getErrorCount());
    }


    @Override
    protected void tearDown() throws Exception {
        this.queryCommand = null;
        this.queryParametersValidator = null;
        this.errors = null;
    }
}
