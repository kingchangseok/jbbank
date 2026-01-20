<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="error.jsp" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="com.ecams.common.base.StringHelper"%>
<%
	String acptNo = StringHelper.evl(request.getParameter("acptno"),"");
	String userId = StringHelper.evl(request.getParameter("user"),"");
	String itemId = StringHelper.evl(request.getParameter("itemid"),"");
%>
<c:import url="/webPage/common/common.jsp" />

<!-- contener S -->
<body id="wrapper" style="padding: 10px;">
    <div class="content">
    	<div id="history_wrap">프로그램<strong>&gt; 소스비교</strong></div>
        <!-- line1 S-->    
		<div style="width:100%; display: inline-block; padding: 3px 0;">
			<div class="az_search_wrap" style="width:57%; margin-right:5px;vertical-align: top;padding-bottom: 16px">
				<div class="az_in_wrap por" style="width:100%; padding: 0 0 0 10">
					<!-- 시스템 -->		
	                <div class="width-50 dib vat">
	                    <label id="lbSystem" style="width:50px;" class=" poa">시스템</label>
	                    <div style="margin-left: 50px;">
							<input id="txtSysMsg" name="txtSysMsg" type="text" class="width-100" readonly >
						</div>
					</div>
				    <!-- 프로그램명 -->
	                <div class="width-50 dib vat">
	                    <label id="lbRsrcName" style="width:70px;margin-left:10px;" class="poa">프로그램명</label>
	                    <div style="margin-left: 70px;">
							<input id="txtProgId" name="txtProgId" type="text" style="width:calc(100% - 8px)" readonly>
						</div>
					</div>					
				</div>
				<div class="dib vat" style="width:calc(100% - 8px);">
				    <!-- 프로그램경로 -->
	                   <div class="vat">
	                       <label id="lbDirPath" class="poa" style="margin-left: 10px;">경로</label>
						 <div style="margin-left: 60px;margin-top: 2px;">
							<input id="txtDir" name="txtDir" type="text" style="width:100%;" readonly>
						</div>
					</div>					
				</div>
				<div class="margin-5-right width-100" style="padding: 0 8 0 8">                	
					<!-- 게시판 S-->
				    <div style="height:150px;width:100%; margin-top: 2px;">
				    	<div data-ax5grid="grdProgHistory_Acptno" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
					</div>	
				</div>
			</div>
			<div class="az_search_wrap" style="width:42%;vertical-align: top;">
				<!-- 게시판 S-->
			    <div class="az_board_basic" style="height:151px; padding: 0 8 0 8">
			    	<div data-ax5grid="grdProgHistory" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
				</div>	
				<div class="width-100 tar margin-5-top" style="padding: 0 8 0 0">
					<input id="optWord" type="radio" name="optradio"  value="W" onchange="optradio_change();"/>
					<label for="optWord">단어검색</label>
					<input id="optLine" type="radio"  name="optradio"  value="L" onchange="optradio_change();" checked/>
					<label for="optLine">라인검색</label>
					<input id="txtSearch" name="txtSearch" type="text" style="width:200px;" onchange="txtSearch_change();" >
					<button id="btnSearch" name="btnSearch"  class="btn_basic_s margin-5-left">찾기</button>
					
					<div class="row">
						<button id="btnExcel" class="btn_basic_s margin-5-left">엑셀저장</button>
						<button id="btnVerDiff" class="btn_basic_s margin-5-left" tooltip="선택한 두 버전 비교">버전비교</button>
						<button id="btnSrcDiff" class="btn_basic_s margin-5-left" tooltip="전체내용 표시">전체비교</button>
						<button id="btnChgPart" class="btn_basic_s margin-5-left" tooltip="변경내용만 표시">변경부분</button>
						<!-- <button id="btnChgPart" class="btn_basic_s margin-5-left">변경위치</button> -->
						<button id="btnExit" class="btn_basic_s margin-5-left">닫기</button>
					</div>
				</div>
			</div>
		</div>
		
		<!--line1 E-->
		<div class="vat cb" style="margin-top:-8px;">
			<div>
               	<label class="tit_60 poa fontStyle-cncl">삭제라인</label>
                <div class="ml_60">
					<input class="width-8 fontStyle-cncl" id="txtDelLine" type="text" style="text-align: center;" readonly/>
					<label class="margin-3-left tit_60 poa fontStyle-ing tac">추가라인</label>
					<input class="width-8 fontStyle-ing ml_60" id="txtAddLine" type="text" style="text-align: center;" readonly/>
	               	<label class="margin-3-left tit_60 poa fontStyle-error tac">변경(전)</label>
					<input class="width-8 ml_60 fontStyle-error" id="txtBefLine" type="text" style="text-align: center;" readonly/>
	               	<label class="margin-3-left tit_60 poa fontStyle-error tac">변경(후)</label>
					<input class="width-8 ml_60 fontStyle-error" id="txtAftLine" type="text" style="text-align: center;" readonly/>
				</div>
			</div>
		</div>	
		<div class="margin-5-top width-100" style="height:calc(100% - 360px);">
			<!-- 게시판 S-->
		    <div class="az_board_basic" style="height:100%">
		    	<div data-ax5grid="grdDiffSrc" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
			</div>	
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
	<input type="hidden" name="itemid" value="<%=itemId%>"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript"	src="<c:url value="/js/ecams/winpop/PopSourceDiffInf.js"/>"></script>