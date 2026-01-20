<!--  
	* 화면명: 엑셀Data Load
	* 화면호출: 단위테스트, 개발검수 -> 엑셀로드
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body>
	<div class="pop-header">
		<div>
			<label>[엑셀Data Load]</label>
		</div>
		<div>
			<button type="button" class="close" aria-label="닫기" onclick="popClose(false)">
			  <span aria-hidden="true">&times;</span>
			</button>
		</div> 
	</div>
	<div class="container-fluid pop_wrap">
		<div class="half_wrap_cb">
			<div class="l_wrap width-30">
				<div class="margin-5-right">
					<label class="tit_80 poa">구분</label>
			        <div class="ml_80">
						<input id="txtMsg" type="text" class="dib width-90" readonly>
					</div>
				</div>
		 	</div>
		 	<div class="r_wrap width-70">
		 		<div class="margin-5-left">
					<label class="tit_80 poa">업무</label>
			        <div class="ml_80">
			        	<div class="width-80">
							<div class="ml_80">
								<input id="optNomal"  type="radio" name="radio" value="normal"/>
								<label for="optNomal">정상건</label>
								<input id="optErr" type="radio" name="radio" value="err"/>
								<label for="optErr">오류건</label>
								<input id="optAll" type="radio" name="radio" value="all"/>
								<label for="optAll">전체</label>
							</div>
							<div class="dib" id="divChkOk">
								<input type="checkbox" class="checkbox-batch" id="chkOk" data-label="정상건만등록"/>
							</div>
			        	</div>
						<div class="vat dib poa_r">
							<button id="btnQry" class="btn_basic_s">등록</button>
							<button id="btnExit" class="btn_basic_s margin-5-left">닫기</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="az_board_basic" style="height: 82%;">
		    	<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: false, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
			</div>
		</div>
	</div>
</body>	
<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/sr/LoadExcelModal.js"/>"></script>