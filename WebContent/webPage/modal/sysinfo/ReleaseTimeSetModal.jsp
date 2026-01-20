<!--  
	* 화면명: 정기배포설정
	* 화면호출: 시스템정보 -> 정기배포설정 클릭
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<div class="pop-header">
	<div>
		<label>정기배포설정</label>
	</div>
	<div>
		<button type="button" class="close" aria-label="닫기" onclick="popClose()">
		  <span aria-hidden="true">&times;</span>
		</button>
	</div> 
</div>
<div class="container-fluid pop_wrap">
	<!--line1-->					
	<div class="half_wrap_cb"  style="display:none;">
		<div class="l_wrap width-30">
	     	<input id="optAll"  type="radio" name="releaseChkS" value="all"/>
			<label for="optAll" >전체</label>
			<input id="optRelease" type="radio" name="releaseChkS" value="release"/>
			<label for="optRelease">정기배포대상</label>
			<input id="optUnRelease" type="radio" name="releaseChkS" value="unRelease"/>
			<label for="optUnRelease">정기배포비대상</label>
		</div>
		<div class="r_wrap">
			<label class="tit_60 poa">시스템</label>
			<div class="ml_60">
				<input id="txtSysMsg" type="text" style="width:250px">
				<!-- <button id="btnSearch" class="btn_basic_s">조회</button> -->
			</div>
		</div>
	</div>
	<!--line2-->
	<div class="sm-row az_board_basic" style="height: 85%">
		<div data-ax5grid="releaseGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
	</div>
	<div class="sm-row cb" style="font-size: 0px">
		<div class="l_wrap" style="">
			<input id="optCheck" type="radio" name="releaseChk" value="optCheck"/>
			<label for="optCheck" >전체설정</label>
			<input id="optUnCheck" type="radio" name="releaseChk" value="optUnCheck"/>
			<label for="optUnCheck">전체해제</label>
		</div>
		<div class="l_wrap width-30" style="display:none;">
	     	<input type="checkbox" class="checkbox-rel" id="chkSun" name="date" data-label="일"/>
			<input type="checkbox" class="checkbox-rel" id="chkMon" name="date" data-label="월"/>
			<input type="checkbox" class="checkbox-rel" id="chkTue" name="date" data-label="화"/>
			<input type="checkbox" class="checkbox-rel" id="chkWed" name="date" data-label="수"/>
			<input type="checkbox" class="checkbox-rel" id="chkThu" name="date" data-label="목"/>
			<input type="checkbox" class="checkbox-rel" id="chkFri" name="date" data-label="금"/>
			<input type="checkbox" class="checkbox-rel" id="chkSat" name="date" data-label="토"/>
		</div>
		<div class="width-25 dib vat" style="display:none;">
			<label class="tit_80 poa margin-20-left">빌드시간</label>
			<div class="ml_80 dib" style="width:calc(100% - 80px);">
				<input id="txtBuildTime" type="text" class="timepicker width-100" autocomplete="off" />
			</div>
		</div>
		<div class="width-25 dib vat">
			<label class="tit_80 poa margin-20-left">배포시간</label>
			<div class="ml_80 dib" style="width:calc(100% - 80px);">
				<input id="txtDeployTime" type="text" class="timepicker width-100" autocomplete="off" />
			</div>
		</div>
		<div class="tac float-right">
			<button id="btnSearch" class="btn_basic_s margin-5-right">조회</button>
			<button id="btnReq" class="btn_basic_s">등록</button>
			<button id="btnClose" class="btn_basic_s margin-5-left">닫기</button>
		</div>
	</div>
	<!--button-->
</div>
<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/sysinfo/ReleaseTimeSetModal.js"/>"></script>