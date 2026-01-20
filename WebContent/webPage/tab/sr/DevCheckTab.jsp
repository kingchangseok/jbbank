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
                    <div class="ml_50">
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
                    <div data-ax5grid="grdLst" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 30}" style="height: 100%;"></div>    
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
                <div class="row tab_wrap">
                    <div class="vat width-100">
						<!--tab-->
                        <ul class="tabs">
							<li rel="tab1" id="tab1Li" class="on">*테스트조건</li>
							<li rel="tab2" id="tab2Li">*확인사항</li>
						</ul>
                    </div>       
                </div>
                <div class="tab_container" style="height: 35%;">
			      	<div id="tab1" class="tab_content" style="height: 100%;">
						<div class="row half_wrap_cb" style="height: 100%;">
							<div class="sm-row">
								<div class="dib float-left" style="width: calc(100% - 45px); height: 100%;">
									<div class="az_board_basic az_board_basic_in" style="height: 100%;">
										<div data-ax5grid="grdCond" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>	
									</div>
								</div>
								<div class="dib vat poa" style="margin-left: 2px">
									<div><button class="btn_basic_s" id="btnCondAdd" style="width: 45px">추가</button></div>
									<div class="margin-5-top"><button class="btn_basic_s" id="btnCondDel" style="width: 45px">제거</button></div>
								</div>
							</div>
					    </div>
			       	</div>
			       	<div id="tab2" class="tab_content" style="height: 100%;">
						<div class="row half_wrap_cb" style="height: 100%;">
							<div class="sm-row">
								<div class="dib float-left" style="width: calc(100% - 45px); height: 100%;">
									<div class="az_board_basic az_board_basic_in" style="height: 100%;">
										<div data-ax5grid="grdChk" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>	
									</div>
								</div>
							</div>
							<div class="dib vat poa" style="margin-left: 2px">
								<div><button class="btn_basic_s" id="btnChkAdd" style="width: 45px">추가</button></div>
								<div class="margin-5-top"><button class="btn_basic_s" id="btnChkDel" style="width: 45px">제거</button></div>
							</div>
					    </div>
					</div>
			   	</div>
            </div>
            <div class="dib vat width-45">
                <div class="row" style="margin-left: 10px;">
					<div class="row">
                        <div class="vat width-100">
                            <label class="dib poa">&nbsp;&nbsp;테스트수행인</label>
                            <div class="ml_100">
                            	<div class="dib vat">
                            		<input id="txtTester" type="text" style="width:100px;" readonly disabled>
                            	</div>                      
                            </div>
                        </div>
                    </div> 
                    <div class="row">
                        <div class="dib vat width-50">
                            <label class="dib poa">&nbsp;&nbsp;테스트수행일</label>
                            <div class="ml_100">
                                <div id="divPicker" class="az_input_group dib vat" data-ax5picker="testdate">
                                    <input id="datTestDay" name="datTestDay" type="text" placeholder="yyyy/mm/dd" style="width:100px;">
                                    <span id="calDatTestDay" class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
                                </div>                            
                            </div>
                        </div>
                        <div class="dib vat width-50">
                        	<label class="dib poa">&nbsp;&nbsp;*투입시간</label>
                            <div class="ml_70">
                            	<div class="dib vat">
                            		<input id="txtExpTime" type="text" style="width:100px;" readonly>
                            	</div>                      
                            </div>
                        </div>
                    </div> 
                    <div class="row write_wrap">
	                    <div class="vat width-100">
	                        <label class="dib poa" id="lblCase">&nbsp;&nbsp;*최종결과의견</label>
	                        <div class="ml_100">
	                            <div class="width-100">
	                                <textarea id="txtResult" name="txtResult" style="align-content:flex-start; width:100%; height:40px; resize: none; overflow-y:auto; padding: 0px;"></textarea>
	                            </div>
	                        </div>
	                    </div>       
	                </div>
                    <div class="row">
                        <div class="vat width-100">
                        	<div style="display:none;" id="fileSave"></div>
                            <button id="btnTestLog" name="btnTestLog" class="btn_basic_s" disabled>증빙첨부</button>
                        </div>           
                    </div>
                    <div class="row por" style="height:30%">
                        <div class="az_board_basic" style="height: 100%;">
                            <div data-ax5grid="grdFile" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>   
                        </div>	
                    </div>
					<div class="row">
						<div class="vat width-100">
                            <button id="btnDel" name="btnDel" class="btn_basic_s r_wrap" style="margin-left:5px;">삭제</button>
                            <button id="btnReject" name="btnReject" class="btn_basic_s r_wrap" style="margin-left:5px;">검수반려</button>
                            <button id="btnAdd" name="btnAdd" class="btn_basic_s r_wrap" style="margin-left:5px;">등록/수정</button>
                            <button id="btnTestEndding" name="btnAdd" class="btn_basic_s r_wrap">개발검수완료</button>
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
<script type="text/javascript" src="<c:url value="/js/ecams/tab/sr/DevCheckTab.js"/>"></script>