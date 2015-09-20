<%@ include file="IncludeTop.jsp" %>


<center><h2><fmt:message key="jspNewOrder.detail" /></h2></center>
<p>

<html:form action="/shop/newOrder.do" styleId="workingOrderForm" method="post" >

<TABLE border=1 align="center" cellpadding=3 cellspacing=1>
<TR><TD colspan=2><FONT size=4><B><fmt:message key="jspNewOrder.info" /></B></FONT>
    </TD>
</TR>
<TR><TD><fmt:message key="account.fedex" />:</TD>
    <TD><html:text name="workingOrderForm" property="order.fedex" />
        <html:errors property="order.fedex" /> </TD>
</TR>
<TR><TD colspan=2><FONT size=4><B><fmt:message key="jspNewOrder.shippingA" /></B></FONT>
    </TD>
</TR>

<TR><TD><fmt:message key="account.firstName" />:</TD>
    <TD><html:text name="workingOrderForm" property="order.shipToFirstName" />
        <html:errors property="order.shipToFirstName" /></TD>
</TR>
<TR><TD><fmt:message key="account.lastName" />:</TD>
    <TD><html:text name="workingOrderForm" property="order.shipToLastName" />
        <html:errors property="order.shipToLastName" /></TD>
</TR>
<TR><TD><fmt:message key="account.address1" />:</TD>
    <TD><html:text size="40" name="workingOrderForm" property="order.shipAddress1" />
        <html:errors property="order.shipAddress1" /></TD>
</TR>
<TR><TD><fmt:message key="account.address2" />:</TD>
    <TD><html:text size="40" name="workingOrderForm" property="order.shipAddress2" />
        <html:errors property="order.shipAddress2" /></TD>
</TR>
<TR><TD><fmt:message key="account.city" />:</TD>
    <TD><html:text name="workingOrderForm" property="order.shipCity" />
        <html:errors property="order.shipCity" /></TD>
</TR>
<TR><TD><fmt:message key="account.provState" />:</TD>
    <TD><html:text size="4" name="workingOrderForm" property="order.shipProvState" />
        <html:errors property="order.shipProvState" /></TD>
</TR>
<TR><TD><fmt:message key="account.postalZip" />:</TD>
    <TD><html:text size="10" name="workingOrderForm" property="order.shipPostalZip" />
        <html:errors property="order.shipPostalZip" /></TD>
</TR>
<TR><TD><fmt:message key="account.country" />:</TD>
    <TD><html:text size="15" name="workingOrderForm" property="order.shipCountry" />
        <html:errors property="order.shipCountry" /></TD>
</TR>

<c:if test="${workingOrderForm.order.specialRequest}" >
    <TR><TD colspan=2>
    <FONT size=4><B><fmt:message key="order.comment" />:</B></FONT>
    </TD></TR>
    <TR><TD colspan=2>
    <html:textarea name="workingOrderForm" property="order.comments"
                cols="70" rows="10"/>
    </TD></TR>
</c:if>

</TABLE>

<p>
<center>
<input align="center" type="image" src="../images/button_submit.gif">
</center>
</p>
</html:form>

