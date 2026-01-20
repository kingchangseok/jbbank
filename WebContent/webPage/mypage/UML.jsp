<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<style>
#back-img {
    height: 86%;
    background-size: contain;
    position: absolute;
    width: 75%;
    background-repeat: no-repeat;
    background-image: url(/img/UML.jpg);
    display: inline-block;
    left: 12%;
    top: 7%;
}

.components {
	height: 86%;
    width: 75%;
    position: absolute;
    display: inline-block;
    text-align: left;
    left: 12%;
    top: 7%;
}

.components button {
	position: absolute;
}

.blueBtn {
    color: white;
    border-radius: 8px;
    position: relative;
    background: #3492db;
    height: 4.5%;
    width: 10.5%;
    transition: background 0.3s ease;
}

.blueBtn:hover {
	background: #4aacf7;
}

.greenBtn {
    color: white;
    border-radius: 8px;
    position: relative;
    background: #04cc04;
    height: 4.5%;
    width: 10.5%;
    transition: background 0.3s ease;
}

.greenBtn:hover {
	background: #05e605;
}

.orangeBtn {
    color: white;
    border-radius: 8px;
    background: #f7b72d;
    height: 5.3%;
    transition: background 0.3s ease;
}

.orangeBtn:hover {
	background: #ffc954;
}

.yellowBtn {
	color: black;
    border-radius: 8px;
    border: 1px solid #dbd295;
    height: 4.5%;
    width: 10.5%;
	background-color: #fad764;
	transition: background-color 150ms ease-in;
	background-image:
	  linear-gradient( 
	  	to top, rgba(250, 230, 100, 0.1), 
	  	rgba(250, 230, 100, 0.2) 30%, 
	  	rgba(255, 255, 255, 0.3) 60%, 
	  	rgba(255, 255, 255, 0.5) 
	  );
}

.yellowBtn:hover {
	background-color: #ffed7d;
}

.grayBtn {
	color: black;
    border-radius: 8px;
    border: 1px solid #bbbbbb;
    height: 4.5%;
    width: 10.5%;
	background-color: #dddddd;
	transition: background-color 150ms ease-in;
	background-image:
	  linear-gradient( 
	  	to top, rgba(255, 255, 255, 0.1), 
	  	rgba(255, 255, 255, 0.2) 30%, 
	  	rgba(255, 255, 255, 0.5) 60%, 
	  	rgba(255, 255, 255, 0.7) 
	  );
}

.grayBtn:hover {
	background-color: #eeeeee;
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

p {
    border-radius: 50%;
    background: red;
    width: 25px;
    height: 25px;
    color: white;
    text-align: center;
    position: absolute;
    padding: 3px;
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


</style>

<div class="contentFrame">
	<div id="history_wrap" style="background-color: #fff;">기본관리<strong>&gt; 프로세스 흐름도</strong></div>
	<div style="width: 100%; height: 90%;">
		<img src="/img/UML3.jpg" id="back-img">
		<div class="components">
			<button class="greenBtn" id="srReg" style="top: 4.2%;left: 3.2%;" value="">SR등록</button>
			<button class="greenBtn" id="" style="top: 14.8%;left: 3.2%;" value="">SR접수</button>
			<button class="greenBtn" id="" style="top: 25.5%;left: 3.2%;" value="">개발계획서</button>
			<button class="greenBtn" id="" style="top: 35.9%;left: 3.2%;" value="">개발책임자승인</button>
			<button class="greenBtn" id="" style="top: 45.4%;left: 3.2%;" value="">BA승인</button>
			<button class="greenBtn" id="" style="top: 56.1%;left: 3.2%;" value="">전산/인사팀장승인</button>
			<button class="blueBtn" id="" style="top: 90.3%;left: 3.0%;" value="">개발빌드</button>
			<button class="blueBtn" id="" style="top: 78.8%;left: 20.0%;" value="">취약점점검</button>
			<button class="blueBtn" id="" style="top: 90.3%;left: 20.0%;" value="">개발배포</button>		
			<button class="blueBtn" id="" style="top: 45.8%;left: 33.2%;" value="">테스트적용요청</button>
			<button class="blueBtn" id="" style="top: 78.8%;left: 33.2%;" value="">배포담당자승인</button>
			<button class="blueBtn" id="" style="top: 90.3%;left: 33.2%;" value="">테스트빌드</button>
			<button class="blueBtn" id="" style="top: 35.8%;left: 48.7%;" value="">DBA작업및승인</button>
			<button class="blueBtn" id="" style="top: 45.8%;left: 48.7%;" value="">배포담당자승인</button>
			<button class="blueBtn" id="" style="top: 57.4%;left: 48.7%;" value="">테스트배포</button>
			<button class="yellowBtn" id="" style="top: 68.4%;left: 48.7%;" value="">통합테스트</button>
			<button class="blueBtn" id="" style="top: 90.3%;left: 48.7%;" value="">현업이행검증승인</button>
			<button class="blueBtn" id="" style="top: 14.8%;left: 65.9%;" value="">운영적용요청</button>
			<button class="blueBtn" id="" style="top: 25.3%;left: 65.9%;" value="">개발책임자승인</button>
			<button class="blueBtn" id="" style="top: 35.8%;left: 65.9%;" value="">BA확인</button>
			<button class="blueBtn" id="" style="top: 45.8%;left: 65.9%;" value="">QA승인</button>
			<button class="blueBtn" id="" style="top: 57.4%;left: 65.9%;" value="">IT감사승인</button>
			<button class="blueBtn" id="" style="top: 68.2%;left: 65.9%;" value="">전산/인사팀장승인</button>
			<button class="blueBtn" id="" style="top: 78.8%;left: 65.9%;" value="">배포담당자승인</button>
			<button class="blueBtn" id="" style="top: 90.3%;left: 65.9%;" value="">운영빌드</button>
			<button class="blueBtn" id="" style="top: 68.2%;left: 79.9%;" value="">DBA작업및승인</button>
			<button class="blueBtn" id="" style="top: 78.8%;left: 79.9%;" value="">배포담당자승인</button>
			<button class="blueBtn" id="" style="top: 90.3%;left: 79.9%;" value="">운영배포</button>
			<button class="grayBtn" id="" style="top: 45.8%;left: 19.9%;" value="">개발적용요청</button>
			<button class="yellowBtn" id="" style="top: 78.8%;left: 48.7%;" value="">현업테스트</button>
			<button class="yellowBtn" id="" style="top: 36.0%;left: 33.3%;" value="">단위테스트</button>
			<button class="grayBtn" id="" style="top: 6.2%;left: 20.1%;" value="">체크아웃</button>
			<button class="grayBtn" id="" style="top: 13.8%;left: 20.1%;" value="">프로그램수정</button>
			<button class="grayBtn" id="" style="top: 21.6%;left: 20.1%;" value="">체크인</button>
			<button class="grayBtn" id="" style="top: 6.2%;left: 33.4%;" value="">체크아웃취소</button>
			<button class="grayBtn" id="" style="top: 21.6%;left: 33.4%;" value="">개발완료</button>
			<button class="greenBtn" id="" style="top: 4%;left: 88.3%;" value="">SR완료</button>
			<p style="left: 12.8%;top: 13.5%;">2</p>
			<p style="left: 12.8%;top: 24%;">4</p>
		</div>
	</div>
</div>


<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/mypage/UML.js"/>"></script>