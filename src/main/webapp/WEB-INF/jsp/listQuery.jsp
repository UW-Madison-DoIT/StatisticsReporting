<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<a href="graph.html">New Query</a>
<table border="1">
    <tr>
        <th/>
        <c:forEach var="fact" items="${results.columnKeys}">
            <th>${fact.name}</th>
        </c:forEach>
    </tr>
    <c:forEach var="timestamp" items="${results.rowKeys}">
        <tr>
            <th>${upsfn:toDate(timestamp)}</th>
            <c:forEach var="tableEntry" items="${upsfn:rowEntries(results, timestamp)}">
                <td>${tableEntry.value}</td>
            </c:forEach>
        </tr>
    </c:forEach>
</table>
