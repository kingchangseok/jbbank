<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<style>
.color-red {
	color: red;
	font-weight: bold;
}

</style>

<div id="wrapper">
	<div class="contentFrame">
		<div id="history_wrap">보고서 <strong>&gt; 프로그램보유현황</strong></div>
		<div class="az_search_wrap">
			<div class="az_in_wrap sr_status">
				<div class="row">
					<div class="width-25 dib por">
						<div class="vat dib margin-10-right">
                        	<label>시스템</label>
                        </div>
                        <div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" onchange="" class="dib" style="width:calc(100% - 80px);"></div>
                        <div id="cboJob" data-ax5select="cboJob" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" onchange="" class="dib" style="display: none;"></div>
					</div>
					<div class="width-25 dib por vat">
						<div class="dib vat margin-10-right">
                        	<label>조회기준일</label>
                        </div>
                       <div id="divPicker" class="az_input_group dib" data-ax5picker="basic">
							<div class="dib">
				            	<input id="datStD" type="text" placeholder="yyyy/mm/dd" style="width:100px;"><span class="btn_calendar"><i class="fa fa-calendar-o"></i></span>						
							</div>
						</div>													    
					</div>
					<div class="dib float-right">
						<button class="btn_basic_s" id="btnSearch" style="width: 70px;">조회</button>
						<button class="btn_basic_s margin-3-left" data-grid-control="excel-export" id="btnExcel">엑셀저장</button>
					</div>
				</div>
			</div>
		</div>	
		<div class="az_board_basic">
			<div id="mainGrid" data-ax5grid="mainGrid" data-ax5grid-config="{}" style="width:100%; height: 100%;" class="resize"></div>
		</div>
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/report/PrgPosessionReport.js"/>"></script>