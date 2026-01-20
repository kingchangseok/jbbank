<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<% pageContext.setAttribute("replaceChar","\n");%>
<% pageContext.setAttribute("replaceChar2","\t");%>
<% pageContext.setAttribute("replaceChar3","\r");%>
<% pageContext.setAttribute("replaceChar4","\"");%>
<c:set var="replaceChar5" value='\\"'/>
<c:set var="message" >${fn:replace(fn:replace(fn:replace(fn:replace(requestScope['javax.servlet.error.message'],replaceChar, "\\n"),replaceChar2,"\\t"),replaceChar3,"\\r"), replaceChar4, replaceChar5) }</c:set>
{
"status" : "<c:out value="${requestScope['javax.servlet.error.status_code']}"/>","message" : "<c:out value="${message}" escapeXml="false" />"
}
