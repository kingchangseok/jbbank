<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page errorPage="error.jsp" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import="com.ecams.common.base.StringHelper"%>
<%
	String acptNo = StringHelper.evl(request.getParameter("acptno"),"");
	String userId = StringHelper.evl(request.getParameter("user"),"");
	String itemId = StringHelper.evl(request.getParameter("itemid"),"");
    String codeList = request.getParameter("codeList");
%>
<c:import url="/webPage/common/common.jsp" />

<!-- contener S -->
<div id="wrapper" style="overflow:auto;">
    <div class="content" style="margin: 10px;">
        <!-- line1 S-->   
        <div id="history_wrap">	요청상세 <strong>&gt; 소스비교</strong></div> 
        <!-- line2 s-->
		<div class="margin-5-top vat cb" style="margin-top: 0px;">		
			<div class="l_wrap az_search_wrap" style="width: 225px;height: 150;padding-top: 5px;">
				<div class="margin-5-left">
					<div>
	                	<label class="tit_60 poa txt_r">신청번호</label>
	                    <div class="ml_60" style="height: 28px;">
							<input class="width-100 txt_r" id="txtAcptno" type="text" style="text-align: center;width: 154;" readonly>
						</div>
					</div>
					<div>
	                	<label class="tit_60 poa txt_r">삭제라인</label>
	                    <div class="ml_60">
							<input class="width-100 txt_r" id="txtDelLine" type="text" style="text-align: center;width: 80;" readonly>
							<div class="dib vat">
								 <button id="btnVerDiff" class="btn_basic_s">소스비교</button>
							</div>
						</div>
					</div>
					<div class="margin-3-top">
	                	<label class="tit_60 poa" style="color: #1d74b2;">추가라인</label>
	                    <div class="ml_60">
							<input class="width-100" id="txtAddLine" type="text" style="text-align: center;color: #1d74b2;width: 80;" readonly />
							<div class="dib vat">
								<button id="btnSrcDiff" class="btn_basic_s">변경부분</button>
		                	</div>
						</div>
					</div>
					<div class="margin-3-top">
	                	<label class="tit_60 poa" style="color: #b21db2;">변경(전)</label>
	                    <div class="ml_60">
							<input class="width-100" id="txtBefLine" type="text" style="text-align: center;color: #b21db2;width: 80;" readonly />
							<div class="dib vat">
							<button id="btnChgPart" class="btn_basic_s">변경위치</button>
	                	</div>
						</div>
					</div>
					<div class="margin-3-top">
	                	<label class="tit_60 poa" style="color: #b21db2;">변경(후)</label>
	                    <div class="ml_60">
							<input class="width-100" id="txtAftLine" type="text" style="text-align: center;color: #b21db2;width: 80;" readonly />
							<div class="dib vat">
								<button id="btnLeft" class="btn_basic_s l_wrap">◀</button>
								<button id="btnRight" class="btn_basic_s r_wrap magin" style="margin-left: 11;">▶</button>
	                		</div>
						</div>
					</div>
				</div>
			</div>		
			<!-- line2 e-->
            <div class="r_wrap" style="width: calc(100% - 230px)">
            	<div class="cb">
            		<!-- line 1-->
            		<div class="r_wrap" style="width:calc(100%)">
						<div class="margin-5-right">                	
							<!-- 게시판 S-->
						    <div class="az_board_basic" style="height:150px;">
						    	<div data-ax5grid="grdProgHistory" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
							</div>	
						</div>
					</div>
				</div>
				<div class="margin-5-top cb width-100">
					<!-- <div id="cboCharacter" data-ax5select="cboCharacter" data-ax5select-config="{size:'sm',theme:'primary'}" style="width:70px; display: none;" class="dib" ></div> -->
					<div class="dib" style="width:21px;"></div>
					<div class="dib vat">
						<input id="optWord"  type="radio" name="optradio" class="vam !important"  value="W" onchange="optradio_change();" checked/>
						<label for="optWord">단어검색</label>
						<input id="optLine" type="radio"  name="optradio"  value="L" onchange="optradio_change();"/>
						<label for="optLine">라인검색</label>
					</div>
					<input id="txtSearch" name="txtSearch" type="text" style="width:calc(100% - 402px);" onchange="txtSearch_change();" placeholder="검색할 단어를 입력하세요.">
					<button id="btnSearch" name="btnSearch"  class="btn_basic_s margin-5-left" >찾기</button>
					<button id="btnExcel" class="btn_basic_s margin-5-left">엑셀저장</button>
				</div>	
			</div>	
		</div>	
		<div class="width-100 margin-10-top" style="height: 55%;" id="girdDiv">
		    <div class="az_board_basic" style="height:100%";>
		    	<div data-ax5grid="grdDiffSrc" data-ax5grid-config="{showLineNumber: true}" style="height: 100%;"></div>
			</div>
		</div>	
		<div class="row" style="font-size:0px;">
			<div class="dib width-100">
	        	<label class="tit_60 poa">변경(전)</label>
	            <div class="ml_60">
					<textarea id="txtBefSrc" class="width-100" readonly style="padding: 5px 5px; box-sizing: border-box; border: 1px solid #ddd; background-color: #fff; font-size: 12px; resize: none; height: 40px;"></textarea>
				</div>
			</div>
			<div class="dib width-100 margin-5-top">
	        	<label class="tit_60 poa">변경(후)</label>
	            <div class="ml_60">
					<textarea id="txtAftSrc" class="width-100" readonly style="padding: 5px 5px; box-sizing: border-box; border: 1px solid #ddd; background-color: #fff; font-size: 12px; resize: none; height: 40px;"></textarea>
				</div>
			</div>	
		</div>
		<!-- line4 e-->
		
	</div>
</div>

<form name="getSrcData">
	<input type="hidden" name="acptno" value="<%=acptNo%>"/>
	<input type="hidden" name="user" value="<%=userId%>"/>
	<input type="hidden" name="itemid" value="<%=itemId%>"/>
	<input type="hidden" name="codeList" value="<%=codeList%>"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript"	src="<c:url value="/js/ecams/winpop/PopSourceDiffView.js"/>"></script>