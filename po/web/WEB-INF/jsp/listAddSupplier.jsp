<%@ include file="common/taglibs-directive.jsp" %>

<html>
<head>
<%@ include file="common/head-meta.jsp"%>
</head>


<body>
<%@ include file="common/top-sidebar.jsp" %>

<div id="main-copy">
      <h1><fmt:message key="jspSupplier.info" /></h1>
	  <p></p>


<html:form method="post" action="/addSupplier">
	
<table class="edit">
	<tr><th><i><fmt:message key="jspSupplier.edit" />:</i></th> 
	<tr><th><fmt:message key="supplier.name" />:</th>
	    <td><html:text property="name" size="25"/>
			<html:errors property="name"/></td>
	</tr>
	<tr><th><fmt:message key="supplier.salesRepName" />:</th>
	    <td><html:text property="salesRepName" size="20"/>
			<html:errors property="salesRepName"/></td>
	</tr>
	<tr><th><fmt:message key="supplier.salesRepContact" />:</th>
	    <td><html:text property="salesRepContact" size="35"/>
			<html:errors property="salesRepContact"/></td>
	</tr>
</table>
&nbsp;
<html:cancel>
	<fmt:message key="button.cancel"/>
</html:cancel>
&nbsp;&nbsp;
<html:submit>
	<fmt:message key="button.addSupplier"/>
</html:submit>
</html:form>

<br></br>

<h1><fmt:message key="jspSupplier.list" /></h1>
<br></br>
<table class="list">
  <tr>
    <th><fmt:message key="supplier.name" /></th>
    <th><fmt:message key="supplier.salesRepName" /></th>
    <th><fmt:message key="supplier.salesRepContact" /></th>
    <th></th>
  </tr>

<c:forEach var="supplier" items="${suppliers}">
  <tr>  
    <td><c:out value="${supplier.name}"/></td>
    <td><c:out value="${supplier.salesRepName}"/></td>
    <td><c:out value="${supplier.salesRepContact}"/></td>

	<authz:authorize ifAllGranted="PO_Admin"> 
      <td><a href="<c:url value="/deleteSupplier.html"><c:param name="supId" value="${supplier.id}"/></c:url>">
	      <fmt:message key="jspSupplier.delete" /></a></td>
	</authz:authorize>

  </tr>
</c:forEach>
</table>

</div>

<%@ include file="common/footer.jsp"%>

</body>
</html>


