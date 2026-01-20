<!--  
	* 화면명: 업무정보
	* 화면호출: 시스템정보 -> 업무등록 클릭
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">
	<div class="pop-header">
		<div>
			<label>[업무별GIT연결]</label>
		</div>
		<div>
			<button type="button" class="close" aria-label="닫기" onclick="popClose()">
				<span aria-hidden="true">&times;</span>
			</button>
		</div> 
	</div>
	<div class="container-fluid pop_wrap">
		<!--line1 S-->
		<div class="row">
			<div class="az_board_basic az_board_basic_in" style="height: calc(100% - 180px)">
		    	<div data-ax5grid="jobGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
			</div>
		</div>
		
		<div class="az_search_wrap">
			<div class="az_in_wrap">
				<div class="row">					
					<div class="width-30 dib vat"> 
		              	<label class="tit_70 dib poa text-right">업무코드</label>
		               	<div class="ml_80">
							<input id="txtJobCd" type="text" class="width-100" readonly/>
		               	</div>
					</div>
					<div class="dib vat width-70" >
		              	<label class="tit_70 dib poa text-right">업무명</label>
		               	<div class="ml_80">
							<input id="txtJobName" type="text" class="width-100" readonly/>
		               	</div>
					</div>
				</div>		
				
				<div class="row">					
					<div class="width-50 dib vat"> 
		              	<label class="tit_70 dib poa text-right">프로젝트명</label>
		               	<div class="ml_80">
							<input id="txtPrjName" type="text" class="width-100" />
		               	</div>
					</div>
					<div class="width-20 dib vat">
		              	<label class="tit_70 dib poa text-right">프로젝트ID</label>
		               	<div class="ml_80">
							<input id="txtPrjId" type="text" class="width-100" />
		               	</div>
					</div>
					<div class="dib vat width-30" >
		              	<label class="tit_70 dib poa text-right">Branch</label>
		               	<div class="ml_80">
							<input id="txtBranch" type="text" class="width-100" />
		               	</div>
					</div>
				</div>		
				<div class="row">
					<div class="dib vat" style="width:calc(100% - 92px)" >
		              	<label class="tit_70 dib poa text-right">프로젝트홈</label>
		               	<div class="ml_80">
							<input id="txtPrjHome" type="text" class="width-100" />
		               	</div>
					</div>
					<div class="dib vat tar" >
						<button id="btnReq" class="margin-3-left btn_basic_s">등록</button>
						<button id="btnClose" class="margin-3-left btn_basic_s">닫기</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/sysinfo/GitJobModal.js"/>"></script>