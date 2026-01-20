<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<div class="contentFrame">
    <!-- history S-->
    <div id="history_wrap"></div>
    <!-- history E-->         
    <!-- 검색 S-->    
	<div class="az_search_wrap">
		<div class="az_in_wrap">
			<div class="l_wrap dib" style="width: 80%;">
                <div class="por">
                	<div class="width-20 dib vat">
	                	<label class="tit-80 dib poa">시스템</label>
	                	<div class="ml_80">
		                    <div id = "cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm', theme:'primary',selecteditable: true}" class="width-100"></div> 	
	                	</div>
					</div>
					<div class="width-20 dib vat" id="discDiv">
                		<label class="tit-80 dib poa">&nbsp;&nbsp;&nbsp;신청부서</label>
	                	<div class="ml_80">
							<div id="cboDept" data-ax5select="cboDept" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100"></div>
						</div>
					</div>
					<div class="width-20 dib vat" id="discDiv">
                		<label class="tit-80 dib poa">&nbsp;&nbsp;&nbsp;진행상태</label>
	                	<div class="ml_100">
							<div id="cboStat" data-ax5select="cboStat" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-95"></div>
						</div>
					</div>
					<div class="width-20 dib vat">
						<input type="checkbox" class="checkbox-file" id="chkEmg" data-label="긴급건만"/>
					</div> 
                	<div class="dib vat">
	                	<label class="tit-80 dib poa">&nbsp;&nbsp;&nbsp;조회시작일</label>
	                	<div class="ml_80" id="divPicker" data-ax5picker="basic"class="az_input_group dib">
							<input id="datStD" name="datStD" type="text" placeholder="yyyy/mm/dd" style="width: 80px;">
							<button id="btnStD" class="btn_calendar">
								<i class="fa fa-calendar-o"></i>
							</button>
						</div>
					</div>
				</div>
                <div class="row por">
                	<div class="width-20 dib vat" id="discDiv">
                		<label class="tit-80 dib poa">신청구분</label>
	                	<div class="ml_80">
							<div id="cboSin" data-ax5select="cboSin" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100"></div>
						</div>
					</div>
                	<div class="width-20 dib vat" id="idDiv">
                		<label class="tit-80 dib poa">&nbsp;&nbsp;&nbsp;신청인</label>
	                	<div class="ml_80">
							<input id="txtUser" type="text" class="width-100" />
	                	</div>
					</div>
                	<div class="width-40 dib vat" id="idDiv">
                		<label class="tit-80 dib poa">&nbsp;&nbsp;&nbsp;작업지시 제목</label>
	                	<div class="ml_100">
							<input id="txtIsr" type="text" class="width-100" />
	                	</div>
					</div>
					<div class="dib vat">
	                	<label class="tit-80 dib poa">&nbsp;&nbsp;&nbsp;조회종료일</label>
	                	<div class="ml_80" id="divPicker2" data-ax5picker="basic2"class="az_input_group dib">
							<input id="datEdD" name="datEdD" type="text" placeholder="yyyy/mm/dd" style="width: 80px;">
							<button id="btnEdD" class="btn_calendar">
								<i class="fa fa-calendar-o"></i>
							</button>
						</div>
					</div>
				</div>
				<div class="row por">
					<div class="row thumbnail dib">
						<span class="r_nail margin-10-top">반려 또는 취소</span>
						<span class="p_nail">시스템처리 중 에러발생</span>
						<span class="g_nail">처리완료</span>
						<span class="b_nail">진행중</span>
					</div>
				</div>
			</div>
			<div class="r_wrap dib">
				<div class="dib vat">
					<button class="btn_basic_s" data-grid-control="excel-export" id="btnExcel" style="margin-left: 5px;">엑셀저장</button>
					<div class="row">
						<button class="btn_basic_s" id="btnSearch" style="width: 70px; margin-left: 5px;">조회</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!--검색E-->		

    <!-- 게시판 S-->
    <div class="az_board_basic" style="height: 80%">
    	<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;" class="resize"></div>
	</div>	
</div>
<form name="popPam">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
	<input type="hidden" name="adminYN"/>
	<input type="hidden" name="rgtList"/>
	<input type="hidden" name="itemid"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/approval/RequestStatus.js"/>"></script>
