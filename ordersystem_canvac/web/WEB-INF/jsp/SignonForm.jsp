<%@ include file="IncludeTop.jsp" %>


<form action="<c:url value="/shop/signon.do"/>" method="POST">

<c:if test="${!empty signonForwardAction}">
<input type="hidden" name="forwardAction" value="<c:url value="${signonForwardAction}"/>"/>
</c:if>

<table align="center" border="0">
<tr>
<td colspan="2"><fmt:message key="jspSignon.enter" />
<br />&nbsp;</td>
</tr>
<tr>
<td><fmt:message key="account.userName" />:</td>
<td><input type="text" name="userId"/></td>
</tr>
<tr>
<td><fmt:message key="account.password" />:</td>
<td><input type="password" name="password"/></td>
</tr>
<tr>
<td>&nbsp;</td>
<td><input type="image" border="0" src="../images/button_submit.gif" name="update" /></td>
</tr>
</table>

</form>



