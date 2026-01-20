<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />
<link href="https://fonts.googleapis.com/css?family=Noto+Sans+KR:300&display=swap" rel="stylesheet">
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
	width:35%;
	height: 260px;
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
	height: 260px;
	margin-left:-1px;
	overflow: hidden;
	float: none;
	width:25%;
	display: inline-block;
	text-align: left;
}

.timeline #divTimeLine{
	background: #fff;
}

.timeline .item{
	margin-bottom: 6px;
	padding-top: 1px;
	padding-bottom: 1px;
	box-shadow: 1px 1px 2px 1px lightgrey !important;
}

.timeline .item i{
	position: absolute;
    margin-top: 29px;
    margin-left: 26px;
}

.fa-clock:before{
	font-size:16px;
}

.timeline .item_info{
	margin:0px 0 4px 10px;
	white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
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
/*
.piechart{

	margin-left:-1px;
	overflow: hidden;
	width:30%;
	margin-right:7px;
	float : none;
	display: inline-block;
	border : 1px solid #ddd;
}
*/
.card_info {
	float:none;
	height: 60px;
}

.card_info dl{
	width:20%;
	height:60px;
	margin-left: 10px;
	margin-right: 10px;
}


.card_info dl dt{
	width:44%;
	height:60px;
	background: transparent;
	vertical-align: top;
	padding-top:7px;
}
/*
.piechart div:first-child{
	background: transparent !important;
}
*/

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
	padding-top:4px;
	padding-right:30px;
}

.half_wrap_cb{
	text-align: center;
}

.progressbar dl dd{
	margin-left: 0px;
	height:20px;
	margin-top:6px;
	line-height: 21px;
	border-radius: 3px;
	overflow: hidden;
}

.progressbar dl dt{
	height: 7px;
    top: -12px;
    font-family: 'Noto Sans KR', sans-serif;
    font-size: 13px;
    position: relative;
    width: 250px;
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
	font-size: 13px;
	margin-top: 0.5%;
}

.titleLb {
	height: 35px; 
	margin-left: 7px;
	margin-right: 7px;
	border-bottom: 1px solid #ddd;
}

.contntBd {
	border: 2px solid #eee;
}

.scauto {
	overflow: auto;
	height: 215px;
}

tr:first-child > td > .fc-day-grid-event:hover {
	width: 130px;
}
</style>
<div class="contentFrame">
	 <!--line 1-->
	<div class="half_wrap_cb">
	
	<!-- 
		<div class="l_wrap txt_info">
			<ul>
				<li><i class="fas fa-angle-right"></i> 미결<span id="lblApproval">[5]</span></li>
				<li><i class="fas fa-angle-right"></i> SR<span id="lblSr">[5]</span></li>
				<li><i class="fas fa-angle-right"></i> 오류<span id="lblErr">[5]</span></li>
			</ul>
		</div>
	 -->
		<div class="r_wrap card_info margin-10-top">
	        <dl class="mainBorder">
	          <dt><i class="fas angle-double-right"></i></dt>
	          <dd><div style="font-size:24px; color:black;">100</div><div style="font-size:11px;">SR등록</div></dd>
	        </dl>
	        <dl class="mainBorder">
	          <dt><i class="fas fa-file-movie-o"></i></dt>
	          <dd><div style="font-size:24px; color:black;">2</div><div style="font-size:11px;">체크아웃</div></dd>
	        </dl>
	        <dl class="mainBorder">
	          <dt><i class="fas fa-deviantart"></i></dt>
	          <dd><div style="font-size:24px; color:black;">3</div><div style="font-size:11px;">운영배포</div></dd>
	        </dl>
	        <dl class="mainBorder">
	          <dt><i class="fas fa-git"></i></dt>
	          <dd><div style="font-size:24px; color:black;">11</div><div style="font-size:11px;">SR완료</div></dd>
	        </dl>
		</div>
    </div>
    
    <!--line 2-->
	<div class="margin-20-top half_wrap_cb ">
		<!-- 파이차트 -->
		<div class="dib width-22 piechart margin-15-right contntBd vat">
			<div class="panel-body text-center dib margin-20-right" id="pieDiv" style="width: 100%;">
				<div class="titleLb poa" style="z-index: 2;width: 21%;">
					<label class="sm-font" style="vertical-align: middle; margin-top: 0.7%;" id="lblPieTitle"></label>
				</div>
		    	<div id="pieAppliKinds" style="margin-top: 5px;"></div>
		    </div>
		</div>
		
		<!-- 바차트 -->
		<div class="progressbar progDiv contntBd vat">
			<div class="titleLb">
				<label class="sm-font" style="vertical-align: middle;" id=""> SR진행 현황</label>
			</div>
			<div class="scauto">
				<div class="margin-10-right progTop" id="divSrList">
				</div>
			</div>
		</div>
		
		<!-- 타임라인 -->
		<div class="timeline timelineDiv contntBd">
			<div class="titleLb" style="text-align: center !important;">
				<label class="sm-font" style="vertical-align: middle;" id="">타임라인</label>
			</div>
			<div class="scauto">
				<div class="margin-5-left margin-5-right timeline_box" id="divTimeLine">
					
				</div>
			</div>
		</div>
		
	</div>
	
	<!--line 3-->
	<div class="margin-15-top half_wrap_cb">
		<!-- 캘린더 -->
		<div class="width-42 dib vat margin-15-right contntBd" id="calBg">
			<div class="titleLb">
				<label class="sm-font" style="vertical-align: middle;" id="">업무 캘린더</label>
			</div>
			<div id="divCal width-100" style="padding: 10px;">
				<div id='calendar'></div>
			</div>
		</div>	
		
		<!-- 라인차트 -->
		<div class="container-fluid dib contntBd">
			<div class="titleLb">
				<label class="sm-font" style="vertical-align: middle;" id="">월별 업무현황</label>
			</div>
			<div id="line-chart" class="width-100"></div>
		</div>
	</div> 
	
	
</div>
<form name="popPam">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
</form>
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/main/eCAMSMainPie.js"/>"></script>