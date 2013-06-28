/* Copyright 2006 The JA-SIG Collaborative.  All rights reserved.
*  See license distributed with this file and
*  available online at http://www.uportal.org/license.html
*/

package edu.wisc.my.stats.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.web.util.WebUtils;

import edu.wisc.my.stats.domain.ColumnInformation;
import edu.wisc.my.stats.domain.ExtraParameter;
import edu.wisc.my.stats.domain.Fact;
import edu.wisc.my.stats.domain.QueryInformation;
import edu.wisc.my.stats.domain.TimeResolution;
import edu.wisc.my.stats.query.ExtraParameterValuesProvider;
import edu.wisc.my.stats.query.FactProvider;
import edu.wisc.my.stats.query.QueryExecutionManager;
import edu.wisc.my.stats.query.QueryInformationProvider;
import edu.wisc.my.stats.util.Table;
import edu.wisc.my.stats.web.chart.StatsTableDatasetProducer;
import edu.wisc.my.stats.web.command.QueryCommand;
import edu.wisc.my.stats.web.command.QueryParameters;

/**
 * @author Eric Dalquist <a href="mailto:eric.dalquist@doit.wisc.edu">eric.dalquist@doit.wisc.edu</a>
 * @version $Revision: 1.1 $
 */
public class GraphingFormController extends DynamicMultiPageFormController {
    private FactProvider factProvider;
    private QueryInformationProvider queryInformationProvider;
    private ExtraParameterValuesProvider extraParameterValuesProvider;
    private QueryExecutionManager<Long, Fact, Double> queryExecutionManager;
    
    /**
     * @return the extraParameterValuesProvider
     */
    public ExtraParameterValuesProvider getExtraParameterValuesProvider() {
        return this.extraParameterValuesProvider;
    }
    /**
     * @param extraParameterValuesProvider the extraParameterValuesProvider to set
     */
    @Required
    public void setExtraParameterValuesProvider(ExtraParameterValuesProvider extraParameterValuesProvider) {
        this.extraParameterValuesProvider = extraParameterValuesProvider;
    }

    /**
     * @return the factProvider
     */
    public FactProvider getFactProvider() {
        return this.factProvider;
    }
    /**
     * @param factProvider the factProvider to set
     */
    @Required
    public void setFactProvider(FactProvider factProvider) {
        this.factProvider = factProvider;
    }

    /**
     * @return the queryExecutionManager
     */
    public QueryExecutionManager<Long, Fact, Double> getQueryExecutionManager() {
        return this.queryExecutionManager;
    }
    /**
     * @param queryExecutionManager the queryExecutionManager to set
     */
    @Required
    public void setQueryExecutionManager(QueryExecutionManager<Long, Fact, Double> queryExecutionManager) {
        this.queryExecutionManager = queryExecutionManager;
    }

    /**
     * @return the queryInformationProvider
     */
    public QueryInformationProvider getQueryInformationProvider() {
        return this.queryInformationProvider;
    }
    /**
     * @param queryInformationProvider the queryInformationProvider to set
     */
    @Required
    public void setQueryInformationProvider(QueryInformationProvider queryInformationProvider) {
        this.queryInformationProvider = queryInformationProvider;
    }
    
    /**
     * @see edu.wisc.my.stats.web.DynamicMultiPageFormController#getCurrentPageNumber(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors)
     */
    @Override
    protected int getCurrentPageNumber(HttpServletRequest request, Object command, Errors errors) throws Exception {
        int page = this.getInitialPage(request, command);
        
        final String[] values = request.getParameterValues("ISSUBMIT");
        if (values == null) {
            return page;
        }

        for (final String value : values) {
            try {
                page = Math.max(page, Integer.parseInt(value));
            }
            catch (NumberFormatException nfe) {
                //Ignore
            }
        }
        
        return page;
    }
    /**
     * @see edu.wisc.my.stats.web.DynamicMultiPageFormController#getTargetPageNumber(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
     */
    @Override
    protected int getTargetPageNumber(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        final QueryCommand queryCommand = (QueryCommand)command;
        
        final boolean hasRequiredExtraParameters = hasRequiredExtraParameters(queryCommand);
        if (!hasRequiredExtraParameters) {
            return 1;
        }
        else if (page < 2 && queryCommand.isHasAdvancedOptions()) {
            return 2;
        }
        else {
            return 3;
        }
    }
    
    //TODO move this into a validator & use the validator.validatePage2 in getTargetPageNumber to see if page2 should be returned
    protected boolean hasRequiredExtraParameters(final QueryCommand queryCommand) {
        final QueryParameters queryParameters = queryCommand.getQueryParameters();

        final Map<String, List<String>> extraParameterValues = queryCommand.getExtraParameterValues();

        final Set<Fact> queryFacts = queryParameters.getFacts();
        final Set<QueryInformation> queryInformationSet = this.queryInformationProvider.getQueryInformation(queryFacts);
        for (final QueryInformation queryInformation : queryInformationSet) {
            final Map<ExtraParameter, ColumnInformation> extraParametersMap = queryInformation.getExtraParameters();
            if (extraParametersMap != null) {
                final Set<ExtraParameter> extraParameters = extraParametersMap.keySet();
                for (final ExtraParameter extraParameter : extraParameters) {
                    if (!extraParameterValues.containsKey(extraParameter.getName())) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
    
    /**
     * @see org.springframework.web.servlet.mvc.AbstractFormController#isFormSubmission(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected boolean isFormSubmission(HttpServletRequest request) {
        return WebUtils.hasSubmitParameter(request, "ISSUBMIT");
    }
    /**
     * @see edu.wisc.my.stats.web.DynamicMultiPageFormController#referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
     */
    @Override
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        final QueryCommand queryCommand = (QueryCommand)command;
        final Map<Object, Object> model = new HashMap<Object, Object>();

        switch (page) {
            case 0: {
                final Set<Fact> facts = this.factProvider.getFacts();
                model.put("facts", facts);
                model.put("resolutions", TimeResolution.values());
            } break;
            case 1: {
                final Map<Object, Object> extraParametersMap = this.showExtraParameters(queryCommand, errors);
                model.putAll(extraParametersMap);
            } break;
            case 3: {
                //Execute the query
                final Table<Long, Fact, Double> results = this.queryExecutionManager.executeQuery(queryCommand);
                model.put("results", results);

                final StatsTableDatasetProducer datasetProducer = new StatsTableDatasetProducer(results);
                model.put("resultsProducer", datasetProducer);
            }
        }
        
        return model;
    }
    
    protected Map<Object, Object> showExtraParameters(QueryCommand queryCommand, Errors errors) {
        //Get the Set of QueryInformation objects for the Facts
        final QueryParameters queryParameters = queryCommand.getQueryParameters();
        final Set<Fact> facts = queryParameters.getFacts();
        final Set<QueryInformation> allQueryInformation = this.queryInformationProvider.getQueryInformation(facts);
        
        //The Map to store the ExtraParameters and their possible values that need to be prompted for
        final Map<ExtraParameter, List<String>> extraParametersData = new HashMap<ExtraParameter,List<String>>();
        
        //Populate the extraParametersData Map
        for (final QueryInformation queryInformation : allQueryInformation) {
            final Map<ExtraParameter, ColumnInformation> extraParametersMap = queryInformation.getExtraParameters();
            if (extraParametersMap != null) {
                final Set<ExtraParameter> extraParameters = extraParametersMap.keySet();
                for (final ExtraParameter extraParameter : extraParameters) {
                    if (!extraParametersData.containsKey(extraParameter)) {
                        final List<String> possibleValues = this.extraParameterValuesProvider.getPossibleValues(extraParameter);
                        extraParametersData.put(extraParameter, possibleValues);
                    }
                }
            }
        }
        
        final Map<Object, Object> model = new HashMap<Object, Object>();
        model.put("extraParametersData", extraParametersData);
        
        return model;
    }
}
