<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<style>
.color-red {
	color: red;
	font-weight: bold;
}

.checkIcon { /* 툴팁박스 체크 아이콘 */
	content: '';
    width: 8.8px;
    height: 6.4px;
    position: absolute;
    top: 11px;
    /* right: 0px; */
    border: 2px solid #3492db;
    border-top: none;
    border-right: none;
    background: transparent;
    -webkit-transform: rotate(-50deg);
    -moz-transform: rotate(-50deg);
    -ms-transform: rotate(-50deg);
    -o-transform: rotate(-50deg);
    transform: rotate(-50deg);
}

.tipLabel { /* 툴팁 박스 내부 라벨 */
	line-height: 20px;
	font-size: 12px;
	font-weight: normal;
	letter-spacing: 0.2px;
	margin-left: 15px;
}
</style>

<div id="wrapper">
	<div class="contentFrame">
		<div id="history_wrap">보고서 <strong>&gt; 주간ISR처리현황</strong></div>
		<div class="az_search_wrap">
			<div class="az_in_wrap sr_status">
				<div class="l-wrap width-100 vat">
						<div class="width-35 dib por vat">
							<div class="dib vat margin-10-right">
	                        	<label>&nbsp;&nbsp;&nbsp;조회기준일</label>
	                        </div>
	                        <div id="picker" data-ax5picker="picker" class="az_input_group dib">
								<div class="dib margin-40-right">
					            	<input id="date" type="text" placeholder="yyyy/mm/dd" style="width:150px;" autocomplete="off"><span class="btn_calendar"><i class="fa fa-calendar-o"></i></span>						
								</div>
							</div>													    
						</div>
			       <div class="r_wrap">
				      <div class="vat dib">
					    <div class="vat dib margin-5-left">
						  <button id="btnSearch" name="btnSearch" class="btn_basic_s">조회</button>
					    </div>
					    <div class="vat dib margin-5-left">
						  <button id="btnExcel" name="btnExcel" class="btn_basic_s">엑셀저장</button>
				   	    </div>
				      </div>
			      </div>
				</div>						
			</div>
		</div>	
		<div class="az_board_basic">
			<div id="mainGrid" data-ax5grid="mainGrid" data-ax5grid-config="{}" style="width:100%; height: 100%;" class="resize">
			</div>
		</div> 
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/report/WeeklySRReport.js"/>"></script>