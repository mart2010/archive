<%@ include file="common/taglibs-directive.jsp" %>

<html>
<head>
<%@ include file="common/head-meta.jsp"%>
</head>


<body>
<%@ include file="common/top-sidebar.jsp" %>

<div id="main-copy">
      <h1><fmt:message key="jspHome.welcome" /></h1>

      <p><fmt:message key="jspHome.connInfo" /></p>

	
<table class="edit">
	
	<tr><th><fmt:message key="user.firstName" />:</th>
	    <td><c:out value="${currentUser.firstName}" /></td>
	</tr>
	<tr><th><fmt:message key="user.lastName" />:</th>
	    <td><c:out value="${currentUser.lastName}" /></td>
	</tr>
	<tr><th><fmt:message key="user.email" />:</th>
	    <td><c:out value="${currentUser.email}" /></td>
	<tr><th><fmt:message key="user.role" />:</th>
	    <td><c:out value="${currentUser.roles}" /></td>
	<tr><th><fmt:message key="user.lab" />:</th>
	    <td><c:out value="${currentUser.labs}" /></td>
	<tr><th><fmt:message key="user.createdDate" />:</th>
	    <td><fmt:formatDate value="${currentUser.createdDate}" pattern="${defaultDatePattern}" /></td>
	<tr><th><fmt:message key="user.updatedDate" />:</th>
	    <td><fmt:formatDate value="${currentUser.updatedDate}" pattern="${defaultDatePattern}" /></td>
	</tr>
</table>

</div>


<%@ include file="common/footer.jsp"%>
</body>
</html>
