<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<div class="contentFrame">
	<div id="history_wrap">보고서<strong>&gt; 파일대사 전체조회</strong></div>
      
	<div class="az_search_wrap">
		<div class="az_in_wrap">
			<div class="por">
				<div class="width-100 dib">
					<button id="btnExcel" class="btn_basic_s r_wrap">엑셀저장</button>
					<button id="btnQry" class="btn_basic_s r_wrap" style="margin-right: 5px;">조회</button>
				</div>
			</div>
		</div>
	</div>
	
	<div class="az_board_basic" style="height: 88%;">
		<div id="mainGrid" data-ax5grid="mainGrid" data-ax5grid-config="{}" style="height: 100%;" class="frameResize"></div>
	</div>
	
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/report/FileViewAllReport.js"/>"></script>
	