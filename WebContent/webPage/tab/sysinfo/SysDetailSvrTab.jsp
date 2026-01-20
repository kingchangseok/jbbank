<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<body id="tabBody">
	<div class="half_wrap">
		<!--left wrap-->
		<div class="l_wrap width-50">
			<div class="margin-5-right">
				<div class="sm-row">
					<label class="tit_80 poa">서버종류</label>
	                <div class="ml_80">
						<div id="cboSvr" data-ax5select="cboSvr" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:100%;"></div>
					</div>
				</div>
		 		<div class="sm-row por">
				 	<label class="tit_80 poa">서버명/OS</label>
	                <div class="ml_80">
	                	<input id="txtSvrName" class="vat width-45 dib" type="text" placeholder="hostname/서버구분명칭"></input>
						<div id="cboOs" data-ax5select="cboOs" data-ax5select-config="{size:'sm',theme:'primary'}" class="dib" style="width:calc(55% - 5px);"></div>
					</div>
				</div>
			 	<div class="sm-row half_wrap_cb por">
			 		<label class="tit_80 poa">IP/PORT</label>
				 	<div class="ml_80">
			 			<input id="txtSvrIp" class="width-45" type="text" placeholder="서버 IP"/>
						<input id="txtPort" style="width:calc(55% - 5px);" type="text" placeholder="agent port"/>
						<!-- <input id="txtSeq"  type="text" placeholder="서버순서" style="width:calc(25% - 10px);"/> -->
				 	</div>
				</div>
			 	<div class="sm-row por">
			 		<label class="tit_80 poa">계정/비밀번호</label>
				 	<div class="ml_80">
				 		<input id="txtUser" class="width-45" type="text" placeholder="agent 설치계정"/>
				 		<input id="txtPass" type="password" style="text-security: disc; -webkit-text-security: disc; width:calc(55% - 5px);" autocomplete="off" placeholder="계정 비밀번호" />
				 	</div>
				</div>
			 	<div class="sm-row">
			 		<label class="tit_80 poa">Home-Dir</label>
				 	<div class="ml_80">
			 			<input id="txtHome" class="width-100" type="text"></input>
			 		</div>
			 	</div>
			 	<div class="sm-row">
			 		<label class="tit_80 poa">Agent-Dir</label>
				 	<div class="ml_80">
			 			<input id="txtDir" class="width-100" type="text" placeholder="agent 설치경로"></input>
			 		</div>
			 	</div>
			 	<div class="sm-row">
			 		<label class="tit_80 poa">버퍼사이즈</label>
	                <div class="ml_80">
						<div id="cboBuffer" data-ax5select="cboBuffer" data-ax5select-config="{size:'sm',theme:'primary',selSize:5}" style="width:100%;"></div>
					</div>		 		
			 	</div>
				<!-- <div class="sm-row">
			 		<label class="tit_80 poa">동기화홈경로</label>
			 		<div class="ml_80">
			 			<input id="txtTmp" class="width-100" type="text"></input>
			 		</div>
			 	</div> -->
		 		<div class="sm-row">
		 			<div class="l_wrap width-50">
			 			<input type="checkbox" class="checkbox-IP" id="chkIp" data-label="IP만변경"/>
				 		<input type="checkbox" class="checkbox-IPC" id="chkIpC" data-label="IP변경하여 복사"/>
		 			</div>
					<div class="r_wrap width-50">
				 		<label id="lblAftIp" class="tit_60 poa">변경IP</label>
				 		<div class="ml_60">
					 		<input id="txtAftIp"class="width-100" type="text"></input>
				 		</div>
					</div>		 		
		 		</div>
			</div>
	 	</div>
	 	<!--right wrap-->
	 	<div class="r_wrap width-50">
	 		<div class="margin-5-left">
		 		<div class="por">
		 			<label class="title_s">서버속성</label> 
		 			<div class="poa_r">
			 			<input type="checkbox" class="checkbox-IP" id="chkAllSvr" data-label="전체선택"/>
		 			</div>
		 		</div>
		 		<div class="scrollBind sm-row" style="height:176px;">
					<ul class="list-group" id="ulSyrInfo"></ul>
				</div>
				<!-- <div class="sm-row por">
		 			<label class="title_s">비고</label>
		 		</div>
				<div class="sm-row" style="height:42px;">
					<form>
			        	<textarea id="txtEtc" name="txtEtc" style="width:100%;height:100%;resize: none;padding: 5px 5px; box-sizing: border-box; border: 1px solid #ddd; background-color: #fff; font-size: 12px;"></textarea>
			      	</form>
				</div> -->
		 		<div class="tar">
		 			<div class="dib" id="dibChkBase">
			 			<input type="checkbox" class="checkbox-IP" id="chkBase" data-label="기준서버"/>
		 			</div>
		 			<!-- <input type="checkbox" class="checkbox-IP" id="chkLogView" data-label="서버로그작성"/> -->
		 			<input type="checkbox" class="checkbox-IP" id="chkErr" data-label="장애"/>
		 			<input type="checkbox" class="checkbox-IP" id="chkStop" data-label="일시정지"/>
		 		</div>
		 		<div class="sm-row tar">
					<button id="btnReq" class="btn_basic_s">등록</button>
					<button id="btnUpdt" class="btn_basic_s">수정</button>
					<button id="btnCls" class="btn_basic_s">폐기</button>
					<button id="btnQry" class="btn_basic_s">조회</button>
					<button id="btnExl" class="btn_basic_s">엑셀저장</button>
					<!-- <button id="btnExit" class="btn_basic_s">닫기</button> -->
		 		</div>
		 	</div>
		</div>
	</div>
	<!--테이블 S-->
	<div class="sm-row">
		<div class="az_board_basic" style="height: calc(100% - 280px);">
			<div data-ax5grid="svrInfoGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
		</div>
	</div>
</body>

<script type="text/javascript" src="<c:url value="/scripts/bluebird-3.5.0.min.js"/>"></script>
<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/tab/sysinfo/SysDetailSvrTab.js"/>"></script>