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
                    <div class="l_wrap dib vat" style="width:100%;">
                        <div class="dib vat">
                            <label class="dib poa" id="lbHANDLER">&nbsp;&nbsp;*만족도</label>
                            <div class="ml_80">
                                <div id="hbxWeb" class="vat width-100" style="border: 1px solid #b5b5b5;">
                                    <input style="width:auto;margin-top:5px;" id="rdo1" tabindex="8" type="radio" value="01" name="radiogroup1" checked="checked">
                                    <label id="lbRdo" for="rdo1">매우만족</label>
                                    <input style="width:auto;margin-top:5px;" id="rdo2" tabindex="8" type="radio" value="02" name="radiogroup1" >
                                    <label id="lbRdo" for="rdo2">만족</label>
                                    <input style="width:auto;margin-top:5px;" id="rdo3" tabindex="8" type="radio" value="02" name="radiogroup1" >
                                    <label id="lbRdo" for="rdo3">보통</label>
                                    <input style="width:auto;margin-top:5px;" id="rdo4" tabindex="8" type="radio" value="02" name="radiogroup1" >
                                    <label id="lbRdo" for="rdo4">불만족</label>
                                    <input style="width:auto;margin-top:5px;" id="rdo5" tabindex="8" type="radio" value="02" name="radiogroup1" >
                                    <label id="lbRdo" for="rdo5">매우불만족</label>                                                                                                            
                                </div>     
                            </div>
                        </div>
                    </div>                        
                </div> 
                
                <div class="row">
                    <div class="dib vat width-100">                        
                        <label class="dib poa" id="lblConf">&nbsp;&nbsp;*종료의견</label>
                        <div class="ml_80">
                            <textarea id="txtConf" name="txtConf" style="align-content:flex-start; width:100%; height:300px; resize: none; overflow-y:auto;"></textarea>              
                        </div>
                    </div> 
                </div>
                
                <div class="row">
                    <div class="dib vat l_wrap">
                        <label class="dib poa" id="lblEndDt" style="visibility:hidden;">&nbsp;&nbsp;요청종료일시</label>
                        <div class="ml_80">
                            <input id="txtEndDt" type="text" class="" style="visibility:hidden;">
                        </div>
                    </div>
                    <div class="dib vat r_wrap">
                        <div class="vat dib">
                            <button class="btn_basic_s" id="btnReq" style="margin-left: 0px; margin-right: 0px;">종료등록</button>
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
<script type="text/javascript" src="<c:url value="/js/ecams/tab/sr/SRCompleteByUserTab.js"/>"></script>
