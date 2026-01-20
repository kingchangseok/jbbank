<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp"/>

<!-- contener S -->
<div id="wrapper">
    <div class="contentFrame">
    	<div style="height: calc(100% - 400px);"> <!-- 100% - 360px --> 
	        <div id="history_wrap"></div>
		    <!-- 검색 S-->    
			<div class="az_search_wrap">
				<div class="az_in_wrap por">
					<div class="row">
		                <div class="width-20 dib vat">
		                    <label id="lbSystem" class="tit_50 poa">시스템</label>
		                    <div class="ml_50 vat">
								<div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="dib width-100"></div>
							</div>
						</div>
		                <div class="width-20 dib vat">
		                    <label id="lbRsrcName" class="tit_80 poa">&nbsp;&nbsp;&nbsp;프로그램명</label>
		                    <div class="ml_80">
								<input id="txtRsrcName" name="txtRsrcName" type="text" class="width-95 dib" placeholder="프로그램명을 입력하세요.">
							</div>
						</div>	
						<div class="dib vat" style="width: calc(60% - 85px)">
			            	<label class="tit_80 poa">프로그램경로</label>
			                <div class="ml_80 vat">
								<input id="txtDir" name="txtDir" type="text" class="dib margin-5-right" style="width:calc(100% - 110px);" placeholder="프로그램경로를 입력하세요.">
								<input type="checkbox" class='checkbox-pie' name='chkLike' id='chkLike' data-label="Like검색(경로)">
							</div>
						</div>
						<div class="float-right">
							<button id="btnQry" class="btn_basic_s margin-5-left" style="width:70px;">조회</button>
						</div>
					</div>
				</div>
			</div>
		    <!-- 검색 E-->
		    <!-- 게시판 S-->
		    <div class="az_board_basic" style="height: calc(100% - 90px);">
		    	<div data-ax5grid="grdProgList" style="height: 100%;"></div>
			</div>
		</div>	
		<!-- 게시판 E -->
		<!-- 하단 S-->
		<div class="half_wrap" style="height: 360px;">
			<!-- tab_기본정보 S-->
			<div class="tab_wrap">
				<ul class="tabs">
					<li rel="tabProgBase" id="tab1" class="on" style="vertical-align: middle;">기본정보</li>
					<li rel="tabProgHistory" id="tab2" style="vertical-align: middle;">변경내역</li>
				</ul>
			</div>
			<!-- tab E-->			
			<div> <!--  tab_container -->
		       	<!-- 프로그램기본정보 -->
		       	<div id="tabProgBase" class="tab_content dib" style="width:100%">
		       		<iframe id="frmProgBase" name="frmProgBase" src='/webPage/tab/programinfo/ProgBaseTab.jsp' width='100%' height='100%' frameborder="0" scrolling="no" ></iframe>
		       	</div>
		       	<!-- 프로그램기본정보  END -->
		       	
		       	<!-- 변경내역 START -->
		       	<div id="tabProgHistory" class="tab_content dib" style="width:100%;">
		       		<iframe id="frmProgHistory" name="frmProgHistory" src='/webPage/tab/programinfo/ProgHistoryTab.jsp' width='100%' height='100%' frameborder="0" scrolling="no"></iframe>
		       	</div>
		       	<!-- 변경내역 END -->
		   	</div>
		</div>	
    </div>
</div>
<!-- contener E -->
</body>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/program/ProgramInfo.js"/>"></script>