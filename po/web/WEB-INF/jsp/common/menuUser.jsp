<%--class="highlight" should be defined for the current selected menu item--%> 
<%-- as the following : <a href="./index.html" class="highlight">Item</a> |  --%>

<div class="subHeader">
	<span class="doNotDisplay">Navigation: </span>&nbsp; |
	<a href="<c:url value="/editUser.html">
				<c:param name="username" value="${currentUser.name}"/>
			 </c:url>"><fmt:message key="jspMenu.myAccount" />&nbsp;</a> |
	<a href="<c:url value="/listItem.html"/>"><fmt:message key="jspMenu.listItem" />&nbsp;</a> |
	<a href="<c:url value="/listSupplier.html"/>"><fmt:message key="jspMenu.listSupplier" />&nbsp;</a> |
	<a href="<c:url value="/listUserPo.html"/>"><fmt:message key="jspMenu.listMyPO" />&nbsp;</a> |
	<a href="<c:url value="/editUserPo.html"/>"><fmt:message key="jspMenu.editMyPO" />&nbsp;</a> |
</div>

