<%@ include file="common/taglibs-directive.jsp" %>

<html>
<head>
<%@ include file="common/head-meta.jsp"%>
<script language="JavaScript" src="<c:url value="/public/scripts/CalendarPopup.js"/>"></script>

</head>
<body>
<%@ include file="common/top-sidebar.jsp" %>

<div id="main-copy">

<h1><fmt:message key="jspSearch.criteria"/></h1>
<br></br>

<html:form method="post" action="/admin/listSearch">
<table class="edit">
<tr><td><fmt:message key="jspSearch.lab" />:</tdh>
	<td><html:select property="labId">
			<html:option value="-1"><fmt:message key="jspSearch.allLab"/></html:option>
			<html:options collection="labs" property="id" labelProperty="name"/>
		</html:select></td>
	<td><fmt:message key="jspSearch.fromDate" />:</td>
	<td>
	<script language="JavaScript" type="text/javascript" id="jscal1x">
      var cal1x = new CalendarPopup();
      cal1x.showNavigationDropdowns( );
      cal1x.setYearSelectStartOffset(60);
    </script>
    <html:text size="8" property="receivedDateFrom"/>
    <a href="" onClick="cal1x.select(document.forms[0].receivedDateFrom,
     'anchor1x','${defaultDatePattern}'); return false;" TITLE="select date" 
     NAME="anchor1x" ID="anchor1x">select</a>
	<html:errors property="receivedDateFrom"/>
	</td>
</tr>
<tr>
	<td><fmt:message key="jspSearch.user" />:</td>
	<td><html:select property="userId">
			<html:option value="-1"><fmt:message key="jspSearch.allUser"/></html:option>
			<html:options collection="users" property="id" labelProperty="name"/>
		</html:select></td>

	<td><fmt:message key="jspSearch.toDate" />:</td>
	<td> 
	<script language="JavaScript" type="text/javascript" id="jscal2x">
      var cal2x = new CalendarPopup();
      cal2x.showNavigationDropdowns( );
      cal2x.setYearSelectStartOffset(60);
    </script>
    <html:text size="8" property="receivedDateTo"/>
    <a href="" onClick="cal2x.select(document.forms[0].receivedDateTo,
     'anchor2x','${defaultDatePattern}'); return false;" TITLE="select date" 
     NAME="anchor2x" ID="anchor2x">select</a>
	<html:errors property="receivedDateTo"/>
	</td>
	
</tr>
</table>
<br></br>
&nbsp;
<html:cancel><fmt:message key="button.cancel"/></html:cancel>
&nbsp;&nbsp;
<html:submit><fmt:message key="button.search"/></html:submit>
</html:form>


<c:if test="${not empty count}">
<h1><fmt:message key="jspSearch.list"/></h1>
<br></br>
<fmt:message key="jspSearch.count">
 <fmt:param value="${count}"/>
</fmt:message>
<table class="list">
  <tr>
    <th><fmt:message key="po.poId" /></th>
	<th><fmt:message key="po.orderBy" /></th>
    <th><fmt:message key="po.project" /></th>
    <th><fmt:message key="po.orderFor" /></th>
    <th><fmt:message key="po.lab" /></th>
 <%--     <th><fmt:message key="po.lineItems" /></th> --%>
    <th><fmt:message key="po.confirmNumber" /></th>
    <th><fmt:message key="po.paymentType" /></th>
    <th><fmt:message key="po.currentStatusChange" /></th>
    <th><fmt:message key="po.currentStatusChangeDate" /></th>
    <th><fmt:message key="po.revEffCADTotal" /></th>
    <th><fmt:message key="po.revEffCADTax" /></th>
  </tr>

<c:forEach var="po" items="${pos}">
  <tr>  
    <td><c:out value="${po.poId}"/></td>
    <td><c:out value="${po.orderByUser.name}"/></td>
    <td><c:out value="${po.project}"/></td>
    <td><c:out value="${po.orderFor}"/></td>
    <td><c:out value="${po.lab.name}"/></td>
 <%-- comment out because we don't eagerly fetch lineItems...  <td><c:out value="${po.poLineItems}"/></td>  --%>
    <td><c:out value="${po.confirmNumber}"/></td>
    <td><c:out value="${po.paymentType}"/></td>    
    <td><c:out value="${po.currentStatusChange}"/></td>
    <td><fmt:formatDate value="${po.currentStatusChangeDate}" pattern="${defaultDatePattern}" /></td>
    <td><c:out value="${po.revisedEffectiveCADTotal}"/></td>
    <td><c:out value="${po.revisedEffectiveCADTax}"/></td>
  </tr>
</c:forEach>
</table>
</c:if>
      
           
</div>

<%@ include file="common/footer.jsp"%>

</body>
</html>
