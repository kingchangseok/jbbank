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
		<div id="history_wrap"></div>
		<div class="az_search_wrap">
			<div class="az_in_wrap sr_status">
				<div class="l-wrap width-100 vat">
					<div class="width-25 dib por">
						<div class="vat dib margin-10-right">
                        	<label>구분</label>
                        </div>
                        <div id="cboGbn" data-ax5select="cboGbn" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true,selecteditable: true}" class="dib" style="width:calc(100% - 80px);">
					    </div>
					</div>

					<div class="width-25 dib por vat">
						<div class="dib vat margin-10-right">
                        	<label>조회기간</label>
                        </div>
                        <div id="picker" data-ax5picker="picker" class="az_input_group dib">
							<input id="datStD" name="datStD" type="text" placeholder="yyyy/mm/dd" style="width:100px;" class="f-cal" autocomplete="off">
							<button id="btnStD" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
							<span class="sim">∼</span>
							<input id="datEdD" name="datEdD" type="text" placeholder="yyyy/mm/dd" style="width:100px;" class="f-cal" autocomplete="off">
							<button id="btnEdD" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
						</div>													    
					</div>
					<div class="width-50 dib vat tar">
						<button class="btn_basic_s" id="btnSearch">조 회</button>
						<button class="btn_basic_s margin-3-left" data-grid-control="excel-export" id="btnExcel">엑셀저장</button>
					</div>
				</div>						
			</div>
		</div>	
		<div class="az_board_basic">
			<div id="mainGrid" data-ax5grid="mainGrid" style="width:100%; height: 100%;" class="resize"></div>
		</div>
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/administrator/AdminLogHistory.js"/>"></script>