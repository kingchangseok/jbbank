<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
<c:import url="/webPage/common/common.jsp" />
	<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
	<link href="/css/ecams/common/NotoSansKR.css" rel="stylesheet" type="text/css">
	<title>형상관리 시스템 (REAL)</title>
<link rel="stylesheet" href="/css/ecams/common/toolTip2.css">
<style type="text/css">
    iframe {
    	min-width: 1004px;
    }
    
	.lib{
		display:inline-block;
	}
	
	.line {
		width: 1px;
		height: 26px;
		border: solid 1px #cccccc;
		margin: 0 10 0 15px;
		background-color: #cccccc;
	}
		
	.nav-alarm-icon {
		width: 26px;
		height: 26px;
		object-fit: contain;
	}
	
	.topMenu{
		width:900px; 
		/* height: 55px; */
		height: 100%; 
		float:left;
	}
	#ulMenu{
		/* height: 55px; */
		margin-left: 50px;
	}
	
	.ilb {
		display: inline-block;
	}
	
	.subTitle{
		/*width: 26px;*/
		height: 26px; 
		border-radius: 2px;
		text-align: center;
		margin: 0px;
	}
	
	.sr{
		background-color: #2471c8;
	}
	.error{
		background-color: #d20a0a;
	}
	
	#divSrCnt {
		margin-top: 2px;
		background-color: #ffffff;
	}
	#divSrCnt:hover{
		background-color: #2471c8;
		border-radius: 2px;
	}
	#divSrCnt:hover #SR{
		color: #ffffff;
	}
	#divErrCnt {
		margin-top: 2px;
		background-color: #ffffff;
	}
	#divErrCnt:hover{
		background-color: #d20a0a;
		border-radius: 2px;
	}
	#divErrCnt:hover #eRR{
		color: #ffffff;
	}
	
	#errCnt{
		width: 30px;/*9px;*/
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
		/*width: 28px;*/
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
		margin-right:5px;
	}
	
	#srCnt{
		width: 30px;/*18px;*/
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
		margin-right:5px;
	}
	
    #topMenu{
    	width: 100%;
    	min-width:1250px;
		height: 45px;
		position: relative;
		box-shadow: 0 2px 3px 0 rgba(0, 0, 0, 0.1);
		background-color: #ffffff;
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
		margin-left: 13px;
		display: inline-block;
		cursor: pointer;
	}
	
	.topMenu .dIcon.dIcon-first{
		margin-top:15px;
		margin-left: 55px;
		display: inline-block;
	}
	
    .dIcon .menuIcon {
    	margin: 0 auto;
    	margin-top: 3px;
		width: 26px;
		/* height: 26px; */
		object-fit: contain;
	}
	.dIcon:hover .txt1{
		color: #2471c8;
	}
	/* 
	.dIcon .menuIcon.menu1{
		background-image: url(../../img/mainNew_img/nav-icon-1.svg);
	}
	.dIcon:hover .menuIcon.menu1{
		background-image: url(../../img/mainNew_img/nav-icon-1-selected.svg);
		background-repeat: no-repeat;
	}
	
	.dIcon .menuIcon.menu2{
		background-image: url(../../img/mainNew_img/nav-icon-2.svg);
	}
	.dIcon:hover .menuIcon.menu2{
		background-image: url(../../img/mainNew_img/nav-icon-2-selected.svg);
		background-repeat: no-repeat;
	}
	
	.dIcon .menuIcon.menu3{
		background-image: url(../../img/mainNew_img/nav-icon-3.svg);
	}
	.dIcon:hover .menuIcon.menu3{
		background-image: url(../../img/mainNew_img/nav-icon-3-selected.svg);
		background-repeat: no-repeat;
	}
	
	.dIcon .menuIcon.menu4{
		background-image: url(../../img/mainNew_img/nav-icon-4.svg);
	}
	.dIcon:hover .menuIcon.menu4{
		background-image: url(../../img/mainNew_img/nav-icon-4-selected.svg);
		background-repeat: no-repeat;
	}
	
	.dIcon .menuIcon.menu5{
		background-image: url(../../img/mainNew_img/nav-icon-5.svg);
	}
	.dIcon:hover .menuIcon.menu5{
		background-image: url(../../img/mainNew_img/nav-icon-5-selected.svg);
		background-repeat: no-repeat;
	}
	
	.dIcon .menuIcon.menu6{
		background-image: url(../../img/mainNew_img/nav-icon-6.svg);
	}
	.dIcon:hover .menuIcon.menu6{
		background-image: url(../../img/mainNew_img/nav-icon-6-selected.svg);
		background-repeat: no-repeat;
	}
	
	.dIcon .menuIcon.menu7{
		background-image: url(../../img/mainNew_img/nav-icon-7.svg);
	}
	.dIcon:hover .menuIcon.menu7{
		background-image: url(../../img/mainNew_img/nav-icon-7-selected.svg);
		background-repeat: no-repeat;
	}
	
	.dIcon .menuIcon.menu8{
		background-image: url(../../img/mainNew_img/nav-icon-8.svg);
	}
	.dIcon:hover .menuIcon.menu8{
		background-image: url(../../img/mainNew_img/nav-icon-8-selected.svg);
		background-repeat: no-repeat;
	}
	
	.dIcon .menuIcon.menu9{
		background-image: url(../../img/mainNew_img/nav-icon-9.svg);
	}
	.dIcon.adminMenu {
		display: none;
	}
	.dIcon:hover .menuIcon.menu9{
		background-image: url(../../img/mainNew_img/nav-icon-9-selected.svg);
		background-repeat: no-repeat;
	}
	 */
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
		margin-left: -3px;
	}
	
	.txt1:hover{
		color: #2471c8;
	}
	
</style>
</head>

<body style="width: 100%; background-color: #ffffff; margin:0px !important;">
	<div id="wrapper">
		<div id="topMenu">
			<div style="margin-left: 12px; float:left; cursor: pointer;" >
				<img alt="azsoft" src="/img/jbbank_logo.png" style="width: 150px; height: 45px;" id="logo" class="logo">
			</div>
			<div class="topMenu lang_menu" style="margin-left: 187px;">
				<ul id="ulMenu"></ul>
			</div>
			
			<div style="width: 250px; height: 100%; float:right;">
				<div style="float:right;margin-top: 7px; width:230px;">
					<!--
					<div style="float:left;" id="divSrCnt" title="SR진행현황 바로가기">
						<label id="SR" style="font-size: 15px; height:22px; cursor:pointer;">SR</label>
						<label class="subTitle sr" id="srCnt" style="font-size: 15px; height:22px; cursor:pointer;">0</label>
					</div>
					
					<div class="line" style="float:left;"></div>
					
					<div style="float:left;" id="divErrCnt" title="신청현황 바로가기">
						<label id="eRR" style="font-size: 15px; height:22px; cursor:pointer;">오류</label>
						<label class="subTitle error" id="errCnt" style="font-size: 15px; height:22px; cursor:pointer;">0</label>
					</div>
					<div class="line" style="float:left;"></div>
					-->
					
					<div style="float:left;">
						<label class="l_wrap" id="loginUser0" style="font-size: 13px; margin: 0px; display: none; color: #2471c8;"></label>
						<label class="r_wrap" id="loginUser" style="font-size: 13px; margin: 0px; display: none;">님 로그인</label>
						<!-- <img src="../../img/mainNew_img/nav-alarm-icon.svg" class="nav-alarm-icon"> -->
					</div>
					
					<div class="line" style="float:left; margin-right:15px; margin-left:15px; display: none;"></div>
					
					<div style="float:left; margin-top: 2px;">
						<img src="../../img/mainNew_img/exit.svg" title="로그아웃" id="imgLogOut" style="widt:18px; height:18px; margin-top: 2px; cursor:pointer;">
						<a id="logOut" class="vat" style="font-size:13px; cursor: pointer;" title="로그아웃">로그아웃</a>
					</div>
				</div>
			</div>
		</div>
		
		<div id="eCAMSFrame" class="content">
		</div>
	</div>

<form name="frmCAMSBase">
	<input type="hidden" id="userid" name="userid"/>
	<input type="hidden" id="scnidx" name="scnidx"/>
</form>

<form name="popPam">
	<input type="hidden" id="cm_acptno" name="cm_acptno"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="/js/ecams/main/eCAMSBase.js"></script>

</body>
</html>