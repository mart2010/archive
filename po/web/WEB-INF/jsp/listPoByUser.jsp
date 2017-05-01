<%@ include file="common/taglibs-directive.jsp" %>

<html>
<head>
<%@ include file="common/head-meta.jsp"%>
</head>


<body>
<%@ include file="common/top-sidebar.jsp" %>


<div id="main-copy">
    <h1><fmt:message key="jspPo.myList"/></h1>
<br></br>

<!-- should use the dynamic pageSize property from the AppConfig bean -->

<table class="list">
  <tr>
    <th><fmt:message key="po.poId" /></th>
    <th><fmt:message key="po.project" /></th>
    <th><fmt:message key="po.orderFor" /></th>
    <th><fmt:message key="po.lab" /></th>
    <th><fmt:message key="po.estRecepDate" /></th>
    <th><fmt:message key="po.currentStatusChange" /></th>
    <th><fmt:message key="po.currentStatusChangeDate" /></th>
  </tr>

<c:forEach var="po" items="${poList}">
  <tr>  
    <c:choose>
    <c:when test="${!po.editableByUser}"> 
	<td><c:out value="${po.poId}"/></td>
    </c:when>
    <c:otherwise>
    <td><a href="<c:url value="/editUserPo.html"><c:param name="poId" value="${po.poId}"/></c:url>">
	    <c:out value="${po.poId}"/></a></td>
    </c:otherwise>
    </c:choose>
    <td><c:out value="${po.project}"/></td>
    <td><c:out value="${po.orderFor}"/></td>
    <td><c:out value="${po.lab.name}"/></td>
    <td><fmt:formatDate value="${po.estimateReceptionDate}" pattern="${defaultDatePattern}" /></td>
    <td><c:out value="${po.currentStatusChange}"/></td>
    <td><fmt:formatDate value="${po.currentStatusChangeDate}" pattern="${defaultDatePattern}" /></td>
  </tr>
</c:forEach>
</table>

<button type="button" onclick="location.href='<html:rewrite forward="home" />'">
      	<fmt:message key="button.cancel"/>
</button>
           
</div>


<%@ include file="common/footer.jsp"%>

</body>
</html>
