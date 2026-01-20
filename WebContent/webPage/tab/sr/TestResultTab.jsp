<!--  
	* 화면명: 개발계획/실적 등록
	* 화면호출:
	 1) 개발계획/실적 -> 개발계획/실적 탭
	 2) 체크아웃, 체크인 등 SR정보 버튼 클릭 -> 개발계획/실적 탭 
-->

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp"/>
<style>
[data-ax5select] .ax5select-display.input-sm, [data-ax5select] .input-group-sm>.ax5select-display.form-control, [data-ax5select] .input-group-sm>.ax5select-display.input-group-addon, [data-ax5select] .input-group-sm>.input-group-btn>.ax5select-display.btn
{
	min-width : 0px !important;
}

.timeBox{
	padding: 6px;
    border: 1px solid #3473c8;
}
</style>
<!-- contener S -->
<body style="padding: 10px !important; width: 100% !important; min-width: 0px !important;">
    <!-- <div class="content"> -->
		<!-- 하단 S-->
	<div class="l_wrap width-100 vat write_wrap"><!--ver2-->
		<div class="row">
			<dl>
				<dt><label>SR-ID</label></dt>
				<dd>
					<div style="width:30%;" class="dib vat">
						<input id="txtsrid" type="text" style="width: 100%;" disabled="disabled">
					</div>
					<div class="float-right" style="font-size: 0px;" >
						<div class="dib vat">
							<button id="cmdResultTest" class="btn_basic_s" style="width: 110px;" disabled=true>테스트결과첨부</button>
						</div>
					</div>
					
				</dd>
			</dl>
		</div>
		<div class="row">
			<dl>
				<dt><label>SR제목</label></dt>
				<dd>
					<div style="width:100%;" class="dib vat">
						<input id="txttitle" type="text" style="width: 100%;" disabled="disabled">
					</div>
				</dd>
			</dl>
		</div>
		<div class="row">
			<dl>
				<dt><label>테스트문서</label></dt>
				<dd>
					<label id="lblmenu2" style="color: blue; font-size: 12px; width: calc(100% - 79px); display: none; text-algin: left;"> 마우스 우클릭 메뉴를 통해 첨부파일 제거 가능</label>
					<div style="width:100%;" class="dib vat">
						<div class="az_board_basic scroll_h az_board_basic_in" style="height: 290px">
					    	<div data-ax5grid="grdDoc" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
					    </div>
					</div>
				</dd>
			</dl>
		</div>
	</div>
	<div style="display: none;" id="fileSave">
		<input type="file" name="doc" id="doc" accept-charset="UTF-8" multiple="multiple"/>
	</div>
</body>
	
<!-- contener E -->

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/tab/sr/TestResultTab.js"/>"></script>