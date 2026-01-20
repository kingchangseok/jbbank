<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<style>
.width-250 {
	width: 250px;
}
</style>
<div class="contentFrame">
	<div id="history_wrap">보고서 <strong>&gt; 시스템별사용자현황</strong></div>
		<div class="az_search_wrap" style="min-width: 1160px;">
			<div class="az_in_wrap">
				<div class="row">
					<div class="dib vat width-20 ">
	                	<label class="tit_60 poa">시스템</label>
		               	<div class="ml_70">
			            	<div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100"></div>
		                </div>
	                </div>		
	                <div class="dib vat float-right">
						<button class="btn_basic_s" id="btnQry" style="width: 70px;">조회</button>
						<button class="btn_basic_s margin-3-left" id="btnExcel" style="width: 70px;">엑셀저장</button>
	                </div>
				</div>
			</div>
		</div>

	    <!-- 게시판 S-->
	    <div class="az_board_basic" style="height:calc(100% - 90px)">
	    	<div id="dataGrid" data-ax5grid="dataGrid" data-ax5grid-config="{}" style="width:100%; height: 100%;">
			</div>
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
<script type="text/javascript" src="<c:url value="/js/ecams/report/SysUserList.js"/>"></script>