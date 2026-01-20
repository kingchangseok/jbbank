<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<div class="contentFrame">
  	<div id="history_wrap"></div>
	<div class="az_search_wrap">
		<div class="az_in_wrap">
         	<div class="row">
				<div class="dib vat" style="width:140px;">
					<input id="rdo1" type="radio" name="radio" value="01" checked/>
					<label for="rdo1">노드 #1</label>
					<input id="rdo2" type="radio" name="radio" value="02"/>
					<label for="rdo2">노드 #2</label>
				</div>
				<div class="dib vat" style="width:calc(100% - 650px);">
					<div class="dib vat width-45" style="min-width: 200px">
						<label class="tit_50 poa">인스턴스</label>
	                    <div class="ml_50 vat">
							<div id="cboInstance" data-ax5select="cboInstance" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="dib margin-5-right" style="width: calc(100% - 100px)"></div>
							<input type="checkbox" class="checkbox-pie" id="chkBef" data-label="이전건조회"/>
						</div>
					</div>
					<div class="dib vat width-55">
						<label class="tit_40 poa">일시</label>
						<div class="ml_40 vat">
							<div id="cboDate" data-ax5select="cboDate" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="dib" style="width: calc(100% - 10px)"></div>
						</div>
					</div>
				</div>
				<div class="dib vat float-right" style="width:510px;">
					<label class="tit_80 poa">채널거래제어</label>
					<div class="ml_80 vat">
						<input type="radio" name="radioCh" id="chkOn" data-label="제어"/>
						<input type="radio" name="radioCh" id="chkOff" data-label="해제"/>
						<label class="tit_50 dib vat">리프레시</label>
						<input type="checkbox" class="checkbox-pie" id="chkRef" data-label="On"/>
						<label class="tit_50 dib vat">인스턴스</label>
						<input type="radio" name="radioIn" id="chkStart" data-label="기동"/>
						<input type="radio" name="radioIn" id="chkStop" data-label="종료"/>
						<button id="btnReq" class="btn_basic_s margin-5-left" style="width:60px;">적용</button>
					</div>
				</div>
			</div>
			<div class="row">
				<button id="btnSta" class="btn_basic_s float-right dib vat" style="margin-top: 23px">거래제어 상태조회</button>
				<p class="font_12 fontStyle-bold">* 긴급 변경 - Node1 : 거래제어 수행 ->(반영 후) 리플레쉬 수행 ->거래제어 해제, Node2 :거래제어 수행 ->(반영 후)리플레쉬 수행 ->거래제어 해제</p>
				<p class="font_12 fontStyle-bold">* 정기 변경 - Node1 : 거래제어 수행 ->인스턴스 종료->(반영 후)인스턴스 기동 ->거래제어 해제, Node2 : 거래제어 수행 ->인스턴스 종료->(반영 후)인스턴스 기동 ->거래제어 해제</p>
				<p class="font_12 fontStyle-bold">* 장애 사항 - 해당 Node/업무 인스턴스 선택 -> 장애발생 Node의 인스턴스 거래제어</p>
				
			</div>
		</div>
	</div>
	<div>
	    <div class="az_board_basic az_board_basic_in" style="height: 46%;">
	    	<div data-ax5grid="first-grid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;" class="resize"></div>
		</div>	
	</div>
	<div class="row">
	    <div class="az_board_basic az_board_basic_in" style="height: 30%;">
	    	<div data-ax5grid="second-grid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;" class="resize"></div>
		</div>	
	</div>
</div>

<form name="popPam">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
	<input type="hidden" name="adminYN"/>
	<input type="hidden" name="rgtList"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript"	src="<c:url value="/js/ecams/apply/TransactionControl.js"/>"></script>