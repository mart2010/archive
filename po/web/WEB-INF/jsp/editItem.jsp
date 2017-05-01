<%@ include file="common/taglibs-directive.jsp" %>

<html>
<head>
<%@ include file="common/head-meta.jsp"%>
</head>


<body>
<%@ include file="common/top-sidebar.jsp" %>

<div id="main-copy">
      <h1><fmt:message key="jspItem.edit" /></h1>
<br></br>


<html:form method="post" action="/saveItem">
	<input type="hidden" name="goToListSupplier">
	<script>
	     function set(target) {
         document.forms[0].goToListSupplier.value=target;
	     }
	</script>



<table class="edit">
	<tr><th><fmt:message key="item.name" />:</th>
	    <td><html:text property="name" size="25"/>
			<html:errors property="name"/></td>
	</tr>
	<tr><th><fmt:message key="item.desc" />:</th>
	    <td><html:text property="desc" size="50"/>
			<html:errors property="desc"/></td>
	</tr>
	<tr><th><fmt:message key="item.number" />:</th>
	    <td><html:text property="itemNumber" size="15"/>
			<html:errors property="itemNumber"/></td>
	</tr>
	<tr><th><fmt:message key="item.manufacturer" />:</th>
	    <td><html:text property="manufacturer" size="30"/>
			<html:errors property="manufacturer"/></td>
	</tr>
	<tr><th><fmt:message key="item.distributor" />:</th>
	    <td><html:text property="distributor" size="30"/>
			<html:errors property="distributor"/></td>
	</tr>
	<tr><th><fmt:message key="item.format" />:</th>
	    <td><html:text property="format" size="25"/>
			<html:errors property="format"/></td>
	</tr>

	<tr><th><fmt:message key="item.listPrice" />:</th>
	    <td><html:text property="listPriceString" size="6"/>
			<html:errors property="listPriceString"/>
			<html:select property="listPriceCurrencyString"> 
				<html:options property="currencies" />	
			</html:select>
		</td>
	</tr>
	<tr><th><fmt:message key="obj.updatedDate" />:</th>
	    <td><fmt:formatDate value="${selItem.updatedDate}" pattern="${defaultDatePattern}" /></td>
	<tr><th><fmt:message key="obj.updatedBy" />:</th>
	    <td><c:out value="${selItem.updatedBy}" /></td>
	</tr>
	
</table>

<table align="left">
    <tr>
		<td><button type="button" onclick="location.href=
						'<html:rewrite action="/deleteItem" 
							paramId='itemId' paramName='selItem' paramProperty='id'/>'">
		        <fmt:message key="button.delete"/>
   			</button></td>
		<td>&nbsp;</td><td>&nbsp;</td>
		<td><html:cancel styleClass="button">
                <fmt:message key="button.cancel"/>
            </html:cancel>
		</td><td>&nbsp;</td>
		<td><html:submit styleClass="button">
            	  <fmt:message key="button.save"/>
            </html:submit></td>
    </tr>
</table>

<br></br>

<h1><fmt:message key="jspItem.itemSupply" /></h1>
<br></br>
<i><fmt:message key="jspItem.supplierRule" />:</i>

<table class="edit">
	<tr><td colspan="2"><html:errors property="suppliers"/></td></tr>
	    
	<c:forEach items="${itemForm.itemSuppliers}" var="itemSupplier">  

<authz:authorize ifAllGranted="PO_Admin"> 
	<tr><td><fmt:message key="jspItem.supplier"/></td>
			<td><html:select name="itemSupplier" property="supplier.id" indexed="true">
				<html:option value="-1"><fmt:message key="jspItem.supDrop"/></html:option>
				<html:options collection="suppliers" property="id" labelProperty="name"/>
			 </html:select></td>
	</tr>
	<tr><th><fmt:message key="itemSupply.catalogNb" />:</th>
	    <td><html:text name="itemSupplier" property="catalogNumber" indexed="true" size="25"/></td>
	</tr>
	<tr><th><fmt:message key="itemSupply.bidInfo" />:</th>
	    <td><html:text name="itemSupplier" property="bidInfo" indexed="true" size="50"/></td>
	</tr>
	<tr><th><fmt:message key="itemSupply.bidYear" />:</th>
	    <td><html:text name="itemSupplier" property="bidYear" indexed="true" size="10"/></td>
	</tr>
</authz:authorize>

<authz:authorize ifNotGranted="PO_Admin"> 
	<tr><td><fmt:message key="jspItem.supplier"/></td>
			
			<c:choose>
			<c:when test="${empty itemSupplier.supplier.id}">
			<td><html:select name="itemSupplier" property="supplier.id" indexed="true">
				<html:option value="-1"><fmt:message key="jspItem.supDrop"/></html:option>
				<html:options collection="suppliers" property="id" labelProperty="name"/>
			    </html:select></td>
			 </c:when>
			 <c:otherwise>
			 <td><html:select name="itemSupplier" property="supplier.id" indexed="true">
				<html:option value="${itemSupplier.supplier.id}"><c:out value="${itemSupplier.supplier.name}" /></html:option>
				</html:select></td>
			 </c:otherwise>
			 </c:choose>
	</tr>
</authz:authorize>

	</c:forEach>

	<tr><th align="center">
			<html:submit onclick="set('true');">
            	  <fmt:message key="button.createSupplier"/>
            </html:submit></th>
	</tr>

</table>

</html:form>

</div>

<%@ include file="common/footer.jsp"%>

</body>
</html>


