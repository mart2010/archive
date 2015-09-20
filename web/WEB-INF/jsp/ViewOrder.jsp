<%@ include file="IncludeTop.jsp" %>


<table width="60%" align="center" border="1" cellpadding="3" cellspacing="1">
<tr><td align="center" colspan="2">
    <font size="4"><b><fmt:message key="jspViewOrder.nb" /> <c:out value="${order.orderId}"/></b></font>
    </td>
<tr/>
<tr><td align="center" colspan="2">
    <font size="3"><b><fmt:formatDate value="${order.orderDate}" pattern="yyyy/MM/dd" /></b></font>
    </td>
</tr>
<tr><td colspan="2">
    <font size="4"><b><fmt:message key="jspViewOrder.confirmDtl" /></b></font>
    </td>
</tr>
<tr><td>
    <fmt:message key="account.fedex" />:</td><td><c:out value="${order.fedex}"/>
    </td>
</tr>
<tr><td colspan="2">
<font size="4"><b><fmt:message key="jspNewOrder.shippingA" /></b></font>
</td></tr><tr><td>
<fmt:message key="account.firstName" />:</td><td><c:out value="${order.shipToFirstName}"/>
</td></tr>
<tr><td>
<fmt:message key="account.lastName" />:</td><td><c:out value="${order.shipToLastName}"/>
</td></tr>
<tr><td>
<fmt:message key="account.address1" />:</td><td><c:out value="${order.shipAddress1}"/>
</td></tr>
<tr><td>
<fmt:message key="account.address2" />:</td><td><c:out value="${order.shipAddress2}"/>
</td></tr>
<tr><td>
<fmt:message key="account.city" />:</td><td><c:out value="${order.shipCity}"/>
</td></tr>
<tr><td>
<fmt:message key="account.provState" />:</td><td><c:out value="${order.shipProvState}"/>
</td></tr>
<tr><td>
<fmt:message key="account.postalZip" />:</td><td><c:out value="${order.shipPostalZip}"/>
</td></tr>
<tr><td>
<fmt:message key="account.country" />:</td><td><c:out value="${order.shipCountry}"/>
</td></tr>

<tr><td colspan="2">

<table width="100%" align="center" border="1" cellspacing="2" cellpadding="3">
  <tr>
  <td><b><fmt:message key="item.name" /></b></td>
  <td><b><fmt:message key="lineitem.qty" /></b></td>
  <td><b><fmt:message key="lineitem.price" /></b></td>
  </tr>
<c:forEach var="lineItem" items="${order.lineItems}">
  <tr>
  <td>
    <c:out value="${lineItem.itemName}"/>
  </td>
  <td><c:out value="${lineItem.quantity}"/></td>
  <td align="right"><fmt:formatNumber value="${lineItem.price}" pattern="$#,##0.00"/></td>
  </tr>
</c:forEach>
  <tr>
  <td colspan="5" align="right"><b><fmt:message key="order.total" />:<fmt:formatNumber value="${order.totalPrice}" pattern="$#,##0.00"/></b></td>
  </tr>
</table>
</td></tr>

</table>

