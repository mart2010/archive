<%@ include file="common/taglibs-directive.jsp" %>

<html>
<head>
<%@ include file="common/head-meta.jsp"%>
</head>


<body>
<%@ include file="common/top-sidebar.jsp" %>


<div id="main-copy">
      <h1><fmt:message key="jspUser.edit" /></h1>
<br></br>

<html:form method="post" action="/saveUser">
	
<table class="edit">
	<tr><th><fmt:message key="user.username" />:</th>
	    <td><c:if test="${selUser.name == null}">
		    	<html:text property="name" size="20"/>    
	    	</c:if> 
		    <c:if test="${selUser.name != null}">
		    	<html:text readonly="true" property="name" size="20"/>    
	    	</c:if> 
	    	<html:errors property="name"/>
		</td>
	</tr>
	<tr><th>&nbsp;</th></tr>
	<tr><th><i><fmt:message key="jspUser.optionchangepswd" />:</i></th> 
	</tr>
	<tr><th><fmt:message key="user.password" />:</th>
	    <td><html:password property="password" size="15"/>
			<html:errors property="password"/></td>
	</tr>
	<tr><th><fmt:message key="user.confPassword" />:</th>
	    <td><html:password property="confPassword" size="15" />
	        <html:errors property="confPassword"/></td>
	    
	</tr>
	
	<%--Beginning of userInfo --%>
	<tr><th>&nbsp;</th></tr>
	<tr><th><i><fmt:message key="jspUser.info" />:</i></th> 
	<tr><th><fmt:message key="user.firstName" />:</th>
	    <td><html:text property="firstName" size="15"/>
			<html:errors property="firstName"/></td>
	</tr>
	<tr><th><fmt:message key="user.lastName" />:</th>
	    <td><html:text property="lastName" size="15"/>
			<html:errors property="lastName"/></td>
	</tr>
	<tr><th><fmt:message key="user.email" />:</th>
	    <td><html:text property="email" size="25"/>
			<html:errors property="email"/></td>
	</tr>
	<tr><th><fmt:message key="user.phoneNumber" />:</th>
	    <td><html:text property="phoneNumber" size="20"/>
			<html:errors property="phoneNumber"/></td>
	</tr>

	<%--Beginning of Role and Lab --%>
	<tr><th><fmt:message key="jspUser.role" />:</th>
	    <td>
			<c:forEach  items="${roles}" var="role">
				<nobr>
				<authz:authorize ifAllGranted="PO_Admin"> 
					<html:multibox property="selRoles" value="${role.name}" />
				</authz:authorize>
				<authz:authorize ifNotGranted="PO_Admin"> 
					<html:multibox property="selRoles" value="${role.name}" disabled="true"/>
				</authz:authorize>
				<c:out value="${role.nameLabel}"/>
				</nobr>
			</c:forEach>
			<html:errors property="selRoles"/>
		</td></tr>	

	<tr><th><fmt:message key="jspUser.lab" />:</th>
		<td> 
			<c:forEach  items="${labs}" var="lab">
				<nobr>
				<authz:authorize ifAllGranted="PO_Admin"> 
					<html:multibox property="selLabs" value="${lab.name}" />
				</authz:authorize>
				<authz:authorize ifNotGranted="PO_Admin"> 
					<html:multibox property="selLabs" value="${lab.name}" disabled="true" />
				</authz:authorize>
					<c:out value="${lab.name}"/>
				</nobr>
			</c:forEach>
			<html:errors property="selLabs"/>
		</td></tr>
		<authz:authorize ifAllGranted="PO_Admin">
			<tr><th><fmt:message key="jspUser.newLab"/>: </th>
			    <td><html:text property="newLabName" maxlength="20"/>
			    	<html:errors property="newLabName"/></td>
			</tr>
    	</authz:authorize>

</table>
	<!-- button to do: check those property, check confirmDelete...-->
        	<authz:authorize ifAllGranted="PO_Admin">     
				<%-- Allow delete only for existing Item --%>
				<%-- would be nice to a a JS which would confirm the Delete ... --%>
				<c:if test="${selUser.name != null}">
				<button type="button" onclick="location.href='<html:rewrite 
					action='/admin/deleteUser'  paramId='username' paramName='selUser' paramProperty='name' />'">
        			<fmt:message key="button.delete"/>
		    	</button>
		    	</c:if>
            </authz:authorize>
 &nbsp;&nbsp;
            <html:cancel>
                <fmt:message key="button.cancel"/>
            </html:cancel>
&nbsp;
            <html:submit>
            	  <fmt:message key="button.save"/>
            </html:submit>


</html:form>

</div>

<%@ include file="common/footer.jsp"%>

</body>
</html>
