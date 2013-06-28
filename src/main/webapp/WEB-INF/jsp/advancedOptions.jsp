<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>
<p>
    Graph Options
</p>
<br/>
<div>
    <form:form id="advancedOptions" method="GET">
        <input type="hidden" name="ISSUBMIT" value="2"/>
        
        <table>
            <tr>
                <td align="right" valign="top">
                    Width (px):
                </td>
                <td align="left" valign="top">
                    <form:input path="advancedOptions.width"/>
                </td>
                <td align="left" valign="top"><form:errors path="advancedOptions.width*" cssStyle="color: red;"/></td>
            </tr>
            <tr>
                <td align="right" valign="top">
                    Width (px):
                </td>
                <td align="left" valign="top">
                    <form:input path="advancedOptions.height"/>
                </td>
                <td align="left" valign="top"><form:errors path="advancedOptions.height*" cssStyle="color: red;"/></td>
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