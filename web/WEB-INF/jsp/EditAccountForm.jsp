<%@ include file="IncludeTop.jsp" %>

<html:form styleId="workingAccountForm" method="post" action="/shop/editAccount.do">
<html:hidden name="workingAccountForm" property="validate" value="editAccount" />
<html:hidden name="workingAccountForm" property="account.userId" />

<p>
<right><b><a href="<c:url value="/shop/listOrders.do"/>"><fmt:message key="jspAccount.viewOrder" /> </a>
</b></right>


<table cellpadding="10" cellspacing="0" align="center" border="1"><tr><td>

<font><h3><fmt:message key="jspAccount.userInfo" /></h3></font>
<table border="0" cellpadding="3" cellspacing="1">
<tr><td><fmt:message key="account.userId" />:</td>
    <td><c:out value="${workingAccountForm.account.userId}"/></td>
</tr>
<tr><td colspan="2"><font size="2"><i><fmt:message key="jspAccount.optionchangepswd" />:</i></font>
    </td>
</tr>
<tr><td><fmt:message key="jspAccount.npassword" />:</td>
    <td><html:password name="workingAccountForm" property="account.password" />
        <html:errors property="password"/></td>
</tr>

<tr>
    <td><fmt:message key="account.confPassword" />:</td>
    <td><html:password name="workingAccountForm" property="repeatedPassword" />
        <html:errors property="password"/></td>
</tr>
</table>

<%@ include file="IncludeAccountFields.jsp" %>

</td></tr></table>

<br /><center>
<input border="0" type="image" src="../images/button_submit.gif" name="submit" value="Save Account Information" />
</center>

</html:form>

