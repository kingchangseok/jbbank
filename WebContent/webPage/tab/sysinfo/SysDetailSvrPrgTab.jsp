<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body id="tabBody">
	<!-- tab E-->
	<div class="half_wrap" style="border: 0px;">
		<!--left wrap-->
		<div class="l_wrap width-50">
			<div class="margin-5-right">
				<div>
					<label class="tit_60 poa">서버종류</label>
	                <div class="ml_60">
						<div id="cboSvrItem" data-ax5select="cboSvrItem" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100 dib"></div>
					</div>
				</div>					
				<!--테이블 S-->
				<div class="row">
					<div class="az_board_basic" style="height: 30%;">
						<div data-ax5grid="svrItemGrid" data-ax5grid-config="{showLineNumber: false, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
					</div>
				</div>
			</div>
	 	</div>
	 	<!--right wrap-->
	 	<div class="r_wrap width-50">	
			<div class="margin-5-left">
				<div class="por">
					<label class="title_s">프로그램종류</label>
					<div class="poa_r">
						<input type="checkbox" class="checkbox-prg" id="chkAllItem" data-label="전체선택"  />
					</div>
				</div>
				<div class="scrollBind row" style="height: 30%;">
	    			<ul class="list-group" id="ulItemInfo"></ul>
	   			</div>
	   		</div>
	   	</div>
	</div>
	<div class="az_in_wrap" style="background: #f8f8f8; height:30px; padding: 0 5px">
	   	<div class="sm-row">	
	   		<label class="tit_70 poa">홈디렉토리</label>	
			<div class="ml_70" >
				<input id="txtVolpath" type="text" style="width:calc(100% - 218px);"></input>
		 		<div class="dib tar">
		 			<button id="btnExl" class="btn_basic_s margin-5-left">엑셀저장</button>
					<button id="btnReqItem" class="btn_basic_s margin-5-left">등록</button>
					<button id="btnClsItem" class="btn_basic_s margin-5-left">폐기</button>
					<button id="btnQryItem" class="btn_basic_s margin-5-left">조회</button>
					<!-- <button id="btnExitItem" class="btn_basic_s margin-5-left" data-grid-control="excel-export">닫기</button> -->
		 		</div>
			</div>
		</div>
	</div>
	
	<!--테이블 S-->
	<div class="row">
		<div class="az_board_basic" style="height: calc(80% - 150px);">
			<div data-ax5grid="itemGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
		</div>
	</div>
	<!--테이블 E-->
	
 </body>
<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/tab/sysinfo/SysDetailSvrPrgTab.js"/>"></script>