<%@ include file="common/taglibs-directive.jsp" %>

<html>
<head>
<%@ include file="common/head-meta.jsp"%>
</head>


<body>
<%@ include file="common/top-sidebar.jsp" %>


<div id="main-copy">
    <h1><fmt:message key="jspUser.list"/></h1>
</br>
<!-- should use the dynamic pageSize property from the AppConfig bean -->

<table class="list">

  <tr>
    <th><fmt:message key="user.username" /></th>
    <th><fmt:message key="user.firstName" /></th>
    <th><fmt:message key="user.lastName" /></th>
    <th><fmt:message key="user.email" /></th>
    <th><fmt:message key="user.phoneNumber" /></th>
    <th><fmt:message key="user.createdDate" /></th>
    <th><fmt:message key="user.updatedDate" /></th>
  </tr>

<c:forEach var="user" items="${userList}">
  <tr>  
    <td><a href="<c:url value="/editUser.html"><c:param name="username" value="${user.name}"/></c:url>">
	    <c:out value="${user.name}"/></a></td>
    <td><c:out value="${user.firstName}"/></td>
    <td><c:out value="${user.lastName}"/></td>
    <td><c:out value="${user.email}"/></td>
    <td><c:out value="${user.phoneNumber}"/></td>
    <td><fmt:formatDate value="${currentUser.createdDate}" pattern="${defaultDatePattern}" /></td>
    <td><fmt:formatDate value="${currentUser.updatedDate}" pattern="${defaultDatePattern}" /></td>
  </tr>
</c:forEach>

</table>

    <button type="button" onclick="location.href='<html:rewrite forward="home" />'">
        <fmt:message key="button.cancel"/>
    </button>
&nbsp;
    <button type="button" style="margin-right: 5px"
        onclick="location.href='<html:rewrite forward="addUser"/>'">
        <fmt:message key="button.add"/>
    </button>
    


            
</div>


<%@ include file="common/footer.jsp"%>

</body>
</html>
