<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<style>
#back-img {
    background-size: contain;
    background-repeat: no-repeat;
}

.components {
	height: 86%;
    width: 90%;
    position: absolute;
    display: inline-block;
    left: 5%;
    top: 7%;
    text-align: left;
}

.components > button {
	position: absolute;
	height: 5%;
	width: 10%;
	/*
	font-family: Noto Sans KR;
    font-size: 13px;*/
    font-weight: bold;
}

.blueBtn {
    position: relative;
    background-image: url(/img/flow/blue2.gif);
    background-repeat: round;
    background-size: auto;
    transition: all 0.3s ease;
    text-decoration:underline;
    cursor: pointer;
}

.blueBtn:hover {
    background-image: url(/img/flow/b_over.gif);
    color: white;
}

.greenBtn {
    position: relative;
    background-image: url(/img/flow/green.gif);
    background-repeat: round;
    background-size: auto;
    transition: all 0.3s ease;
}

.greenBtn:hover {
	background-image: url(/img/flow/green_over.gif);
	color: white;
}

.devTestBtn {
    color: blue;
    font-weight: bold;
    border-radius: 8px;
    background: white;
    transition: all 0.3s ease;
}

.devTestBtn:hover {
/* 	background: rgb(36, 113, 200); */
	text-shadow: 0 0 3px blue;
	color: white;
}

.yellowBtn {
    position: relative;
    background-image: url(/img/flow/orange.gif);
    background-repeat: round;
    background-size: auto;
    transition: all 0.3s ease;
    text-decoration:underline;
    cursor: pointer;
}

.yellowBtn:hover {
    background-image: url(/img/flow/o_over.gif);
    color: white;
}

.grayBtn {
    position: relative;
    background-image: url(/img/flow/gray3.gif);
    background-repeat: round;
    background-size: auto;
    transition: all 0.3s ease;
    cursor: default;
}

.grayBtn:hover {
    background-image: url(/img/flow/g_over.gif);
    color: white;
    cursor: default;
}

.circleBtn {
	color: black;
    border-radius: 30px;
    background: #fcf2b1;
    border: 1px solid #dbd295;
    height: 5.3%;
    transition: background 0.3s ease;
}

.circleBtn:hover {
	background: #faf4cd;
}

.state {
    border-radius: 50%;
    background: linear-gradient(24deg, blue, #6e88fa);
    width: 25px;
    height: 25px;
    color: white;
    text-align: center;
    position: absolute;
    padding: 3px;
    display: none;
}

.errCnt {
    width: 25px;
    height: 25px;
    border-radius: 50%;
    padding: 3px;
	position: absolute;
	font-weight: bold;
	background: red;
	color: white;
	font-size: 14px;
	text-align: center;
	display: none;
}

.tri1 {
	position: absolute;
	width: 0px;
	height: 0px;
	border-top: 25px solid transparent;
	border-right: 69px solid red;
	border-bottom: 25px solid transparent;
}

.tri2 {
	position: absolute;
	width: 0px;
	height: 0px;
	border-top: 25px solid transparent;
	border-left: 69px solid red;
	border-bottom: 25px solid transparent;
}

.cond {
	position: absolute;
    height: 5%;
    width: 10%;
    /*
    font-family: Noto Sans KR;*/
    font-size: 12px;
    background-image: url(/img/flow/puple.gif);
    background-repeat: round;
    background-size: cover;
}
.cond:hover {
    background-image: url(/img/flow/p_over.gif);
    color: white;
    cursor: default;
}

.condDiv button {
	background-color: white;
    cursor: default;
}

.labelBtn {
	position: absolute;
    background-color: white;
    font-family: Noto Sans KR;
    font-size: 12px;
}

</style>

<div class="contentFrame">
	<div id="history_wrap" style="background-color: #fff; display:block !important;"><strong>형상관리프로세스</strong></div>
	<div style="width: 100%; height: 90%;">
		<img class="components" src="/img/UMLSHDS.jpg" id="back-img">
		<div class="components">
			<div class="condDiv">
				<button class="cond" style="top: 15.6%;left: 45.3%;">기등록여부</button>
				<button class="cond" style="top: 15.6%;left: 26.5%;">Tmax3.0/AnyLink</button>
				<button class="cond" style="top: 46.1%;left: 45.3%;">테스트환경</button>
				<button class="cond" style="top: 66%;left: 24.8%;">정상</button>
				<button class="cond" style="top: 75.3%;left: 80.5%;">승인</button>
			</div>
			<div class="labelDiv">
				<button class="labelBtn" style="top: 4%;left: 51.2%;cursor:default;">사용자</button>
				<button class="labelBtn" style="top: 93.5%;left: 73.5%;cursor:default;">결재자</button>
				<button class="labelBtn" style="top: 15%;left: 55.5%;cursor:default;">Yes</button>
				<button class="labelBtn" style="top: 20.5%;left: 32%;cursor:default;">Yes</button>
				<button class="labelBtn" style="top: 45.8%;left: 44%;cursor:default;">Yes</button>
				<button class="labelBtn" style="top: 71%;left: 30.2%;cursor:default;">Yes</button>
				<button class="labelBtn" style="top: 80%;left: 862%;cursor:default;">Yes</button>
				<button class="labelBtn" style="top: 15%;left: 44%; color: rgb(255, 0, 0);cursor:default;">No</button>
				<button class="labelBtn" style="top: 15%;left: 25%; color: rgb(255, 0, 0);cursor:default;">No</button>
				<button class="labelBtn" style="top: 45.8%;left: 55.5%; color: rgb(255, 0, 0);cursor:default;">No</button>
				<button class="labelBtn" style="top: 65.5%;left: 23.3%; color: rgb(255, 0, 0);cursor:default;">No</button>
				<button class="labelBtn" style="top: 75%;left: 90.5%; color: rgb(255, 0, 0);cursor:default;">No</button>
				<button class="labelBtn" style="top: 7%;left: 30.8%;cursor:default;">실행모듈을 별도로</button>
				<button class="labelBtn" style="top: 9.5%;left: 30.8%;cursor:default;">관리하는 경우</button>
				<button class="labelBtn" style="top: 18%;left: 93.2%;cursor:default;">진행및결과확인</button>
				<button class="labelBtn" style="top: 67%;left: 40.5%;cursor:default;">진행및결과확인</button>
				<button class="labelBtn" style="top: 29%;left: 90%;cursor:default;" id="txtReq01"></button>
				<button class="labelBtn" style="top: 31%;left: 90%;cursor:default;" id="txtReq02"></button>
				<button class="labelBtn" style="top: 33%;left: 90%;cursor:default;" id="txtReq03"></button>
				<button class="labelBtn" style="top: 65.5%;left: 55.5%;cursor:default;" id="txtReq11"></button>
				<button class="labelBtn" style="top: 67.5%;left: 55.5%;cursor:default;" id="txtReq12"></button>
				<button class="labelBtn" style="top: 69.5%;left: 55.5%;cursor:default;" id="txtReq13"></button>
				<button class="labelBtn" style="top: 71.5%;left: 55.5%;cursor:default;" id="txtReq14"></button>
				<button class="labelBtn" style="top: 36%;left: 55.5%;cursor:default;" id="txtReq21"></button>
				<button class="labelBtn" style="top: 38%;left: 55.5%;cursor:default;" id="txtReq22"></button>
				<button class="labelBtn" style="top: 40%;left: 55.5%;cursor:default;" id="txtReq23"></button>
			</div>
						
			<button class="blueBtn" id="cmdRegister" style="top: 15.66%;left: 2.8%;" value="" onmouseover="mouseover('cmdRegister')" onmouseout="mouseout('cmdRegister')" rel="/webPage/dev/ProgRegister.jsp">프로그램신규등록</button>
			<button class="blueBtn" id="cmdSvrReg" style="top: 1.8%;left: 2.8%;" value="" onmouseover="mouseover('cmdSvrReg')" onmouseout="mouseout('cmdSvrReg')" rel="/webPage/dev/ProgRegister.jsp">개발서버연계등록</button>
			<button class="blueBtn" id="cmdLocalReg" style="top: 1.8%;left: 13.9%;" value="" onmouseover="mouseover('cmdLocalReg')" onmouseout="mouseout('cmdLocalReg')" rel="/webPage/dev/ProgRegister.jsp">로컬영역연계등록</button>
			<button class="blueBtn" id="cmdModule" style="top: 1.8%;left: 25.2%;" value="" onmouseover="mouseover('cmdModule')" onmouseout="mouseout('cmdModule')" rel="/webPage/program/ModuleInfo.jsp">실행모듈정보</button>
			<button class="blueBtn" id="cmdDevTool" style="top: 23.7%;left: 26.5%;" value="" onmouseover="mouseover('cmdDevTool')" onmouseout="mouseout('cmdDevTool')" rel="/webPage/program/DevTool.jsp">개발툴연계등록</button>
			<button class="blueBtn" id="cmdCheckOut" style="top: 15.6%;left: 63.9%;" value="01" onmouseover="mouseover('cmdCheckOut')" onmouseout="mouseout('cmdCheckOut')" rel="/webPage/dev/CheckOut.jsp">체크아웃</button>
			<button class="blueBtn" id="cmdCkCncl" style="top: 8.2%;left: 87.8%;" value="11" onmouseover="mouseover('cmdCkCncl')" onmouseout="mouseout('cmdCkCncl')" rel="/webPage/dev/CheckOutCnl.jsp">체크아웃취소</button>
			<button class="blueBtn" id="cmdConfSta1" style="top: 23.8%;left: 87.8%;" value="01" onmouseover="mouseover('cmdConfSta1')" onmouseout="mouseout('cmdConfSta1')" rel="/webPage/dev/DistributeStatus.jsp">신청현황</button>
			<button class="blueBtn" id="cmdTest" style="top: 55.5%;left: 24.8%;" value="03" onmouseover="mouseover('cmdTest')" onmouseout="mouseout('cmdTest')" rel="/webPage/apply/ApplyRequest.jsp">체크인(테스트)</button>
			<button class="blueBtn" id="cmdReal" style="top: 55.5%;left: 63.9%;" value="04" onmouseover="mouseover('cmdReal')" onmouseout="mouseout('cmdReal')" rel="/webPage/apply/ApplyRequest.jsp">체크인(운영)</button>
			<button class="blueBtn" id="cmdTestCncl" style="top: 66%;left: 3.7%;" value="12" onmouseover="mouseover('cmdTestCncl')" onmouseout="mouseout('cmdTestCncl')" rel="/webPage/apply/ApplyRequest.jsp">테스트적용취소</button>
			<button class="blueBtn" id="cmdConfSta2" style="top: 65.7%;left: 45.3%;" value="" onmouseover="mouseover('cmdConfSta2')" onmouseout="mouseout('cmdConfSta2')" rel="/webPage/approval/RequestStatus.jsp">신청현황</button>
			<button class="blueBtn" id="cmdConf" style="top: 65.7%;left: 80.5%;" value="" onmouseover="mouseover('cmdConf')" onmouseout="mouseout('cmdConf')" rel="/webPage/approval/ApprovalStatus.jsp">결재절차진행</button>
			
			<!-- <button class="blueBtn" id="cmdCheckIn" style="top: 89.3%;left: 54.5%;" value="04" rel="/webPage/approval/RequestStatus.jsp">테스트적용</button>
			<p style="top: 80.8%; left: 65.4%;" id="cmdCheckInState" class="state"></p>-->
			<!-- <button class="yellowBtn" id="cmdConfirm" style="top: 2%;left: 18%;" value="" onmouseover="mouseover('cmdConfirm')" onmouseout="mouseout('cmdConfirm')" rel="/webPage/approval/ApprovalStatus.jsp">결재대기</button>-->
			
			<button class="grayBtn" id="cmdEdit" style="top: 35.7%;left: 45.3%;" value="99" onmouseover="mouseover('cmdEdit')" onmouseout="mouseout('cmdEdit')" rel="">소스수정및테스트</button>
			<button class="grayBtn" id="cmdEnd" style="top: 84.3%;left: 80.5%;" value="" onmouseover="mouseover('cmdEnd')" onmouseout="mouseout('cmdEnd')" rel="">종료</button>
		</div>
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/main/UML.js"/>"></script>