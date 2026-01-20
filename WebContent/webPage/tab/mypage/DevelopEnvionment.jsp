<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />



<div class="contentFrame">
    
	<div class="az_search_wrap">
		<div class="az_in_wrap por">
		
            <div class="width-100 dib vat">
               	<div>
                    <label class="tit_150 poa">시스템명</label>
                    <div style="margin-left: 130px;">
						<div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-20" ></div>
					</div>
				</div>
               	<div class="sm-row">
                    <label class="tit_150 poa">개발 Home Directory</label>
                    <input id="txtDir" type="text" style="width: 600px; margin-left: 130px;">
                    <button id="btnReg" class="btn_basic_s margin-5-left">등록</button>
			   </div>
               <div class="sm-row">
               <label>&nbsp;&nbsp;&nbsp;</label>
	           </div>
               <div class="sm-row">
               <label>등록된 개발환경 목록</label>
	           </div>
		   </div>
	</div>
	
		<div class="az_board_basic" style="height:100%">
	    	<div id="mainGrid" data-ax5grid="mainGrid" data-ax5grid-config="{}" style="width:100%; height: 100%;" class="resize">
			</div>
		</div>
		
		<div class="r_wrap" style="margin-top: 5px;">
            <button id="btnQry" class="btn_basic_s margin-5-left">조회</button>
            <button id="btnDel" class="btn_basic_s margin-5-left">삭제</button>
		</div>
		
    </div>
</div>

 <c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/tab/mypage/DevelopEnvionment.js"/>"></script>