<!--  
	화면 명: 선행작업선택
	화면호출:
	1) 운영배포 -> 선행작업연결 클릭
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">

<body>
<div class="pop-header">
	<div>
		<label id="lbSub">선행작업선택</label>
	</div>
	<div>
		<button type="button" class="close" aria-label="닫기" onclick="popClose()">
		  <span aria-hidden="true">&times;</span>
		</button>
	</div> 
</div>
<div class="container-fluid pop_wrap">
	<!--line1 S-->
	<div>					
		<div class="width-100 dib vat">
            <label id="lbUser" class="title">[선행작업 선택가능 목록]</label>
		</div>
		<div class="row">
			<div class="az_board_basic az_board_basic_in" style="height:40%">
		    	<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height:100%"></div>
			</div>
		</div>
	</div>
	<!--line2 S-->
	<div class="row half_wrap_cb">
		<!--프로그램 신청목록-->
		<div class="l_wrap width-50">
			<div class="margin-5-right">
				<div class="width-100 dib vat">
                    <label id="lbUser" class="title">[프로그램 신청목록]</label>
				</div>				
				<div class="row">
					<div class="az_board_basic az_board_basic_in" style="height:36%">
				    	<div data-ax5grid="secondGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height:100%"></div>
					</div>
				</div>
			</div>
		</div>
		<!--프로그램 신청목록-->
		<div class="r_wrap width-50">
			<div>
				<div class="width-100 dib vat">
                    <label id="lbUser" class="title">[선택한 선행작업]</label>
				</div>				
				<div class="row">
					<div class="az_board_basic az_board_basic_in" style="height:36%">
				    	<div data-ax5grid="thirdGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height:100%"></div>
					</div>
				</div>
			</div>
		</div>	
	</div>
	<div class="row tac float-right">
		<button id="btnReq" class="btn_basic_s">선택</button>
		<button id="btnClose" class="btn_basic_s">닫기</button>
	</div>
</div>
<!-- contener E -->
</body>	

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/request/BefJobSetModal.js"/>"></script>