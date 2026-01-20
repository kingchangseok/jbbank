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
		<div id="history_wrap">보고서 <strong>&gt; SR통계</strong></div>
		<div class="az_search_wrap">
			<div class="az_in_wrap sr_status">
				<div class="l-wrap width-100 vat">
					<div>
						<div class="width-22 dib por">
							<div class="vat dib margin-10-right">
	                        	<label>시스템</label>
	                        </div>
	                        <div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary'}" onchange="" class="dib" style="width:calc(100% - 80px);">
						    </div>
						</div>

						<div class="width-20 dib por vat" style="display: none;">
							<div class="dib vat margin-10-right">
		                    	<label id="lbJob">업무</label>
		                    </div>
							<div id="cboSrJob" data-ax5select="cboSrJob" data-ax5select-config="{size:'sm',theme:'primary'}" class="dib" style="width:calc(100% - 50px);">
						    </div>
						</div>
						
						<div class="width-15 dib por vat">
							<div class="dib vat margin-10-right">
		                    	<label id="lbJob">기간구분</label>
		                    </div>
							<div id="cboPeriod" data-ax5select="cboPeriod" data-ax5select-config="{size:'sm',theme:'primary'}" class="dib" style="width:calc(100% - 90px);">
						    </div>
						</div>
						<div class="width-35 dib por vat">
						</div>
						<div class="vat dib margin-5-right float-right poa vat" id="btnDiv" style="right:0;">
							<button class="btn_basic_s margin-10-right" data-grid-control="excel-export" style="width: 70px;" id="btnSearch">조 회</button>
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
<script type="text/javascript" src="<c:url value="/js/ecams/report/SRStatistics.js"/>"></script>