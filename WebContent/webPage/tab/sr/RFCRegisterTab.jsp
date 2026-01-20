<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />
<style>
.wCheck-label{
	margin-right : 0px !important;
}

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

<div class="" style="font-size:0;">
    <div class="row">
        <div class="l_wrap dib vat" style="width:100%;">
            <div class="dib vat width-40">
                <div class="row">
                    <div class="vat width-100">
                        <label class="dib poa" id="lblCnt">&nbsp;&nbsp;RFC발행회차</label>
                        <div class="ml_100">
                            <div id="cboCnt" data-ax5select="cboCnt" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-50" style="min-width: 0;"></div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="vat width-100">
                        <label class="dib poa" id="txtCreatdt">&nbsp;&nbsp;RFC발행요청일</label>
                        <div class="ml_100">
                            <input id="txtCreatdt" type="text" class="width-50" disabled>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="vat width-100">
                        <label class="dib poa" id="lblRfcEndDt">&nbsp;&nbsp;RFC발행완료일</label>
                        <div class="ml_100">
                            <input id="txtRfcEndDt" type="text" class="width-50" disabled>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="vat width-100">
                        <label class="dib poa" id="lbUser">&nbsp;&nbsp;RFC발행인</label>
                        <div class="ml_100">
                            <input id="txtEditor" type="text" class="width-50" disabled>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="vat width-100">
                        <label class="dib poa" id="lbSta">&nbsp;&nbsp;상태</label>
                        <div class="ml_100">
                            <input id="txtSta" type="text" class="width-50" disabled>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="vat width-100">
                        <label class="dib poa" id="lbEXPRUNTIME2">&nbsp;&nbsp;*예상소요시간</label>
                        <div class="ml_100">
                            <input id="txtEXPRUNTIME" type="number" class="width-50">
                            <label class="dib poa" id="lbEXPRUNTIME">&nbsp;&nbsp;*시간단위입력</label>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="vat width-100">
                        <label class="dib poa" id="lbEXPMM">&nbsp;&nbsp;예상투입공수</label>
                        <div class="ml_100">
                            <input id="txtEXPMM" type="text" class="width-50" disabled>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="vat width-100">
                        <label class="dib poa" id="" style="visibility: hidden;">&nbsp;&nbsp;</label>
                        <div class="ml_100">
                            <label class="dib poa" id="lbEXPMM2">&nbsp;&nbsp;*M/M단위 입력(소숫점2자리까지 입력)</label>
                            <input id="" type="text" class="width-30" style="visibility:hidden;">
                        </div>
                    </div>
                </div>                
                <div class="row">
                    <div class="vat width-100">
                        <label class="dib poa" id="">&nbsp;&nbsp;완료예정일</label>
                        <div class="ml_100">
                            <div id="divPicker" class="az_input_group dib" data-ax5picker="tabDate">
                                <input id="datEXPDAY" name="datEXPDAY" type="text" placeholder="yyyy/mm/dd" style="width:50%;">
                                <span id="cal1" class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
                                <button id="btnUpdate" name="btnUpdate" class="btn_basic_s width-25" style="margin-left: 10px;">수정</button>
                            </div>
                            <input id="txtEXPDAY" type="text" class="width-30" style="display:none;"> 
                        </div>
                    </div>
                </div>
                <div class="row write_wrap">
                    <div class="vat width-100">
                        <input class="dib poa" id="chkEXPRSC" tabindex="8" type="checkbox" value="" style="margin-top: 5px; display:none;" name="">
                        <label class="dib poa" id="lbEXPRSC">&nbsp;&nbsp;예상소요자원</label>
                        <div class="ml_100">
                            <textarea id="txtEXPRSC" name="txtEXPRSC" style="align-content:flex-start; width:100%; height:160px; resize: none; overflow-y:auto;"></textarea>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="vat width-100">
                        <div class="r_wrap width-100">
                            <button id="btnReq" name="btnReq" class="btn_basic_s r_wrap width-20">결재상신</button>
                            <button id="btnSave" name="btnSave" class="btn_basic_s r_wrap width-20" style="margin-right:5px;">저장</button>
                        </div>
                    </div>           
                </div>     	                                                                                                                                                            
            </div>
            
            <div class="dib vat width-60">
                <div class="row" style="margin-left: 10px;">
                    <div class="row">
                        <div class="vat width-100">
                            <button id="btnAddFile" name="btnAddFile" class="btn_basic_s width-15">파일첨부</button>
                        </div>
                    </div>
                    <div class="row">
                        <div class="vat width-100">
                            <label id="lblDept" class="dib poa">&nbsp;&nbsp;&nbsp;수신부서/파트&nbsp;추가</label>
                        </div>
                    </div>           
                    <div class="row">
                        <div class="vat width-100">
                            <div data-ax5grid="grdFile"
                            data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}"
                            style="height: 60%;"></div>                        
                        </div>
                    </div>
                    <div class="row">
                        <div class="vat width-100">
                            <label class="dib" id="lblConf">&nbsp;&nbsp;결재/반려의견</label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="vat width-100 write_wrap">
                            <textarea id="txtConf" name="txtConf" style="align-content:flex-start; width:100%; height:110px; resize: none; overflow-y:auto;"></textarea>
                        </div>
                    </div>
                    <div class="row">
                        <div class="vat width-100">
                            <div class="r_wrap width-100">
                                <button id="btnOk" name="btnOk" class="btn_basic_s r_wrap width-20" disable>결재</button>
                                <button id="btnCncl" name="btnCncl" class="btn_basic_s r_wrap width-20" style="margin-right:5px;" disable>반려</button>
                                <button id="btnConf" name="btnConf" class="btn_basic_s r_wrap width-20" style="margin-right:5px;" disable>결재정보</button>
                            </div>
                        </div>           
                    </div>                                                                                            
                </div>
            </div>
        </div>
    </div>	
</div>


<form name="popPam">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
</form>
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/tab/sr/RFCRegisterTab.js"/>"></script>
