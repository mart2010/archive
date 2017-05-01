<%-- *************  <head> **************** --%>

	<title><fmt:message key="webapp.title" /></title>
	
	<%-- *************  Meta **************** --%>
	<%-- HTTP 1.1 --%>
	<meta http-equiv="Cache-Control" content="no-store"/>
	<%-- HTTP 1.0 --%>
	<meta http-equiv="Pragma" content="no-cache"/>
	<%-- Prevents caching at the Proxy Server --%>
	<meta http-equiv="Expires" content="0"/>
	        
	<meta content="text/html; charset=utf-8" http-equiv="Content-Type" /> 
	<meta name="author" content="Martin Ouellet (KLM2O)"/>

	<link rel="stylesheet" type="text/css" href="<c:url value='/public/stylesheet/stylesheet.css'/>" />

	
	<%-- Useful Variable to avoid the numerous call --%>
	<c:set var="currentUser" value="${sessionScope['ACEGI_SECURITY_CONTEXT'].authentication.principal}" scope="request"/>

	<%-- Global for all JSP pages, to changes along with the Default Pattern set in DateUtil class. --%>
	<c:set var="defaultDatePattern" value="dd/MM/yyyy" />

