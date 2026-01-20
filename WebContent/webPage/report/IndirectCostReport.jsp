<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />


<div id="wrapper">
	<div class="contentFrame">
		<div id="history_wrap">보고서 <strong>&gt; 간접비 배부자료</strong></div>
		<div class="az_search_wrap">
			<div class="az_in_wrap sr_status">
				<div class="l-wrap width-100 vat">
						<div class="width-40 dib por vat">
							<div class="dib vat margin-10-right">
	                        	<label>&nbsp;&nbsp;&nbsp;조회기준</label>
	                        </div>
	                        <div id="picker" data-ax5picker="picker" class="az_input_group dib">
								<div class="dib margin-30-right">
					            	<input id="date" type="text" style="width:150px;" autocomplete="off"><span class="btn_calendar"><i class="fa fa-calendar-o"></i></span>						
								</div>
							</div>
						    <input id="opt1" name="radio" type="radio" value="opt1" checked="checked" style="margin-bottom: 5px;"/> 
						    <label for="opt1" style="margin-right: 10px;">상반기</label> 
						    <input id="opt2" name="radio"  type="radio" value="opt2" style="margin-bottom: 5px;"/> 
						    <label for="opt2">하반기</label> 													    
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
<script type="text/javascript" src="<c:url value="/js/ecams/report/IndirectCostReport.js"/>"></script>