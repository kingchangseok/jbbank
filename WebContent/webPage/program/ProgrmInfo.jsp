<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp"/>

<!-- contener S -->
<div id="wrapper">
    <div class="content">=    
	    <!-- 검색 S-->    
		<div class="az_search_wrap">
			<div class="az_in_wrap por">
				<!-- 시스템 -->		
                <div class="width-33 dib vat">
                    <label id="lbSystem" class="tit_80 poa">시스템</label>
                    <div class="ml_80 vat">
						<div id="cboSysCd" data-ax5select="cboSysCd" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-80 dib" style="width:90%;"></div>
					</div>
				</div>
			    <!-- 프로그램종류 -->
                <div class="width-33 dib vat">
                    <label id="lbJawon" class="tit_80 poa">프로그램종류</label>
                    <div class="ml_80">
						<div id="cboJawon" data-ax5select="cboJawon" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:90%;" class="width-80 dib"></div>
					</div>
				</div>
			    <!-- 프로그램명 -->
                <div class="width-33 dib vat">
                    <label id="lbRsrcName" class="tit_80 poa">프로그램명</label>
                    <div class="ml_80">
						<input id="txtRsrcName" name="txtRsrcName" type="text" class="width-95 dib" style="width:calc(100% - 38px);" >
					</div>
					<button id="btnQry" name="btnQry" class="btn_basic_s margin-5-left margin-10-right poa_r" data-grid-control="excel-export">조회</button>
				</div>				
				<div class="margin-5-top">
				    <!-- 프로그램경로 -->
                    <div class="vat">
                        <label id="lbDirPath" class="tit_80 poa">프로그램경로</label>
						<div class="ml_80">
							<input id="txtDirPath" name="txtDirPath" type="text" class="width-100">
						</div>
					</div>					
				</div>
			</div>
		</div>
	    <!-- 검색 E-->
	    <!-- 게시판 S-->
	    <div class="az_board_basic" style="height:45%;">
	    	<div data-ax5grid="grdProgList" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
		</div>	
		<!-- 게시판 E -->
		<!-- 하단 S-->
		<div class="half_wrap margin-10-top">
			<!-- tab_기본정보 S-->
			<div class="tab_wrap">
				<ul class="tabUl">
					<li rel="tab1" id="tab1Li" class="on">기본정보</li>
					<li rel="tab2" id="tab2Li">변경내역</li>
				</ul>
			</div>
			<!-- tab E-->			
			<div style="height:30%"> <!--  tab_container -->
		       	<!-- 프로그램기본정보 -->
		       	<div id="tab1" class="tab_content" style="width:100%">
		       		<iframe id="frmProgBase" name="frmProgBase" src='/webPage/tab/programinfo/ProgBaseTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
		       	</div>
		       	<!-- 프로그램기본정보  END -->
		       	
		       	<!-- 변경내역 START -->
		       	<div id="tab2" class="tab_content" style="width:100%">
		       		<iframe id="frmProgHistory" name="frmProgHistory" src='/webPage/tab/programinfo/ProgHistoryTab.jsp' width='100%' height='100%' frameborder="0"></iframe>
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