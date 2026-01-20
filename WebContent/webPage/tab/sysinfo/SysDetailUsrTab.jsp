<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body id="tabBody">
	<div class="half_wrap">
		<!--left wrap-->
		<div class="l_wrap width-70">
			<div class="l_wrap width-50">
				<div class="margin-5-right">
					<div>
						<label class="tit_60 poa">서버종류</label>
		                <div class="ml_60">
							<div 	id="cboSvrUsr" data-ax5select="cboSvrUsr" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100 dib"></div>
						   <!--  <div class="dib vat poa_r">
							    <input type="checkbox" class="checkbox-usr" id="chkAllUsr" data-label="전체선택"  />
						    </div> -->
						</div>
					</div>					
					<!--테이블 S-->
					<div class="row">
						<div class="az_board_basic" style="height: 30%;">
							<div data-ax5grid="svrUsrGrid" data-ax5grid-config="{showLineNumber: false lineNumberColumnWidth: 40}" style="height: 100%;"></div>
						</div>
					</div>
				</div>
			</div>
			<div class="r_wrap width-50">
				<div class="margin-5-left">
					<div class="por">
						<label id="lblSvrUsr" class="title_s">사용업무</label>
						<div class="poa_r">
							<input type="checkbox" class="checkbox-usr" id="chkAllSvrJob" data-label="전체선택"  />
						</div>
					</div>
					<div class="scrollBind row" style="height: 30%;">
		   				<ul class="list-group" id="ulSvrInfo"></ul>
		   			</div>
		   		</div>
			</div>
	 	</div>
	 	<!--right wrap-->
	 	<div class="r_wrap width-30">
	 		<div class="margin-10-left">
				<label class="title_s">계정정보</label>
			 	<div style="margin-top:5px;">
			 		<label class="tit_80 poa">계정</label>
				 	<div class="ml_80">
				 		<input id="txtSvrUsr"class="width-100" type="text" />
				 	</div>
				</div>				 			
			 	<div class="row">
			 		<label class="tit_80 poa">그룹</label>
				 	<div class="ml_80">
			 			<input id="txtGroup" class="width-100" type="text"></input>
			 		</div>
			 	</div>				 			
			 	<div class="row">
			 		<label class="tit_80 poa">권한</label>
				 	<div class="ml_80">
			 			<input id="txtMode" class="width-100" type="text" maxlength="3"></input>
			 		</div>
			 	</div>				 			
			 	<div class="row">
			 		<label class="tit_80 poa">DB계정</label>
				 	<div class="ml_80">
			 			<input id="txtDbUsr" class="width-100" type="text" autocomplete="off"></input>
			 		</div>
			 	</div>				 			
			 	<div class="row">
			 		<label class="tit_80 poa">DB비밀번호</label>
				 	<div class="ml_80">
			 			<input id="txtDbPass" class="width-100" type="password" autocomplete="off"></input>
			 		</div>
			 	</div>
			 	<div class="row">
			 		<label class="tit_80 poa">DB연결자</label>
				 	<div class="ml_80">
			 			<input id="txtDbConn" class="width-100" type="text"></input>
			 		</div>
			 	</div>
		 		<div class="row tar" style="margin-top:10px;">
					<button id="btnReqUsr" class="btn_basic_s">등록</button>
					<button id="btnUsrClose" class="btn_basic_s">폐기</button>
					<button id="btnQryUsr" class="btn_basic_s">조회</button>
					<!-- <button id="btnExitUsr" class="btn_basic_s">닫기</button> -->
		 		</div>
		 	</div>
		</div>
	</div>
	<!--테이블 S-->
	<div class="az_board_basic row" style="height: calc(80% - 120px);">
		<div data-ax5grid="accGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
	</div>
</body>
<!--테이블 E-->

<script type="text/javascript" src="<c:url value="/scripts/bluebird-3.5.0.min.js"/>"></script>
<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams//tab/sysinfo/SysDetailUsrTab.js"/>"></script>