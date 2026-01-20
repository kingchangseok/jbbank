<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />
<style>
.sel {
	min-width: 220px;
}
.width-23 {
	width: calc(26% - 38px);
}
.borderBox{
	padding: 0 5px; 
	border: 1px solid #ddd;
}
</style>

<!-- contener S -->
<div id="wrapper">
    <div class="contentFrame">
        <!-- history S--> 
        <div id="history_wrap">보고서 <strong>&gt; 프로그램목록</strong></div>
        <!-- history E-->    
        <!-- 검색 S-->    
		<div class="az_search_wrap">
			<div class="az_in_wrap">
				<div class="por">					
					<!-- 시스템 -->	
					<div class="width-23 dib vat">
					    <div class="margin-3-top margin-3-bottom">
		                  	<label id="lbSys" class="tit_80 poa">시스템</label>
							<div class="ml_60">
								<div id="cboSysCd" data-ax5select="cboSysCd" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-95 dib" style=" min-width: 130px;">
									
							    </div>
							</div>
							<div class="row">
			                    <label id="lbJob" class="poa tit_80">업무</label>
			                    <div class="ml_60">
									<div id="cboJob" data-ax5select="cboJob" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-95 dib" style=" min-width: 130px;">
										
								    </div>
								</div>
							</div>
						</div>
					</div>				
					<!-- 조건선택1 -->	
					<div class="width-20 dib sel margin-5-left margin-10-right">
					    <div class="margin-3-top margin-3-bottom">
		                  	<label id="lbSel1" class="tit_80 poa">조건선택1</label>
							<div class="ml_90">
								<div id="conditionSel1" data-ax5select="conditionSel1" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-95 dib"></div>
							</div>
							<div class="row">
			                    <label id="prgStatusLabel" class="poa tit_80"></label>
			                    <div class="ml_90">
									<div id="prgStatusSel" data-ax5select="prgStatusSel" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-95 dib"></div>
								</div>
							</div>
						</div>
					</div>
					<!-- 조건선택2 -->	
					<div class="width-20 dib vat sel margin-5-left margin-10-right">
					    <div class="margin-3-top margin-3-bottom">
		                  	<label id="lbSel2" class="tit_80 poa">조건선택2</label>
							<div class="ml_90">
								<div id="conditionSel2" data-ax5select="conditionSel2" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-90 dib">
									
							    </div>
							</div>
							<div class="row">
								<label id="conditionLabel" class="poa tit_80"></label>
			                    <div class="ml_90">
									<input id="conditionText" name="conditionText" type="text" class="width-90">
								</div>
							</div>
						</div>
					</div>		
					<!-- 버튼 -->
					<div class="dib tar vat float-right">
						<button class="btn_basic_s" data-grid-control="excel-export" id="btnExcel">엑셀저장</button>
	                    <div class="row">
	                    	<button class="btn_basic_s margin-5-left" id="btnSearch" style="width:70px;">조회</button>
	                    </div>
					</div>
					<div class="row vat">
						<div class="width-23 dib">
							<div class="por vat tar width-95 float-right">
								<input type="checkbox" class="checkbox-pie" id="chkDetail" data-label="세부항목포함"/>
							</div>
						</div>
						<div class="width-20 dib">
							<div class="dib float-right">
								<input id="Opt_Qry3" type="radio" name="rdoGbn1" value="0" /> <label for="Opt_Qry3">전체</label> 
								<input id="Opt_Qry4" type="radio" name="rdoGbn1" value="1" checked="checked" /> <label for="Opt_Qry4">신규중 제외</label>
								<input id="Opt_Qry5" type="radio" name="rdoGbn1" value="2" /> <label for="Opt_Qry5">신규중</label>
							</div>
						</div>
						<div class="width-20 dib">
							<div class="dib float-right">
								<input id="Opt_Qry0" type="radio" name="rdoGbn2" value="0" /> <label for="Opt_Qry0">전체</label> 
								<input id="Opt_Qry1" type="radio" name="rdoGbn2" value="1" checked="checked" /> <label for="Opt_Qry1">폐기분 제외</label>
								<input id="Opt_Qry2" type="radio" name="rdoGbn2" value="2" /> <label for="Opt_Qry2">폐기분</label>
							</div>
						</div>
					</div>
				</div>	
			</div>
		</div>
		
		<div class="az_board_basic" style="height:85%">
	    	<div id="mainGrid" data-ax5grid="mainGrid" data-ax5grid-config="{}" style="width:100%; height: 100%;" class="resize">
		
			</div>
		</div>
		
		<form name="popPam">
			<input type="hidden" name="acptno"/>
			<input type="hidden" name="user"/>
			<input type="hidden" name="itemid"/> 
			<input type="hidden" name="syscd"/>
			<input type="hidden" name="sysmsg"/>
			<input type="hidden" name="jobcd"/>
			<input type="hidden" name="rsrccd"/>
			<input type="hidden" name="rsrcname"/>
			<input type="hidden" name="dirpath"/>
		</form>
    </div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/program/PrgListReport.js"/>"></script>