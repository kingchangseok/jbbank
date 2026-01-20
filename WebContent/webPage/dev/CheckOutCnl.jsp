<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<div class="contentFrame">
  	<div id="history_wrap"></div>
       <!-- 검색 S-->      
	<div class="az_search_wrap">
		<div class="az_in_wrap checkout_tit">
         	<div class="row por">
              		<!-- 시스템 -->		
                <div class="width-30 dib vat">
                    <label id="lbSystem" class="tit_60 poa">*시스템</label>
                    <div class="ml_60 vat">
						<div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="dib width-50"></div>
					</div>
				</div>
				<div class="width-15 dib vat tar">
					<input type="checkbox" class='checkbox-pie' name='chkMySelf' id='chkMySelf' data-label="본인건">
	              	</div>
		           <div class="width-55 dib vat">
		              	<label id="lblProg" class="tit_100 poa">프로그램명</label>
	                   <div class="ml_80 vat">
							<input id="txtRsrcName" name="txtRsrcName" type="text" style="width: calc(100% - 80px);">
							<button id="btnSearch" class="btn_basic_s ml_10" style="width:70px;">검색</button>
						</div>
				</div>
			</div>
		</div>
	</div>
	<!--검색E-->
	<div>
		<!-- 게시판 S-->
	    <div class="az_board_basic az_board_basic_in" style="height: 37%;">
	    	<div data-ax5grid="first-grid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;" class="resize"></div>
		</div>	
		<div class="por margin-5-top margin-5-bottom">
			<div class=""><input type="checkbox" class="checkbox-pie" id="chkDetail" data-label="체크아웃취소항목상세보기"></input></div>
			<div class="poa_r">
				<div class="vat dib">
					<button id="btnAdd" class="btn_basic_s">추가</button>
				</div>
				<div class="vat dib">
					<button id="btnDel" class="btn_basic_s">제거</button>
				</div>
			</div>
		</div>
		<!-- 게시판 E -->
	</div>
	
	<div>
		<!-- 게시판 S-->
	    <div class="az_board_basic az_board_basic_in" style="height: 39%;">
	    	<div data-ax5grid="second-grid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;" class="resize"></div>
		</div>	
		<!-- 게시판 E -->
	</div>
	
	<div class="row">
		<div class="width-100 dib vat">
			<label class="tit_60 poa">*신청사유</label>
            <div class="ml_60 tal">
				<input id="txtSayu" name="txtSayu" type="text" style="width: calc(100% - 115px);"></input>
				<div class="vat dib">
					 <button id="btnReq" class="btn_basic_s" style="width:110px;">대여취소</button>
				</div>
			</div>
		</div>
	</div>
</div>

<form name="popPam">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
	<input type="hidden" name="adminYN"/>
	<input type="hidden" name="rgtList"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript"	src="<c:url value="/js/ecams/dev/CheckOutCnl.js"/>"></script>