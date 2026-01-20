<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp"/>
<c:import url="/js/ecams/common/commonscript.jsp"/>

<script type="text/javascript" src="<c:url value="/js/ecams/register/SRRegister.js"/>"></script>

<!-- contener S -->
<div class="contentFrame">
	<div class="half_wrap" style="height:100%; margin-top:10px;">
	 	<!-- SR등록/접수 START -->
		<iframe id="frmSRRegister" name="frmSRRegister" src='/webPage/tab/sr/SRRegisterTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
	    <!-- SR등록/접수  END -->
	</div>
</div>
<!-- contener E -->