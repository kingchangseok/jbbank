<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<body style="min-width: auto;">
	<div style="overflow-y: hidden;">
	<div class="az_search_wrap" style="padding: 10px;">
		<div class="row">
			<div class="width-85 dib vat">
	        	<label id="lbHandlerDept" class="tit_100 poa tac">주관부서-담당자</label>
			    <div class="ml_110 dib az_board_basic" style="width: 80%; height: 20%;">
				    <div data-ax5grid="deptGrid" style="height: 100%;"></div>
				</div>
			</div>
			<div class="dib vat" style="width: calc(15% - 10px);">
				<label id="lbTestDat" class="tit_100 poa">Test일자</label>
				<div class="ml_60">
					<input class="width-100 dib" id="txtDatTest" type="text"></input>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="width-95 dib vat margin-10-top">
				<label id="lbCnclSayu1" class="tit_100 poa tac" style="line-height: 17px">신규 및 변경<br>요구사항</label>
				<textarea id="txtEtcSayu" name="txtEtcSayu" class="ml_110" rows="14" cols="75" style="width: calc(100% - 45px); padding: 5px; box-sizing: border-box; border: 1px solid #ddd; background-color: #fff; font-size: 12px; resize:none; overflow-y:auto;"></textarea>
			</div>
		</div>
		<div class="row">
			<div class="width-95 dib vat margin-10-top">
				<label id="lbTestResult" class="tit_100 poa tac">TEST결과</label>
				<div class="dib ml_110" style="border: 1px solid #ddd; padding-left: 5px;">
					<input id="radio0" type="radio" name="rdoGbn1" value="01" checked="checked" /> <label for="radio0">적정</label>
					<input id="radio1" type="radio" name="rdoGbn1" value="02" /> <label for="radio1">부적합</label> 
					<input id="radio2" type="radio" name="rdoGbn1" value="03" /> <label for="radio2">추가개선요함</label>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="width-95 dib vat margin-10-top">
				<label id="lbCnclSayu2" class="tit_100 poa tac" style="line-height: 17px">부적합 사유 및<br>추가개선 요구사항</label>
				<textarea id="txtCnclSayu" name="txtCnclSayu" class="ml_110" rows="2" cols="75" style="width: calc(100% - 45px); padding: 5px; box-sizing: border-box; border: 1px solid #ddd; background-color: #fff; font-size: 12px; resize:none; overflow-y:auto;"></textarea>
			</div>
		</div>
		<div class="margin-10-top">
			<div class="dib vat margin-5-right float-right">
				<button id="btnUpdt" class="btn_basic_s float-right">수정</button>
	        </div>
		</div>
	</div>
	</div>
</body>
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/tab/request/ProgramTestTab.js"/>"></script>
