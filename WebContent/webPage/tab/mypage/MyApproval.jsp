<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<c:import url="/js/ecams/common/commonscript.jsp" />


<div class="contentFrame">
      <!-- history E-->   
	<div class="half_wrap_cb">
		<div class="l_wrap width-30">
			<div class="margin-5-right">				   
				<!--검색E-->
				<!--메뉴체계 S-->					
				<div class="half_wrap">
					<div class="scrollBind row" style="height: 100%; border: 1px dotted gray;">
						<ul id="treeMenu" class="ztree"></ul>
	    			</div>
				</div>
	   		</div>
		</div>
		<div class="r_wrap width-70">
			<div class="margin-5-left">		   
				<div class="row">
				    <input id="Name" name="radio" type="radio" value="Name" checked="checked" /> 
					<label for="Name" style="margin-right: 10px;">성명</label> 
					<input id="Buse" name="radio"  type="radio" value="Buse" /> 
					<label for="Buse">부서명</label> 
                    <input id="txtUser" type="text" style="width:250px; margin-left: 350px;">
                    <button id="btnSearch" class="btn_basic_s margin-5-left">검색</button>
                </div>
 
				<div class="row" style="margin-top: 10px;">		
					<div data-ax5grid="first-grid" data-ax5grid-config="{}" style="height: 42%;"></div>
				</div>

				<div class="row" style="margin-left: 350px;">		
                    <button id="dataDel" class="btn_basic_s margin-10-top">▲</button>
                    <button id="dataAdd" class="btn_basic_s margin-10-top">▼</button>
				</div>
				
				<div class="row"  style="margin-top: 10px">
					<div class="dib vat width-35">
	                	<label class="tit_100 poa margin-10-left">결재선구분</label>
                        <div  class="ml_100 vat">
							<div id="cboSignNM" data-ax5select="cboSignNM" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100" ></div>
						</div>
	                </div>
	                <div class="dib vat width-35" style="margin-left: 50px">
	                	<label class="tit_100 poa margin-10-left">결재선명칭</label>
	                	<div class="ml_100 vat">
		                	<input id="txtSignNM" type="text" class="width-100" style="margin-left: 10px;"/>
	                	</div>
	                </div>
	                <div class="dib vat width-15" style="margin-left: 63px">
                    <button id="btnReg" class="btn_basic_s margin-0-left">등록</button>
                    <button id="btnDel" class="btn_basic_s margin-0-left">삭제</button>
                    </div>
			    </div>

				<div class="row" style="margin-top: 10px;">		
					<div data-ax5grid="second-grid" data-ax5grid-config="{}" style="height: 42%;"></div>
				</div>
			</div>
		</div>
	</div>
</div>



<script type="text/javascript" src="<c:url value="/js/ecams/tab/mypage/MyApproval.js"/>"></script>