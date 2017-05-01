<%@ include file="common/taglibs-directive.jsp" %>

<html>
<head>
<%@ include file="common/head-meta.jsp"%>
<script language="JavaScript" src="<c:url value="/public/scripts/CalendarPopup.js"/>"></script>
</head>


<body>
<%@ include file="common/top-sidebar.jsp" %>

<div id="main-copy">
    <h1><fmt:message key="jspPo.listValOrdered"/></h1>

<html:form method="post" action="/admin/saveFollowUpPo">
<html:errors property="effPrice"/>
<html:errors property="estDate"/>
<table class="list">
  <tr>
    <th><fmt:message key="po.poId" /></th>
	<th><fmt:message key="po.orderBy" /></th>
    <th><fmt:message key="po.lab" /></th>
    <th><fmt:message key="po.lineItems" /></th>
    <th><fmt:message key="po.confirmNumber" /></th>
    <th><fmt:message key="po.paymentType" /></th>
    <th><fmt:message key="po.status" /></th>
    <th><fmt:message key="po.estPrice" /></th>
    <th><fmt:message key="po.estRecepDate" /><c:out value=" (${defaultDatePattern})"/> </th>
    <th><fmt:message key="po.revEffCADTotal" /></th>
    <th><fmt:message key="po.revEffCADTax" /></th>
	<th><fmt:message key="po.selectFollowUp" /></th>
  </tr>
  <c:choose>
  	<c:when test="${empty validatedMapPos}">
	  <tr><td colspan="10"  style="font-size: 150%"><fmt:message key="jspPo.noValidatedList"/></td></tr>
  	</c:when>
  	<c:otherwise>
	  <tr><td colspan="10"  style="font-size: 150%"><fmt:message key="jspPo.validatedList"/></td></tr>
	</c:otherwise>
  </c:choose>
<c:forEach var="po" items="${validatedMapPos}">
  <tr>
    <td><c:out value="${po.key}"/></td>
	<td><c:out value="${po.value.orderByUser.name}"/></td>
    <td><c:out value="${po.value.lab.name}"/></td>
    <td><c:out value="${po.value.poLineItems}"/></td>
	<td><html:text property="confirmNb(${po.key})" size="10"/></td>
    <td><html:select property="payType(${po.key})"> 
		<html:options collection="paymentTypes" property="name" labelProperty="name"/></html:select></td>
    <td><html:select property="statusCode(${po.key})"> 
		<html:options collection="statusForValidated" property="code" labelProperty="code"/></html:select></td>
    <td><c:out value="${po.value.estCalculatedCADTotalPersisted}"/></td>
	<td><html:text size="8" property="estimateReceptionDate(${po.key})"/></td>
	<td><html:text property="revisedCADTotal(${po.key})" size="5"/></td>
	<td><html:text property="revisedCADTax(${po.key})" size="4"/></td>
	<td><html:multibox property="selPoIds" value="${po.key}"/></td>
  </tr>
</c:forEach>
  <c:choose>
  	<c:when test="${empty orderedMapPos}">
	  <tr><td colspan="10" style="font-size: 150%"><fmt:message key="jspPo.noOrderedList"/></td></tr>
  	</c:when>
  	<c:otherwise>
	  <tr><td colspan="10" style="font-size: 150%"><fmt:message key="jspPo.orderedList"/></td></tr>
	</c:otherwise>
  </c:choose>
 
<c:forEach var="po" items="${orderedMapPos}">
  <tr>
    <td><c:out value="${po.key}"/></td>
	<td><c:out value="${po.value.orderByUser.name}"/></td>
    <td><c:out value="${po.value.lab.name}"/></td>
    <td><c:out value="${po.value.poLineItems}"/></td>
	<td><html:text property="confirmNb(${po.key})" size="10"/></td>
    <td><html:select property="payType(${po.key})"> 
		<html:options collection="paymentTypes" property="name" labelProperty="name"/></html:select></td>
    <td><html:select property="statusCode(${po.key})"> 
		<html:options collection="statusForOrdered" property="code" labelProperty="code"/></html:select></td>
    <td><c:out value="${po.value.estCalculatedCADTotalPersisted}"/></td>
	
	<td><html:text size="8" property="estimateReceptionDate(${po.key})"/></td>
	<td><html:text property="revisedCADTotal(${po.key})" size="5"/></td>
	<td><html:text property="revisedCADTax(${po.key})" size="4"/></td>
	<td><html:multibox property="selPoIds" value="${po.key}"/></td>
  </tr>
</c:forEach>


</table>
&nbsp;&nbsp;
  <html:cancel>
            <fmt:message key="button.cancel"/>
  </html:cancel>
&nbsp;
  <html:submit>
          	<fmt:message key="button.update"/>
  </html:submit>

</html:form>
            
</div>


<%@ include file="common/footer.jsp"%>

</body>
</html>
