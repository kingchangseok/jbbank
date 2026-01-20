<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<style>
.ml_100>label {
	font-weight: normal;
	height: 25px;
}

.ml_100>textarea {
	width: 98%;
	border: 0px;
	resize: none;
	margin-top: 5px;
}

.myInfo_title {
    padding-bottom: 5px;
    padding-top: 5px;
    margin-bottom: 10px;
    width: 100%;
    background: #f6f6f6;
}

.myInfo_card {
	/* height: 290px; */
	height: 100%;
	background-color: #fff;
	box-shadow: #DDD 1px 2px 3px 1px;
}

.myInfo_card fieldset{
	height: 100%;
}

.card_contents{
	margin-left: 15px;
}

.title_label{
	font-size: 16px;
	margin-left: 7px;
}

.halfHeight{
	height: calc(50% - 35px);
	min-height: 300px;
}
</style>

<div class="contentFrame">
	<div id="history_wrap" style="background-color: #fff;">기본관리<strong>&gt; MY SR현황 조회</strong></div>
	<div class="az_search_wrap">
		<div style="width:98%;margin:auto; min-width:1004px;" class="halfHeight"><!-- background-color: #fff; padding: 5px 20px;  -->
			<div class="cb">
				<div class="dib myInfo_card" style="width: calc(50% - 14px);"> 
					<fieldset style="border:3px #2477c1;margin: 10px;">
						<div class="myInfo_title" title="본인이 개발담당자인 SR접수 이후의 단기SR 목록"><label class="title_label">진행중인 단기SR [ 건수:<span id="shortCnt">0</span> ]</label></div>
						<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: calc(100% - 65px);"></div>
					</FIELDSET>
				</div>
				
				<div class="dib myInfo_card" style="width: calc(50% - 13px); margin-left: 22px;">
					<fieldset style="border:3px #2477c1;margin: 10px;">
						<div class="myInfo_title" title="본인이 결재 요청한 SR 목록"><label class="title_label">결재대기 SR [ 건수:<span id="approvalCnt">0</span> ]</label> <div class="dib float-right"> <button id="btnReq" class="btn_basic_s margin-5-right" title="">전체조회</button></div> </div>
		               	<div data-ax5grid="secondGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: calc(100% - 65px);"></div>
					</fieldset>
				</div>
			</div>
			
		</div>
		<div style="width:98%;margin:auto; min-width:1004px; margin-top: 12px;" class="halfHeight"><!-- background-color: #fff; padding: 5px 20px;  -->
			<div class="cb">
				<div class="dib myInfo_card" style="width: calc(50% - 14px);"> 
					<fieldset style="border:3px #2477c1;margin: 10px;">
						<div class="myInfo_title" title="본인이 개발담당자인 SR접수 이후의 중장기SR 목록"><label class="title_label">진행중인 중장기SR [ 건수:<span id="lognCnt">0</span> ]</label></div>
						<div data-ax5grid="thirdGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: calc(100% - 65px);"></div>
					</FIELDSET>
				</div>
				
				<div class="dib myInfo_card" style="width: calc(50% - 13px);margin-left: 22px;">
					<fieldset style="border:3px #2477c1;margin: 10px;">
						<div class="myInfo_title" title="본인이 PGM 담당자인 미접수 SR 목록"><label class="title_label">미접수 SR [ 건수:<span id="registerSRCnt">0</span> ]</label></div>
		               	<div data-ax5grid="fourthGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: calc(100% - 65px);"></div>
					</fieldset>
				</div>
			</div>
			
		</div>
	</div>
</div>

<form name="popPam">
	<input type="hidden" name="acptno" />
	<input type="hidden" name="srid" />
	<input type="hidden" name="user" />
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/mypage/MyPage.js"/>"></script>