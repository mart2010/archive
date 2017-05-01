<%@ include file="/WEB-INF/jsp/common/taglibs-directive.jsp" %>

<%@ page language="java" isErrorPage="true" %>

<html>
<head>
	<%@ include file="/WEB-INF/jsp/common/head-meta.jsp"%>
</head>

<body>
<%@ include file="/WEB-INF/jsp/common/top-sidebar.jsp" %>

<div id="main-copy">
<h1><fmt:message key="jspError.title" /></h1>


<!-- the following produces the error page info -->

<!-- determine error -->
<c:choose>
      <c:when test="${not empty pageContext.exception}">
         <c:set var="problemType">JSP Exception</c:set>
         <c:set var="appException" value="${pageContext.exception}"/> 
         <c:set var="causeException" value="${appException.cause}"/>
      </c:when>
      <c:when test="${not empty requestScope['javax.servlet.
                                                 error.exception']}">
         <c:set var="problemType">Servlet Exception</c:set>
         <c:set var="appException" value="${requestScope['javax.
                                               servlet.error.exception']}"/> 
         <c:set var="causeException" value="${appException.rootCause}"/>
      </c:when>
      <c:when test="${not empty requestScope['org.apache.struts.action.
                                                 EXCEPTION']}">
         <c:set var="problemType">Struts Exception</c:set>
         <c:set var="appException" value="${requestScope['org.apache.
                                               struts.action.EXCEPTION']}"/>
         <c:set var="causeException" value="${appException.cause}"/>
      </c:when>
      <c:otherwise>
         <c:set var="problemType">Unidentified Server Error</c:set>
      </c:otherwise>
</c:choose>


<!-- start framework -->
<table cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td valign="top" colspan="2">    
         <table cellpadding="4" cellspacing="0" border="0" 
          width="100%">
            <tr valign="top">
               <td>
                  <table cellpadding="2" cellspacing="1" border="0" 
                  width="80%">
                     <tr>
                        <td colspan="2"><fmt:message key="jspError.msg" />
                        </td>
                     </tr>
                     <tr><td colspan="2">
                           <html:errors/>
                     </td></tr>
                     <tr valign="top">
                        <td>
                           <b>Problem type</b>
                           <br/><c:out value="${problemType}"/>
                        </td>
                        <td>
                           <b>Problem details</b>
                           <c:if test="${not empty
                            requestScope['javax.servlet.error.message']}">
                            <br/>
                           <c:out value=
                             "${requestScope['javax.servlet.error.message']}"
                           />
                           </c:if>
                           <c:if test="${not empty appException}">
                              <br/><c:out value="${appException.message}"/>
                              &nbsp;
                           </c:if>
                        </td>
                     </tr>
                     <c:if test="${not empty causeException}">
                     <tr>
                        <td>
                           <b>Caused by</b>
                           <br/><c:out value="${causeException}"/>
                        </td>
                        <td>
                           <b>Cause details</b>
                           <br/><c:out value="${causeException.message}"/>
                           &nbsp;
                        </td>
                     </tr>
                     </c:if>
                  </table>
                  <table id="showDetailsLinkDiv" style="{display:inline}"
                       cellpadding="2" cellspacing="1" border="0" width="80%">
                     <tr>
                        <td align="left">
                           [ <a href="javascript:showDetails( )">
                             Show details</a> ]
                        </td>
                     </tr>
                  </table>
                  <table id="hideDetailsLinkDiv" style="{display:none}"
                       cellpadding="2" cellspacing="1" border="0" width="80%">
                     <tr>
                        <td align="left">
                           [ <a href="javascript:hideDetails( )">
                             Hide details</a> ]
                        </td>
                     </tr>
                  </table>
              <!-- begin details -->
                  <div id="stackTraceDiv" style="{display:none}">
                  <c:if test="${not empty appException}">
                     <p></p>
                     <table cellpadding="4" cellspacing="0" 
                            border="0" width="100%">
                        <tr>
                           <td>
                                 <h3>Exception stack trace</h3>
                           </td>
                        </tr>
                     </table>
                     <b><c:out value="${appException}"/></b>
                     <br/>
                     <table align="center" cellpadding="0" cellspacing="0"
                            border="0" width="90%" class="pod">
                        <c:forEach var="stackItem" 
                                 items="${appException.stackTrace}">
                           <tr><td><c:out value="${stackItem}"/></td></tr>
                         </c:forEach>
                     </table>
                  </c:if>
                  <c:if test="${not empty causeException}">
                     <p></p>
                     <table cellpadding="4" cellspacing="0" 
                            border="0" width="100%">
                        <tr>
                           <td>
                                 <h3>Cause stack trace</h3>
                           </td>
                        </tr>
                     </table>
                     <b><c:out value="${causeException}"/></b>
                     <br/>
                     <table align="center" cellpadding="0" cellspacing="0"
                           border="0" width="90%" class="pod">
                        <c:forEach var="stackItem"
                             items="${causeException.stackTrace}">
                           <tr><td><c:out value="${stackItem}"/></td></tr>
                         </c:forEach>
                     </table>
                  </c:if>
                  </div>
              <!-- end details -->
               </td>
            </tr>
         </table>
         </td>
    </tr>
</table>

<!-- Begin javascript to toggle detail -->
   <script language="javascript">
      function showDetails( ) {
        document.getElementById("showDetailsLinkDiv").style.display = "none";
        document.getElementById("hideDetailsLinkDiv").style.display = "inline";
        document.getElementById("stackTraceDiv").style.display = "inline";
      }
      function hideDetails( ) {
        document.getElementById("showDetailsLinkDiv").style.display = "inline";
        document.getElementById("hideDetailsLinkDiv").style.display = "none";
        document.getElementById("stackTraceDiv").style.display = "none";
      }
   </script>



</div>

<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
</body>
</html>
