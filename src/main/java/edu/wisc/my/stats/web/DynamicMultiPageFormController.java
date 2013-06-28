/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractFormController;
import org.springframework.web.servlet.mvc.AbstractWizardFormController;

import edu.wisc.my.stats.web.command.validation.PageValidator;


/**
 * <p>
 * A semi-clone of Spring's {@link AbstractWizardFormController} that removes the next/previous/finish/cancel
 * page management. There are two abstract methods {@link #getCurrentPageNumber(HttpServletRequest, HttpServletResponse, Object, BindException)}
 * and {@link #getTargetPageNumber(HttpServletRequest, HttpServletResponse, Object, BindException)} that the sub-class
 * must implemenet to ensure the correct page is rendered.
 * </p>
 * 
 * <p>
 * When the controller is initialy viewed the initital page will be displayed. The initial
 * page is determined by a call to {@link #getInitialPage(HttpServletRequest, Object)}.
 * </p>
 * <p>
 * {@link #referenceData(HttpServletRequest, Object, Errors, int)} is called before any page
 * is rendered to allow for data to be added to the model by the implementing class.
 * </p>
 * <p>
 * The target page will be ignored during a form submission that has bind errors. In that
 * case the current page will be re-displayed.
 * </p> 
 * 
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public abstract class DynamicMultiPageFormController extends AbstractFormController {
    private String[] pages;
    private String pageAttribute;
    
    public DynamicMultiPageFormController() {
        // AbstractFormController sets default cache seconds to 0.
        super();

        // Always needs session to keep data from all pages.
        this.setSessionForm(true);

        // Never validate everything on binding -> validate individual pages.
        this.setValidateOnBinding(false);
    }
    
    /**
     * @return the pageAttribute
     */
    public final String getPageAttribute() {
        return this.pageAttribute;
    }
    /**
     * @param pageAttribute the pageAttribute to set
     */
    public void setPageAttribute(String pageAttribute) {
        this.pageAttribute = pageAttribute;
    }

    /**
     * @return the pages
     */
    public final String[] getPages() {
        return this.pages;
    }
    /**
     * @param pages the pages to set
     */
    @Required
    public void setPages(String[] pages) {
        this.pages = pages;
    }

    /**
     * Gets the current page number for the request.
     * 
     * @param request Current request.
     * @param command Current command object, binding for this request has already occured.
     * @param errors Current errors object, binding for this request has already occured.
     * @return The page number that the current request originates from.
     * @throws Exception
     */
    protected abstract int getCurrentPageNumber(HttpServletRequest request, Object command, Errors errors) throws Exception;
    
    /**
     * Gets the page number that should be displayed for the request.
     * 
     * @param request Current request.
     * @param command Current command object, binding for this request has already occured.
     * @param errors Current errors object, binding for this request has already occured.
     * @param page The current page number, as returned by {@link #getCurrentPageNumber(HttpServletRequest, Object, Errors)}
     * @return The page number that should be displayed for the request.
     * @throws Exception
     */
    protected abstract int getTargetPageNumber(HttpServletRequest request, Object command, Errors errors, int page) throws Exception;

    /**
     * @see org.springframework.web.servlet.mvc.AbstractFormController#processFormSubmission(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
     */
    @Override
    protected final ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        //get the current page number
        final int currentPage = this.getCurrentPageNumberInternal(request, command, errors);
        
        //Set page number as request attribute
        this.addPageNumberToRequest(request, currentPage, false);
        
        //validate page if not suppressed
        if (!this.suppressValidation(request)) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Validating wizard page " + currentPage + " for form bean '" + this.getCommandName() + "'");
            }
            this.validatePage(command, errors, currentPage);
        }
        
        //post process page
        this.postProcessPage(request, command, errors, currentPage);
        
        if (errors.hasErrors()) {
            return this.showPage(request, errors, currentPage);
        }
        else {
            //get target page number
            final int targetPage = this.getTargetPageNumber(request, command, errors, currentPage);
            
            return this.showPage(request, errors, targetPage);
        }
    }
    
    /**
     * Tries to get the current page number via {@link #getPageNumberFromRequest(HttpServletRequest)} if not found it goes to
     * {@link #getCurrentPageNumber(HttpServletRequest, Object, Errors)}.
     */
    protected final int getCurrentPageNumberInternal(HttpServletRequest request, Object command, Errors errors) throws Exception {
        final Integer currentPage = this.getPageNumberFromRequest(request);
        if (currentPage != null) {
            return currentPage;
        }
        
        return this.getCurrentPageNumber(request, command, errors);
    }

    /**
     * @see org.springframework.web.servlet.mvc.AbstractFormController#showForm(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.validation.BindException)
     */
    @Override
    protected final ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        //Call showPage with the inital page
        final Object command = errors.getTarget();
        final int initialPage = this.getInitialPage(request, command);
        return this.showPage(request, errors, initialPage);
    }
    
    protected final ModelAndView showPage(HttpServletRequest request, BindException errors, int page) throws Exception {
        //Check for valid page number
        final Object command = errors.getTarget();
        final int pageCount = this.getPageCount(request, command);
        if (page < 0 || page > pageCount) {
            throw new ServletException("Invalid wizard page number: " + page);
        }
        
        //Set page number as request attribute
        this.addPageNumberToRequest(request, page, true);

        //Add page number to model
        final Map<Object, Object> controlModel = this.createControlModel(page);

        //Get the viewName (based on the current page)
        final String viewName = this.getViewName(request, errors.getTarget(), page);
        
        return this.showForm(request, errors, viewName, controlModel);
    }
    
    /**
     * Calls page-specific referenceData method.
     */
    protected final Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        final int currentPage = this.getCurrentPageNumberInternal(request, command, errors);
        return this.referenceData(request, command, errors, currentPage);
    }

    /**
     * Create a reference data map for the given request, consisting of
     * bean name/bean instance pairs as expected by ModelAndView.
     * <p>Default implementation delegates to referenceData(HttpServletRequest, int).
     * Subclasses can override this to set reference data used in the view.
     * @param request current HTTP request
     * @param command form object with request parameters bound onto it
     * @param errors validation errors holder
     * @param page current wizard page
     * @return a Map with reference data entries, or <code>null</code> if none
     * @throws Exception in case of invalid state or arguments
     * @see #referenceData(HttpServletRequest, int)
     * @see ModelAndView
     */
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        return this.referenceData(request, page);
    }

    /**
     * Create a reference data map for the given request, consisting of
     * bean name/bean instance pairs as expected by ModelAndView.
     * <p>Default implementation returns null.
     * Subclasses can override this to set reference data used in the view.
     * @param request current HTTP request
     * @param page current wizard page
     * @return a Map with reference data entries, or <code>null</code> if none
     * @throws Exception in case of invalid state or arguments
     * @see ModelAndView
     */
    protected Map referenceData(HttpServletRequest request, int page) throws Exception {
        return null;
    }


    /**
     * Creates a base control model for the specified page, by default if a pageAttribute
     * is set the current page is added to the model using the pageAttribute.
     * 
     * @param page The page to create a control model for
     * @return A control model for the page, null is ok.
     */
    protected Map<Object, Object> createControlModel(int page) {
        final Map<Object, Object> controlModel = new HashMap<Object, Object>();
        if (this.pageAttribute != null) {
            controlModel.put(this.pageAttribute, new Integer(page));
        }
        return controlModel;
    }

    /**
     * Adds the page number as a request attribute using the key returned by 
     * {@link #getPageSessionAttributeName(HttpServletRequest)}. If this is
     * a session for the page number is also added to the session using the
     * same key.
     * 
     * @param request The request to set the page value on.
     * @param page The page value to set on the request and possibly session.
     * @param setInSession If true and is a session form the page will be stored in the session, if false and is a session form the page will be removed from the session.
     */
    protected final void addPageNumberToRequest(HttpServletRequest request, int page, boolean setInSession) {
        // Set page session attribute, expose overriding request attribute.
        final Integer pageInteger = new Integer(page);
        final String pageAttrName = this.getPageSessionAttributeName(request);
        
        if (this.isSessionForm()) {
            final HttpSession session = request.getSession();
            
            if (setInSession) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Setting page session attribute [" + pageAttrName + "] to: " + pageInteger);
                }
                
                session.setAttribute(pageAttrName, pageInteger);
            }
            else {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Removing page session attribute [" + pageAttrName + "]");
                }
                
                session.removeAttribute(pageAttrName);
            }
        }

        request.setAttribute(pageAttrName, pageInteger);
    }
    
    protected final Integer getPageNumberFromRequest(HttpServletRequest request) {
        // Check for overriding attribute in request.
        final String pageAttrName = this.getPageSessionAttributeName(request);
        Integer pageAttr = (Integer)request.getAttribute(pageAttrName);
        if (pageAttr != null) {
            return pageAttr;
        }

        // Check for explicit request parameter. ?

        // Check for original attribute in session.
        if (this.isSessionForm()) {
            final HttpSession session = request.getSession();
            pageAttr = (Integer)session.getAttribute(pageAttrName);

            if (pageAttr != null) {
                return pageAttr;
            }
        }

        return null;
    }
    
    
    /**
     * Return the initial page of the wizard, i.e. the page shown at wizard startup.
     * Default implementation delegates to <code>getInitialPage(HttpServletRequest)</code>.
     * @param request current HTTP request
     * @param command the command object as returned by formBackingObject
     * @return the initial page number
     * @see #getInitialPage(HttpServletRequest)
     * @see #formBackingObject
     */
    protected int getInitialPage(HttpServletRequest request, Object command) {
        return this.getInitialPage(request);
    }

    /**
     * Return the initial page of the wizard, i.e. the page shown at wizard startup.
     * Default implementation returns 0 for first page.
     * @param request current HTTP request
     * @return the initial page number
     */
    protected int getInitialPage(HttpServletRequest request) {
        return 0;
    }
    
    /**
     * Return the page count for this wizard form controller.
     * Default implementation delegates to <code>getPageCount()</code>.
     * <p>Can be overridden to dynamically adapt the page count.
     * @param request current HTTP request
     * @param command the command object as returned by formBackingObject
     * @return the current page count
     * @see #getPageCount
     */
    protected int getPageCount(HttpServletRequest request, Object command) {
        return this.getPageCount();
    }
    
    /**
     * Return the number of wizard pages.
     * Useful to check whether the last page has been reached.
     * <p>Note that a concrete wizard form controller might override
     * <code>getPageCount(HttpServletRequest, Object)</code> to determine
     * the page count dynamically. The default implementation of that extended
     * <code>getPageCount</code> variant returns the static page count as
     * determined by this <code>getPageCount()</code> method.
     * @see #getPageCount(javax.servlet.http.HttpServletRequest, Object)
     */
    protected final int getPageCount() {
        return this.pages.length;
    }
    
    /**
     * Return the name of the HttpSession attribute that holds the page object
     * for this wizard form controller.
     * <p>Default implementation delegates to the <code>getPageSessionAttributeName</code>
     * version without arguments.
     * @param request current HTTP request
     * @return the name of the form session attribute, or <code>null</code> if not in session form mode
     * @see #getPageSessionAttributeName
     * @see #getFormSessionAttributeName(javax.servlet.http.HttpServletRequest)
     * @see javax.servlet.http.HttpSession#getAttribute
     */
    protected String getPageSessionAttributeName(HttpServletRequest request) {
        return this.getPageSessionAttributeName();
    }

    /**
     * Return the name of the HttpSession attribute that holds the page object
     * for this wizard form controller.
     * <p>Default is an internal name, of no relevance to applications, as the form
     * session attribute is not usually accessed directly. Can be overridden to use
     * an application-specific attribute name, which allows other code to access
     * the session attribute directly.
     * @return the name of the page session attribute
     * @see #getFormSessionAttributeName
     * @see javax.servlet.http.HttpSession#getAttribute
     */
    protected String getPageSessionAttributeName() {
        return this.getClass().getName() + ".PAGE." + this.getCommandName();
    }
    
    /**
     * Return the name of the view for the specified page of this wizard form controller.
     * Default implementation takes the view name from the <code>getPages()</code> array.
     * <p>Can be overridden to dynamically switch the page view or to return view names
     * for dynamically defined pages.
     * @param request current HTTP request
     * @param command the command object as returned by formBackingObject
     * @return the current page count
     * @see #getPageCount
     */
    protected String getViewName(HttpServletRequest request, Object command, int page) {
        return this.pages[page];
    }

    /**
     * Template method for custom validation logic for individual pages.
     * Default implementation looks at all configured validators and for each validator
     * that implements the {@link PageValidator} interface the {@link PageValidator#validatePage(int, Object, Errors)}
     * method is called. 
     * 
     * <p>Implementations will typically call fine-granular validateXXX methods of this
     * instance's validator, combining them to validation of the corresponding pages.
     * The validator's default <code>validate</code> method will not be called by a
     * dynamic page form controller!
     * @param command form object with the current wizard state
     * @param errors validation errors holder
     * @param page number of page to validate
     * @see org.springframework.validation.Validator#validate
     */
    protected void validatePage(Object command, Errors errors, int page) {
        final Validator[] validators = this.getValidators();
        if (validators == null) {
            return;
        }
        
        for (final Validator validator : validators) {
            if (validator instanceof PageValidator) {
                ((PageValidator)validator).validatePage(page, command, errors);
            }
        }
    }

    /**
     * Post-process the given page after binding and validation, potentially
     * updating its command object. The passed-in request might contain special
     * parameters sent by the page.
     * <p>Only invoked when displaying another page or the same page again,
     * not when finishing or cancelling.
     * @param request current HTTP request
     * @param command form object with request parameters bound onto it
     * @param errors validation errors holder
     * @param page number of page to post-process
     * @throws Exception in case of invalid state or arguments
     */
    protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
    }
}
