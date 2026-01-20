<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body>
	<div class="pop-header">
		<div>
			<label>자동신규</label>
		</div>
	</div>
	<div>
		<button type="button" class="close" aria-label="닫기" onclick="popClose()">
		  <span aria-hidden="true">&times;</span>
		</button>
	</div> 
	<div class="container-fluid pop_wrap">
		<div class="az_in_wrap">
			<div class="cb">
				<!-- 시스템 -->		
                <div class="width-30 float-left">
					<div class="margin-5-right">
						<label class="tit_100 poa">시스템</label>
                        <div class="ml_100">
							<input id="txtSysMsg" type="text" class="width-100" readonly="readonly"></input>
						</div>
					</div>
				</div>
				<!-- 서버선택 -->		
                <div class="width-30 float-left" id="divjob" style="display:none">
					<div class="margin-5-left">
						<label class="tit_60 poa text-right">업무</label>
                        <div class="ml_70 tal">
							<div id="cboJob" data-ax5select="cboJob" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100">
						    </div>
						</div>
					</div>
				</div>
				<!-- 프로그램명 -->		
                <div class="width-40 float-left" id="divprog" style="display:none">
					<div class="margin-5-right dib vat" style="width:calc(100% - 110px)">
						<label class="tit_90 poa text-right">프로그램명</label>
                        <div class="ml_100 tal">
							<input id="txtProg" type="text" class="width-100"></input>
						</div>
					</div>
					<input type="checkbox" class='checkbox-pie dib vat margin-5-left' name='chkFind' id='chkFind' data-label="결과내검색">
				</div>
			</div>
		</div>
		<div class="row az_board_basic" style="height: calc(100% - 110px)">
			<div data-ax5grid="regGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
		</div>
		<!-- <div class="row az_board_basic">
			<div data-ax5grid="regGrid2" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="display: none;"></div>
		</div> -->
	</div>
	<div class="float-right dib">
		<button id="btnExcel" class="btn_basic_s">엑셀저장</button>
		<button id="btnQry" class="btn_basic_s">조회</button>
		<button id="btnConfirm" class="btn_basic_s">등록</button>
		<button id="btnClose" class="btn_basic_s margin-10-right">닫기</button>
	</div>
</body>	

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/dev/NewAutoRegisterModal.js"/>"></script>