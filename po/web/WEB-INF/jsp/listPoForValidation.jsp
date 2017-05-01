<%@ include file="common/taglibs-directive.jsp" %>

<html>
<head>
<%@ include file="common/head-meta.jsp"%>
</head>


<body>
<%@ include file="common/top-sidebar.jsp" %>


<div id="main-copy">
    <h1><fmt:message key="jspPo.listProposed"/></h1>
<br></br>

<!-- should use Frame to limit the size of the table of 'Proposed' POs -->
<html:form method="get" action="/admin/validatePo">
	<input type="hidden" name="refresh" />
	<script>
	     function set(target) {
         document.forms[0].refresh.value=target;
	     }
	</script>

<table class="list">

  <tr>
    <th><fmt:message key="po.poId" /></th>
    <th><fmt:message key="po.orderBy" /></th>
    <th><fmt:message key="po.lab" /></th>
    <th><fmt:message key="po.project" /></th>
    <th><fmt:message key="po.orderFor" /></th>
    <th><fmt:message key="po.currentStatusChange" /></th>
    <th><fmt:message key="po.currentStatusChangeDate" /></th>
    <th><fmt:message key="po.estPrice" /></th>
    <th><fmt:message key="po.lineItems" /></th>
    <th><fmt:message key="po.selectValidation" /></th>
  </tr>

<c:forEach var="po" items="${proposedMapPos}">
  <tr>  
    <td><a href="<c:url value="/admin/goToEditProposedPO.html"><c:param name="poId" value="${po.key}"/></c:url>">
	    <c:out value="${po.key}"/></a></td>
    <td><c:out value="${po.value.orderByUser.name}"/></td>
    <td><c:out value="${po.value.lab.name}"/></td>
    <td><c:out value="${po.value.project}"/></td>
    <td><c:out value="${po.value.orderFor}"/></td>
    <td><c:out value="${po.value.currentStatusChange}"/></td>
    <td><fmt:formatDate value="${po.value.currentStatusChangeDate}" pattern="${defaultDatePattern}" /></td>
    <td><c:out value="${po.value.estCalculatedCADTotal}"/></td>
    <td><c:out value="${po.value.poLineItems}"/></td>
    <td><html:multibox property="selPoIds" value="${po.key}"/></td>
  </tr>
</c:forEach>
</table>

  <html:submit onclick="set('true');">
   		<fmt:message key="button.refresh"/>
  </html:submit>
&nbsp;&nbsp;
  <html:cancel>
            <fmt:message key="button.cancel"/>
  </html:cancel>
&nbsp;
  <html:submit>
          	<fmt:message key="button.validate"/>
  </html:submit>

<p></p>
  <b><fmt:message key="jspPo.estAmountPos" />= <c:out value="${estTotalAmount}"/></b>

</html:form>
            
</div>


<%@ include file="common/footer.jsp"%>

</body>
</html>
