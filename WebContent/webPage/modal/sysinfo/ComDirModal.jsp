<!--  
	* 화면명: 공통디렉토리정보
	* 화면호출: 시스템정보 -> 공통디렉토리 클릭
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">
	<div class="pop-header">
		<div>
			<label>[공통디렉토리정보]</label>
		</div>
		<div>
			<button type="button" class="close" aria-label="닫기" onclick="popClose()">
				<span aria-hidden="true">&times;</span>
			</button>
		</div> 
	</div>
	<div class="container-fluid pop_wrap">
		<div>	      
	        <!-- 검색 S-->    
			<div class="az_search_wrap">
				<div class="az_in_wrap">
					<!-- 시스템 -->		
	                <div>
						<label class="tit_80 poa">시스템</label>
	                    <div class="ml_80">
							<input id="txtSysMsg" class="width-100" type="text"></input>
						</div>
					</div>
	                <div class="sm-row cb">
						<label class="tit_80 poa">디렉토리구분</label>
	                    <div class="ml_80">
							<div id="cboDir" data-ax5select="cboDir" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:100%;"></div>
						</div>
					</div>
					<!-- line2 -->		
	                <div class="sm-row cb">
	                	<div class="float-left width-25">
							<div class="margin-15-right">
								<label class="tit_80 poa">서버IP</label>
			                    <div class="ml_80">
									<input id="txtSvrIp" class="width-100" type="text"></input>
								</div>
							</div>
						</div>
	                	<div class="float-left width-25">
							<div class="margin-15-right">
								<label class="tit_60 poa">Port</label>
			                    <div class="ml_60">
									<input id="txtPort" class="width-100" type="text" maxlength="5"></input>
								</div>
							</div>
						</div>
	                	<div class="float-left width-25">
							<div class="margin-15-right">
								<label class="tit_60 poa">계정</label>
			                    <div class="ml_60">
									<input id="txtUser" class="width-100" type="text"></input>
								</div>
							</div>
						</div>
	                	<div class="float-left width-25">
							<div>
								<label class="tit_80 poa">비밀번호</label>
			                    <div class="ml_80">
									<input id="txtPass" class="width-100" type="text"></input>
								</div>
							</div>
						</div>
					</div>
					<!-- 디렉토리명 -->		
	                <div class="sm-row">
						<label class="tit_80 poa">디렉토리명</label>
	                    <div class="ml_80">
							<input id="txtDir" class="width-100" type="text"></input>
						</div>
					</div>
					<!-- 실행파일명 -->		
	                <div class="sm-row por">
						<label class="tit_80 poa">실행파일명</label>
	                    <div class="ml_80">
							<input id="txtShell" class="width-70" type="text"></input>
							<div class="dib poa_r">
								<button id="btnReq" class="btn_basic_s">등록</button>
								<button id="btnCls" class="btn_basic_s margin-5-left">폐기</button>
								<button id="btnQry" class="btn_basic_s margin-5-left">조회</button>
								<button id="btnExit" class="btn_basic_s margin-5-left">닫기</button>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!--검색E-->
			<div class="sm-row">
				<div class="az_board_basic az_board_basic_in" style="height: 55%">
			    	<div data-ax5grid="dirGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
				</div>
			</div>
		</div>
	</div>
	<!-- contener E -->
</body>	

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/sysinfo/ComDirModal.js"/>"></script>

