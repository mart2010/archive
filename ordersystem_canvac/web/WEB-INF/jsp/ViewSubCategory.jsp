<%@ include file="IncludeTop.jsp" %>


<table align="center" border="1" cellspacing="4" cellpadding="3">
  <tr>
    <td align="center" colspan="3"><h2><c:out value="${categoryName}"/></h2></td>
  </tr>
  <tr><td>&nbsp;</td>
      <td><b><fmt:message key="subCategory.name" /></b></td>
      <td><b><fmt:message key="subCategory.desc" /></b></td>
  </tr>
<c:forEach items="${subCategoryList}" var="subCategory">
  <tr>
    <td><a href="<c:url value="/shop/viewItem.do"><c:param name="subCategoryId" value="${subCategory.subCategoryId}"/></c:url>">
	    <font color="blue"><c:out value="${subCategory.subCategoryId}"/></font>
	    </a></td>
    <td><c:out value="${subCategory.name}"/></td>
    <td><c:out value="${subCategory.description}"/></td>
  </tr>
</c:forEach>
</table>

