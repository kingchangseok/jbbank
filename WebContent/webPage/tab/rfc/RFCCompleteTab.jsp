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
            <div class="dib vat width-25">
                <label class="dib poa" id="lblEditor">&nbsp;&nbsp;*변경담당자</label>
                <div class="ml_100">
                    <div id="cboEditor" data-ax5select="cboEditor" data-ax5select-config="{size:'sm',theme:'primary'}" class="dib width-97" style="min-width:0;"></div>                                                        
                </div>
            </div> 
            <div class="dib vat width-25">
                <div id="hbxWeb" class="vat width-100" style="border: 1px solid #b5b5b5;">
                    <input style="width:auto;margin-top:5px;" id="optEnd" tabindex="8" type="radio" value="01" name="rdogp" checked="checked">
                    <label id="lbRdo" for="rdo1">정상종료</label>
                    <input style="width:auto;margin-top:5px;" id="optCncl" tabindex="8" type="radio" value="02" name="rdogp" >
                    <label id="lbRdo" for="rdo2">진행중단</label>
                </div>       
            </div>     
            <div class="dib vat width-25">
                <label class="dib poa" id="lblSta">&nbsp;&nbsp;&nbsp;상태</label>
                <div class="ml_60">
                    <input id="txtSta" type="text" disabled>          
                </div>
            </div>                           
        </div>
    </div>

    <div class="row">
        <div class="l_wrap dib vat" style="width:100%;">
            <div class="dib vat width-50">
                <div class="row">
                    <div class="l_wrap dib vat" style="width:100%;">
                        <div class="dib vat width-50">
                            <label class="dib poa" id="lblWriteDay">&nbsp;&nbsp;완료요청일</label>
                            <div class="ml_100">
                                <input id="txtWriteDay" type="text" class="width-100" disabled>
                            </div>
                        </div>
                        <div class="dib vat width-50">                        
                            <label class="dib poa" id="lblEndDate">&nbsp;&nbsp;완료일</label>
                            <div class="ml_80">
                                <input id="txtEndDate" type="text" class="width-100" disabled>                         
                            </div>
                        </div> 
                    </div>                        
                </div>                   
                <div class="row">
                    <div class="l_wrap dib vat" style="width:100%;">
                        <div class="dib vat width-50">
                            <label class="dib poa" id="lblRealTerm">&nbsp;&nbsp;실소요일</label>
                            <div class="ml_100">
                                <input id="txtRealTerm" type="text" class="width-100" disabled>
                            </div>
                        </div>
                        <div class="dib vat width-50">                        
                            <label class="dib poa" id="lblRealMM">&nbsp;&nbsp;실소요공수</label>
                            <div class="ml_80">
                                <input id="txtRealMM" type="text" class="width-100" disabled>                        
                            </div>
                        </div> 
                    </div>                        
                </div> 
                <div class="row">
                    <div class="l_wrap dib vat" style="width:100%;">
                        <div class="dib vat width-50">
                            <label class="dib poa" id="lblMonitor">&nbsp;&nbsp;모니터링기간</label>
                            <div class="ml_100">
                                <input id="txtMonitor" type="text" class="width-100" disabled>
                            </div>
                        </div>
                        <div class="dib vat width-50">                        
                            <label class="dib poa" id="lblRealDay">&nbsp;&nbsp;적용일</label>
                            <div class="ml_80">
                                <input id="txtRealDay" type="text" class="width-100" disabled>                        
                            </div>
                        </div> 
                    </div>                        
                </div> 
                <div class="row">
                    <div class="vat width-100">
                        <label class="dib poa" id="txtRealDay">&nbsp;&nbsp;작업기간</label>
                        <div class="ml_100">
                            <div id="divPicker" class="az_input_group dib" data-ax5picker="botDate">
                                <input id="datTermSt" name="datTermSt" type="text" placeholder="yyyy/mm/dd" style="width:100px;">
                                <span id="cal1" class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
                                <label id="lblTermSt" style="margin: 0 7px">부터</label>
                                <input id="datTermEd" name="datTermEd" type="text" placeholder="yyyy/mm/dd" style="width:100px;">
                                <span id="cal2" class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
                                <label id="lblTermEd" style="margin: 0 7px">까지</label>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row write_wrap">
                    <div class="vat width-100">
                        <label class="dib poa" id="lblEtc">&nbsp;&nbsp;참고사항</label>
                        <div class="ml_100">
                            <textarea id="txtEtc" name="txtEtc" style="align-content:flex-start; width:100%; height:194px; resize: none; overflow-y:auto;"></textarea>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="vat width-100">
                        <div class="r_wrap width-100">
                            <button id="btnReq" name="btnReq" class="btn_basic_s r_wrap width-20" disabled>등록</button>
                            <button id="btnConf" name="btnConf" class="btn_basic_s r_wrap width-20" style="margin-right:5px;" disabled>결재정보</button>
                        </div>
                    </div>           
                </div>                                                                                                                                                 
            </div>
            
            <div class="dib vat width-50">
                <div class="row" style="margin-left: 10px;">
                    <div class="row">
                        <div class="vat width-100">
                            <button id="btnAddFile" name="btnAddFile" class="btn_basic_s width-15">파일첨부</button>
                        </div>
                    </div>       
                    <div class="row">
                        <div class="vat width-100">
                            <div data-ax5grid="grdFile"
                            data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}"
                            style="height: 40%;"></div>                        
                        </div>
                    </div>
                    <div class="row">
                        <div class="vat width-100">
                            <label class="dib" id="lblConf">&nbsp;&nbsp;결재/반려의견</label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="vat width-100 write_wrap">
                            <textarea id="txtConf" name="txtConf" style="align-content:flex-start; width:100%; height:96px; resize: none; overflow-y:auto;"></textarea>
                        </div>
                    </div>
                    <div class="row">
                        <div class="vat width-100">
                            <div class="r_wrap width-100">
                                <button id="btnOk" name="btnOk" class="btn_basic_s r_wrap width-20" disabled>결재</button>
                                <button id="btnCncl" name="btnCncl" class="btn_basic_s r_wrap width-20" style="margin-right:5px;" disabled>반려</button>
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
<script type="text/javascript" src="<c:url value="/js/ecams/tab/rfc/RFCCompleteTab.js"/>"></script>
