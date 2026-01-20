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
            <div class="row">
                <div class="vat width-100">
                    <label class="dib poa" id="lblChgE">&nbsp;&nbsp;변경담당자</label>
                    <div class="ml_100">
                        <div class="width-100">
                            <div id="cboEditor" data-ax5select="cboEditor" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-20 dib" style="min-width:0;"></div>                 
                        </div>
                    </div>
                </div>
            </div>
            <div class="row por" style="height:80%">
                <div class="az_board_basic" style="height: 100%;">
                    <div data-ax5grid="grdLst"
                    data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 30}"
                    style="height: 100%;"></div>    
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
<script type="text/javascript" src="<c:url value="/js/ecams/tab/rfc/TargetOutputTab.js"/>"></script>
