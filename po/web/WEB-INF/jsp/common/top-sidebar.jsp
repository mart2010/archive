
     <%-- ##### Top-Header ##### --%>

    <div id="header">
    	
		<div class="superHeader">
        	<div class="right">
				<fmt:message key="jspHome.welcome" />
      		</div>
		</div>

      	<div class="midHeader">
        	<h1 class="headerTitle">PO4LAB 1.0</h1>
      	</div>

		<%--Menu Role-Sensitive --%>
		<authz:authorize ifNotGranted="PO_Admin,PO_User" >
			<!-- no Logged Menu  -->
		    <div class="subHeader">
				<span class="doNotDisplay">Navigation: </span>
				| <a href="<c:url value="/login.html"/>"><fmt:message key="jspMenu.login" />&nbsp;</a> |
	        </div>
	    </authz:authorize>
		<%-- Logged in as Admin (has both PO_User and PO_Admin priv --%>		
		<authz:authorize ifAllGranted="PO_Admin" >
			<%@ include file="menuAdmin.jsp"%>
		</authz:authorize>

	
		<%-- Logged in as User --%>
		<authz:authorize ifAllGranted="PO_User" ifNotGranted="PO_Admin" >
			<%@ include file="menuUser.jsp"%>
		</authz:authorize>
	</div>


	
    <%-- ##### Side-bar ##### --%>

	<div id="side-bar">
		<div>
		   <p class="sideBarTitle"><fmt:message key="jspSideBar.connection" /></p>
           <span class="sideBarText">
             <fmt:message key="user.username" />:  
				<i><authz:authentication operation="principal"/></i>
           </span>
           <span class="sideBarText">
             <fmt:message key="user.role" />:  
				<authz:authorize ifAnyGranted="PO_Admin,PO_User" >
					<i><c:out value="${currentUser.roles}"/></i>
				</authz:authorize>				
           </span>
		</div>

		
	   <authz:authorize ifAnyGranted="PO_Admin,PO_User" >
	   <div>
		<p class="sideBarTitle">Navigation</p>
			<ul>
          	<li><a href="<c:url value="/login.html"/>">&rsaquo;  <fmt:message key="jspMenu.login" /></a></li>
          	<li><a href="<c:url value="/home.html"/>">&rsaquo;  <fmt:message key="jspMenu.home" /></a></li>
          	<li><a href="<c:url value="/logout.html"/>">&rsaquo;  <fmt:message key="jspMenu.logout" /></a></li>
        	</ul>
		</div>
		</authz:authorize>

  		<%-- For Admin which are also User, add the User functions Menu --%>
		<authz:authorize ifAllGranted="PO_Admin,PO_User">
		<div>
		 <p class="sideBarTitle"><fmt:message key="jspMenu.userSection" /></p>
			<ul>
          	<li><a href="<c:url value="/editUser.html"><c:param name="username" value="${currentUser.name}"/>
			 			 </c:url>">&rsaquo;  <fmt:message key="jspMenu.myAccount" /></a></li>
			<li><a href="<c:url value="/listItem.html"/>">&rsaquo;  <fmt:message key="jspMenu.listItem" /></a></li>
			<li><a href="<c:url value="/listSupplier.html"/>">&rsaquo;  <fmt:message key="jspMenu.listSupplier" /></a></li>
          	<li><a href="<c:url value="/listUserPo.html"/>">&rsaquo;  <fmt:message key="jspMenu.listMyPO" /></a></li>
          	<li><a href="<c:url value="/editUserPo.html"/>">&rsaquo;  <fmt:message key="jspMenu.editMyPO" /></a></li>
        	</ul>
		</div>
		</authz:authorize>


  	</div>


<div id="main-copy">
	<html:messages message="true" id="msg" property="<%= org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE %>">
	  		<p style="font-size: 11pt; font-style: italic; font-weight: 700"><c:out value="${msg}"/></p>
	</html:messages>
</div>

