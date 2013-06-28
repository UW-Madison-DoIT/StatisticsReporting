<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<%@ include file="/WEB-INF/jsp/includes/header.jsp" %>
<a href="graph.html">New Query</a>

<cewolf:overlaidchart
    id="portalStatsGraph"
    title="Login Statistics"
    type="overlaidxy"
    xaxistype="date"
    yaxistype="number"
    xaxislabel="Time"
    yaxislabel="Requests">
    <cewolf:plot type="xyline">
        <cewolf:data>
            <cewolf:producer id="resultsProducer"/>
        </cewolf:data>
    </cewolf:plot>
</cewolf:overlaidchart> 
<p>
    <cewolf:img chartid="portalStatsGraph" renderer="cewolf" width="1024" height="768">
        <cewolf:map tooltipgeneratorid="resultsProducer"/>
    </cewolf:img>
</p>
<%@ include file="/WEB-INF/jsp/listQuery.jsp" %>
        
<%@ include file="/WEB-INF/jsp/includes/footer.jsp" %>