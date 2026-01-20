<!--  
	* 화면명: 사용자정보 전체조회
	* 화면호출: 사용자정보 -> 전체사용자조회 클릭
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body>
	<div class="pop-header">
		<div>
			<label>사용자정보전체조회</label>
		</div>
		<div>
			<button type="button" class="close" aria-label="닫기" onclick="popClose()">
			  <span aria-hidden="true">&times;</span>
			</button>
		</div> 
	</div>
	<div class="container-fluid pop_wrap">
		<div class="half_wrap_cb">
			<div class="width-25 float-left">
				<label class="tit_40 poa">팀명</label>
				<div class="ml_40">
					<div id="cboTeam" data-ax5select="cboTeam" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" style="width:100%;" ></div>
				</div>
			</div>
			<div class="width-25 float-left margin-5-left">
				<label class="tit_60 poa">담당직무</label>
				<div class="ml_60">
					<div id="cboRgtCd" data-ax5select="cboRgtCd" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" style="width:100%;" ></div>
				</div>
			</div>
			<div class="width-25 float-left" style="display: none;">
				<label class="tit_60 poa">사용자명</label>
				<div class="ml_60">
					<input id="txtSearch" type="text" class="width-100">
				</div>
			</div>
			<div class="dib margin-10-left width-25">
		     	<input id="optAll" type="radio" name="userRadio" value="all" checked="checked"/>
				<label for="optAll">전체</label>
				<input id="optActive" type="radio" name="userRadio" value="active"/>
				<label for="optActive">폐쇄사용자제외</label>
				<input id="optInActive" type="radio" name="userRadio" value="inActive"/>
				<label for="optInActive">폐쇄사용자만</label>
			</div>
			<div class="r_wrap width-20 tar">			
		     	<button id="btnExcel" class="btn_basic_s">엑셀저장</button>
				<button id="btnQry" class="btn_basic_s">조회</button>
				<button id="btnExit" class="btn_basic_s">닫기</button>
			</div>
		</div>
		<div class="row az_board_basic" style="height: 88%">
			<div data-ax5grid="userGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
		</div>
	</div>
</body>	

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/userinfo/AllUserInfoModal.js"/>"></script>