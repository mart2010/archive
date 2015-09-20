<%@ include file="IncludeTop.jsp" %>

<center>
  <b><fmt:message key="jspListOrder.myOrder" />:</b>
</center>
<table align="center" border="1" cellspacing="2" cellpadding="3">
  <tr>  <td><b><fmt:message key="order.id" /> </b></td>
        <td><b><fmt:message key="order.date" /> </b></td>
        <td><b><fmt:message key="order.comment" /> </b></td>
        <td><b><fmt:message key="order.total" /></b></td>
  </tr>

<c:if test="${empty orderList}">
<tr><td colspan="8"><b><fmt:message key="jspListOrder.noHistory" /></b></td></tr>
</c:if>

<c:forEach var="order" items="${orderList}">
  <tr>
  <td><b><a href="<c:url value="/shop/viewOrder.do"><c:param name="orderId" value="${order.orderId}"/></c:url>">
	  <font color="BLACK"><c:out value="${order.orderId}"/></font>
  </a></b></td>
  <td><fmt:formatDate value="${order.orderDate}" pattern="yyyy/MM/dd"/></td>
  <td width="100" height="20"><c:out value="${order.comments}"/> </td>
  <td><fmt:formatNumber value="${order.totalPrice}" pattern="$#,##0.00"/></td>
  </tr>
</c:forEach>
</table>

