<%@ include file="IncludeTop.jsp" %>

<c:if test="${!empty itemList.pageList}">
<center>
<b><fmt:message key="jspSearch.searchResult" >
     <fmt:param value="${itemList.nrOfElements}" />
   </fmt:message></b>
</center>
</c:if>


<table align="center" border="1" cellspacing="4" cellpadding="3">
  <tr>  <td><b><fmt:message key="subCategory.name" /></b></td>
        <td><b><fmt:message key="item.name" /></b></td>
        <td><b><fmt:message key="item.unitPrice" /></b></td>  <td>&nbsp;</td>  </tr>

  <c:if test="${empty itemList.pageList}">
  <tr><td colspan="4"><b><fmt:message key="jspSearch.searchNoResult" /></b></td></tr>
  </c:if>

<c:forEach var="item" items="${itemList.pageList}">
  <tr>
    <td><c:out value="${item.subCategoryId}"/></td>
    <td><c:out value="${item.name}"/></td>
    <td><fmt:formatNumber value="${item.unitPrice}" pattern="$#,##0.00"/></td>
    <td><a href="<c:url value="/shop/addItemToCart.do"><c:param name="workingItemId" value="${item.itemId}"/></c:url>">
     <img border="0" src="../images/button_add_to_cart.gif"/>
   </a></td>
   </tr>
</c:forEach>

  <tr>
  <td>
  <c:if test="${!itemList.firstPage}">
    <a href="?page=previous"><img border="0" src="../images/button_previous.gif"/>
    </a>
  </c:if>
  <c:if test="${!itemList.lastPage}">
    <a href="?page=next"><img border="0" src="../images/button_next.gif"/></a>
  </c:if>
  </td>
  </tr>

</table>