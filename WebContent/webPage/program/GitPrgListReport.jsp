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
				<!-- 시스템 -->	
				<div class="row width-20 dib vat">
                  	<label id="lbSys" class="tit_60 poa text-right">시스템</label>
					<div class="ml_70">
						<div id="cboSysCd" data-ax5select="cboSysCd" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100 dib" style=" min-width: 130px;">
							
					    </div>
					</div>
				</div>	
				<div class="width-20 dib vat">
                    <label id="lbJob" class="poa tit_60 text-right">업무</label>
                    <div class="ml_70">
						<div id="cboJob" data-ax5select="cboJob" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100 dib" style=" min-width: 130px;">										
					    </div>
					</div>
				</div>	
				<div class="width-20 dib vat">
                    <label class="poa tit_60 text-right">상태</label>
                    <div class="ml_70">
						<div id="cboSta" data-ax5select="cboSta" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100 dib" style=" min-width: 130px;">										
					    </div>
					</div>
				</div>	
				<div class="width-20 dib vat">
                    <label class="poa tit_70 text-right">프로그램명</label>
                    <div class="ml_80">
						<input id="txtProg" name="txtProg" type="text" class="width-100">
					</div>
				</div>		
				<!-- 버튼 -->
				<div class="dib tar vat float-right">
					<button class="btn_basic_s" data-grid-control="excel-export" id="btnExcel">엑셀저장</button>
                    <button class="btn_basic_s margin-5-left" id="btnSearch" style="width:70px;">조회</button>
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
<script type="text/javascript" src="<c:url value="/js/ecams/program/GitPrgListReport.js"/>"></script>