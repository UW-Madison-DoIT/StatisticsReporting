<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>
<p>
    Extra information is needed for some of the Facts you selected. Please provide the
    information below.
</p>
<div>
    <form:form id="extraParameters" method="GET">
        <input type="hidden" name="ISSUBMIT" value="1"/>
        
        <table>
            <c:forEach var="extraParamEntry" items="${extraParametersData}">
                <tr>
                    <td valign="top" align="right">
                        ${extraParamEntry.key.name}:
                    </span>
                    <td valign="top">                        
                        <c:choose>
                            <c:when test="${empty extraParamEntry.value}">
                                <form:input path="extraParameterValues['${extraParamEntry.key.name}']"/>
                            </c:when>
                            <c:otherwise>
                                <form:select path="extraParameterValues['${extraParamEntry.key.name}']" 
                                    items="${extraParamEntry.value}" 
                                    multiple="${extraParamEntry.key.multivalued}"
                                    size="5"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
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