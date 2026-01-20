<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<style>
.mt-12{
	margin-top: 12px;
}

.contents1 {
	width: calc(18% + 130px);
}

.textBoxDiv {
	margin-left: 50px;
	width: 30%;
}

.textBox {
	width: 100%;
}

.comboBox {
	width: calc(70% - 60px);
}
</style>
<div class="contentFrame">
	<div id="history_wrap">기본관리 <strong>&gt; 부재이력조회</strong></div>
		<div class="az_search_wrap">
			<div class="az_in_wrap">
				<div class="row">
					<div class="dib vat mt-12 contents1">
	                	<label class="poa">부재자</label>
	                	<div class="textBoxDiv vat dib">
		                	<input id="absenteeTxt" type="text" class="textBox">
	                	</div>
		               	<div class="margin-10-left comboBox dib">
			            	<div id="absenteeSel" data-ax5select="absenteeSel" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100 dib"></div>
		                </div>
	                </div>
	                <div class="dib vat contents1 mt-12" style="margin-left: 10px;">
	                	<label class="poa">대결자</label>
	                	<div class="textBoxDiv vat dib">
		                	<input id="subApproverTxt" type="text" class="textBox">
	                	</div>
		               	<div class="margin-10-left comboBox dib">
			            	<div id="subApproverSel" data-ax5select="subApproverSel" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100"></div>
		                </div>
	                </div>
	                <div class="float-right mt-12">
						<button class="btn_basic_s" id="btnQry" style="width: 70px;">조회</button>
	                </div>
	                <div class="dib float-right" style="width: 275px;">
						<div class="height-30 tar margin-right-50 dib">
							<label class="wLabel-left" style="width: 0px;"></label>
							<input id="radioCkOut" name="radioGroup" tabindex="8" type="radio" value="optCkOut" checked="checked"/>
							<label for="radioCkOut" style="margin-right: 10px;">부재시작일기준</label>
							<input id="radioCkIn" name="radioGroup" tabindex="8" type="radio" value="optCkIn"/>
							<label for="radioCkIn">부재종료일기준</label>
						</div>
						<div class="width-16 dib vat" style="min-width: 290px;">
							<div id="divPicker" data-ax5picker="basic" class="az_input_group dib">
								<input id="dateSt" name="start_date" type="text" placeholder="yyyy/mm/dd" style="width:80px;">
								<span class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
								<span class="sim">∼</span>
								<input id="dateEd" name="end_date" type="text" placeholder="yyyy/mm/dd" style="width:80px;">
								<span class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

	    <!-- 게시판 S-->
	    <div class="az_board_basic">
	    	<div id="mainGrid" data-ax5grid="mainGrid" data-ax5grid-config="{}" style="width:100%; height: 100%;" class="resize">
			</div>
		</div>

</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/mypage/AbsenceHistory.js"/>"></script>