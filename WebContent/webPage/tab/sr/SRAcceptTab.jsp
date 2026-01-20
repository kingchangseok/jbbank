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
            <div class="dib vat width-30">
                <div class="row">
                    <div id="hbxWeb" class="vat width-100" style="border: 1px solid #b5b5b5;">
                        <input style="width:auto;margin-top:5px;" id="rdo1" tabindex="8" type="radio" value="01" name="radiogroup1" checked="checked">
                        <label id="lbRdo" for="rdo1">접수</label>
                        <input style="width:auto;margin-top:5px;" id="rdo2" tabindex="8" type="radio" value="02" name="radiogroup1">
                        <label id="lbRdo" for="rdo2">이관</label>
                        <input style="width:auto;margin-top:5px;" id="rdo3" tabindex="8" type="radio" value="03" name="radiogroup1">
                        <label id="lbRdo" for="rdo3">수신파트추가</label>
                    </div>
                </div>
                <div class="row">
                    <div class="vat width-100">
                        <label class="dib poa" id="lblCreatdt">&nbsp;&nbsp;접수일</label>
                        <div class="ml_80">
                            <input id="txtCreatdt" type="text" class="width-100" disabled="disabled">
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="vat width-100">
                        <label class="dib poa" id="lblEditor">&nbsp;&nbsp;접수인</label>
                        <div class="ml_80">
                            <input id="txtEditor" type="text" class="width-100" disabled="disabled">
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="vat width-100">
                        <label class="dib poa" id="lblSta">&nbsp;&nbsp;상태</label>
                        <div class="ml_80">
                            <input id="txtSta" type="text" class="width-100" disabled="disabled">
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="vat width-100">
                        <label class="dib poa" id="lblCateType">&nbsp;&nbsp;*분류유형</label>
                        <div class="ml_80">
                            <div id="cboCateType" data-ax5select="cboCateType" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100" style="min-width: 0;"></div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="vat width-100">
                        <label class="dib poa" id="lblDetCate">&nbsp;&nbsp;*상세보류</label>
                        <div class="ml_80">
                            <div id="cboDetCate" data-ax5select="cboDetCate" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100" style="min-width: 0;"></div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="vat width-100">
                        <label class="dib poa" id="lblJobRank">&nbsp;&nbsp;*작업순위</label>
                        <div class="ml_80">
                            <div id="cboJobRank" data-ax5select="cboJobRank" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100" style="min-width: 0;"></div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="vat width-100">
                        <label class="dib poa" id="lblHandler">&nbsp;&nbsp;*처리담당자</label>
                        <div class="ml_80">
                            <input id="txtHandler" type="text" class="width-30">
                        </div>
                    </div>
                </div> 
                <div class="row">
                    <div class="vat width-100">
                        <label class="dib poa" id="">&nbsp;&nbsp;</label>
                        <div class="ml_80">
                            <div id="cboHandler" data-ax5select="cboHandler" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100" style="min-width: 0;"></div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="vat width-100">
                        <label class="dib poa" id="lblReqOrg">&nbsp;&nbsp;요청기관</label>
                        <div class="ml_80">
                            <input id="txtReqOrg" type="txtReqOrg" class="width-60" readonly>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="vat width-100">
                        <label class="dib poa" id="lblPayYn">&nbsp;&nbsp;결제관련</label>
                        <div class="ml_80">
                            <div id="cboPayYn" data-ax5select="cboPayYn" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-30" style="min-width: 0;"></div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="vat width-100">
                        <label class="dib poa" id="lblCnclYn">&nbsp;&nbsp;반려여부</label>
                        <div class="ml_80">
                            <input id="txtCnclYn" type="text" class="width-30" disabled>
                        </div>
                    </div>
                </div>                                                                                                                                            
            </div>
            
            <div class="dib vat width-70">
                <div class="row" style="margin-bottom: 20px;">
                    <div class="vat width-100">
                        <label id="lblDept" class="dib poa">&nbsp;&nbsp;&nbsp;수신부서/파트&nbsp;추가</label></div>
                    </div><div class="row">
                    <div class="vat width-100">
                        <div class="pop_wrap">			
                            <div class="row scrollBind" style="height: 60%">
                                <div id="divTree">
                                    <ul id="treeDept" class="ztree"></ul>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="vat width-100">
                        <div class="r_wrap width-30" style="margin-right:10px;">
                            <button id="btnReq" name="btnReq" class="btn_basic_s r_wrap width-25">접수</button>
                            <button id="btnCncl" name="btnCncl" class="btn_basic_s r_wrap width-25" style="margin-right:5px;">반려</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row write_wrap">
        <div class="vat width-100">
            <label class="dib poa" id="lblCnclSayu">&nbsp;&nbsp;반려사유</label>
            <div class="ml_80">
                <textarea id="txtCnclSayu" name="txtCnclSayu" style="align-content:flex-start; width:50%; height:45px; resize: none; overflow-y:auto;"></textarea>
            </div>
        </div>
    </div>		
</div>


<form name="popPam">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
</form>
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/tab/sr/SRAcceptTab.js"/>"></script>
