<%@ include file="common/taglibs-directive.jsp" %>

<html>
<head>
<%@ include file="common/head-meta.jsp"%>

	<script language="JavaScript">
	     	function getSelCatId(control) {
       			form = control.form;
       			form.action = "listItem.html?selCategoryId=";
       			form.action += control.value;
       			form.submit( );
   			}
	     	function getSelProdId(control) {
       			form = control.form;
       			form.action = "listItem.html?selProductId=";
       			form.action += control.value;
       			form.submit( );
   			}
    </script>
</head>


<body>
<%@ include file="common/top-sidebar.jsp" %>


<%-- ***************  Main page *****************--%>

<div id="main-copy">
    <h1><fmt:message key="jspItem.title" /></h1>
<p></p>
<fmt:message key="jspItem.cat"/>
<br></br>
<table class="edit">

<html:form method="post" action="/newCat">	
<authz:authorize ifAllGranted="PO_Admin">
	<tr><td><fmt:message key="jspItem.selCat"/>:</td>
		<td><html:select name="selCategory" property="id" onchange="getSelCatId(this);">
				<html:options collection="categories" property="id"
			     		   labelProperty="name"/></html:select></td>
	     <th><button type="button" onclick="location.href='<html:rewrite 
	     					action="/deleteCat" paramId='catId' paramName='selCategory' paramProperty='id'/>'">
	            <fmt:message key="button.deleteCat"/>
			 </button></th>
	</tr>
	<tr><td colspan="2"><html:errors property="catName"/>&nbsp;</td></tr>
	<tr><td><fmt:message key="jspItem.newName"/>:</td><td><html:text property="name" size="25"/><html:errors property="name"/></td></tr>
	<tr><td><fmt:message key="jspItem.newDesc"/>:</td><td><html:text property="desc" size="40"/></td></tr>

	<tr><th></th><th><html:submit><fmt:message key="button.addCat"/></html:submit></th></tr>
</authz:authorize>

<authz:authorize ifNotGranted="PO_Admin">
	<tr><td><fmt:message key="jspItem.selCat"/>:</td>
		<td><html:select name="selCategory" property="id" onchange="getSelCatId(this);">
				<html:options collection="categories" property="id"
			     		   labelProperty="name"/></html:select></td>
	</tr>
</authz:authorize>

</html:form>
</table>

<p></p>
<fmt:message key="jspItem.prod"/>
<br></br>

<table class="edit">

<html:form method="post" action="/newProd">

<authz:authorize ifAllGranted="PO_Admin">
	<tr><td><fmt:message key="jspItem.selProd"/>:</td>
		<td><html:select name="selProduct" property="id" onchange="getSelProdId(this);">
				<html:options  collection="products" property="id" 	
			               labelProperty="name"/></html:select></td>
	     <th><button onclick="location.href='<html:rewrite 
	     				action="/deleteProd" paramId='prodId' paramName='selProduct' paramProperty='id' />'">
	        	<fmt:message key="button.deleteProd"/>
			 </button></th>
	</tr>
	<tr><td colspan="2"><html:errors property="prodName"/>&nbsp;</td></tr>
	<tr><td><fmt:message key="jspItem.newName"/>:</td><td><html:text property="name" size="25"/><html:errors property="name"/></td></tr>  
	<tr><td><fmt:message key="jspItem.newDesc"/>:</td><td><html:text property="desc" size="40"/></td></tr>  

	<tr><th></th><th><html:submit><fmt:message key="button.addProd"/></html:submit></th></tr>
</authz:authorize>	

<authz:authorize ifNotGranted="PO_Admin">
	<tr><td><fmt:message key="jspItem.selProd"/>:</td>
		<td><html:select name="selProduct" property="id" onchange="getSelProdId(this);">
				<html:options  collection="products" property="id" 	
			               labelProperty="name"/></html:select></td>
	</tr>
</authz:authorize>

</html:form>
</table>

<p></p>
    <h1><fmt:message key="jspItem.itemList" /></h1>
<br></br>
<table class="list">

  <tr>
    <th><fmt:message key="item.name" /></th>
    <th><fmt:message key="item.number" /></th>
    <th><fmt:message key="item.format" /></th>
    <th><fmt:message key="item.listPrice" /></th>
	<th></th>    
  </tr>

<c:forEach var="item" items="${items}">
  <tr>  
    <td><a href="<c:url value="/editItem.html"><c:param name="itemId" value="${item.id}"/></c:url>">
	    <c:out value="${item.name}"/></a></td>
    <td><c:out value="${item.itemNumber}"/></td>
    <td><c:out value="${item.distributor}"/></td>
    <td><c:out value="${item.listPrice}" /></td>
    <td><a href="<c:url value="/addItemToUserPo.html"><c:param name="itemId" value="${item.id}"/></c:url>">
     		<fmt:message key="jspItem.addItemPO" />
   		</a></td>
  </tr>
</c:forEach>
</table>

	<button type="button" onclick="location.href='<html:rewrite forward="home" />'">
	        	<fmt:message key="button.cancel"/>
      </button>
&nbsp;
	<button type="button" style="margin-right: 5px"
	        		onclick="location.href='<html:rewrite forward="addItem"/>'">
	        	<fmt:message key="button.createItem"/>
   	</button>


</div>

<%@ include file="common/footer.jsp"%>

</body>
</html>
