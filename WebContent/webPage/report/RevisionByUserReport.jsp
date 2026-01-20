<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />



<div class="contentFrame">
	<!-- history S-->
	<div id="history_wrap">보고서 <strong>&gt; 개인별변경추이</strong></div>
	<!-- history E-->         
    
    <!-- 검색 S-->    
	<div class="az_search_wrap">
		<div class="az_in_wrap por">
		
            <div class="width-20 dib vat">
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
			
            <div class="width-22 dib vat">
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
			
			<div class="width-25 dib vat">
               	<div class="margin-15-left">
                	<div>
	                    <label class="tit_150 poa">사용자명</label>
	                    <div class="por" style="margin-left: 70px;">
							<input id="txtUserName" type="text" class="dib" style="width: 70px;">
		                    <div class="dib" style="width:calc(100% - 70px);">
								<div id="cboUser" data-ax5select="cboUser" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100" ></div>
							</div>
						</div>
					</div>
					<div class="sm-row">
	                    <label id="lblTeam0" class="tit_150 poa" style="color: red; margin-left: 70px;">[사용자명 입력 후 엔터]</label>
   				    </div>
				</div>
			</div>
			
            <div class="width-33 dib vat">
               	<div class="margin-5-left">
                	<div class="sm-row tar">
                		<div id="divPicker" data-ax5picker="basic" class="az_input_group dib">
							<input id="dateSt" name="start_date" type="text" placeholder="yyyy/mm/dd" style="width: 92px;"> 
							<span class="btn_calendar"><i class="fa fa-calendar-o"></i></span> 
							<span class="sim">&sim;</span> 
							<input id="dateEd" name="end_date" type="text" placeholder="yyyy/mm/dd" style="width: 92px;">
							<span class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
						</div>
						<button id="btnSearch" class="btn_basic_s margin-5-left">조회</button>
					</div>					
					<div class="sm-row tar">
						<input id="optDay" name="radio" type="radio" value="optDay" checked="checked" /> 
						<label for="optDay" style="margin-right: 10px;">일별</label> 
						<input id="optWeek" name="radio"  type="radio" value="optWeek" /> 
						<label for="optWeek">주간</label> 
						<input id="optMon" name="radio" type="radio" value="optMon" /> 
						<label for="optMon">월간</label> 
						<input id="chkDel" type="checkbox"/>
						<label for="chkDel">폐기포함</label>
<!-- 					<input type="checkbox" class="checkbox-pie" id="chkDel" data-label="폐기포함"/> -->
						<button id="btnExcel" class="btn_basic_s margin-5-left"  data-grid-control="excel-export" >엑셀저장</button>
					</div>
				</div>
			</div>
			
		</div>
	</div>
	
		<div class="az_board_basic" style="height:100%">
	    	<div id="mainGrid" data-ax5grid="mainGrid" data-ax5grid-config="{}" style="width:100%; height: 100%;" class="resize">
			</div>
		</div>
		
</div>

 <c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/report/RevisionByUserReport.js"/>"></script>