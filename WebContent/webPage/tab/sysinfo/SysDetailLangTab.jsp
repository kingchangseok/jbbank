<!--  
	* 화면명: 언어정보
	* 화면호출: 시스템정보 -> 언어정보 클릭
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body id="tabBody">
	<div class="az_search_wrap">
		<!--left wrap-->
		<div class="l_wrap margin-5-left" style="height:calc(100% - 40px); width:calc(50% - 10px);">	
			<div class="sm-row">
				<div class="az_board_basic az_board_basic_in" style="height:calc(100% - 70px);">
					<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
				</div>
			</div>
		</div>
		
		<!--right wrap-->
		<div class="r_wrap margin-5-right" style="height:calc(100% - 40px); width:calc(50% - 10px);">
			<div class="sm-row">
				<div class="az_board_basic az_board_basic_in" style="height:calc(100% - 70px);">
					<div data-ax5grid="secondGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
				</div>
			</div>
			
			<div class="sm-row por">
				<div class="dib">
					<label>우선순위적용</label>
					<button id="btnUp" class="btn_basic_s margin-5-left md-font">&#9652;</button>
					<button id="btnDown" class="btn_basic_s margin-5-left md-font">&#9662;</button>
				</div>
				<div class="poa_r dib">
					<button id="btnLang" class="btn_basic_s">언어등록</button>
					<button id="btnAdd" class="btn_basic_s">적용</button>
				</div>
			</div>
		</div>
	</div>
</body>	

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/tab/sysinfo/SysDetailLangTab.js"/>"></script>