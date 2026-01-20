<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<style>
.font-blue {
	color: blue;
}

.font-red {
	color: red;
	font-weight: bold;
}

label {
	min-width: 54px;
}

.pic-wid {
	width: 120px;
}
	
.block1 {
	width: calc(100% - 84px);
	min-width: 90px;
	margin-right: 25px;
}

.tr-sum td span {
	color: red;
	font-weight: bold;
}

.item{
	display: inline-block;
	width: calc(25% + 10px);
	vertical-align: top;
}

.rowItem{
	width:calc(100% - 310px);
	display: inline-block;
}

.item.nolbl {width: calc(25% - 30px);}
.item.nolbl .block1{width: calc(100% - 25px);}

.btn_calendar{
	padding: 5px;
}
</style>

<div id="wrapper"> 
	<div class="contentFrame">
		<div id="history_wrap">보고서 <strong>&gt; 형상관리운영현황</strong></div>
		<div class="az_search_wrap">
			<div class="az_in_wrap sr_status">
				<div class="l-wrap width-100 vat">
					<div class="row">
						<div class="dib width-100">
							<div class="rowItem">
								<div class="item nolbl">
									<div class="block1 dib por" style="vertical-align: top;">	
			                        	<label>*조회구분</label>					    
									</div>
								</div>
								<div class="item">
									<div class="dib vat">
			                        	<label>*1단계</label>
			                        </div>
									<div id="step1" data-ax5select="step1" data-ax5select-config="{size:'sm',theme:'primary'}" class="block1 dib por">
									</div>
								</div>
								<div class="item">
									<div class="dib vat">
			                        	<label>*3단계</label>
			                        </div>
									<div id="step3" data-ax5select="step3" data-ax5select-config="{size:'sm',theme:'primary'}" class="block1 dib por">
								    </div>	
								</div>
								<div class="item">
									<div class="dib vat width-100" style="vertical-align: top;">
			                        	<label>시스템</label>			
									<div id="systemSel" style="width: calc(100% - 84px); margin-right: 0px;" data-ax5select="systemSel" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="block1 dib por sys">
								    </div>	
									</div>
								</div>
							</div>
							<div class="dib vat" style="width: 225px;">
								<label>조회시작월</label>
								<div id="picker1" data-ax5picker="basic" class="az_input_group dib margin-10-left pic-wid">
						            <input id="datStD" type="text" placeholder="yyyy/mm/dd" class="width-70 dib">
						            <span class="btn_calendar width-30"><i class="fa fa-calendar-o"></i></span>						
								</div>
							</div>
							<div class="vat dib margin-5-right"><!--수정-->
								<button class="btn_basic_s" id="btnSearch" style="width: 70px;">조회</button>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="dib width-100">
							<div class="rowItem">
								<div class="item nolbl">
									<div id="dateStd" data-ax5select="dateStd" data-ax5select-config="{size:'sm',theme:'primary'}" class="block1 dib por">
								    </div>
								</div>
								<div class="item">
									<div class="dib vat">
			                        	<label>*2단계</label>
			                        </div>
									<div id="step2" data-ax5select="step2" data-ax5select-config="{size:'sm',theme:'primary'}" class="block1 dib por">
									</div>
								</div>
								<div class="item">
									<div class="dib vat">
			                        	<label>*4단계</label>
			                        </div>
									<div id="step4" data-ax5select="step4" data-ax5select-config="{size:'sm',theme:'primary'}" class="block1 dib por">
									</div>
								</div>
								<div class="item">
									<div class="dib vat width-100" style="vertical-align: top;">
			                        	<label>업무</label>			
									<div id="cboJob" style="width: calc(100% - 84px); margin-right: 0px;" data-ax5select="cboJob" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="block1 dib por sys">
								    </div>	
									</div>
								</div>
							</div>
							<div class="dib vat" style="width: 225px;">
								<label>조회종료월</label>
								<div id="picker2" data-ax5picker="basic2" class="az_input_group dib margin-10-left pic-wid">
						            <input id="datEdD" type="text" placeholder="yyyy/mm/dd" class="width-70 dib">
						            <span class="btn_calendar width-30"><i class="fa fa-calendar-o"></i></span>						
								</div>
							</div>
							<div class="vat dib margin-5-right"><!--수정-->
								<button class="btn_basic_s" data-grid-control="excel-export" id="btnExcel" style="display: none;">엑셀저장</button>
							</div>
						</div>
					</div>
				</div>						
			</div>
		</div>	
		<div class="az_board_basic" style="height: 85%;">
			<div id="mainGrid" data-ax5grid="mainGrid" data-ax5grid-config="{}" style="width:100%; height: 100%;" class="resize">
			
			</div>
		</div>
	</div>
	
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/report/ConfigOpReport.js"/>"></script>
