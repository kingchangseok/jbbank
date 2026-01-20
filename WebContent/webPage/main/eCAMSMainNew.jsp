<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<style>

.progressbar .org{
	background: #ff9a9a;

}
.progressbar .green{
	background: #8bffde;
}

.progressbar .blue{
	background: #9aa2ff;
}

.progDiv{
	width:30%;
	height: 85%;
	margin-right:20px;
	float: none;
	display:inline-block;
	text-align: center;
}

::-webkit-scrollbar {
	display:none;
}

.progTop {
	display: inline-block;
	text-align: left;
	width: 100%;
}

.timeline{
	height: 85%;
	margin-left:-1px;
	overflow: hidden;
	float: none;
	width:30%;
	display: inline-block;
	text-align: left;
}

.timeline #divTimeLine{
	background: #fff;
}

.timeline .item{
	margin-bottom: 12px;
	box-shadow: 1px 1px 4px 1px #e5e5e5 !important;
}

.timeline .item i{
	position: absolute;
    margin-top: 29px;
    margin-left: 26px;
}

.fa-clock:before{
	font-size:13px;
}

.timeline .item_info{
	margin: 0px 0 -3px 10px;
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
    padding-bottom: 4px;
}

.timeline .item_info p{
	display: inline-block;
    font-size: 13px;
    width: 81px;
    margin-left: 40px;
    padding: 4px;
}

.timeline .item_info div {
	padding: 3px;
	font-family: 'Noto Sans KR', sans-serif;
	font-size: 13px;
}

.timeline .item:before{
	background-color: #fff;
}

.timeline .item_info small{
	font-size:12px;
	
}

.timeline .timeline_box h4{
	font-size:13px;
	margin-bottom:10px;
	color: #666;
}

#divCal{
	padding:8px;
	width:36%;
	margin-right:35px !important;
	display:inline-block;
	border : 1px solid #ddd;
	float: none;
	vertical-align: top;
	
}

td.fc-more-cell{
	background-color: transparent !important;
}
.card_info {
	float:none;
	height: 100px;
}

.card_info dl{
	width:17.5%;
	height:80px;
	margin: 0 10px;
	border: 1px solid #eee;
}


.card_info dl dt{
	width:44%;
	height:78px;
	background: transparent;
	vertical-align: top;
	padding-top:7px;
	background: #fff;
}

.tui-chart .tui-chart-tooltip-area .tui-chart-tooltip .tui-chart-default-tooltip{
    -webkit-border-radius: 3px !important;
    border-radius: 3px !important;
    background-clip: padding-box !important;
    background-color: #5f5f5f !important;
    background-color: rgba(85, 85, 85, 0.95) !important;
}

.angle-double-right:before{
	content: "\f101";
    font-size: 46px;
    background: #fff;
    color: #39afd1;
    border-radius: 20px;
}

.fa-deviantart:before{
	content: "\f1bd";
    font-size: 46px;
    background: #fff;
    color: #39afd1;
    border-radius: 20px;
}

.fa-git:before{
    font-size: 46px;
}

.fa-file-movie-o:before{
    font-size: 46px;
}
.card_info dl dd{
	width:50%;
	text-align: right;
	padding: 0 0 0 0;
	padding-top:13px;
	padding-right:30px;
}

.half_wrap_cb{
	text-align: center;
}

.progressbar dl dd{
	margin-left: 0px;
	height:20px;
	margin-top:20px;
	line-height: 21px;
	border-radius: 3px;
	overflow: hidden;
}

.progressbar dl dt{
	height:20px;
	top:-22px;
}

.progressbar dl dd span{
	height:20px !important;
   	border-top-left-radius: 3px;
   border-bottom-left-radius: 3px;
}

.mainBorder {
	box-shadow: 1px 1px 4px 1px lightgrey !important;
}

.sm-font {
	font-family: 'Noto Sans KR', sans-serif;
	font-size: 14px;
	margin-top: 0.5%;
	text-align: left !important;
	color: #000;
	line-height: 37px;
}

.titleLb {
	height: 40px; 
	margin-left: 7px;
	margin-right: 7px;
	border-bottom: 1px solid #ddd;
}

.contntBd {
	border: 1px solid #eee;
}

.scauto {
	overflow: auto;
	height: 90%;
}

.scauto2 {
	overflow: auto;
	height: 35%;
}

tr:first-child > td > .fc-day-grid-event:hover {
	width: 130px;
}

.mainBorder{box-shadow: none;}
.angle-double-right:before,
.fa-file-movie-o:before,
.fa-deviantart:before,
.fa-git:before{font-size: 46px; color: #39afd;}
.card_info dl dt{padding-top: 16px;}
</style>
<link href="https://fonts.googleapis.com/css?family=Noto+Sans+KR:300&display=swap" rel="stylesheet">



<div class="contentFrame">
	 <!--line 1-->
	<div class="half_wrap_cb">
		<div class="r_wrap card_info">
			<dl class="mainBorder">
				<dt>
					<i class="fas angle-double-right"></i>
				</dt>
				<dd>
					<div style="font-size:24px; color:black;" id="approvalCnt">0</div>
					<div style="font-size:13px;color: #95aac9;">미결</div>
				</dd>
			</dl>
		<dl class="mainBorder">
			<dt>
				<i class="fas angle-double-right"></i>
			</dt>
			<dd>
				<div style="font-size:24px; color:black;" id="srRegCnt">100</div>
				<div style="font-size:13px;color: #95aac9;">등록</div>
			</dd>
		</dl>
		<dl class="mainBorder">
			<dt>
				<i class="fas fa-file-movie-o"></i>
			</dt>
			<dd>
				<div style="font-size:24px; color:black;" id="devSrCnt">2</div>
				<div style="font-size:13px;color: #95aac9;">개발</div>
			</dd>
		</dl>
		<dl class="mainBorder">
			<dt>
				<i class="fas fa-deviantart"></i>
			</dt>
			<dd>
				<div style="font-size:24px; color:black;" id="testSrCnt">3</div>
				<div style="font-size:13px;color: #95aac9;">테스트</div>
			</dd>
		</dl>
		<dl class="mainBorder">
			<dt>
				<i class="fas fa-git"></i>
			</dt>
			<dd>
				<div style="font-size:24px; color:black;" id="appySrCnt">11</div>
				<div style="font-size:13px;color: #95aac9;">적용</div>
			</dd>
			</dl>
		</div>
	</div>
    
    <!--line 2-->
	<div class=" half_wrap_cb">
		<div class="progressbar progDiv contntBd vat">
			<div class="titleLb">
				<label class="sm-font" style="vertical-align: middle;" id=""> SR진행 현황</label>
			</div>
			<div class="scauto">
				<div class="margin-10-right progTop" id="divSrList">
				</div>
			</div>
		</div>
		
		<div class="timeline timelineDiv contntBd">
			<div class="titleLb tac">
				<label class="sm-font" style="vertical-align: middle;" id="">타임라인</label>
			</div>
			<div class="scauto2">
				<div class="margin-5-left margin-5-right timeline_box" id="divTimeLine">
				</div>
			</div>
			
			<div class="titleLb tac">
				<label class="sm-font" style="vertical-align: middle;" id="">업무캘린더</label>
			</div>
			<div class="scauto2" style="height: 55%">
				<div id='calendar' style="height: 100%"></div>
			</div>
		</div>
		
		<div class="dib width-30 piechart margin-15-right contntBd vat" style="height: 85%">
			<div class="titleLb tac">
				<label class="sm-font" style="vertical-align: middle;" id="">신청 목록</label>
			</div>
			<div class="panel-body text-center dib margin-20-right" id="areaDiv" style="width: 100%;">
		    	<div id="chart-area" style="margin-top: 5px;"></div>
		    </div>
		    <div class="titleLb tac">
				<label class="sm-font" style="vertical-align: middle;" id="pieLabel">프로그램 신청 현황</label>
			</div>
			<div class="panel-body text-center dib margin-20-right" id="pieDiv" style="width: 100%;">
		    	<div id="pieAppliKinds" style="margin-top: 5px;"></div>
		    </div>
		</div>
	</div>
	
	
</div>
<form name="popPam">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
</form>
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/main/eCAMSMainNew.js"/>"></script>