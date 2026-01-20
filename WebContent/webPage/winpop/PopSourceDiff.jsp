<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="error.jsp" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="com.ecams.common.base.StringHelper"%>
<%
	String acptNo = StringHelper.evl(request.getParameter("acptno"),"");
	String userId = StringHelper.evl(request.getParameter("user"),"");
	String itemid = StringHelper.evl(request.getParameter("itemid"),"");
    String codeList = request.getParameter("codeList");
%>
<c:import url="/webPage/common/common.jsp" />

<!-- contener S -->
<body id="wrapper" style="padding: 10px;">
   <div class="content">
        <!-- history S-->
        <div id="history_wrap">프로그램<strong>&gt; 소스비교</strong></div>
        <!-- history E-->
        <!-- line1 S-->    
		<div class="az_search_wrap">
			<div class="az_in_wrap por">
				<!-- 시스템 -->
                <div class="width-30 dib vat">
                    <label id="lbSystem" class="tit_80 poa">시스템</label>
                    <div class="ml_80">
						<input id="txtSysMsg" name="txtSysMsg" type="text" class="width-95 dib" readonly >
					</div>
				</div>
			    <!-- 프로그램명 -->
                <div class="width-70 dib vat">
                    <label id="lbRsrcName" style="width:70px;" class="poa">프로그램명</label>
                    <div style="margin-left: 70px;">
						<input id="txtProgId" name="txtProgId" type="text" class="width-100 dib" readonly>
					</div>
				</div>
			  	<div class="width-100 dib vat row">
				    <!-- 프로그램경로 -->
                    <div class="vat">
                        <label id="lbDirPath" class="tit_80 poa">프로그램경로</label>
						<div class="ml_80">
							<input id="txtDir" name="txtDir" type="text" class="width-100" readonly>
						</div>
					</div>
			  	</div>
			</div>
		</div>
		<!--line1 E-->
		<!-- line2 S-->	
		<div class="margin-5-top vat cb" style="margin-top: 0px;">
            <div class="l_wrap" style="width:calc(100% - 150px);">
            	<div class="cb">
            		<!-- line 1-->
            		<div class="l_wrap" style="width:calc(100% - 105px);)">
						<div class="margin-5-right">
							<!-- 게시판 S-->
						    <div class="az_board_basic" style="height:150px;">
						    	<div data-ax5grid="grdProgHistory" style="height: 100%;"></div>
							</div>	
						</div>
					</div>
					<div class="r_wrap">
						<div class="margin-10-right">
		                	<div>
								<button id="btnVerDiff" class="btn_basic_s" style="width:92px;" tooltip="선택한 두 버전 비교">버전비교</button>
		                	</div>
		                	<div class="margin-3-top">
								<button id="btnSrcDiff" class="btn_basic_s" style="width:92px;" tooltip="전체내용 표시">전체비교</button>
		                	</div>
		                	<div class="margin-3-top">
								<button id="btnChgPart" class="btn_basic_s" style="width:92px;" tooltip="변경내용만 표시">변경부분</button>
		                	</div>
		                	<div class="margin-3-top">
								<button id="btnChgLine" class="btn_basic_s" style="width:92px;" tooltip="변경라인으로 이동">변경라인</button>
		                	</div>
		                	<!-- <div class="margin-3-top">
		                		<button id="btnLeft" class="btn_basic_s">◀</button>
		                		<button id="btnRight" class="btn_basic_s">▶</button>
		                	</div> -->
						</div>
					</div>
				</div>
			</div>	
            <div class="r_wrap" style="width:150px">
				<div class="margin-5-left">
					<div>
	                	<label class="tit_60 poa fontStyle-cncl">삭제라인</label>
	                    <div class="ml_60">
							<input class="width-100 fontStyle-cncl" id="txtDelLine" type="text" style="text-align: center;" readonly/>
						</div>
					</div>
					<div class="margin-3-top">
	                	<label class="tit_60 poa fontStyle-ing">추가라인</label>
	                    <div class="ml_60">
							<input class="width-100 fontStyle-ing" id="txtAddLine" type="text" style="text-align: center;" readonly/>
						</div>
					</div>
					<div class="margin-3-top">
	                	<label class="tit_60 poa fontStyle-error">변경(전)</label>
	                    <div class="ml_60">
							<input class="width-100 fontStyle-error" id="txtBefLine" type="text" style="text-align: center;" readonly/>
						</div>
					</div>
					<div class="margin-3-top">
	                	<label class="tit_60 poa fontStyle-error">변경(후)</label>
	                    <div class="ml_60">
							<input class="width-100 fontStyle-error" id="txtAftLine" type="text" style="text-align: center;" readonly/>
						</div>
					</div>
				</div>
			</div>
		</div>	
		<!-- line2 e-->		
		<!-- line3 s-->
		<div class="margin-5-top cb width-100 tar">
			<!-- <div class="l_wrap width-50">
				<label id="lbCharacter" style="width:73px;" class="poa">Character Set</label>
				<div class="ml_80" id="divCharacter">
					<div id="cboCharacter" data-ax5select="cboCharacter" data-ax5select-config="{size:'sm',theme:'primary'}" style="width: 100px" class="dib" ></div>
				</div>
			</div> -->
			<!-- <div class="r_wrap width-50 tar"> -->
				<input id="optWord"  type="radio" name="optradio" value="W" onchange="optradio_change();"/>
				<label for="optWord">단어검색</label>
				<input id="optLine" type="radio"  name="optradio" value="L" onchange="optradio_change();" checked/>
				<label for="optLine" id="lblLine">라인검색</label>
				<input id="txtSearch" name="txtSearch" type="text" style="width:200px;" onchange="txtSearch_change();">
				
				<button id="btnSearch" name="btnSearch" class="btn_basic_s margin-5-left" >찾기</button>
				<button id="btnExcel" class="btn_basic_s margin-5-left">엑셀저장</button>
				<button id="btnExit" name="btnExit" class="btn_basic_s margin-5-left">닫기</button>
			<!-- </div> -->
		</div>	
		<!-- line3 e-->	
		<!-- line4 s-->
		<div class="margin-5-top width-100" style="height:calc(100% - 370px);">
			<!-- 게시판 S-->
		    <div class="az_board_basic margin-5-bottom" style="height:100%";>
		    	<div data-ax5grid="grdDiffSrc" style="height: 100%;"></div>
			</div>	
			<!-- 게시판 E -->
			<!-- line4 e-->
			<div class="row">
	        	<label class="tit_60 poa">변경(전)</label>
	            <div class="ml_60">
					<input id="txtBefSrc" class="width-100" type="text" readonly />
				</div>
			</div>
			<div class="margin-5-top">
	        	<label class="tit_60 poa">변경(후)</label>
	            <div class="ml_60">
					<input id="txtAftSrc" class="width-100" type="text" readonly />
				</div>
			</div>
		</div>	
	</div>
</body>

<form name="getSrcData">
	<input type="hidden" name="acptno" value="<%=acptNo%>"/>
	<input type="hidden" name="user" value="<%=userId%>"/>
	<input type="hidden" name="itemid" value="<%=itemid%>"/>
	<input type="hidden" name="codeList" value=<%=codeList%>>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript"	src="<c:url value="/js/ecams/winpop/PopSourceDiff.js"/>"></script>