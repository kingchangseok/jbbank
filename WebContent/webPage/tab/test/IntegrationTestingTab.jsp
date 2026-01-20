<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />


<div class="" style="font-size:0;">
    <div class="row">
        <div class="l_wrap dib vat" style="width:100%;">
            <div class="row">
                <div class="vat width-100">
                    <label class="dib poa" id="lblTestCnt">&nbsp;&nbsp;테스트횟수</label>
                    <div class="ml_60">
                        <div class="width-100">
                            <div id="cboTestCnt" data-ax5select="cboTestCnt" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-20 dib" style="min-width:0;margin-left:2%"></div>
                            <button id="btnExlTmp" name="btnExlTmp" class="btn_basic_s r_wrap width-8">엑셀템플릿</button>
                            <button id="btnExlLoad" name="btnExlLoad" class="btn_basic_s r_wrap width-8" disabled>엑셀로드</button>
                            <button id="btnExlSave" data-grid-control="excel-export" name="btnExlSave" class="btn_basic_s r_wrap width-8" disabled>엑셀저장</button>
                            <button id="btnCaseCp" name="btnCaseCp" class="btn_basic_s r_wrap width-12" disabled>테스트케이스복사</button>                            
                        </div>
                    </div>
                </div>
            </div>
            <div class="row por" style="height:50%">
                <div class="az_board_basic" style="height: 100%;">
                    <div data-ax5grid="grdLst" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 30, rowSelectorColumnWidth : 27}"  style="height: 100%;" class="resize"></div>    
                </div>	
            </div>              
        </div>
    </div>

    <div class="row">
        <div class="l_wrap dib vat" style="width:100%;margin-top: 10px">
            <div class="dib vat width-60">
                <div class="row">
                    <div class="vat width-100">
                        <label class="dib poa" id="lblTestId">&nbsp;&nbsp;테스트 ID</label>
                        <div class="ml_100">
                            <input id="txtTestId" type="text" class="width-40" disabled>
                            <input class="" id="chkOpen" tabindex="8" type="checkbox" value="" style="margin-top: 5px;" name="chkOpen">                            
                            <label class="dib " id="">&nbsp;&nbsp;테스트케이스 신규</label>                            
                            <input class="" id="chkAllPass" tabindex="8" type="checkbox" value="" style="margin-top: 5px;" name="chkAllPass">                            
                            <label class="dib poa" id="">&nbsp;&nbsp;해당사항없음</label>             
                        </div>
                    </div>
                </div>  
                <div class="row">
                    <div class="vat width-100 write_wrap">
                        <label class="dib poa" id="lblCase">&nbsp;&nbsp;*테스트케이스</label>
                        <div class="ml_100">
                            <textarea id="txtCase" name="txtCase" style="align-content:flex-start; width:100%; height:70px; resize: none; overflow-y:auto;"></textarea>
                        </div>
                    </div>
                </div>
                <div id="testTab" class="row">
                    <div class="tab_wrap">
                        <ul class="tabs">
                            <li rel="tabTest" id="tab1" class="on">테스트조건</li>
                            <li rel="tabCheck" id="tab2" >확인사항</li>
                            <div class="r_wrap margin-10-right" style="margin-top: 3px;">
                                <button id="btngrdCondAdd" class="btn_basic_s" >추가</button>
                                <button id="btngrdCondDel" class="btn_basic_s margin-2-left" >제거</button>
                            </div>
                        </ul>
                    </div>
                    <div class="half_wrap" style="height:91%">
                        <div id="tabTest" class="" style="width:100%">
                            <div data-ax5grid="grdCond" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height:200px;" class="resize"></div>
                        </div>
                        <div id="tabCheck" class="" style="width:100%;">   
                            <div data-ax5grid="grdChk" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height:200px;" class="resize"></div>
                        </div>
                    </div>
                </div>                    
            </div>
            
            <div class="dib vat width-40">
                <div class="row" style="margin-left: 10px;">
                    <div class="row">
                        <div class="vat width-100">
                            <label class="dib poa" id="lblTester">&nbsp;&nbsp;테스트수행인</label>
                            <div class="ml_100">
                                <input id="txtTester" type="text" class="width-50" readonly disabled>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="vat width-100">
                            <label class="dib poa" id="lblTestDay">&nbsp;&nbsp;테스트수행일</label>
                            <div class="ml_100">
                                <input id="txtTestDay" type="text" class="width-50" readonly>
                                <div id="divPicker" class="az_input_group dib" data-ax5picker="testdate">
					             	<input id="datTestDay" name="datTestDay" type="text" placeholder="yyyy/mm/dd" style="width:80px;">
					            	<span id="calDatTestDay" class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
                                </div>                            
                            </div>
                        </div>
                    </div>                    
                    <div class="row">
                        <div class="vat width-100 write_wrap">
                            <label class="dib poa" id="lblResult">&nbsp;&nbsp;결과</label>
                            <div class="ml_100">
                                <textarea id="txtResult" name="txtResult" style="align-content:flex-start; width:100%; height:70px; resize: none; overflow-y:auto;"></textarea>
                            </div>
                        </div>
                    </div>                    
                    <div class="row">
                        <div class="vat width-100">
                            <button id="btnTestLog" name="btnTestLog" class="btn_basic_s dib poa" style="margin-left: 15px">테스트Log</button>
                            <div class="ml_100">
                                <div class="vat width-100">
                                    <div data-ax5grid="grdFile"  data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 180px;" class="resize"></div>                        
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="vat width-100">
                            <div class="r_wrap width-100">
                                <button id="btnEnd" name="btnEnd" class="btn_basic_s r_wrap" style="margin-right:5px;">통합테스트종료</button>
                                <button id="btnDel" name="btnDel" class="btn_basic_s r_wrap" style="margin-right:5px;">삭제</button>
                                <button id="btnAdd" name="btnAdd" class="btn_basic_s r_wrap" style="margin-right:5px;">등록</button>
                            </div>
                        </div>           
                    </div>                                                                                            
                </div>
            </div>
        </div>
    </div>	    

</div>


<form name="popPam">
	<input type="hidden" name="code"/>
	<input type="hidden" name="user"/>
    <input type="hidden" name="redcd"/>
    <input type="hidden" name="isrId"/>
</form>
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/tab/test/IntegrationTestingTab.js"/>"></script>
