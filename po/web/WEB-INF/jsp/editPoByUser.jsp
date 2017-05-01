<%@ include file="common/taglibs-directive.jsp" %>

<html>
<head>
<%@ include file="common/head-meta.jsp"%>
</head>

<body>
<%@ include file="common/top-sidebar.jsp" %>

<c:choose>
<c:when test="${empty currentPo}">
<p align="center">
<button type="button" onclick="location.href=
			'<html:rewrite action="/listItem"/>'">
       	<fmt:message key="button.addLineItem"/>
</button>
<p>
</c:when>

<c:otherwise>
<html:form method="post" action="/saveUserPo">
	<input type="hidden" name="refresh">
	<script>
	     function set(target) {
         document.forms[0].refresh.value=target;
	     }
	</script>
	
<div id="main-copy">
      <h1><fmt:message key="jspPo.info" /></h1>

<br></br>	
<table class="edit">
	<tr><th><fmt:message key="po.project" />:</th>
	    <td><html:text property="project" size="30"/>
			<html:errors property="project"/></td>
	</tr>
	<tr><th><fmt:message key="po.orderFor" />:</th>
	    <td><html:text property="orderFor" size="20"/>
			<html:errors property="orderFor"/></td>
	</tr>
	<tr><th><fmt:message key="po.lab" />:</th>
	    <td><html:select property="selLabId">
				<html:optionsCollection name="currentUser" property="labs" value="id" label="name"/>
			</html:select></td>
	</tr>
	<tr><th><fmt:message key="po.estPrice" />:</th>
	    <td><c:out value="${currentPo.estCalculatedCADTotal}"/> </td>
	</tr>
	<tr><th><fmt:message key="po.openDate" />:</th>
	    <td><fmt:formatDate value="${currentPo.openDate}" pattern="${defaultDatePattern}" /></td>
	</tr>

</table>
</div>

<p></p>

<div id="main-copy">
      <h1><fmt:message key="jspPo.details" /></h1>
<br></br>
<html:errors property="quantity"/>
<table  class="list">
  <tr>
    <th><fmt:message key="item.name" /></th>
    <th><fmt:message key="item.number" /></th>
    <th><fmt:message key="item.listPrice" /></th>
    <th><fmt:message key="lineitem.qty" /></th>
    <th><fmt:message key="supplier.name" /></th>    
	<th><fmt:message key="itemSupply.catalogNb" /></th>    
    <th><fmt:message key="itemSupply.bidInfo" /></th>
    <th><fmt:message key="po.biddingFlag" /></th>  
    <th></th>
  </tr>
  <tr></tr>

<c:forEach items="${currentPo.poLineItems}" var="lineItem" >
  <tr>  
    <td><c:out value="${lineItem.item.name}"/></td>
    <td><c:out value="${lineItem.item.itemNumber}"/></td>
    <td><c:out value="${lineItem.item.listPrice}"/></td>
    <td><html:text property="qty(${lineItem.item.id})" size="4"/></td>
    <td><html:select property="supId(${lineItem.item.id})">
			<html:optionsCollection name="lineItem" property="item.itemSupplies" value="supplier.id" label="supplier.name"/>
		</html:select></td>
    <td><html:text property="catalogNb(${lineItem.item.id})" disabled="true"/></td>
<!--    <td><c:out value="bidInfo(${lineItem.item.id})"/></td>		-->
    <td><html:text property="bidInfo(${lineItem.item.id})" disabled="true"/></td>		
	<td><c:if test="${lineItem.item.biddingAssociated}">**</c:if></td>
    <td><a href="<c:url value="/removeItemToUserPo.html"><c:param name="itemId" value="${lineItem.item.id}"/></c:url>">
     		<fmt:message key="jspPo.removeLineItemPO" /></a>
   	</td>
  </tr>
</c:forEach>
</table>


<button type="button" onclick="location.href=
						'<html:rewrite action="/goToListItem"/>'">
		<fmt:message key="button.addLineItem"/>
</button>
<br></br>
<html:submit onclick="set('true');">
		<fmt:message key="button.refresh"/>
</html:submit>
<html:cancel>
	<fmt:message key="button.cancel"/>
</html:cancel>

<html:submit>
	<fmt:message key="button.order"/>
</html:submit>


</div>
</html:form>
</c:otherwise>
</c:choose>

<%@ include file="common/footer.jsp"%>

</body>
</html>
