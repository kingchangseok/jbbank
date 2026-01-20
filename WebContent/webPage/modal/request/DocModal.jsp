<!-- 
화면 명: 산출물등록점검
화면호출:
1) 운영배포 > 신청버튼클릭시
 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<style>
	table td {
		border: solid 2px #888;
		font: bold;
		text-align: center;
		background-color: #f8f8f8;
		font-size: 12px;
	}
	
</style>

<body style="width: 100% !important; height: 100% !important; min-height: 630px !important ">
<div class="pop-header">
	<div>
		<label id="lbSub">[산출물등록점검]</label>
	</div>
	<div>
		<button type="button" class="close" aria-label="닫기" onclick="popClose()">
		  <span aria-hidden="true">&times;</span>
		</button>
	</div> 
</div>
<div class="container-fluid pop_wrap">
	<div class="row width-100">
		<div class="por">					
			<div class="width-100 dib vat">
				<table style="width: 100%">
					<tbody>
						<tr> 
							<td rowspan="2" style="width: 60%">1. 업무단위산출물</td>
							<td colspan="2">2. 프로젝트일치여부</td>
							<td rowspan="2">3. 현업인수테스트</td>
						</tr>
						<tr>
							<td>체크인</td>
							<td>체크아웃</td>
						</tr>
						<tr>
							<td style="width: 60%">
								<label id="txtDefineDoc" style="width: calc(100% - 250px)"></label>
								<img alt="azsoft" src="../../../img/add24.png" style="width: 20px; height: 20px; margin-top: 3px; cursor: pointer;" id="btnDocAdd">
								<img alt="azsoft" src="../../../img/minus24.png" style="width: 20px; height: 20px; margin-top: 3px; cursor: pointer;" id="btnDocDel">
							<!-- 	<button id="btnDocAdd" class="btn_basic_s" style="width:30px;">+</button>
								<button id="btnDocDel" class="btn_basic_s" style="width:30px;">-</button> -->
								<button id="btnDefineDoc" class="btn_basic_s" style="width:108px;">표준양식</button>
							</td>
							<td>
								<label id="lblInPrj" style="width: 100%; text-decoration: underline; "></label>
							</td>
							<td>
								<label id="lblOutPrj" style="width: 100%; text-decoration: underline;"></label>
							</td>
							<td>
								<label style="width: 100%; color: #0000FF">대상제외</label>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>	
		
		
		<div class="row width-100">
				<label id="lbUser" class="tit_100" style="margin-left: 10px;">4.QA검증의견</label>
				
				<div class="vat dib" style="float: right;">
					<label id="lblAcpt" style="width:150px" class="tit_100">요청번호 :</label>
				</div>
		</div>	
		
		<div class="row width-100 dib" style="border: 2px solid #E8EEF9; border-radius: 5px;">
			<div class="row width-100">
				<label id="lbJawon" class="tit_120 poa" style="margin-left: 15px;">1. 업무단위산출물</label>
	            <div class="ml_120" style="margin-right: 5px; margin-top: 5px;">
	            	<textarea id="txtDefineQA" class="width-100" rows="8" style="padding: 12px 20px; box-sizing: border-box; border: 1px solid #ddd; background-color: #fff; font-size: 12px; resize: none; height:80px;"></textarea>
				</div>
			</div>	
			<div class="row width-100">
				<label id="lbJawon" class="tit_120 poa" style="margin-left: 15px;">2. 기타</label>
	            <div class="ml_120" style="margin-right: 5px; margin-bottom: 5px;">
	            	<textarea id="txtEtcQA" class="width-100" rows="8" style="padding: 12px 20px; box-sizing: border-box; border: 1px solid #ddd; background-color: #fff; font-size: 12px; resize: none; height:80px;"></textarea>
				</div>
			</div>	
			<button id="btnQa" class="btn_basic_s" style="width:70px; float: right; margin-right: 5px; margin-bottom: 5px;">저장</button>
		</div>
	</div>	
	
	<div class="row width-100 dib" style="border: 2px solid #E8EEF9; border-radius: 5px;">
		<label id="lbJawon" class="tit_130 dib" style="margin-left: 15px;">5. 점검항목별 점검결과</label>
		<div class="az_board_basic" style="height:35%; padding: 5px;">
			<div data-ax5grid="resultGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%"></div>
		</div>	
		<button id="btnResultMSG" class="btn_basic_s" style="width:70px; float: right; margin-right: 5px; margin-bottom: 5px; margin-top: 10px;">저장</button>
	</div>
	<div class="margin-5-top tar">
		<button id="btnRefresh" class="btn_basic_s">최신정보가져오기</button>
		<button id="btnRetryEnd" class="btn_basic_s">재등록완료</button>
		<button id="btnRetry" class="btn_basic_s">재등록통보</button>
		<button id="btnReq" class="btn_basic_s">점검완료</button>
		<button id="btnConf" class="btn_basic_s">신청</button>
		<button id="btnClose" class="btn_basic_s">닫기</button>
	</div>
</div>

<div style="display:none;" id="fileSave"></div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/modal/request/DocModal.js"/>"></script>
	