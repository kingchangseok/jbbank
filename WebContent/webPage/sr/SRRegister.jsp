<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp"/>
<c:import url="/js/ecams/common/commonscript.jsp"/>

<script type="text/javascript" src="<c:url value="/js/ecams/sr/SRRegister.js"/>"></script>

<div class="contentFrame">
	<div id="history_wrap"></div>

	<!-- PrjListTab.jsp -->
  	<div class="az_board_basic" style="height: 30%;">
		<div class="l_wrap width-48 dib vat" style="width:100%; height:100%">
   				<iframe id="frmPrjList" name="frmPrjList" src='/webPage/tab/sr/PrjListTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
   		</div>
    </div>        
    
    <!-- SR등록/접수 START -->
    <div class="half_wrap" style="height: 65%;">
		<iframe id="frmSRRegister" name="frmSRRegister" src='/webPage/tab/sr/SRRegisterTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
	</div>
</div>