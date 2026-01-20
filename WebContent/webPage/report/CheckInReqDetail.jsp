<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<div class="contentFrame">
    <!-- history S-->
    <div id="history_wrap">보고서 <strong>&gt; 체크인내역 세부조회 </strong></div>
    <!-- history E-->         
    <!-- 검색 S-->    
	<div class="az_search_wrap">
		<div class="az_in_wrap">
                <div class="por">
                	<div class="width-20 dib vat">
	                	<label class="tit-80 dib poa">시스템</label>
	                	<div class="ml_60">
		                    <div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100"></div>
	                	</div>
					</div>
                	<div class="float-right">
                		<div class="dib">
						<div id="divPicker" data-ax5picker="basic"class="az_input_group dib">
							<label class="tit-80 dib poa">변경시작일</label>
							<div class="ml_70 dib margin-20-right">
								<input id="dateSt" name="datStD" type="text" placeholder="yyyy/mm/dd" style="width: 80px;">
								<button id="btnStD" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
							</div>
							<label class="tit-80 dib poa">변경종료일</label>
							<div class="ml_70 dib margin-10-right">
								<input id="dateEd" name="datEdD" type="text" placeholder="yyyy/mm/dd" style="width: 80px;">
								<button id="btnEdD" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
							</div>
						</div>
						</div>
						<button class="btn_basic_s" id="btnSearch" style="width: 70px;">조회</button>
					</div>
				</div>
                <div class="row por">
                	<div class="width-20 dib vat">
	                	<label class="tit-80 dib poa">관련문서</label>
	                	<div class="ml_60">
		                   <input id="txtPrjno" type="text" class="width-100" />
	                	</div>
					</div>
                	<div class="width-20 dib vat" id="discDiv">
					</div>
                	<div class="width-20 dib vat" id="idDiv">
					</div>
					<div class="float-right">
						<button class="btn_basic_s" id="btnExcel" style="width: 70px;">엑셀저장</button>
	                </div>
s				</div>
		</div>
	</div>
	<!--검색E-->		

    <!-- 게시판 S-->
    <div class="az_board_basic" style="height: 80%">
    	<div data-ax5grid="mainGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;" class="resize"></div>
	</div>	
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/report/CheckInReqDetail.js"/>"></script>
