<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<style>
	.write_wrap dl {
		font-size: 0px;
	}
	
	.row:after {
		content:""; 
		display:block; 
		clear:both;
	}
	
	.checkIcon { /* 툴팁박스 체크 아이콘 */
		content: '';
	    width: 8.8px;
	    height: 6.4px;
	    position: absolute;
	    top: 11px;
	    /* right: 0px; */
	    border: 2px solid #3492db;
	    border-top: none;
	    border-right: none;
	    background: transparent;
	    -webkit-transform: rotate(-50deg);
	    -moz-transform: rotate(-50deg);
	    -ms-transform: rotate(-50deg);
	    -o-transform: rotate(-50deg);
	    transform: rotate(-50deg);
	}

	.tipLabel { /* 툴팁 박스 내부 라벨 */
		line-height: 20px;
		font-size: 12px;
		font-weight: normal;
		letter-spacing: 0.2px;
		margin-left: 15px;
	}

	@media(max-width: 1356px) {
		#TimeLineBox{width: auto !important;}
		.thumbnail {margin-left: 10px;}
	}

	.ztree * {
	    padding: 0;
	    margin: 0;
	    font-size: 12px;
	    font-family: Verdana, Arial, Helvetica, AppleGothic, sans-serif;
	}
</style>

<div style="font-size:0;">
    <div class="row">
        <div class="l_wrap dib vat" style="width:100%;">
            <div class="row">
                <div class="vat width-100">
                    <label class="dib poa">&nbsp;&nbsp;*개발자</label>
                    <div class="ml_60">
                        <div class="width-100">
                            <div id="cboEditor" data-ax5select="cboEditor" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-20 dib" style="min-width:0;margin-left:2%"></div>
                            <button id="btnExlTmp" name="btnExlTmp" class="btn_basic_s r_wrap" style="margin-left:5px;">엑셀템플릿</button>
                            <button id="btnExlLoad" name="btnExlLoad" class="btn_basic_s r_wrap" style="margin-left:5px;" disabled>엑셀로드</button>
                            <button id="btnExlSave" data-grid-control="excel-export" name="btnExlSave" class="btn_basic_s r_wrap" style="margin-left:5px;" disabled>엑셀저장</button>
                            <button id="btnCaseCp" name="btnCaseCp" class="btn_basic_s r_wrap" disabled>테스트케이스복사</button>                            
                        </div>
                    </div>
                </div>
            </div>
            <div class="row por" style="height:30%">
                <div class="az_board_basic" style="height: 100%;">
                    <div data-ax5grid="grdLst"
                    data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 30}"
                    style="height: 100%;"></div>    
                </div>	
            </div>              
        </div>
    </div>

    <div class="row">
        <div class="l_wrap dib vat" style="width:100%;">
            <div class="dib vat width-55">
                <div class="row">
                    <div class="vat width-100">
                        <label class="dib poa" id="lblTestId">&nbsp;&nbsp;테스트 ID</label>
                        <div class="ml_100">
                            <input id="txtTestId" type="text" class="width-20" style="margin-right: 5px" disabled>
                            <input type="checkbox" class="checkbox-open" id="chkOpen" data-label="테스트케이스 신규"/>
                        </div>
                    </div>
                </div>                
                <div class="row">
                    <div class="vat width-100">
                        <label class="dib poa" id="lblWriteDay">&nbsp;&nbsp;작성일</label>
                        <div class="ml_100">
                            <div class="width-100">
                                <input id="txtWriteDay" type="text" class="width-30" disabled>
                                <label class="dib" id="lblWriter">&nbsp;&nbsp;작성인</label>
                                <input id="txtWriter" type="text" class="width-30" disabled>
                            </div>
                        </div>
                    </div>                                        
                </div> 
                <div class="row">
                    <div class="vat width-100">
                        <label class="dib poa" id="lblDevTit">&nbsp;&nbsp;*구분</label>
                        <div class="ml_100">
                            <div class="width-100">
                                <div id="cboTestGbn" data-ax5select="cboTestGbn" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-20 dib"></div>
                            </div>
                        </div>
                    </div>       
                </div>
                <div class="row write_wrap">
                    <div class="vat width-100">
                        <label class="dib poa" id="lblCase">&nbsp;&nbsp;*테스트케이스</label>
                        <div class="ml_100">
                            <div class="width-100">
                                <textarea id="txtCase" name="txtCase" style="align-content:flex-start; width:100%; height:60px; resize: none; overflow-y:auto; padding: 0px;"></textarea>
                            </div>
                        </div>
                    </div>       
                </div>
                <div class="row write_wrap">
                    <div class="vat width-100">
                        <label class="dib poa" id="lblRef">&nbsp;&nbsp;*입력내용</label>
                        <div class="ml_100">
                            <div class="width-100">
                                <textarea id="txtRef" name="txtRef" style="align-content:flex-start; width:100%; height:120px; resize: none; overflow-y:auto; padding: 0px;"></textarea>
                            </div>
                        </div>
                    </div>       
                </div>
                <div class="row write_wrap">
                    <div class="vat width-100">
                        <label class="dib poa" id="lblExpResult">&nbsp;&nbsp;*예상결과</label>
                        <div class="ml_100">
                            <div class="width-100">
                                <input id="txtExpResult" type="text" class="width-100">
                            </div>
                        </div>
                    </div>       
                </div>                                                                                                    
            </div>
            <div class="dib vat width-45">
                <div class="row" style="margin-left: 10px;">
                    <div class="row">
                        <div class="vat width-100">
                            <label class="dib poa" id="lblTestDay">&nbsp;&nbsp;테스트수행일</label>
                            <div class="ml_100">
                            	<div class="dib vat" id="divTestDay" style="display: none"><input id="txtTestDay" type="text" style="width:100px;" readonly></div>
                                <div id="divPicker" class="az_input_group dib vat" data-ax5picker="testdate">
                                    <input id="datTestDay" name="datTestDay" type="text" placeholder="yyyy/mm/dd" style="width:100px;">
                                    <span id="calDatTestDay" class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
                                </div>                            
                            </div>
                        </div>
                    </div> 
                    <div class="row">
                        <div class="vat width-100">
                            <label class="dib poa" id="lblRealResult">&nbsp;&nbsp;테스트결과</label>
                            <div class="ml_100 dib vat float-left" style="border: 1px solid #ddd;">
                            	<input id="optPass" type="radio" name="grpRst" value="P"/>
                            	<label for="optPass">Pass</label>
                                <input id="optFail" type="radio" name="grpRst" value="F"/>
								<label for="optFail">Fail</label>
                            </div>
                        </div>
                    </div>                       
                    <div class="row">
                        <div class="vat width-100">
                        	<div style="display:none;" id="fileSave"></div>
                            <button id="btnTestLog" name="btnTestLog" class="btn_basic_s" disabled>증빙첨부</button>
                            <button id="btnDel" name="btnDel" class="btn_basic_s r_wrap" style="margin-left:5px;" disabled>삭제</button>
                            <button id="btnAdd" name="btnAdd" class="btn_basic_s r_wrap" disabled>등록/수정</button>
                            <button id="btnTestRequest" name="btnTestRequest" class="btn_basic_s r_wrap margin-5-right" style="">검수요청</button>
                        </div>           
                    </div>
                    <div class="row por" style="height:35%">
                        <div class="az_board_basic" style="height: 100%;">
                            <div data-ax5grid="grdFile" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>   
                        </div>	
                    </div>                                                                                                               
                </div>
            </div>
        </div>
    </div>	
</div>

<form name="popPam">
	<input type="hidden" name="code"/>
	<input type="hidden" name="scmuser"/>
	<input type="hidden" name="user"/>
    <input type="hidden" name="redcd"/>
    <input type="hidden" name="isrId"/>
</form>

<form id='ajaxform' method='post' enctype='multypart/form-data' style="display: none;">
	<input type="file" id="excelFile" name="excelFile" accept=".xls,.xlsx" accept-charset="UTF-8" />
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/tab/sr/UnitTestTab.js"/>"></script>