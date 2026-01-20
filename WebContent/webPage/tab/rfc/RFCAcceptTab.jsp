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
            <div class="dib vat width-55">
                <div class="row">
                    <div class="vat width-100">
                        <div class="dib vat">
                            <div id="hbxWeb" class="vat width-100" style="border: 1px solid #b5b5b5;" disabled="disabled">
                                <input style="width:auto;margin-top:5px;" id="rfcPass" tabindex="8" type="radio" value="01" name="rdiogp" checked="checked">
                                <label id="lbRdo" for="rdo1">RFC접수</label>
                                <input style="width:auto;margin-top:5px;" id="rfcCanc" tabindex="8" type="radio" value="02" name="rdiogp" >
                                <label id="lbRdo" for="rdo2">RFC반려</label>
                            </div>                           
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="vat width-100">
                        <label class="dib poa" id="lbCHGTYPE">&nbsp;&nbsp;*변경종류</label>
                        <div class="ml_100">
                            <div id="cboCHGTYPE" data-ax5select="cboCHGTYPE" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100" style="min-width: 0;"></div>
                        </div>
                    </div>
                </div>                
                <div class="row">
                    <div class="vat width-100">
                        <label class="dib poa" id="lbCHGUSER">&nbsp;&nbsp;*변경담당자</label>
                        <div class="ml_100">
                            <div class="width-100">
                                <input id="txtCHGUSER" type="text" class="width-20">
                                <div id="cboCHGUSER" data-ax5select="cboCHGUSER" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-58 dib" style="min-width:0;margin-left:2%"></div>
                                <button id="btnAddCHGUSER" name="btnAddCHGUSER" class="btn_basic_s width-18" style="margin-left:2%">추가</button>
                            </div>
                        </div>
                    </div>
                </div>  
                <div class="row">
                    <div class="vat width-100">
                        <div data-ax5grid="grdCHGUSER"
                        data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 30}"
                        style="height: 40%;"></div>                        
                    </div>
                </div>                

                <div class="row">
                    <div class="vat width-100">
                        <button id="btnAddFile" name="btnAddFile" class="btn_basic_s dib poa" style="width:80px;">파일첨부</button>
                        <div class="ml_100">
                            <div data-ax5grid="grdFile"
                            data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 30}"
                            style="height: 40%;"></div>      
                        </div>
                    </div>           
                </div>     	                                                                                                                                                            
            </div>
            
            <div class="dib vat width-45">
                <div class="row" style="margin-left: 10px;">
                    <div class="row">
                        <div class="vat width-100">
                            <label class="dib" id="lblMsg">&nbsp;&nbsp;[삭제요령] : 목록에서 삭제대상 선택 후 더블클릭 하시기 바랍니다.</label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="vat width-100">
                            <label class="dib poa" id="lblRecvDt">&nbsp;&nbsp;RFC접수일시</label>
                            <div class="ml_100">
                                <input id="txtRecvDt" type="text" class="width-50" disabled>
                            </div>
                        </div>
                    </div> 
                    <div class="row">
                        <div class="vat width-100">
                            <label class="dib poa" id="lblRecvUser">&nbsp;&nbsp;RFC접수인</label>
                            <div class="ml_100">
                                <input id="txtRecvUser" type="text" class="width-50" disabled>
                            </div>
                        </div>
                    </div>                       
                    <div class="row">
                        <div class="vat width-100">
                            <label class="dib poa" id="lblRecvGbn">&nbsp;&nbsp;RFC접수구분</label>
                            <div class="ml_100">
                                <input id="txtRecvGbn" type="text" class="width-50" disabled>
                            </div>
                        </div>
                    </div>                      
      
                    <div class="row">
                        <div class="vat width-100">
                            <label class="dib" id="lbSayu">&nbsp;&nbsp;접수/*반려의견</label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="vat width-100 write_wrap">
                            <textarea id="txtSayu" name="txtSayu" style="align-content:flex-start; width:100%; height:200px; resize: none; overflow-y:auto;"></textarea>
                        </div>
                    </div>
                    <div class="row">
                        <div class="vat width-100">
                            <div class="r_wrap width-100">
                                <button id="btnReq" name="btnReq" class="btn_basic_s r_wrap width-20" disabled>등록</button>
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
<script type="text/javascript" src="<c:url value="/js/ecams/tab/rfc/RFCAcceptTab.js"/>"></script>
