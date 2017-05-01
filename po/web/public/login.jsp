<%@ include file="/WEB-INF/jsp/common/taglibs-directive.jsp" %>

<html>
<head>
<%@ include file="/WEB-INF/jsp/common/head-meta.jsp"%>
</head>


<body>
<%@ include file="/WEB-INF/jsp/common/top-sidebar.jsp" %>


<div id="main-copy">
      <h1><fmt:message key="jspLogin.title" /></h1>
	  <p></p>

<div id="loginTable">

<form method="post" id="loginForm" action="<c:url value='/j_acegi_security_check'/>">
<table width="100%">
    <tr>
        <td colspan="2">
            <c:if test="${param.failed != null}">
            <div class="error" 
                style="margin-right: 0; margin-bottom: 3px; margin-top: 3px">
                    <img src="<c:url value="/public/images/iconWarning.gif"/>"
						 class="icon" />
                    <fmt:message key="errors.password.mismatch"/>
                </div>
            </c:if>
        </td>
    </tr>
    <tr>
        <th>
            <label for="j_username" class="required">
                * <fmt:message key="user.username"/>:
            </label>
        </th>
        <td>
            <input type="text" name="j_username" id="j_username" size="20"/>
        </td>
    </tr>
    <tr>
        <th>
            <label for="j_password" class="required">
                * <fmt:message key="user.password"/>:
            </label>
        </th>
        <td>
            <input type="password" name="j_password" id="j_password" size="15"/>
        </td>
    </tr>
    <tr>
    	<td></td>
    	<td><input type="reset" name="reset" value="<fmt:message key="button.reset"/>" />
    		<input type="submit" name="login" value="<fmt:message key="button.login"/>" />
    	</td>
    </tr>
    
</table>
</form>
</div>
</div>


<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>

</body>
</html>
