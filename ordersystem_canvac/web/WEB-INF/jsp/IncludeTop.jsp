<%@ page contentType="text/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>


<html><head><title><fmt:message key="webapp.title" /> </title>
<meta content="text/html; charset=windows-1252" http-equiv="Content-Type" />
<META HTTP-EQUIV="Cache-Control" CONTENT="max-age=0">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<meta http-equiv="expires" content="0">
<META HTTP-EQUIV="Expires" CONTENT="Tue, 01 Jan 1980 1:00:00 GMT">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
</head>
<script language="JavaScript">
	// JavaScript Document
function showhide(couche, effet){
	couche.style.visibility='effet';
}
function MM_reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
MM_reloadPage(true);

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
</script>
<style type="text/css">
A:link {
	text-decoration:underline; 
	color:#000000; 
	font-weight: normal
}
A:visited {
	text-decoration:underline; 
	color:#666666; 
	font-weight: 
	normal
}
A:active {
	text-decoration:none; 
	color:#E01604; 
	font-weight: normal
}
A:hover {
	text-decoration:none; 
	color:#E01604; 
	font-weight: normal
}
.debut_paragraphe { 
	color: #000000; 
	font-size: 10pt; 
	font-weight: bold; 
	font-family: Arial, Helvetica, sans-serif
} 
.detailnews {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 8pt;
	color: #000000;
	visibility: hidden;
}
.document { 
	color: #000000; 
	font-size: 9pt; 
	font-weight: normal; 
	font-family: Verdana, Arial, Helvetica, sans-serif; 
}
.important {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 10pt;
	font-weight: bolder;
	color: #FF0000;
}
.item_table { 
	color: #000000; 
	font-size: 8pt; 
	font-weight: bold; 
	font-family: Arial, Helvetica, sans-serif; 
	text-align: left 
}
.menu_header { 
	color: #000000; 
	font-size: 10pt; 
	font-weight: normal; 
	font-family: Verdana, Arial, Helvetica, sans-serif; 
	text-align: justify 
}
.page {
	clear: none;
	float: none;
	height: 100%;
	width: 90%;
	text-align: left;
}
.paragraphe {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 10pt;
	text-align: justify;
	text-indent: 0.5in;
}
.tabletitre {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 10pt;
	font-weight: bold;
}
.titre1 {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12pt;
	font-weight: bolder;
	color: #000000;
}
.titre_centre { 
	color: #000000; f
	ont-size: 14pt; 
	font-weight: bold; 
	font-family: Arial, Helvetica, sans-serif; 
	text-align: center  
}
.Warnings { 
	color: #CC0000; 
	font-size: 10pt; 
	font-weight: bold; 
	font-family: Arial, Helvetica, sans-serif; 
	text-align: left  
}
.headliner { 
	color: #000000; 
	background-color: #F5f5f5; 
	font-size: 14pt; 
	font-weight: bold; 
	font-family: Arial, Helvetica, sans-serif; 
	text-align: left;   
}
</style>
<body>
<center> <img src="http://member.canvac.ca/image/logomember4.jpg" /></center><br />
	<center>
	<table width="100%" >
	<tr>
		<td valign="top">
			<div class="menu_header">
				<a href="http://member.canvac.ca/news.php" onMouseDown="cores.style.visibility='hidden'; news.style.visibility='visible';   " onMouseOver="cores.style.visibility='hidden'; news.style.visibility='visible';   ">News &amp; Events</a> 
				<div id="news" class="detailnews"> 
					<a href="http://member.canvac.ca/newslet.php" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image1','','http://member.canvac.ca/image/newsletters_b.jpg',1)"><img src="http://member.canvac.ca/image/newsletters_a.jpg" name="Image1" width="150" height="15" border="0" id="Image1" /></a><br>
					<a href="http://member.canvac.ca/training.php" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image5','','http://member.canvac.ca/image/training_b.jpg',1)"><img src="http://member.canvac.ca/image/training_a.jpg" name="Image5" width="150" height="15" border="0" id="Image5" /></a><br>
					<a href="http://member.canvac.ca/others.php" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image6','','http://member.canvac.ca/image/Other_events_b.jpg',1)"><img src="http://member.canvac.ca/image/Other_events_a.jpg" name="Image6" width="150" height="15" border="0" id="Image6" /></a>
				</div>
			</div>
		</td>
		<td valign="top"><img src="http://member.canvac.ca/image/separation_menu.gif"/></td>
		<td valign="top">
			<div class="menu_header">
				<a href="http://member.canvac.ca/cores.php" onMouseDown="cores.style.visibility='visible'; news.style.visibility='hidden';   " onMouseOver="cores.style.visibility='visible'; news.style.visibility='hidden';   ">Core Services</a>
				<div id="cores" class="detailnews"> 
					<a href="http://member.canvac.ca/mhc_typing.php" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image22','','http://member.canvac.ca/image/cores2_b.jpg',1)"><img src="http://member.canvac.ca/image/cores2_a.jpg" name="Image22" width="200" height="15" border="0" id="Image22" /></a><br>
					<a href="http://ora.canvac.ca:8888/canvacWebStore" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image23','','http://member.canvac.ca/image/cores3_b.jpg',1)"><img src="http://member.canvac.ca/image/cores3_a.jpg" name="Image23" width="200" height="15" border="0" id="Image23" /></a><br>
					<a href="http://member.canvac.ca/mhc_genomics.php" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image24','','http://member.canvac.ca/image/cores4_b.jpg',1)"><img src="http://member.canvac.ca/image/cores4_a.jpg" name="Image24" width="200" height="15" border="0" id="Image24" /></a><br>
					<a href="http://ora.canvac.ca:8888/canvacWebStore" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image25','','http://member.canvac.ca/image/cores5_b.jpg',1)"><img src="http://member.canvac.ca/image/cores5_a.jpg" name="Image25" width="200" height="15" border="0" id="Image25" /></a><br>
				</div>
			</div>
		</td>
		
		<td valign="top"><img src="http://member.canvac.ca/image/separation_menu.gif"/></td>
		<td valign="top">
			<div class="menu_header">
				<a href="http://member.canvac.ca/legal.php" onMouseDown="cores.style.visibility='hidden'; news.style.visibility='hidden';   " onMouseOver="cores.style.visibility='hidden'; news.style.visibility='hidden';   ">Legal Documents </a>
			</div>
		</td>
		<td valign="top"><img src="http://member.canvac.ca/image/separation_menu.gif"/></td>
		<td valign="top">
			<div class="menu_header">
				<a href="http://member.canvac.ca/sitemap.php" onMouseDown="cores.style.visibility='hidden'; news.style.visibility='hidden';   " onMouseOver="cores.style.visibility='hidden'; news.style.visibility='hidden';   ">Site Map </a>
			</div>
		</td>
		<td valign="top"><img src="http://member.canvac.ca/image/separation_menu.gif"/></td>
		<td valign="top">
			<div class="menu_header">
				<a href="http://www.canvac.ca/publik/frameset.htm" target="_top"onmousedown="cores.style.visibility='hidden'; news.style.visibility='hidden';   " onMouseOver="cores.style.visibility='hidden'; news.style.visibility='hidden';   ">
					<font color="#CC0000">
						<b>
							<font color="#3333FF">
								Log OFF
							</font>
						</b>
					</font>
				</a>
			</div>
		</td>
	</tr>
</table>
</center>
<img src="http://member.canvac.ca/image/cores_mhcclass.jpg">
<table border="0" cellspacing="0" cellpadding="5" width="100%">
  <tbody>
  <tr>
    <td align="left"><div class="document"><a href="<c:url value="/shop/viewCategory.do"/>"><fmt:message key="category.all" /></a></div></td>
    <td align="right"><div class="document"><a href="<c:url value="/shop/viewCart.do"/>"><fmt:message key="jspTop.seeCart" />&nbsp;</a>

<c:if test="${empty accountForm.account}" >
      <a href="<c:url value="/shop/signonForm.do"/>"><fmt:message key="jspTop.signin" />&nbsp;</a>
</c:if>

<c:if test="${!empty accountForm.account}" >
        <a href="<c:url value="/shop/signon.do?signoff=true"/>"><fmt:message key="jspTop.signout" />&nbsp;</a>
        <a href="<c:url value="/shop/editAccountForm.do"/>"><fmt:message key="jspTop.editAccount" />&nbsp;</a>
</c:if>
      <form action="<c:url value="/shop/searchItem.do"/>" method="post">
			  <input type="hidden" name="search" value="true"/>&nbsp;
                <fmt:message key="jspTop.searchItem" />:<input name="keyword"/>
      </form>
	  </div>
    </td>
  </tr>
  </tbody>
</table>

<c:if test="${empty accountForm.account}">
<left>
    <font size="2"><div class="document">You are a visitor ><br />
    <i>Please signin before completing your order<i></font>
</left>
</c:if>

<c:if test="${!empty accountForm.account}">
<left>
    <font size="2" class="paragraphe">You are signed-in with username ><br />
    <i><c:out value="${accountForm.account.userId}"/><i>
    </font>
</left>
</c:if>
</div>

<!-- Rely on the Jakarta HTML tag for hanlding Errors reporting -->


<!-- Rely on HTML tag for Messages reporting for ActionMessage of property: Global_Message-->
<html:messages id="message" message="true" property="<%= org.apache.struts.action.ActionMessages.GLOBAL_MESSAGE %>">
    <center><h2><bean:write name="message"/><h2></center>
</html:messages>

