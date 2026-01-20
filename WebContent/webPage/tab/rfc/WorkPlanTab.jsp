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

<script type="text/javascript">
	function maxLengthCheck(object) {
		if (object.value.length > object.maxLength){
			object.value = object.value.slice(0, object.maxLength);
		}
		if (object.value < Number(object.min) || object.value > Number(object.max)) {
			object.value = '';
		}
	}
	//커서 아웃될때
	function changeTimeTxt() {
		if (!$('#hourTxt').is(":focus")) {
			if ($('#hourTxt').val() != '' && $('#hourTxt').val().length < 2) {
				$('#hourTxt').val('0'+$('#hourTxt').val());
			} else if ($('#hourTxt').val() == '') {
				$('#hourTxt').val('00');
			}
		}
		if (!$('#minTxt').is(":focus")) {
			if ($('#minTxt').val() != '' && $('#minTxt').val().length < 2) {
				$('#minTxt').val('0'+$('#minTxt').val());
			} else if ($('#minTxt').val() == '') {
				$('#minTxt').val('00');
			}
		}
	}
</script>

<div class="" style="font-size:0;">
    <div class="row">
        <div class="l_wrap dib vat" style="width:100%;">
            <div class="dib vat width-35">
                <label class="dib poa" id="lblISRID">&nbsp;&nbsp;ISR정보</label>
                <div class="ml_80">
                    <input id="txtISRID" type="text" class="width-100" disabled>                  
                </div>
            </div> 
            <div class="dib vat width-25">
                <label class="dib poa" id="lbDOCNO">&nbsp;&nbsp;문서번호</label>
                <div class="ml_60">
                    <input id="txtDOCNO" type="text" class="width-100" disabled>                                                           
                </div>
            </div>     
            <div class="dib vat width-25">
                <label class="dib poa" id="lbREQDT">&nbsp;&nbsp;&nbsp;요청등록일</label>
                <div class="ml_80">
                    <input id="txtREQDT" type="text" class="width-100" disabled>          
                </div>
            </div>                           
        </div>
    </div>

    <div class="row">
        <div class="l_wrap dib vat" style="width:100%;">
            <div class="dib vat width-35">
                <div class="row">
                    <div class="l_wrap dib vat" style="width:100%;">
                        <div class="dib vat width-70">
                            <label class="dib poa" id="lblREQDEPT">&nbsp;&nbsp;요청부서</label>
                            <div class="ml_80">
                                <input id="txtREQDEPT" type="text" class="width-100" class="width-100" disabled>
                            </div>
                        </div>
                        <div class="dib vat width-30">                        
                            <label class="dib poa" id="lbEDITOR">&nbsp;&nbsp;요청인</label>
                            <div class="ml_60">
                                <input id="txtEDITOR" type="text" class="width-100" class="width-100" disabled>                         
                            </div>
                        </div> 
                    </div>                        
                </div>
            </div> 
            <div class="dib vat width-25">
                <label class="dib poa" id="lbSTATUS">&nbsp;&nbsp;상태</label>
                <div class="ml_60">
                    <input id="txtSTATUS" type="text" class="width-100" disabled>                                                           
                </div>
            </div>     
            <div class="dib vat width-25">
                <label class="dib poa" id="lbPROSTATUS">&nbsp;&nbsp;&nbsp;진행현황</label>
                <div class="ml_80">
                    <input id="txtPROSTATUS" type="text" class="width-100" disabled>          
                </div>
            </div>                           
        </div>
    </div>    

    <div class="row">
        <div class="l_wrap dib vat" style="width:100%;">
            <div class="dib vat width-20">
                <label class="dib poa" id="lblEditor">&nbsp;&nbsp;*변경담당</label>
                <div class="ml_80">
                    <div id="cboEditor" data-ax5select="cboEditor" data-ax5select-config="{size:'sm',theme:'primary'}" class="dib width-97" style="min-width:0;"></div>          
                </div>
            </div> 
            <div class="dib vat width-65">
                <label class="dib poa" id="lbJOBRECORD">&nbsp;&nbsp;*작업내역</label>
                <div class="ml_80">
                    <input id="txtJOBRECORD" type="text" class="width-100">                                                           
                </div>
            </div>                             
        </div>
    </div>
    
    <div class="row">
        <div class="l_wrap dib vat" style="width:100%;">
            <div class="dib vat width-20">
                <label class="dib poa" id="lblJOBGBN">&nbsp;&nbsp;*작업구분</label>
                <div class="ml_80">
                    <div id="cboJOBGBN" data-ax5select="cboJOBGBN" data-ax5select-config="{size:'sm',theme:'primary'}" class="dib width-97" style="min-width:0;"></div>          
                </div>
            </div> 
            <div class="dib vat width-20">
                <label class="dib poa" id="lbMONTERM">&nbsp;&nbsp;*모니터링</label>
                <div class="ml_80">
                    <div id="cboMONTERM" data-ax5select="cboMONTERM" data-ax5select-config="{size:'sm',theme:'primary'}" class="dib width-97" style="min-width:0;"></div>
                </div>
            </div>
            <div class="dib vat">
                <label class="dib poa" id="lbJOBTIME">&nbsp;&nbsp;*작업예정일시</label>
                <div class="ml_100">
                    <input id="txtJOBDATE" type="text" style="width:80px;min-width:80px;" readonly>
                    <div id="divPicker" class="az_input_group dib vat" data-ax5picker="jobDate">
                        <input id="dfJOBDATE" name="dfJOBDATE" type="text" placeholder="" style="width:100px;">
                    </div>    
                    <div class="dib timePickerDiv">
                        <input class="numberTxt" type="number" id="hourTxt" min="0" max="23" maxlength="2" oninput="maxLengthCheck(this)" style="width:37px;min-width:37px;">
                        <label style="height: 22px; vertical-align: top; font-size: 12px;">&nbsp;:</label>
                        <input class="numberTxt" type="number" id="minTxt" min="0" max="59" maxlength="2" oninput="maxLengthCheck(this)" style="width:37px;min-width:37px;">
                    </div>                
                </div>
            </div>  
            <div class="dib vat">
                <label class="dib poa" id="lbENDRPTDAY" style="line-height: 13px;">&nbsp;&nbsp;*완료보고서<br>&nbsp;&nbsp;&nbsp;제출예정일</label>
                <div class="ml_80">
                    <input id="txtENDRPTDAY" type="text" style="width:80px;" readonly>  
                    <div id="divPicker" class="az_input_group dib" data-ax5picker="rptDate">
                        <input id="dfENDRPTDAY" name="dfENDRPTDAY" type="text" placeholder="" style="width:100px;">
                    </div>    
                </div>
            </div>   
            <div class="dib vat">
                <button id="btnAddFile" name="btnAddFile" class="btn_basic_s" style="margin-left:5px;">파일첨부</button>
            </div>
        </div>
    </div>

    <div class="row write_wrap">
        <div class="l_wrap dib vat" style="width:100%;">
            <div class="dib vat width-30">
                <label class="dib poa" id="lblJOBDETAIL">&nbsp;&nbsp;*작업내용</label>
                <div class="ml_80">
                    <textarea id="txtJOBDETAIL" name="txtJOBDETAIL" style="align-content:flex-start; width:100%; height:230px; resize: none; overflow-y:auto;"></textarea>
                </div>
            </div> 
            <div class="dib vat width-30">
                <label class="dib poa" id="lbREFMSG">&nbsp;&nbsp;협조사항</label>
                <div class="ml_60">
                    <textarea id="txtREFMSG" name="txtREFMSG" style="align-content:flex-start; width:100%; height:230px; resize: none; overflow-y:auto;"></textarea>
                </div>
            </div> 
            <div class="dib vat width-30">
                <div data-ax5grid="grdFile"
                data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}"
                style="height: 60%;margin-left: 5px;"></div>      
            </div>
            <div class="dib vat width-10">
                <div class="l_wrap dib vat" style="margin-left: 5px;margin-top:180px;">
                    <button id="btnSave" name="btnSave" class="btn_basic_s width-100" style="display: block;"disabled>저장</button>
                    <button id="btnReq" name="btnReq" class="btn_basic_s width-100" disabled>결재상신</button>
                </div>
            </div>                                          
        </div>
    </div>

    <div class="row write_wrap">
        <div class="l_wrap dib vat" style="width:100%;">
            <div class="dib vat width-60">
                <label class="dib poa" id="lblConf">&nbsp;&nbsp;결재/반려의견</label>
                <div class="ml_100">
                    <textarea id="txtConf" name="txtConf" style="align-content:flex-start; width:100%; height:150px; resize: none; overflow-y:auto;"></textarea>
                </div>
            </div>   

            <div class="dib vat width-40">
                <button id="btnConf" name="btnConf" class="btn_basic_s" style="margin-right:5px;" disabled>결재정보</button>
                <button id="btnOk" name="btnOk" class="btn_basic_s" style="margin-right:5px;" disabled>결재</button>
                <button id="btnCncl" name="btnCncl" class="btn_basic_s" disabled>반려</button>
            </div>    
        </div>
    </div>          
</div>


<form name="popPam">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
</form>
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/tab/rfc/WorkPlanTab.js"/>"></script>
