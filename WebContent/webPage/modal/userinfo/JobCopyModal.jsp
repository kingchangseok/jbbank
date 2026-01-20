<!--  
	* 화면명: 사용자권한복사
	* 화면호출: 사용자정보 -> 권한복사 클릭
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body>
	<div class="pop-header">
		<div>
			<label>[사용자권한복사]</label>
		</div>
		<div>
			<button type="button" class="close" aria-label="닫기" onclick="popClose()">
			  <span aria-hidden="true">&times;</span>
			</button>
		</div> 
	</div>
	<div class="container-fluid pop_wrap">
		<!--line1-->					
		<div class="row por">
			<label class="tit_80 poa">사용자[From]</label>
	        <div class="ml_80">
	        	<input id="txtFromUser" type="text" class="tit_200 margin-left-5" />
	        	<input id="txtFromUserId" type="text" class="tit_200 margin-left-5" style="display: none;"/>
	        	<div id="cboFromUser" data-ax5select="cboFromUser" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="dib width-40" ></div>
			</div>
		</div>
		<!--line2-->					
		<div class="row half_wrap_cb">
			<!--left wrap-->
			<div class="l_wrap width-30">
				<div class="margin-5-right">
					<label class="title_s">담당직무</label>
					
					<div class="poa_r">
						<input type="checkbox" class="checkbox-jobcopy" id="chkAllDuty" data-label="전체선택"/>
					</div>
					
					<div class="row scrollBind" style="height: 80%;">
				     	<ul class="list-group" id="ulDutyInfo"></ul>
					</div>
				</div>
		 	</div>
		 	<!--right wrap-->
		 	<div class="r_wrap width-70">
		 		<div class="margin-5-left">
					<label class="title_s">담당업무</label>
					<div class="row az_board_basic" style="height: 80%;">
						<div data-ax5grid="jobGrid" data-ax5grid-config="{showLineNumber: false, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
					</div>
				</div>
			</div>
		</div>
		<!--line3-->					
		<div class="row por">
			<label class="tit_80 poa">사용자[To]</label>
	        <div class="ml_80">
	        	<input id="txtToUser" type="text" class="tit_200 margin-left-5" />
	        	<input id="txtToUserId" type="text" class="tit_200 margin-left-5" style="display: none;"/>
	        	<div id="cboToUser" data-ax5select="cboToUser" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="dib width-40" ></div>
			</div>
			<div class="poa_r dib vat">
				<button id="btnCopy" class="btn_basic_s">복사</button>
				<button id="btnExit" class="btn_basic_s">닫기</button>
			</div>
		</div>
	</div>
	
	<!-- contener E -->
</body>	
<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/userinfo/JobCopyModal.js"/>"></script>