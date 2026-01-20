
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />


<body style="width: 100% !important; height: 100% !important; min-height: 600px !important ">
	<div class="pop-header">
		<div>
			<label class="margin-5-left">[SR업무]</label>
		</div>
		<div>
			<button type="button" class="close" aria-label="닫기" onclick="popClose()">
				<span aria-hidden="true">&times;</span>
			</button>
		</div> 
	</div>
	<div class="container-fluid pop_wrap">
		<div class="half_wrap">
			<!--left wrap-->
			<div class="margin-5-right">
				<div class="sm-row">
					<label class="tit_80 poa">SR업무명</label>
	                <div class="ml_80">
						<input type="text" id="txtSrTitle" value="" style="width: 100%;"/>
					</div>
				</div>
		 		<div class="sm-row por" style="font-size: 0px;">
				 	<div class="dib width-50" >
					 	<label class="tit_80 poa">시스템</label>
		                <div class="ml_80">
							<div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary'}" class="dib" style="width: calc(100% - 9px);"></div>
						</div>
				 	</div>
					<div class="dib width-50 vat">
						<label class="tit_80 poa" style="padding-left: 5px;">BA</label>
					 	<div class="ml_80">
							<div id="cboBA" data-ax5select="cboBA" data-ax5select-config="{size:'sm',theme:'primary'}" class="dib" style="width: 100%;"></div>
					 	</div>
					</div>
				</div>
				<!-- -->
		 		<div class="sm-row por" style="font-size: 0px; display:none;">
				 	<div class="dib width-50" >
					 	<label class="tit_80 poa">PGM담당자</label>
		                <div class="ml_80">
							<div id="cboPGM" data-ax5select="cboPGM" data-ax5select-config="{size:'sm',theme:'primary'}" class="dib" style="width: calc(100% - 9px);"></div>
						</div>
				 	</div>
					<div class="dib width-50 vat">
						<label class="tit_80 poa" style="padding-left: 5px;">개발책임자</label>
					 	<div class="ml_80">
							<div id="cboDevMaster" data-ax5select="cboDevMaster" data-ax5select-config="{size:'sm',theme:'primary'}" class="dib" style="width: 100%;"></div>
					 	</div>
					</div>
				</div>
			 	
		 		<div class="sm-row">
			 		<div class="sm-row tar">
						<button id="btnClear" class="btn_basic_s">초기화</button>
						<button id="btnReq" class="btn_basic_s">등록</button>
						<button id="btnQry" class="btn_basic_s">조회</button>
			 		</div>
			 	</div>
			</div>
		</div>
		<!--테이블 S-->
		<div class="sm-row">
			<div class="az_board_basic" style="height: calc(100% - 220px);">
				<div data-ax5grid="srListGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
			</div>
		</div>
			
		<div class="sm-row float-right">
			<button id="btnDel" class="btn_basic_s">삭제</button>
			<button id="btnExit" name="btnExit" class="btn_basic_s tar">닫기</button>
		</div>
	</div>
</body>
<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/sysinfo/SysSrJob.js"/>"></script>

