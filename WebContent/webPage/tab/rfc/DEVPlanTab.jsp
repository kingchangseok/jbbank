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
            <div class="dib vat width-100">
                <div class="row">
                    <div class="vat width-100">
                        <label class="dib poa" id="lblEditor">&nbsp;&nbsp;*변경담당자</label>
                        <div class="ml_60">
                            <div class="width-100">
                                <div id="cboEditor" data-ax5select="cboEditor" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-20 dib" style="min-width:0;margin-left:2%"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="dib vat width-30">
                        <div class="row">
                            <div class="vat width-100">
                                <div class="dib vat">
                                    <div id="hbxWeb" class="vat width-100">
                                        <input style="width:auto;margin-top:5px;" id="optPlan" tabindex="8" type="radio" value="01" name="rdogp" checked="checked">
                                        <label id="lbRdo" for="rdo1">개발계획</label>
                                    </div>                           
                                </div>
                            </div>
                        </div>               
                        <div class="row">
                            <div class="vat width-100">
                                <label class="dib poa" id="lblWritDay">&nbsp;&nbsp;작성일</label>
                                <div class="ml_100">
                                    <div class="width-100">
                                        <input id="txtWriteDay" type="text" class="width-80" disabled>
                                    </div>
                                </div>
                            </div>
                        </div>           
                        <div class="row">
                            <div class="vat width-100">
                                <label class="dib poa" id="lblWriter">&nbsp;&nbsp;작성인</label>
                                <div class="ml_100">
                                    <div class="width-100">
                                        <input id="txtWriter" type="text" class="width-80" disabled>
                                    </div>
                                </div>
                            </div>
                        </div>                                                          
                        <div class="row">
                            <div class="vat width-100">
                                <label class="dib poa" id="lblExpTime">&nbsp;&nbsp;*예상소요시간</label>
                                <div class="ml_100">
                                    <div class="width-100">
                                        <input id="txtExpTime" type="text" class="width-80" placeholder="*시간단위입력">
                                    </div>
                                </div>
                            </div>
                        </div>  
                        <div class="row">
                            <div class="vat width-100">
                                <label class="dib poa" id="lblExpDev">&nbsp;&nbsp;예상개발공수</label>
                                <div class="ml_100">
                                    <div class="width-100">
                                        <input id="txtExpDev" type="text" class="width-80" disabled>
                                    </div>
                                </div>
                            </div>
                        </div> 
                        <div class="row">
                            <div class="vat width-100">
                                <label class="dib poa" id="">&nbsp;&nbsp;</label>
                                <div class="ml_100">
                                    <div class="width-100">
                                        <label class="dib" id="">*MM단위 입력(소수점 2자리까지 가능)</label>
                                    </div>
                                </div>
                            </div>
                        </div>                          
                        <div class="row">
                            <div class="vat width-100">
                                <label class="dib poa" id="lblExpDevSt">&nbsp;&nbsp;예상개발시작일</label>
                                <div class="ml_100">
                                    <div class="width-100">
                                        <div id="divPicker" class="az_input_group dib" data-ax5picker="devst">
                                            <input id="datExpDevSt" name="datExpDevSt" type="text" placeholder="yyyy/mm/dd">
                                            <span id="calDatExpDevSt" class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>  
                        <div class="row">
                            <div class="vat width-100">
                                <label class="dib poa" id="lblExpDevEd">&nbsp;&nbsp;예상개발종료일</label>
                                <div class="ml_100">
                                    <div class="width-100">
                                        <div id="divPicker" class="az_input_group dib" data-ax5picker="deved">
                                            <input id="datExpDevEd" name="datExpDevEd" type="text" placeholder="yyyy/mm/dd">
                                            <span id="calDatExpDevEd" class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>                     
                        <div class="row">
                            <div class="vat width-100">
                                <div class="ml_100">
                                    <button id="" name="" class="btn_basic_s width-40" style="visibility: hidden;"></button>
                                    <button id="btnReq" name="btnReq" class="btn_basic_s" disabled>등록</button>
                                </div>
                            </div>           
                        </div>                                                                                                                                                                                         
                    </div>
                    
                    <div class="dib vat width-70">
                        <div class="dib vat width-30">
                            <div class="row">
                                <div class="vat width-100">
                                    <div class="dib vat">
                                        <div id="hbxWeb" class="vat width-100">
                                            <input style="width:auto;margin-top:5px;" id="optResult" tabindex="8" type="radio" value="01" name="rdogp">
                                            <label id="lbRdo" for="rdo1">개발실적</label>
                                        </div>                           
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="vat width-100">
                                    <label class="dib poa" id="lblReDevDay">&nbsp;&nbsp;*작업일</label>
                                    <div class="ml_80">
                                        <div class="width-100">
                                            <div id="divPicker" class="az_input_group dib" data-ax5picker="devdate">
                                                <input id="datReDevDate" name="datReDevDate" type="text" placeholder="yyyy/mm/dd" style="width:100px;">
                                                <span id="calDatReDevDate" class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>                            
                            <div class="row">
                                <div class="vat width-100">
                                    <label class="dib poa" id="lblReDevTime">&nbsp;&nbsp;*작업시간</label>
                                    <div class="ml_80">
                                        <div class="width-100">
                                            <input id="txtReDevTime" type="number" class="width-50">
                                        </div>
                                    </div>
                                </div>
                            </div>                     
                            <div class="row">
                                <div class="vat width-100">
                                    <label class="dib poa" id="" style="visibility: hidden;">&nbsp;&nbsp;</label>
                                    <div class="ml_80">                                        
                                        <button id="btnAdd" name="btnAdd" class="btn_basic_s" disabled>등록/수정</button>
                                        <button id="btnDel" name="btnDel" class="btn_basic_s" style="margin-left:5px;" disabled>삭제</button>
                                    </div>
                                </div>           
                            </div>      
                            <div class="row">
                                <div class="vat width-100">
                                    <label class="dib poa" id="lbCHGUSER">&nbsp;&nbsp;※ 일별 투입시간을 시간단위로 입력 <br>[개발계획]등록 후 [개발실적]등록가능</label>
                                </div>           
                            </div>                                       
                        </div>
                        <div class="dib vat width-70">
                            <div class="row">
                                <div class="vat width-100">
                                    <label class="dib poa" id="lbWorkTimeSum">&nbsp;&nbsp;작업시간내역 [합계시간 : 0 시간]</label>
                                    <div class="ml_100">
                                        <div class="width-100 r_wrap">
                                            <label class="dib poa r_wrap" id="">&nbsp;&nbsp;변경담당자 전체보기</label>
                                            <input class="r_wrap" id="chkAll" tabindex="8" type="checkbox" value="" style="margin-top: 5px;" name="chkAll">
                                        </div>
                                    </div>                            
                                </div>           
                            </div>                      
                            <div class="row">
                                <div class="vat width-100">
                                    <div data-ax5grid="grdWork"
                                    data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 30}"
                                    style="height: 70%;"></div>                        
                                </div>
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
<script type="text/javascript" src="<c:url value="/js/ecams/tab/rfc/DEVPlanTab.js"/>"></script>
