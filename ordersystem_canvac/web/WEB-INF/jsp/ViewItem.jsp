<%@ include file="IncludeTop.jsp" %>

<table align="center" border="1" cellspacing="4" cellpadding="3">

<!-- The Peptide Item List View -->
<c:if test="${subCategory.categoryId=='Peptide-HIV' || subCategory.categoryId=='Peptide-HCV'}">
  <tr>
    <td align="center" colspan="5"><h2><c:out value="${subCategory.categoryId}: ${subCategory.subCategoryId}"/></h2></td>
  </tr>
  <tr><td><b><fmt:message key="item.peptidecode" /></b></td>
      <td><b><fmt:message key="item.sequence" /></b></td>
      <td><b><fmt:message key="item.unitPrice" /></b></td>
      <td><b><fmt:message key="item.unitSize" /></b></td>
      <td>&nbsp;</td>
  </tr>
  <c:forEach var="item" items="${itemList.pageList}">
   <tr>
   <td><c:out value="${item.name}"/></td>
   <td><c:out value="${item.charAttribute2}"/></td>
   <td><fmt:formatNumber value="${item.unitPrice}" pattern="$#,##0.00"/></td>
   <td><c:out value="${item.unit}"/></td>
   <td><a href="<c:url value="/shop/addItemToCart.do"><c:param name="workingItemId" value="${item.itemId}"/></c:url>">
     <img border="0" src="../images/button_add_to_cart.gif"/>
   </a></td>
   </tr>
  </c:forEach>
</c:if>

<!-- The Tetramer Item List View -->
<c:if test="${subCategory.categoryId=='Tetramer'}">
  <tr>
    <td align="center" colspan="7"><h2><c:out value="${subCategory.categoryId}: ${subCategory.subCategoryId}"/></h2></td>
  </tr>
  <tr>
    <td><b><fmt:message key="item.tetName" /></b></td>
    <td><b><fmt:message key="item.tetHla" /></b></td>
    <td><b><fmt:message key="item.tetProt" /></b></td>
    <td><b><fmt:message key="item.tetSeq" /></b></td>
    <td><b><fmt:message key="item.unitPrice" /></b></td>
    <td><b><fmt:message key="item.unitSize" /></b></td>
    <td>&nbsp;</td>
  </tr>
  <c:forEach var="item" items="${itemList.pageList}">
   <tr>
   <td><c:out value="${item.name}"/></td>
   <td><c:out value="${item.charAttribute1}"/></td>
   <td><c:out value="${item.charAttribute4}"/></td>
   <td><c:out value="${item.charAttribute2}"/></td>
   <td><fmt:formatNumber value="${item.unitPrice}" pattern="$#,##0.00"/></td>
   <td><c:out value="${item.unit}"/></td>
   <td><a href="<c:url value="/shop/addItemToCart.do"><c:param name="workingItemId" value="${item.itemId}"/></c:url>">
     <img border="0" src="../images/button_add_to_cart.gif"/>
   </a></td>
   </tr>
  </c:forEach>
</c:if>

  <tr><td>
  <c:if test="${!itemList.firstPage}">
    <a href="?page=previous">
        <img border="0" src="../images/button_previous.gif"/></a>
  </c:if>
  <c:if test="${!itemList.lastPage}">
    <a href="?page=next">
        <img border="0" src="../images/button_next.gif"/></a>
  </c:if>
  </td></tr>
</table>
