<!-- 장기체크아웃현황 -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div class="contentFrame">
	<div id="history_wrap">보고서 <strong>&gt; 체크인현황</strong></div>
	<div class="az_search_wrap sm-row">
		<div class="az_in_wrap por">
		
			<div class="por">
				<div class="width-22 dib vat">
					<label class="tit_60 poa">시스템</label> 
					<div class="ml_60">
						<div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-90"></div>
					</div>
				</div>
				<div class="width-22 dib vat">
					<label class="tit_80 poa">업무</label> 
					<div class="ml_80">
						<div id="cboJob" data-ax5select="cboJob" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-90"></div>
					</div>
				</div>
				<div class="width-22 dib vat">
					<label class="tit_80 poa">SPMS</label> 
					<div class="ml_80">
						<input id="txtSPMS" type="text" class="width-90"/>
					</div>
					
				</div>
				<div class="width-34 dib vat">
				<!-- 	<input id="chkDay" type="checkbox" style="margin-top: 5px;"/> 
					<label for="chkDay">신청일기준</label>  -->
					<input id="rdoStrDate"  type="radio" name="rdoDate" value="0" checked="checked"/>
					<label for="rdoStrDate" >신청일기준</label>
					<input id="rdoEndDate" type="radio" name="rdoDate" value="1"/>
					<label for="rdoEndDate">완료일기준</label>
				</div>
			</div>
			
			<div class="row">
				<div class="width-22 dib">
					<label class="tit_60 poa">신청구분</label> 
					<div class="ml_60">
						<div id="cboSin" data-ax5select="cboSin" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-90"></div>
					</div>
				</div>
				<div class="width-22 dib vat">
					<label class="tit_80 poa">신청부서</label> 
					<div class="ml_80">
						<div id="cboDept" data-ax5select="cboDept" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-90"></div>
					</div>
				</div>
				<div class="width-22 dib vat">
					<label class="tit_80 poa">신청자</label> 
					<div class="ml_80">
						<input id="txtUser" type="text" class="width-90"/>
					</div>
				</div>
				<div class="width-34 dib vat">
					<label class="tit_60 poa">조회기간</label> 
					<div class="ml_60">
						<div data-ax5picker="basic" class="az_input_group dib vat">
							<input id="dateSt" name="dateSt" type="text" style="width:100px;" class="f-cal" autocomplete="off">
							<button id="btnSt" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
						</div>
						<span class="sim">∼</span>
						<div data-ax5picker="basic2" class="az_input_group dib vat">
							<input id="dateEd" name="dateEd" type="text" style="width:100px;" class="f-cal" autocomplete="off">
							<button id="btnEd" class="btn_calendar"><i class="fa fa-calendar-o"></i></button>
						</div>
						<button id="btnQry" class="btn_basic_s" style="margin-left: 10px; float: right;">조회</button>
						<button id="btnExcel" class="btn_basic_s" style="margin-left: 5px; float: right;">엑셀저장</button>
					</div>
				</div>
			</div>
			
			<div class="row">
				<div class="width-22 dib vat">
					<label class="tit_60 poa">진행상태</label> 
					<div class="ml_60">
						<div id="cboStep" data-ax5select="cboStep" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-90"></div>
					</div>
				</div>
				<div class="width-22 dib vat">
					<label class="tit_80 poa">처리구분</label> 
					<div class="ml_80">
						<div id="cboGbn" data-ax5select="cboGbn" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-90"></div>
					</div>
				</div>
				<div class="width-22 dib vat">
					<label class="tit_80 poa">프로그램명</label> 
					<div class="ml_80">
						<input id=txtRsrc type="text" class="width-90"/>
					</div>
				</div>
				<div class="width-34 dib vat">
					<label class="tit_60 poa">구분</label> 
					<div class="ml_60">
						<div id="cboQry" data-ax5select="cboQry" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100"></div>
					</div>
				</div>
			</div>

		</div>
	</div>

	<div class="az_board_basic" style="height: 80%;">
		<div id="gridList" data-ax5grid="gridList" data-ax5grid-config="{}" style="height: 100%;" class="frameResize"></div>
	</div>
	
	<form name="popPam">
		<input type="hidden" name="acptno"/>
		<input type="hidden" name="user"/>
		<input type="hidden" name="itemid"/> 
		<input type="hidden" name="syscd"/>
		<input type="hidden" name="rsrccd"/>
		<input type="hidden" name="rsrcname"/>
	</form>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/report/CheckInList.js"/>"></script>