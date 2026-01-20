<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div class="contentFrame">
   <div id="history_wrap">테스트관리 <strong>&gt; 테스트종료</strong></div>
		<div class="az_search_wrap">
			<div class="az_in_wrap sr_status">
	                <div class="por">
	                	<div class="width-25 dib vat">
		                	<label class="tit-150 dib poa">테스트회차</label>
		                	<div class="ml_60">
			                    <div id="cboTest" data-ax5select="cboTest" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:90%;margin-left: 10px;"></div>
		                	</div>
						</div>
						<div class="width-25 dib vat">
		                	<label class="tit-150 dib poa" style="margin-left: 15px">테스트관리자</label>
		                	<div class="ml_100">
								<input id="txtManager" type="text" class="width-100" />
		                	</div>
						</div>
						<div class="width-50 dib vat">
		                	<label class="tit-150 dib poa" style="margin-left: 20px">상태</label>
		                	<div class="ml_60">
								<input id="txtSta" type="text" class="width-100" />
		                	</div>
						</div>
					</div>
			</div>
		</div>
	
	    <div class="az_board_basic" style="height: 38%;">
		    <div data-ax5grid="testEndGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%" class="resize"></div>
	    </div>

		<div class="az_search_wrap">
			<div class="az_in_wrap sr_status">
	                <div class="por">
	                	<div class="width-13 dib vat">
	                        <input id="optOk" name="optOk" tabindex="8" type="checkbox" style="margin-top: 5px;"> 
	                        <label class="dib">&nbsp;정상</label>  
                            <input id="optErr" name="optErr" tabindex="8" type="checkbox" style="margin-top: 5px;"> 
                            <label class="dib">&nbsp;비정상</label>  
						</div>
						<div class="width-32 dib vat">
		                	<label class="tit-150 dib poa" style="margin-left: 15px">완료요청일시</label>
		                	<div class="ml_100">
								<input id="txtEdReqDt" type="text" class="width-100" />
		                	</div>
						</div>
						<div class="width-55 dib vat">
		                	<label class="tit-150 dib poa" style="margin-left: 20px">완료요청</label>
		                	<div class="ml_100">
								<input id="txtEdReqUser" type="text" class="width-100" />
		                	</div>
						</div>
					</div>
	                <div class="por" style="margin-top: 5px;">
	                	<div class="width-13 dib vat">
		                	<label class="tit-150 dib poa" style="margin-top: 5px;">결제/반려의견</label>
						</div>
						<div class="width-32 dib vat">
		                	<label class="tit-150 dib poa" style="margin-left: 15px">완료일시</label>
		                	<div class="ml_100">
								<input id="txtEndDt" type="text" class="width-100" />
		                	</div>
						</div>
						<div class="width-55 dib vat">
		                	<label class="tit-150 dib poa" style="margin-left: 20px">완료구분</label>
		                	<div class="ml_100">
								<input id="txtEndGbn" type="text" class="width-100" />
		                	</div>
						</div>
					</div>
	                <div class="por" style="margin-top: 5px;">
                       <textarea id="txtConf" name="txtConf" style="align-content:flex-start; width:100%; height:70px; resize: none; overflow-y:auto;" ></textarea>
					</div>
                    <button id="btnOk" class="btn_basic_s" style="margin-left: 91%; margin-top: 5px;">결재</button>
                    <button id="btnCncl" class="btn_basic_s margin-5-left"  style="margin-top: 5px;">반려</button>
			</div>
		</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/tab/test/TestCompleteTab.js"/>"></script>