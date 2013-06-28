<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>
<p>
    For a Graph select the data to graph from the Reports box and the Resolution at which to
    graph the data.
</p>    
<p>
    For a summary report just leave the Resolution box blank.
</p>
<br/>
<div>
    <form:form id="queryParameters" method="GET">
        <input type="hidden" name="ISSUBMIT" value="0"/>
        
        <table>
            <tr>
                <td align="right" valign="top">
                    Reports:
                </td>
                <td align="left" valign="top">
                    <form:select path="queryParameters.facts" items="${facts}" itemValue="id" itemLabel="name" multiple="true" size="5"/>
                    
                </td>
                <td align="left" valign="top"><form:errors path="queryParameters.facts*" cssStyle="color: red;"/></td>
            </tr>
            <tr>
                <td align="right" valign="top">
                    Resolution:
                </td>
                <td align="left" valign="top">
                    <form:select path="queryParameters.resolution">
                        <form:options items="${resolutions}" itemValue="name" itemLabel="code"/>
                    </form:select>
                </td>
                <td align="left" valign="top"><form:errors path="queryParameters.resolution*" cssStyle="color: red;"/></td>
            </tr>
            <tr>
                <td align="right">
                    Start Date:
                </td>
                <td align="left" valign="top">
                    <form:input path="queryParameters.start"/>
                    <img src="img/calendar.gif" id="start_trigger"/>
                    <script type="text/javascript">
                        
                        function updateCals(sourceCal) {
                            var startDateField = document.getElementById("queryParameters.start");
                            var endDateField = document.getElementById("queryParameters.end");
                            
                            var sourceField;
                            var targetField;
                            
                            if (sourceCal.params.inputField == startDateField) {
                                sourceField = startDateField;
                                targetField = endDateField;
                            }
                            else {
                                sourceField = endDateField;
                                targetField = startDateField;
                            }
                            
                            if (targetField.value == null || targetField.value.length == 0) {
                                targetField.value = sourceField.value;
                            }
                            else {
                                var sourceDate = Date.parseDate(sourceField.value, "%Y/%m/%d");
                                var targetDate = Date.parseDate(targetField.value, "%Y/%m/%d");
                                
                                if (sourceField.name == "queryParameters.start" && sourceDate > targetDate) {
                                    targetField.value = sourceField.value;
                                }
                                else if (sourceField.name == "queryParameters.end" && targetDate > sourceDate) {
                                    targetField.value = sourceField.value;
                                }
                            }
                        }
                        
                        Calendar.setup({
                            inputField     :    "queryParameters.start",     // id of the input field
                            ifFormat       :    "%Y-%m-%d",      // format of the input field
                            button         :    "start_trigger",  // trigger for the calendar (button ID)
                            align          :    "bR",           // alignment (defaults to "Bl")
                            singleClick    :    true,
                            weekNumbers    :    false,
                            onUpdate       :    updateCals
                        });
                    </script>
                </td>
                <td align="left" valign="top"><form:errors path="queryParameters.start*" cssStyle="color: red;"/></td>
            </tr>
            <tr>
                <td align="right">
                    End Date:
                </td>
                <td align="left" valign="top">
                    <form:input path="queryParameters.end"/>
                    <img src="img/calendar.gif" id="end_trigger"/>
                    <script type="text/javascript">
                        Calendar.setup({
                            inputField     :    "queryParameters.end",     // id of the input field
                            ifFormat       :    "%Y-%m-%d",      // format of the input field
                            button         :    "end_trigger",  // trigger for the calendar (button ID)
                            align          :    "bR",           // alignment (defaults to "Bl")
                            singleClick    :    true,
                            weekNumbers    :    false,
                            onUpdate       :    updateCals
                        });
                    </script>
                </td>
                <td align="left" valign="top"><form:errors path="queryParameters.end*" cssStyle="color: red;"/></td>
            </tr>
            <tr>
                <td colspan="3">
                    <form:checkbox id="hasAdvancedOptions" path="hasAdvancedOptions" cssClass="portlet-form-field"/>
                    <label class="portlet-form-field-label" for="hasAdvancedOptions">View Advanced Options before graphing.</label>
                </td>
            </tr>
            <tr>
                <td colspan="3">
                    <input value="Clear Form" type="reset"/>
                    <input value="Graph" type="submit"/>
                </td>
            </tr>
        </table>
    </form:form>
</div>
<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>