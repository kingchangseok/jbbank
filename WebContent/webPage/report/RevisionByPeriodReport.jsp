<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- 차트 사용 -->
<script src="<c:url value="/scripts/tui-code-snippet.min.js"/>" type="text/javascript"></script>
<script src="<c:url value="/scripts/tui-chart-all.min.js"/>" type="text/javascript"></script>
<link rel="stylesheet" href="<c:url value="/styles/tui-chart.min.css"/>" />

<c:import url="/webPage/common/common.jsp" />



<div class="contentFrame">
	<!-- history S-->
	<div id="history_wrap">보고서 <strong>&gt; 개인별변경추이</strong></div>
	<!-- history E-->         
    
    <!-- 검색 S-->    
	<div class="az_search_wrap">
		<div class="az_in_wrap por">
		
            <div class="width-24 dib vat">
               	<div>
                    <label class="tit_150 poa">시스템</label>
                    <div style="margin-left: 70px;">
						<div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100" ></div>
					</div>
				</div>
               	<div class="sm-row">
                    <label class="tit_150 poa">조회기준</label>
                    <div style="margin-left: 70px;">
						<div id="cboSel" data-ax5select="cboSel" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100" ></div>
					</div>
				</div>
			</div>
			
            <div class="width-26 dib vat">
               	<div class="margin-15-left">
                	<div>
	                    <label class="tit_150 poa">프로그램종류</label>
	                    <div  style="margin-left: 90px;">
							<div id="cboProg" data-ax5select="cboProg" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100" ></div>
						</div>
					</div>
                	<div class="sm-row">
	                    <label class="tit_150 poa">요청구분</label>
	                    <div style="margin-left: 90px;">
							<div id="cboReq" data-ax5select="cboReq" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100" ></div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="width-27 dib vat">
               	<div class="margin-15-left">
                	<div>
	                    <label class="tit_150 poa">요청팀</label>
	                    <div style="margin-left: 70px;">
							<div id="cboTeam" data-ax5select="cboTeam" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-94" ></div>
						</div>
					</div>
					<div class="sm-row">
					      <div  id="divPicker" data-ax5picker="basic" class="az_input_group dib" style="margin-left: 0px;">
							<input id="dateSt" name="start_date" type="text" placeholder="yyyy/mm/dd" style="width: 80px;"> 
							<span class="btn_calendar"><i class="fa fa-calendar-o"></i></span> 
							<span class="sim">&sim;</span> 
							<input id="dateEd" name="end_date" type="text" placeholder="yyyy/mm/dd" style="width: 80px;">
							<span class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
						  </div>
   				    </div>
				</div>
			</div>
			
            <div class="width-23 dib vat">
               	<div class="margin-5-left">
                	<div class="sm-row tar">
						<input id="optDay" name="radio" type="radio" value="optDay" checked="checked" /> 
						<label for="optDay" style="margin-right: 10px;">일별</label> 
						<input id="optWeek" name="radio"  type="radio" value="optWeek" /> 
						<label for="optWeek">주간</label> 
						<input id="optMon" name="radio" type="radio" value="optMon" /> 
						<label for="optMon">월간</label> 
						<input id="chkDel" type="checkbox"/>
						<label for="chkDel">폐기포함</label>
					</div>					
					<div class="sm-row tar">
<!-- 					<input type="checkbox" class="checkbox-pie" id="chkDel" data-label="폐기포함"/> -->
						<button id="btnSearch" class="btn_basic_s margin-5-left">조회</button>
						<button id="btnExcel" class="btn_basic_s margin-5-left"  data-grid-control="excel-export" >엑셀저장</button>
						<button id="btnGraph" class="btn_basic_s margin-5-left">그래프보기</button>
					</div>
				</div>
			</div>
			
		</div>
	</div>
	
  <div id="combo">	
 	<div id="grid" class="az_board_basic" style="height:100%">
	    <div id="mainGrid" data-ax5grid="mainGrid" data-ax5grid-config="{}" style="width:100%; height: 100%;" class="resize">
		</div>
	</div> 
	<div id="graph" class="container-fluid padding-40-top" style="width:100%; height:100%; display: none" class="resize">
		<div class="row" >
		<div id="column-chart"></div>
		</div>
	</div>
  </div>
		
</div>

 <c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/report/RevisionByPeriodReport.js"/>"></script>