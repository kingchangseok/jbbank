<!--  
	* 화면명: PMS검색
	* 화면호출: 신청화면 -> PMS검색
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body style="width: 100% !important; min-width: 0px !important">
	<div class="pop-header">
		<div>
			<label>[PMS목록 검색]</label>
		</div>
		<div>
			<button type="button" class="close" aria-label="닫기" onclick="popClose()">
				<span aria-hidden="true">&times;</span>
			</button>
		</div> 
	</div>
	<div class="container-fluid pop_wrap">
		<div class="row" style="height: 100px; margin-bottom: 0px; padding: 0">
			<div class="half_wrap">
				<div class="cb">
					<!--left-->
					<div class="float-left width-90">
						<div class="row vat cb">
		                    <div class="width-25 float-left">
		                    	<div class="tit_60 poa">
		                        	<label>개발자</label>
		                        </div>
		                        <div class="ml_60 tal">
									<input id="txtDevUsr" type="text" style="width: calc(100% - 10px)">
								</div>
							</div>
		                    <div class="width-25 float-left">
		                    	<div class="tit_60 poa">
		                        	<label>의뢰자</label>
		                        </div>
		                        <div class="ml_60 tal">
									<input id="txtReqUsr" type="text" style="width: calc(100% - 10px)">
								</div>
							</div>
							<div class="width-50 float-left">
								<div class="tit_80 poa">
			                        <label style="display: inherit;">진행상태</label>
			                        <input type="checkbox" class="checkbox-pie" id="chkAll" data-label="전체선택"/>
			                    </div>
			                    <div class="ml_80 dib poa tal" style="height: 85px; width: 50%">
									<div>
										<div class="scrollBind sm-row" style="height: 85px">
											<ul class="list-group" id="lstStaCd"></ul>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="width-25 float-left">
								<div class="tit_60 poa">
		                        	<label>문서번호</label>
		                        </div>
		                        <div class="ml_60">
									<input id="txtDocNo" type="text" style="width: calc(100% - 10px)">
								</div>
							</div>
							<div class="width-25 float-left">
								<div class="tit_60 poa">
		                        	<label>의뢰번호</label>
		                        </div>
		                        <div class="ml_60">
									<input id="txtReqNo" type="text" style="width: calc(100% - 10px)">
								</div>
							</div>
						</div>
						
					</div>
				</div>
				<div class="row">
					<div class="width-50 float-left">
						<div class="tit_60 poa">
                        	<label>제목</label>
                        </div>
                        <div class="ml_60">
							<input id="txtTitle" type="text" style="width: calc(100% - 95px)">
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<div class="row">				
			<div class="width-100 dib vat">
	            <label class="title float-left">PMS목록</label>
	            <button id="cmdFind" class="btn_basic_s float-right">검색</button></div>
			</div>
			<div class="row">
				<div class="az_board_basic az_board_basic_in" style="height: 70%">
			    	<div data-ax5grid="firstGrid" style="height: 100%;"></div>
				</div>
			</div>
		</div>
</body>

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/request/PMSListModal.js"/>"></script>