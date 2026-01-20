<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp"/>

<!-- contener S -->
    <div class="contentFrame">
        <div id="history_wrap"></div>
		<!-- 검색 S-->    
		<div class="az_search_wrap" style="padding-left: 10px; padding-right: 10px;">
			<div class="row">
				<div class="width-30 dib vat">
	               	<label class="tit-150 dib poa">시스템</label>
	               	<div class="ml_100">
	                    <div id="cboSystem" data-ax5select="cboSystem" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" style="width:100%;"></div>
	               	</div>
				</div>
				<div class="width-30 dib vat">
					<label class="tit-150 dib poa">&nbsp;&nbsp;&nbsp;업무</label>
					<div class="ml_100">
						<div id="cboJob" data-ax5select="cboJob" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:100%;"></div>
					</div>
				</div>
				<div class="width-30 dib vat">
					<label class="tit-150 dib poa">&nbsp;&nbsp;&nbsp;관리팀</label>
					<div class="ml_100">
						<div id="cboDept" data-ax5select="cboDept" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100 dib tal"></div>
					</div>
				</div>
				<div class="r_wrap" style="margin-top:3px;">
					<div class="vat dib">
						<button id="btnAuto" name="btnAuto" class="btn_basic_s" style="width:80px;">자동신규</button>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="width-30 dib vat">
					<label class="tit-150 dib poa">프로그램구분</label>
					<div class="ml_100">
						<div id="cboRsrcCd" data-ax5select="cboRsrcCd" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100 dib tal"></div>
					</div>
				</div>
				<div class="width-30 dib vat">
					<label class="tit-150 dib poa">&nbsp;&nbsp;&nbsp;프로그램언어</label>
					<div class="ml_100">
						<div id="cboRsrcGubun" data-ax5select="cboRsrcGubun" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100 dib tal"></div>
					</div>
				</div>
				<div class="width-30 dib vat">
               		<label class="tit-150 dib poa">&nbsp;&nbsp;&nbsp;관리담당자</label>
	               	<div class="ml_100">
						<dd id="txtEditorToolTip"><input id="txtEditor" type="text" class="width-100" readonly/></dd>
	               	</div>
				</div>
				<div class="r_wrap" style="margin-top:3px;">
					<div class="vat dib">
						<button id="btnQry" name="btnQry" class="btn_basic_s" style="width:80px;">조회</button>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="width-60 dib vat">
               		<label class="tit-150 dib poa">프로그램명</label>
	               	<div class="ml_100">
						<input id="txtRsrcName" type="text" class="width-100" placeholder="확장자를 제외한 프로그램명 입력하십시오."/>
	               	</div>
				</div>
				<div class="width-30 dib vat" style="margin-left: 4px;">
               		<label class="tit-150 dib poa">&nbsp;&nbsp;&nbsp;확장자</label>
	               	<div class="ml_100">
						<input id="txtExeName" type="text" placeholder="ex) java, jsp, html"class="width-100" />
	               	</div>
				</div>
				<div class="r_wrap" style="margin-top:3px;">
					<div class="vat dib">
						<button id="btnExec" name="btnExec" class="btn_basic_s" style="width:80px;">등록</button>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="dib" style="width: 90.4%;">
               		<label class="tit-150 dib poa">프로그램설명</label>
	               	<div class="ml_100">
						<input id="txtStory" type="text" class="width-100" />
	               	</div>
				</div>
				<div class="r_wrap" style="margin-top:3px;">
					<div class="vat dib">
						<button id="btnInit" name="btnInit" class="btn_basic_s" style="width:80px;">초기화</button>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="dib" style="width: 90.4%;">
	               	<label class="tit-150 dib poa">프로그램경로</label>
	               	<div class="ml_100">
	                    <div id="cboDir" data-ax5select="cboDir" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:100%;"></div>
	               	</div>
				</div>
				<div class="r_wrap" style="margin-top:3px;">
					<div class="vat dib">
						<button id="btnDel" name="btnDel" class="btn_basic_s" style="width:80px;">삭제</button>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="width-60 dib vat">
					<label class="tit-150 dib poa">적용서버경로</label>
		            <div class="ml_100">
						<input id="txtDsn" type="text" class="width-100" />
		            </div>
				</div>
				<div class="width-30 dib vat" style="margin-left: 4px;">
					<label class="tit-150 dib poa">&nbsp;&nbsp;&nbsp;적용서버홈</label>
		            <div class="ml_100">
						<input id="txtDsnHome" type="text" class="width-100" />
		            </div>
				</div>
			</div>
			<div class="row">
				<div class="width-30 dib vat">
					<label class="tit-150 dib poa">*DOCUMENT</label>
		            <div class="ml_100">
						<input id="rdoYes" type="radio" name="rdoGbn" value="0" checked="checked" /> 
						<label for="rdoYes">YES</label> 
						<input id="rdoNo" type="radio" name="rdoGbn" value="1"/> 
						<label for="rdoNo">NO</label>
		            </div>
				</div>
		        <div class="width-30 dib vat">
					<label class="tit-150 poa fontStyle-cncl">&nbsp;&nbsp;&nbsp;컴파일모드</label>
					<div class="ml_100">
						<div id="cboCompile" data-ax5select="cboCompile" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100 dib tal"></div>
					</div>
				</div>
				<div class="width-30 dib vat">
					<label class="tit-150 poa fontStyle-cncl">&nbsp;&nbsp;&nbsp;메이크모드</label>
					<div class="ml_100">
						<div id="cboMake" data-ax5select="cboMake" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100 dib tal"></div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="width-30 dib vat">
					<p class="txt_r font_12 fontStyle-bold">주) 1.프로그램명은 프로그램명과 확장자를 나눠서 입력하십시오.</p>
				</div>
				<div class="width-30 dib vat">
					<p class="font_12">* 프로그램구분/프로그램언어/업무는 시스템을 기준으로 검색합니다.</p>
				</div>
			</div>
		</div>
		<!-- 게시판 S-->
	    <div class="az_board_basic" style="height:60%;">
	    	<div data-ax5grid="grdProgList" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;" class="resize"></div>
		</div>
	</div>
<!-- contener E -->

<form name="popPam" id="popPam" method="post">
	<INPUT type="hidden" name="UserId" id="UserId"> 
	<INPUT type="hidden" name="SysCd" id="SysCd">
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/dev/ProgRegister.js"/>"></script>