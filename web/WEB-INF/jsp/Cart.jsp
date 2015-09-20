<%@ include file="IncludeTop.jsp" %>

<table align=center border="0" cellspacing="0" width="70%">
	</td>
		<td valign="top" align="center">
			<h2 align="center"><fmt:message key="jspCart.shoppingCart" /></h2>
<form action="<c:url value="/shop/updateCartQuantities.do"/>" method="post">
<table align="center" border="1" cellspacing="2" cellpadding="0">
  <tr>
    <td><b><fmt:message key="item.id" /></b></td>
    <td><b><fmt:message key="subCategory.name" /></b></td>
    <td><b><fmt:message key="item.name" /></b></td>
    <td><b><fmt:message key="item.unitPrice" /></b></td>
    <td><b><fmt:message key="item.unitType" /></b></td>
    <td><b><fmt:message key="lineitem.qty" /></b></td>  <td>&nbsp;</td>
  </tr>

<c:if test="${cartForm.cart.numberOfItems == 0}">
<tr><td colspan="8"><b><fmt:message key="jspCart.cartEmpty" /></b></td></tr>
</c:if>

<c:forEach var="cartItem" items="${cartForm.cart.cartItemList.pageList}">
  <tr>
  <td><c:out value="${cartItem.item.itemId}"/></td>
  <td><c:out value="${cartItem.item.subCategoryId}"/></td>
  <td><c:out value="${cartItem.item.name}"/></td>
  <td align="right"><fmt:formatNumber value="${cartItem.item.unitPrice}" pattern="$#,##0.00" /></td>
  <td><c:out value="${cartItem.item.unit}"/></td>
  <td><input type="text" size="3" name="<c:out value="${cartItem.item.itemId}"/>" value="<c:out value="${cartItem.quantity}"/>" />
  </td>
  <td><a href="<c:url value="/shop/removeItemFromCart.do"><c:param name="workingItemId" value="${cartItem.item.itemId}"/></c:url>">
    <img border="0" src="../images/button_remove.gif" />
  </a></td>
  </tr>
</c:forEach>
<tr>
<td colspan="7" align="right">
<b><fmt:message key="jspCart.subTotal" />:<fmt:formatNumber value="${cartForm.cart.subTotal}" pattern="$#,##0.00" /></b><br/>
<input type="image" border="0" src="../images/button_update_cart.gif" name="update" />
</td>
</tr>
</table>
<center>
  <c:if test="${!cartForm.cart.cartItemList.firstPage}">
    <a href="<c:url value="viewCart.do?page=previousCart"/>">
        <img border="0" src="../images/button_previous.gif"/></a>
  </c:if>
  <c:if test="${!cartForm.cart.cartItemList.firstPage}">
    <a href="<c:url value="viewCart.do?page=nextCart"/>">
        <img border="0" src="../images/button_next.gif"/></a>
  </c:if>
</center>
</form>

<c:if test="${cartForm.cart.numberOfItems > 0}">
<br /><center><a href="<c:url value="/shop/newOrderForm.do"/>"><img border="0" src="../images/button_checkout.gif" /></a></center>
</c:if>

</td>

</tr>
</table>
</table>

