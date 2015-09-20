<H3><fmt:message key="jspAccount.info" /></H3>

<TABLE border=0 cellpadding=3 cellspacing=1>
<TR><TD><fmt:message key="account.firstName" />:</TD>
    <TD><html:text name="workingAccountForm" property="account.firstName" />
        <html:errors property="account.firstName"/></TD>
</TR>
<TR><TD><fmt:message key="account.lastName" />:</TD>
    <TD><html:text name="workingAccountForm" property="account.lastName" />
        <html:errors property="account.lastName"/></TD>
</TR>
<TR><TD><fmt:message key="account.fedex" />:</TD>
    <TD><html:text name="workingAccountForm" property="account.fedex" />
        <html:errors property="account.fedex"/></TD>
</TR>
<TR><TD><fmt:message key="account.email" />:</TD>
    <TD><html:text name="workingAccountForm" property="account.email" />
        <html:errors property="account.email"/></TD>
</TR>
<TR><TD><fmt:message key="account.address1" />:</TD>
    <TD><html:text name="workingAccountForm" property="account.address1" />
        <html:errors property="account.address1"/></TD>
</TR>
<TR><TD><fmt:message key="account.address2" />:</TD>
    <TD><html:text name="workingAccountForm" property="account.address2" />
        <html:errors property="account.address2"/></TD>
</TR>
<TR><TD><fmt:message key="account.city" />:</TD>
    <TD><html:text name="workingAccountForm" property="account.city" />
        <html:errors property="account.city"/></TD>
</TR>
<TR><TD><fmt:message key="account.provState" />:</TD>
<TD><html:text name="workingAccountForm" property="account.provState" />
        <html:errors property="account.provState"/></TD>
</TR>
<TR><TD><fmt:message key="account.postalZip" />:</TD>
    <TD><html:text name="workingAccountForm" property="account.postalZip" />
        <html:errors property="account.postalZip"/></TD>
</TR>
<TR><TD><fmt:message key="account.country" />:</TD>
    <TD><html:text name="workingAccountForm" property="account.country" />
        <html:errors property="account.country"/></TD>
</TR>
</TABLE>

<H3><fmt:message key="jspAccount.profileInfo" /></H3>

<TABLE border=0 cellpadding=3 cellspacing=1 >
<TR><TD>
<fmt:message key="jspAccount.language" />:</TD><TD>
<html:select name="workingAccountForm" property="account.languagePref">
  <html:options name="workingAccountForm" property="languages" />
</html:select>
</TD></TR>
</TABLE>
