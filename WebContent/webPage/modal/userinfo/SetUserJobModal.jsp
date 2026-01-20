<!--  
	* 화면명: 업무권한일괄등록
	* 화면호출: 사용자정보 -> 업무권한일괄등록 클릭
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body>
	<div class="pop-header">
		<div>
			<label>[업무권한일괄등록]</label>
		</div>
		<div>
			<button type="button" class="close" aria-label="닫기" onclick="popClose()">
			  <span aria-hidden="true">&times;</span>
			</button>
		</div> 
	</div>
	<div class="container-fluid pop_wrap">
		<!--line1-->					
		<div class="half_wrap_cb">
			<!--left wrap-->
			<div class="l_wrap width-50">
				<div class="margin-5-right">
					<div>
						<div id="cboUserDiv" data-ax5select="cboUserDiv" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-50 dib" ></div>
						<input id="txtUser" type="text" class="width-49">
					</div>
					<div class="row">
						<div id="cboUser" data-ax5select="cboUser" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-50 dib" ></div>
					    <button id="btnSel" class="btn_basic_s">선택</button>
					</div>		
					
					<div class="row scrollBind">
				     	<ul class="list-group" id="ulUserInfo"></ul>
					</div>	
					<div class="row">
						<input type="checkbox" class="checkbox-setjob float-right" id="chkAllUser" data-label="전체선택"/>
					</div>
				</div>
		 	</div>
		 	<!--right wrap-->
		 	<div class="r_wrap width-50">
		 		<div class="margin-5-left">
					<label class="tit_60 poa">시스템</label>
					<div class="ml_60">
						<div id="cboSysCd" data-ax5select="cboSysCd" data-ax5select-config="{size:'sm',theme:'primary',selecteditable:true}" style="width:100%;" ></div>
					</div>
					<div class="row half_wrap_cb">
						<label class="poa tit_60">업무</label>
						<div class="r_wrap width-100">
							<div class="scrollBind ml_60" style="height: 224px;">
						     	<ul class="list-group" id="ulJobInfo"></ul>
							</div>
						</div>
					</div>	
					<div class="row tar">
						<input type="checkbox" class="checkbox-setjob float-right" id="chkAllJob" data-label="전체선택"/>
					</div>
		 			
				</div>
			</div>
		</div>
		<!--line2-->
		<div class="row az_board_basic" style="height: 53%">
			<div data-ax5grid="jobGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
		</div>
		<!--button-->
		<div class="row tar">
			<button id="btnReq" class="btn_basic_s">등록</button>
			<button id="btnDel" class="btn_basic_s">폐기</button>
			<button id="btnQry" class="btn_basic_s">조회</button>
			<button id="btnExit" class="btn_basic_s">닫기</button>
		</div>
	</div>
</body>	

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/userinfo/SetUserJobModal.js"/>"></script>