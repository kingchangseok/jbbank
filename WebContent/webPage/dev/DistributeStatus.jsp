<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />
<style>
.sel {
	min-width: 220px;
}

.borderBox{
	padding: 0 5px; 
	border: 1px solid #ddd;
}
</style>
<div id="wrapper">
    <div class="contentFrame">
        <div id="history_wrap"></div> 
        <!-- 검색 S-->    
		<div class="az_search_wrap">
			<div class="az_in_wrap">
				<div class="por">					
					<!-- 시스템 -->	
					<div class="width-20 dib vat margin-5-left margin-10-right">
					    <div class="margin-3-top margin-3-bottom">
		                  <label id="lbSys" class="tit_60 poa">신청구분</label>
							<div class="ml_70">
								<div id="cboSin" data-ax5select="cboSin" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100 dib">
							    </div>
							</div>
						</div>
						<div class="margin-3-top margin-3-bottom">
		                  	<label id="lbSys" class="tit_60 poa">진행상태</label>
							<div class="ml_70">
								<div id="cboSta" data-ax5select="cboSta" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100 dib">
							    </div>
							</div>
						</div>
					</div>
					<div class="width-40 dib vat">
					    <div class="margin-3-top margin-3-bottom">
		                  	<label id="lbSys" class="tit_60 poa">프로그램명</label>
							<div class="ml_70">
								<input class="width-100 dib" id="txtPrgName" type="text"></input>
							</div>
						</div>	
						<div class="margin-3-top margin-3-bottom">
		            		<label class="tit_80 poa" style="margin-left: 0px;">신청일</label>
							<div id="divPicker" class="az_input_group dib ml_70" data-ax5picker="basic">
								<input id="dateSt" name="dateSt" type="text"placeholder="yyyy/mm/dd" style="width: 100px;"> 
								<span class="btn_calendar"><i class="fa fa-calendar-o"></i></span> 
								<span class="sim">∼</span>
								<input id="dateEd" name="dateEd"type="text" placeholder="yyyy/mm/dd" style="width: 100px;">
								<span class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
							</div>		
						</div>
					</div>
					<div class="dib vat">
						<div class="margin-20-left">
							<input type="checkbox" class="checkbox-pie" id="chkDetail" data-label="상세조회" />
							<input type="checkbox" class="checkbox-pie" id="chkProg" data-label="프로그램별" />
							<input type="checkbox" class="checkbox-pie" id="chkEmg" data-label="긴급건만" />
						</div>
					</div>
					<div class="float-right">
		               	<button id="btnQry" class="btn_basic_s" style="width: 70px;">조회</button>
		                <div class="row">
						<button id="btnExcel" class="btn_basic_s">엑셀저장</button>
		                </div>
					</div>
				</div>
				<!-- 버튼 -->
		</div>	
	</div>
		<div class="az_board_basic" style="height:80%">
	    	<div id="mainGrid" data-ax5grid="mainGrid" data-ax5grid-config="{}" style="width:100%; height: 100%;" class="resize">
	    	</div>
		</div>
    </div>
</div>
<form name="popPam">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
	<input type="hidden" name="rsrcname"/>
</form>
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/dev/DistributeStatus.js"/>"></script>