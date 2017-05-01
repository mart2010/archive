<%@ include file="IncludeTop.jsp" %>


<table align="center" border="1" cellspacing="4" cellpadding="3">
  <tr>
    <td align="center" colspan="2"><h2><fmt:message key="category.all" /></h2></td>
  </tr>
  <tr>
    <td>&nbsp;</td> <td><b><fmt:message key="category.name" /></b></td>
  </tr>
  <!-- here we iterate over the Category List... -->
  <c:forEach  items="${allCategoryList}" var="category">
  <tr>
    <td><b><a href="<c:url value="/shop/viewSubCategory.do"><c:param name="categoryId" value="${category.categoryId}"/></c:url>">
            <font color="blue"><c:out value="${category.description}"/></font>
            </a></b></td>
    <td><c:out value="${category.name}"/></td>
  </tr>
  </c:forEach>
</table>
