<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<link href="/css/ecams/common/NotoSansKR.css" rel="stylesheet" type="text/css">
<title>Insert title here</title>
</head>
<link rel="stylesheet" href="/css/ecams/common/toolTip2.css">
<style type="text/css">
	.lib{
		display:inline-block;
	}
	
	input[type="button"]{
		cursor: pointer;
	}

    #topMenu{
    	width: 100%;
    	min-width:1524px;
		height: 76px;
		box-shadow: 0 2px 3px 0 rgba(0, 0, 0, 0.1);
		background-color: #ffffff;
    }
    
    #underMenu{
		margin-top:3px;
		width: 100%;
		height: 100%;
		display:inline-block;
    }
    
    #logo{
		width: 89px;
		height: 28px;
		font-family: Noto Sans KR;
		font-size: 24px;
		font-weight: normal;
		font-stretch: normal;
		font-style: normal;
		line-height: normal;
		letter-spacing: -0.44px;
		text-align: center;
		color: #1a1a1a;
    }

	.topMenu .dIcon{
		margin-left: 60px;
		display: inline-block;
		cursor: pointer;
	}
	
	.topMenu .dIcon.dIcon-first{
		margin-top:15px;
		margin-left: 55px;
		display: inline-block;
	}
	
    .dIcon .menuIcon {
    	margin-left: 10px;
		width: 26px;
		height: 26px;
		object-fit: contain;
	}
	
	.dIcon .menuIcon.menu1{
		background-image: url(zeplin_img/nav-icon-1.svg);
	}
	.dIcon:hover .menuIcon.menu1{
		background-image: url(zeplin_img/nav-icon-1-selected.svg);
	}
	
	.dIcon .menuIcon.menu2{
		background-image: url(zeplin_img/nav-icon-2.svg);
	}
	.dIcon:hover .menuIcon.menu2{
		background-image: url(zeplin_img/nav-icon-2-selected.svg);
	}
	
	.dIcon .menuIcon.menu3{
		background-image: url(zeplin_img/nav-icon-3.svg);
	}
	.dIcon:hover .menuIcon.menu3{
		background-image: url(zeplin_img/nav-icon-3-selected.svg);
	}
	
	.dIcon .menuIcon.menu4{
		background-image: url(zeplin_img/nav-icon-4.svg);
	}
	.dIcon:hover .menuIcon.menu4{
		background-image: url(zeplin_img/nav-icon-4-selected.svg);
	}
	
	.dIcon .menuIcon.menu5{
		background-image: url(zeplin_img/nav-icon-5.svg);
	}
	.dIcon:hover .menuIcon.menu5{
		background-image: url(zeplin_img/nav-icon-5-selected.svg);
	}
	
	.dIcon .menuIcon.menu6{
		background-image: url(zeplin_img/nav-icon-6.svg);
	}
	.dIcon:hover .menuIcon.menu6{
		background-image: url(zeplin_img/nav-icon-6-selected.svg);
	}
	
	.dIcon .menuIcon.menu7{
		background-image: url(zeplin_img/nav-icon-7.svg);
	}
	.dIcon:hover .menuIcon.menu7{
		background-image: url(zeplin_img/nav-icon-7-selected.svg);
	}
	
	.dIcon .menuIcon.menu8{
		background-image: url(zeplin_img/nav-icon-8.svg);
	}
	.dIcon:hover .menuIcon.menu8{
		background-image: url(zeplin_img/nav-icon-8-selected.svg);
	}
	
	.dIcon .menuIcon.menu9{
		background-image: url(zeplin_img/nav-icon-9.svg);
	}
	.dIcon:hover .menuIcon.menu9{
		background-image: url(zeplin_img/nav-icon-9-selected.svg);
	}
	
	.txt1{
		width: 50px;
		height: 19px;
		font-family: Noto Sans KR;
		font-size: 13px;
		font-weight: normal;
		font-stretch: normal;
		font-style: normal;
		line-height: normal;
		letter-spacing: -0.24px;
		text-align: center;
		color: #3b3b3b;
		border:none;
		cursor: pointer;
	}
	
	.txt1:hover{
		color: #2471c8;
	}
	
	#middelMenu{
		margin-top:3px;
		width: 100%;
		height: 265px;
		background-color: #f8f8f8;
	}
		
	.nav-alarm-icon {
		width: 26px;
		height: 26px;
		object-fit: contain;
	}
	
	.line {
		width: 1px;
		height: 26px;
		border: solid 1px #cccccc;
		margin: 0 15px;
		background-color: #cccccc;
	}
	
	#errCnt{
		width: 9px;
		height: 22px;
		font-family: Noto Sans KR;
		font-size: 15px;
		font-weight: normal;
		font-stretch: normal;
		font-style: normal;
		line-height: normal;
		letter-spacing: -0.28px;
		text-align: center;
		color: #ffffff;
	}
	
	#eRR{
		width: 28px;
		height: 22px;
		font-family:  Noto Sans KR;
		font-size: 15px;
		font-weight: normal;
		font-stretch: normal;
		font-style: normal;
		line-height: normal;
		letter-spacing: -0.28px;
		text-align: center;
		color: #d20a0a;
		margin-right:9px;
	}
	
	#srCnt{
		width: 18px;
		height: 17px;
		font-family: Noto Sans KR;
		font-size: 15px;
		font-weight: normal;
		font-stretch: normal;
		font-style: normal;
		line-height: normal;
		letter-spacing: -0.28px;
		text-align: center;
		color: #ffffff;
	}
	
	#SR{
		width: 18px;
		height: 22px;
		font-family: Noto Sans KR;
		font-size: 15px;
		font-weight: normal;
		font-stretch: normal;
		font-style: normal;
		line-height: normal;
		letter-spacing: -0.28px;
		text-align: center;
		color: #2471c8;
		margin-right:9px;
	}
	
	.Rectangle-3 {
		position: relative;
		width: 212px;
		height: 194px;
		border-radius: 5px;
		box-shadow: 0 2px 10px 0 rgba(0, 0, 0, 0.15);
		background-color: #ffffff;
		margin-right:10px; 
		float:left;
		margin-top : 36px;
		cursor: pointer;
	    -webkit-transition: margin 0.3s cubic-bezier(.165,.84,.44,1);
	    transition: margin 0.3s cubic-bezier(.165,.84,.44,1);
	}
	
	.Rectangle-3:last-child {
		margin-right:0px; 
	}
	
	.Rectangle-3:hover {
		margin-top:26px;
	    -webkit-transition: margin 0.3s cubic-bezier(.165,.84,.44,1);
	    transition: margin 0.3s cubic-bezier(.165,.84,.44,1);
	}
	
	.Rectangle-3:hover:before, .Rectangle-3:hover:after {
		-webkit-transform-origin: left top;
	    transform-origin: left top;
	    -webkit-transform: scaleX(1);
	    transform: scaleX(1);
	}
	
	.Rectangle-3:hover:after {
	    -webkit-transform-origin: right center;
	    transform-origin: right center;
	}
	.Rectangle-3:before,.Rectangle-3:after  {
	position: absolute;
    top: 6px;
    height: 2px;
    content: '';  
    border-radius: 2px;
  	background-color: #2471c8;
    -webkit-transform: scaleX(0);
    transform: scaleX(0);
    -webkit-transition: -webkit-transform 0.3s cubic-bezier(0.67, 0.18, 0.85, 0.5);
    transition: -webkit-transform 0.3s cubic-bezier(0.67, 0.18, 0.85, 0.5);
    transition: transform 0.3s cubic-bezier(0.67, 0.18, 0.85, 0.5);
    transition: transform 0.3s cubic-bezier(0.67, 0.18, 0.85, 0.5),-webkit-transform 0.3s cubic-bezier(0.67, 0.18, 0.85, 0.5);
	}
	
	.Rectangle-3:before {
	    left: calc(50% - 1px);
	    width: 99px;
	    -webkit-transform-origin: left top;
	    transform-origin: left top;
	}
	.Rectangle-3:after {
		left: 7px;
	    width: 99px;
	    content: '';  
	    -webkit-transform-origin: right center;
	    transform-origin: right center;
	}
	
	.Rectangle-3 .icon {
		width: 50px;
		height: 50px;
		object-fit: contain;
		display: block;
		margin-top:20px;
		margin-left:21px;
		cursor: pointer;
	}
	
	.Rectangle-3 .icon.list-icon {
		background-image: url("zeplin_img/board-icon-1.svg");
	}
	.Rectangle-3:hover .icon.list-icon {
		background-image: url("zeplin_img/board-icon-1-selected.svg");
	}
	
	.Rectangle-3 .icon.register-icon {
		background-image: url("zeplin_img/board-icon-2.svg");
	}
	.Rectangle-3:hover .icon.register-icon {
		background-image: url("zeplin_img/board-icon-2-selected.svg");
	}
	
	.Rectangle-3 .icon.dev-icon {
		background-image: url("zeplin_img/board-icon-3.svg");
	}
	.Rectangle-3:hover .icon.dev-icon {
		background-image: url("zeplin_img/board-icon-3-selected.svg");
	}
	
	.Rectangle-3 .icon.test-icon {
		background-image: url("zeplin_img/board-icon-4.svg");
	}
	.Rectangle-3:hover .icon.test-icon {
		background-image: url("zeplin_img/board-icon-4-selected.svg");
	}
	
	.Rectangle-3 .icon.apploval-icon {
		background-image: url("zeplin_img/board-icon-5.svg");
	}
	.Rectangle-3:hover .icon.apploval-icon {
		background-image: url("zeplin_img/board-icon-5-selected.svg");
	}
	
	.Rectangle-3 .textBox{
		float: right; 
		display: block; 
		margin-top: 40px; 
		margin-right: 20px;
	}
	.Rectangle-3 .textBox.single{
		width: 45px;
	}
	
	.middleText{
		width: 30px;
		height: 24px;
		font-family: Noto Sans KR;
		font-size: 16px;
		font-weight: 500;
		font-stretch: normal;
		font-style: normal;
		line-height: normal;
		letter-spacing: -0.16px;
		text-align: center;
		color: #222222;
	}
	
	.middleCnt{
		width: 17px;
		height: 41px;
		font-family: Noto Sans KR;
		font-size: 28px;
		font-weight: bold;
		font-stretch: normal;
		font-style: normal;
		line-height: normal;
		letter-spacing: -0.28px;
		text-align: center;
		color: #2471c8;
	}
	
	.middelDBCnt{
		font-family: Noto Sans KR;
		font-size: 18px;
		font-weight: normal;
		font-stretch: normal;
		font-style: normal;
		line-height: normal;
		letter-spacing: -0.18px;
		text-align: center;
		color: #666666;
	}
	
	.underLeftMenu{
		width: 110px;
		height: 500px;
		border-radius: 5px;
		background-color: #2471c8;
		display: inline-block;
	}
	
	.leftBar div .underIcon{
		margin-top:19px;
		margin-left : 14px;
		width: 20px;
		height: 20px;
		opacity: 0.7;
	}
	
	.leftBar.on div .underIcon, .leftBar:hover div .underIcon{
		object-fit: contain;
		opacity: 1;
	}
	
	.leftBar div label{
		margin-top:19px;
		margin-left : 9px;
		width: 26px;
		height: 20px;
		opacity: 0.7;
		font-family: Noto Sans KR;
		font-size: 14px;
		font-weight: 500;
		font-stretch: normal;
		font-style: normal;
		line-height: normal;
		letter-spacing: -0.2px;
		color: #ffffff;
		float: right;
		cursor: pointer;
	}
	
	.leftBar.on div label, .leftBar:hover div label{
		opacity: 1;
	}
	
	.underRightMenu {
		width: 100%;
		height: 58px;
		background: rgba(234,234,234, 0.41);
		border-radius: 5px;
		display: inline-block;
		display: inline-block;
	}
	
	.underLb1{
		width: 93px;
		height: 27px;
		font-family: Noto Sans KR;
		font-size: 18px;
		font-weight: bold;
		font-stretch: normal;
		font-style: normal;
		line-height: normal;
		letter-spacing: -0.18px;
		color: #222222;
	}
	
	.underButton{
		width: 60px;
		height: 24px;
		border-radius: 1px;
		border: solid 1px #bbbbbb;
		font-family: Noto Sans KR;
		font-size: 13px;
		font-weight: normal;
		font-stretch: normal;
		font-style: normal;
		line-height: normal;
		letter-spacing: -0.13px;
		text-align: center;
		color: #666666;
		margin-right:24px;
		float: right;
		cursor: pointer;
	}
	
	.srBasicText{
		height: 19px;
		font-family:  Noto Sans KR;
		font-size: 13px;
		font-weight: normal;
		font-stretch: normal;
		font-style: normal;
		line-height: normal;
		letter-spacing: -0.2px;
		text-align: left;
		color: #222222;	
		margin-top : 5px;
		float:left;
		width: 100%;
		white-space: nowrap;
		overflow: hidden;
		display: block;
		text-overflow: ellipsis;
		cursor: pointer;
	}

	.timeline-check:{
		width: 12px;
		height: 12px;
		object-fit: contain;
	  }
  
  .timeLineText{
		width: 240px;
		height: 20px;
		font-family: Noto Sans KR;
		font-size: 14px;
		font-weight: bold;
		font-stretch: normal;
		font-style: normal;
		line-height: normal;
		letter-spacing: -0.28px;
		text-align: center;
		color: #2471c8;
  	}
	
	.oval1{
		width: 6px;
		height: 6px;
		background-color: #cde4ff;
		border-radius: 50%;
	}
	
	.oval2{
		width: 25px;
		height: 25px;
		border: solid 1px #f3f3f3;
		background-color: #f5f4f4;
		border-radius: 50%;
		margin-right: 25px;
		float: right;
	}
	
	.timeLineBase{
		width: 122px;
		height: 19px;
		font-family: Noto Sans KR;
		font-size: 13px;
		font-weight: 500;
		font-stretch: normal;
		font-style: normal;
		line-height: normal;
		letter-spacing: -0.25px;
		color: #222222;
		margin-left: 5px;
	}
	
	.timeLineBase2{
		width: 105px;
		height: 18px;
		font-family: Noto Sans KR;
		font-size: 12px;
		font-weight: normal;
		font-stretch: normal;
		font-style: normal;
		line-height: normal;
		letter-spacing: -0.4px;
		text-align: right;
		color: #666666;
	}
	
	.circle-icon {
		width: 3px;
		height: 14px;
		object-fit: contain;
		margin-left: 11px;
		margin-top: 6px;
	}
	
	.topMenu{
		width:1100px; 
		height: 100%; 
		float:left;
	}
	
	.ilb {
		display: inline-block;
	}
	
	.subTitle{
		width: 26px; 
		height: 26px; 
		border-radius: 2px;
		text-align: center;
	}
	
	.error{
		background-color: #d20a0a;
	}
	
	.sr{
		background-color: #2471c8;
	}
	
	.RectangleGroup{
		width: 1100px;
		margin:0 auto;
	}
	
	.RectangleGroup:after {
		content:""; 
		display:block; 
		clear:both;
	}
	
	.leftBar{
		display: block; 
		width: 110px; 
		height: 58px; 
		border-bottom: 1px solid #104a8c;
		float:left;
		cursor: pointer;
	}
	
	.leftBar div {
		display: inline-block;
	}
	
	.leftBar.on, .leftBar:hover {
		width: 110px;
		height: 58px;
		
		background-color: #104a8c;
	}
	
	.dashBoard{
		display: inline-block;
		float:right;
	}
	
	#underMenu .underBox{
		width:1100px;
		margin: 0 auto;
		margin-bottom:205px;
		margin-top:35px;
		border-radius: 5px;
	  	box-shadow: 0 2px 10px 0 rgba(0, 0, 0, 0.15);
	  	background-color: #ffffff;
	}
	
	.srDetail {
		padding-left:27px;
	}
	
	.timeLineBox .timeLine{
		width:100%;
		margin-top:15px;
	}

	.timeLineBox .srDetail .timeLine:nth-child(2){
		margin-top:20px;
	}
	
	.timeLine div{
		display: inline-block;
	}
	
	.leftGraph{
		height:442px; 
		margin-left: 25px;
		overflow: auto;
		-ms-overflow-style: none; /* IE and Edge */
    	scrollbar-width: none; /* Firefox */
	}
	
	.leftGraph::-webkit-scrollbar {
	    display: none; /* Chrome, Safari, Opera*/
	}
	
	.timeLineBox{
		overflow: auto;
		-ms-overflow-style: none; /* IE and Edge */
    	scrollbar-width: none; /* Firefox */
	}
	
	.timeLineBox::-webkit-scrollbar {
	    display: none; /* Chrome, Safari, Opera*/
	}
	.srStatus{    
		width: 100%;
		height: 30px;
		margin-top:6px;
	}
		
	.srStatus.first{
		margin-top:20px;
	}
	
	.srStatus .srBasicDiv{
		height: 100%;
		width: 345px;
		display: inline-block;
		cursor: pointer;
	}
	
	.srStatus .srBasicDiv div{
		border-radius: 2px;
		background-color: rgba(36,113,200, 0.18);
		height: 100%;
		cursor: pointer;
		padding: 0 10px;
	}
	
	.srStatus.select .srBasicDiv div, .srStatus:hover .srBasicDiv div{
		border-radius: 2px;
		background-color: #2471c8;
		height: 100%;
	}
	.srStatus.select .srBasicDiv .srBasicText, .srStatus:hover .srBasicDiv .srBasicText{
		color: #ffffff;
	}
		
	.srText {
		margin-right:25px; 
		float:right; 
		width: 45px; 
		height: 30px;
		text-align: right;
		display: inline-block;
	}
	
	.srText label{
		width:100%;
		height: 19px;
		font-family: Noto Sans KR;
		font-size: 13px;
		font-weight: 500;
		font-stretch: normal;
		font-style: normal;
		line-height: normal;
		letter-spacing: -0.13px;
		color: #666666;	
		margin-top : 5px;
		float:left;
	}
	
	.srStatus.select .srText label, .srStatus:hover .srText label{
		color: #2471c8;
	}
	
</style>

<body style="width: 100%; background-color: #ffffff; margin:0px !important;">
	<div id="topMenu">
		<div class="topMenu">
			<div style="margin-left: 20px; margin-top:15px; float:left;" >
				<img alt="azsoft" src="/img/P1.jpg" style="width: 95px; height: 34px;">
			</div>
			
			<div  class="dIcon dIcon-first">
				<div class="menuIcon menu1">
				</div>
				<div>
					<label class="txt1">기본관리</label>
				</div>			
			</div>
			
			<div class="dIcon">
				<div class="menuIcon menu2">
				</div>
				<div>
					<label class="txt1">&nbsp;&nbsp;&nbsp;등록</label>
				</div>			
			</div>

			<div class="dIcon">
				<div class="menuIcon menu3">
				</div>
				<div>
					<label class="txt1">&nbsp;&nbsp;&nbsp;개발</label>
				</div>			
			</div>
			
			<div class="dIcon">
				<div class="menuIcon menu4">
				</div>
				<div>
					<label class="txt1">&nbsp;테스트</label>
				</div>			
			</div>
			
			<div class="dIcon">
				<div class="menuIcon menu5">
				</div>
				<div>
					<label class="txt1">&nbsp;&nbsp;&nbsp;적용</label>
				</div>
			</div>
			
			<div class="dIcon">
				<div class="menuIcon menu6">
				</div>
				<div>
					<label class="txt1">결재확인</label>
				</div>			
			</div>
			
			<div class="dIcon">
				<div class="menuIcon menu7">
				</div>
				<div>
					<label class="txt1">&nbsp;보고서</label>
				</div>			
			</div>
			
			<div class="dIcon">
				<div class="menuIcon menu8">
				</div>
				<div>
					<label class="txt1">프로그램</label>
				</div>			
			</div>
			
			<div class="dIcon">
				<div class="menuIcon menu9">
				</div>
				<div>
					<label class="txt1">&nbsp;관리자</label>
				</div>			
			</div>
		</div>
		
		<div style="width: 407px; height: 100%; float:right;">
			<div style="float:right; margin-top: 25px; margin-right:20px; width:100%">
				<div style="float:left;">
					<div class="ilb">
						<label id="SR">SR</label>
					</div>
					<div  class="ilb subTitle sr">
						<label id="srCnt">0</label>
					</div>
				</div>
				
				<div class="line" style="float:left;"></div>
				
				<div style="float:left;">
					<div  class="ilb">
						<label id="eRR">오류</label>
					</div>
					<div class="ilb subTitle error">
						<label id="errCnt">0</label>
					</div>
				</div>
				
				<div class="line" style="float:left;"></div>
				
				<div style="float:left;">
					<label id="loginUser" style="font-size: 15px; height:22px;">로그인</label>
					<!-- <img src="zeplin_img/nav-alarm-icon.svg" class="nav-alarm-icon"> -->
				</div>
				
				<div class="line" style="float:left;"></div>
				
				<div style="float:left;">
					<a id="logOut" class="vat" style="font-size: 15px; cursor: pointer;">로그아웃</a>
				</div>
			</div>
			
		</div>
	</div>
	
	<div id="middelMenu">
		<div class="RectangleGroup">
			<div class="Rectangle-3">
				<div class="icon list-icon">
					<!-- <img src="zeplin_img/board-icon-1.svg" onmouseover="this.src='zeplin_img/board-icon-1-selected.svg';" onmouseout="this.src='zeplin_img/board-icon-1.svg';" class="board-icon1"> -->
				</div>
				<div class="textBox single">
					<div style="display: block; float:right;">
						<label class="middleText">미결</label>
					</div>
					<div style="display: block; text-align: right;">
						<label class="middleCnt" id="approvalCnt">0</label>
					</div>
				</div>
			</div>
			
			<div class="Rectangle-3">
				<div class="icon register-icon">
				</div>
				<div class="textBox single">
					<div style="display: block; block; float:right;">
						<label class="middleText">등록</label>
					</div>
					<div style="display: block; text-align: right;">
						<label class="middleCnt" id="srRegCnt">0</label>
					</div>
				</div>
			</div>
			
			<div class="Rectangle-3">
				<div class="icon dev-icon">
				</div>
				<div class="textBox">
					<div style="display: block; float:right;">
						<label class="middleText">개발</label>
					</div>
					<div style="display: block; text-align: right; height: 41px; margin-top:24px;">
						<label class="middleCnt" id="devSrCnt">0</label>
						<label class="middelDBCnt" id="devSrCntTotal" style="float: right; margin-top: 10px;">/0</label>
					</div>
				</div>
			</div>
			
			<div class="Rectangle-3">
				<div class="icon test-icon">
				</div>
				<div class="textBox">
					<div style="display: block; float:right;">
						<label class="middleText">테스트</label>
					</div>
					<div style="display: block; text-align: right; height: 41px; margin-top:24px;">
						<label class="middleCnt" id="testSrCnt">0</label>
						<label class="middelDBCnt" id="testSrCntTotal" style="float: right; margin-top: 10px;">/0</label>
					</div>
				</div>
			</div>
			
			<div class="Rectangle-3">
				<div class="icon apploval-icon">
				</div>
				<div class="textBox">
					<div style="display: block; float:right;">
						<label class="middleText">적용</label>
					</div>
					<div style="display: block; text-align: right; height: 41px; margin-top:24px;">
						<label class="middleCnt" id="appySrCnt">0</label>
						<label class="middelDBCnt" id="appySrCntTotal" style="float: right; margin-top: 10px;">/0</label>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div id="underMenu">
		<div class="underBox">
			<div class="underLeftMenu">
				<div class="leftBar on" style=" border-top-left-radius: 5px;">
					<div>
						<img src="zeplin_img/sub-icon-1.svg" class="underIcon">
					</div>
					<div>
						<label>SR</label>
					</div>
				</div>
				<div class="leftBar">
					<div>
						<img src="zeplin_img/sub-icon-2.svg" class="underIcon">
					</div>
					<div>
						<label>미결</label>
					</div>
				</div>
				<div class="leftBar">
					<div>
						<img src="zeplin_img/sub-icon-3.svg" class="underIcon">
					</div>
					<div>
						<label>통계</label>
					</div>
				</div>
			</div>
			<div class="dashBoard"> <!-- 우선 대시보드에 나와있는 화면만 구성 나중에 미결, 통계 화면도 나오면 거기에 맞게 추가해야할듯 -->
				<div style="width: 480px; float: left;">
					<div class="underRightMenu">
						<div style="margin-left:25px; margin-top:16px; width: 95%">
							<label class="underLb1">SR진행현황</label>
							<input type="button" class="underButton" id="btnListAll" value="전체보기">
						</div>
					</div>
					<div class="leftGraph" id="leftGraph">
<!-- 				
						<div class="srStatus first">
							<div class="srBasicDiv">
								<div style="width: 95%;">
									<label class="srBasicText">SR등록자가 롤백</label>
								</div>
							</div>
							<div class="srText">
								<label>95%</label>	
							</div>
						</div>
						
						<div class="srStatus select">
							<div class="srBasicDiv">
								<div style="width: 100%;">
									<label class="srBasicText">SR등록자가 롤백</label>
								</div>
							</div>
							<div class="srText">
								<label>100%</label>	
							</div>
						</div>
 -->
					</div>
				</div>
				<div style="width: 509px; float: right; border-left: solid 1px #ebebeb;">
					<div class="underRightMenu">
						<div style="margin-left:25px; margin-top:16px; width: 95%">
							<label class="underLb1">타임라인</label>
							<input type="button" class="underButton" value="전체보기">
						</div>
					</div>
					
					<div class="timeLineBox" style="height:442px;">
					<!-- 
						<div class="srDetail">
							<div style="width:100%; margin-top:24px;">
								<img src="zeplin_img/timeline-check.svg" class="timeline-check">
								<label class="timeLineText">SR등록자가 롤백 신청 할수 있는 로직 추가</label>
							</div>
							<div class="timeLine">
								<div class="oval1"></div>
								<div style="width:210px;">
									<label class="timeLineBase">SR 등록</label>
								</div>
								<div style="width: 109px;">
									<label class="timeLineBase2">2018/12/13 17:26:53</label>
								</div>
								<div style="width: 78px;">
									<label class="timeLineBase2">체크인 결제</label>
								</div>
								<div class="oval2">
									<img src="zeplin_img/3-circle-icon.svg" class="circle-icon">
								</div>
							</div>
							<div class="timeLine">
								<div class="oval1"></div>
								<div style="width:210px;">
									<label class="timeLineBase">SR 접수</label>
								</div>
								<div style="width: 120px;">
									<label class="timeLineBase2">2018/12/13 17:26:53</label>
								</div>
								<div style="width: 70px;">
									<label class="timeLineBase2">체크아웃 신청</label>
								</div>
								<div class="oval2">
									<img src="zeplin_img/3-circle-icon.svg" class="circle-icon">
								</div>
							</div>
							
							<div class="timeLine">
								<div class="oval1"></div>
								<div style="width:210px;">
									<label class="timeLineBase">체크아웃/프로그램등록</label>
								</div>
								<div style="width: 120px;">
									<label class="timeLineBase2">2018/12/13 17:26:53</label>
								</div>
								<div style="width: 70px;">
									<label class="timeLineBase2">개바자접수</label>
								</div>
								<div class="oval2">
									<img src="zeplin_img/3-circle-icon.svg" class="circle-icon">
								</div>
							</div>
						</div>
						 -->
						
					</div>
					
				</div>
			</div>
		</div>
	</div>

	<c:import url="/js/ecams/common/commonscript.jsp" />
	<script type="text/javascript" src="/js/ecams/main/mainNew.js"></script>
</body>
</html>