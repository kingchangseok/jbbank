<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />
<style>
.sel {
	min-width: 220px;
}
.width-23 {
	width: calc(26% - 38px);
}
</style>

<!-- contener S -->
<div id="wrapper">
    <div class="contentFrame">
        <!-- history S--> 
        <div id="history_wrap">관리자 <strong>&gt; 접속자정보</strong></div>
        <!-- history E-->    
        <!-- 검색 S-->    
		<div class="az_search_wrap">
			<div class="az_in_wrap">
				<div class="row r_wrap">
					<button class="btn_basic_s" id="btnSession">session out</button>
					<button class="btn_basic_s margin-5-left" id="btnSearch" style="width:70px;">조회</button>
				</div>
			</div>
		</div>
		
		<div class="az_board_basic" style="height:85%">
	    	<div id="mainGrid" data-ax5grid="mainGrid" data-ax5grid-config="{}" style="width:100%; height: 100%;" class="resize">
		
			</div>
		</div>
    </div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/administrator/WebConnectorList.js"/>"></script>