<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<style>
#back-img {
/*     background-size: contain; */
    background-repeat: no-repeat;
}

.components {
	height: 92%;
    width: 98%;
    position: absolute;
    display: inline-block;
    text-align: left;
}

.components > button {
	position: absolute;
	height: 5.7%;
    width: 9%;
	font-family: Noto Sans KR;
    font-size: 13px;
}

.blueBtn {
    position: relative;
    background-image: url(/img/flow/blue3.gif);
    background-repeat: round;
    background-size: cover;
    transition: all 0.3s ease;
}

.blueBtn:hover {
    background-image: url(/img/flow/b_over.gif);
    color: white;
}

.longBlueBtn {
    position: relative;
    background-image: url(/img/flow/blue4.png);
    background-repeat: round;
    background-size: cover;
    transition: all 0.3s ease;
}

.longBlueBtn:hover {
    background-image: url(/img/flow/b_over2.png);
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
    width: 10.8%;
    font-family: Noto Sans KR;
    font-size: 13px;
    background-image: url(/img/flow/cond.png);
    background-repeat: round;
    background-size: cover;
}

.condDiv > button {
	background-color: transparent;
}

.labelBtn {
    position: absolute;
    font-family: Noto Sans KR;
    font-size: 13px;
    background-color: transparent;
}

</style>

<div class="contentFrame">
	<div id="history_wrap" style="background-color: #fff; display:block !important;"><strong>형상관리프로세스</strong></div>
	<div style="width: 100%; height: 90%;">
	<img class="components" src="/img/UML.png" id="back-img">
	<div class="components">
			<button class="blueBtn" id="cmdPum4" style="top: 89.3%;left: 8.1%;color: blue;" value="31" rel="/webPage/dev/RegistDevRequest.jsp">개발 요청 완료</button>
			<button class="blueBtn" id="cmdTeam1" style="top: 35.2%;left: 33.1%;color: blue;" value="01" rel="/webPage/dev/CheckOutListReport.jsp">대여 요청</button>
			<button class="blueBtn" id="cmdPum1" style="top: 9.7%;left: 17.9%;color: blue;" value="31" rel="/webPage/dev/RegistDevRequest.jsp"><span id="txtPum1">개발요청 담당</span><br>(품질관리/업무팀)</button>
			<button class="blueBtn" id="cmdPum2" style="top: 22.6%;left: 17.9%;color: blue;" value="32" rel="/webPage/dev/SetOrderList.jsp"><span id="txtPum2">업무지시서 담당</span><br>(품질관리/업무팀)</button>
			<button class="blueBtn" id="cmdTeam3" style="top: 58.7%;left: 33.1%;color: blue;" value="04" rel="/webPage/approval/ApprovalStatus.jsp"><span id="txtTeam3">개발 완료(적용)</span><br>품의 및 적용요청</button>
			<button class="blueBtn" id="cmdTeam2" style="top: 47.1%;left: 33.1%;color: blue;" value="01" rel="/webPage/apply/ApplyRequest.jsp">개발/테스트</button>
			<button class="blueBtn" id="cmdProg1" style="top: 35.1%;left: 84.3%;color: blue;" value="01" rel="/webPage/apply/ApplyRequest.jsp"><span id="txtProg1">대여 수행</span><br>(프로그램/산출물)</button>
			<button class="blueBtn" id="cmdProg2" style="top: 58.5%;left: 84.3%;color: blue;" value="01" rel="/webPage/apply/ApplyRequest.jsp"><span id="txtProg2">소스적용 및 컴파일</span><br>(프로그램/산출물)</button>
			<button class="blueBtn" id="cmdProg4" style="top: 89.5%;left: 84.3%;color: blue;" value="OD" rel="/webPage/apply/ApplyConfig.jsp"><span id="txtProg4">배포(시스템적용)</span><br>(프로그램)</button>
			<div class="condDiv">
				<button class="cond" id="cmdPum3" style="top: 76.5%;left: 7.5%;" value="01" rel="/webPage/approval/ApprovalStatus.jsp">품질관리 사전검토</button>
				<button class="cond" id="cmdProg3" style="top: 76.5%;left: 83.4%;" value="40" rel="/webPage/program/DevTool.jsp">운영 반영 승인</button>
				<button class="cond" id="cmdGyul1" style="top: 35.8%;left: 57.6%;" value="01" rel="/webPage/approval/ApprovalStatus.jsp">담당책임자 승인</button>
				<button class="cond" id="cmdGyul2" style="top: 47%;left: 57.6%;" value="01" rel="/webPage/approval/ApprovalStatus.jsp">제3자 확인</button>
				<button class="cond" id="cmdGyul3" style="top: 58.7%;left: 57.6%;" value="01" rel="/webPage/approval/ApprovalStatus.jsp">주관부서 확인</button>
				<button class="cond" id="cmdGyul4" style="top: 76.5%;left: 57.6%;" value="01" rel="/webPage/approval/ApprovalStatus.jsp">부부장/부장 결재</button>
			</div>
			<div class="labelDiv">
				<button class="labelBtn" style="top: 2.2%;left: 11.5%;cursor: default;">품질관리</button>
				<button class="labelBtn" style="top: 2.2%;left: 37%;cursor: default;">업무팀</button>
				<button class="labelBtn" style="top: 2.2%;left: 62%;cursor: default;">결재</button>
				<button class="labelBtn" style="top: 2.2%;left: 85.5%;cursor: default;">프로그램관리자</button>
			</div>
	</div>
	</div>
</div>


<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/approval/eCAMSflow.js"/>"></script>