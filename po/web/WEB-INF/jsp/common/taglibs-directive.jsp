<%-- ***Page directives*** --%>

<%--directive used for enabling EL..should not be necessary for 2.4 servlet api--%> 
<%@page isELIgnored="false"%>

<%-- to do : check all the stuff on error handling and add: errorPage="/public/error.jsp"    --%>
<%@ page language="java" contentType="text/html;charset=utf-8"  
		 pageEncoding="UTF-8"  %>


<%-- ***Include common set of tag libraries*** --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="authz" uri="http://acegisecurity.sf.net/authz" %>
<%-- <%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %> --%>


