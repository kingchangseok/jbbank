<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<!-- contener S -->
<body style="min-width: auto;">
	<div style="height:100%;overflow-y: hidden;">
	<!-- 하단 입력 S-->
	<div> <!-- class="margin-15-left" -->
		<div class="margin-5-top">
		    <!-- 시스템 -->
	        <div class="width-25 dib vat">
	            <label id="lbSysMsg" class="tit_80 poa">시스템</label>
	            <div class="ml_90">
			    	<input id="txtSysMsg" name="txtSysMsg" type="text" class="width-100" readonly>
				</div>
			</div>	
		    <!-- 프로그램 종류 -->
	        <div class="width-25 dib vat">
	            <label id="lbRsrcCd" class="tit_90 poa">&nbsp;&nbsp;&nbsp;프로그램구분</label>
	            <div class="ml_90" id="divTxtRsrcCd">
	            	<input id="txtRsrcCd" name="txtRsrcCd" type="text" style="width:calc(100%);" readonly>
				</div>
				<div class="ml_90" id="divCboRsrcCd">
					<div id="cboRsrcCd" data-ax5select="cboRsrcCd" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true, selSize:9}" style="width:calc(100%);" class="dib"></div>
				</div>
			</div>	
	        <div class="width-25 dib vat">
	 	        <label id="lbLanguage" class="tit_90 poa">&nbsp;&nbsp;&nbsp;언어</label>
	            <div class="ml_90" id="divPrgTxt">
					<input id="txtLanguage" name="txtLanguage" type="text" style="width:calc(100%);" readonly>
				</div>
			</div>	
		    <!-- 업무  -->
	        <div class="dib vat" style="width: calc(25% - 12px);">
	            <label id="lbJob" class="tit_80 poa">&nbsp;&nbsp;&nbsp;업무</label>
	            <div class="ml_90" id="divJobCbo">
					<div id="cboJob" data-ax5select="cboJob" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true, selSize:9}" style="width:calc(100%);" class="dib"></div>
				</div>
			</div>				
		</div>
		<div class="margin-5-top">
	        <div class="width-25 dib vat">
	            <label id="lbProgId" class="tit_80 poa">프로그램명</label>
	            <div class="ml_90">
					<input id="txtProgId" name="txtProgId" type="text" class="width-100" readonly>
				</div>
			</div>	
	        <div class="width-25 dib vat">
	            <label id="lbCompile" class="tit_80 poa fontStyle-cncl">&nbsp;&nbsp;&nbsp;컴파일모드</label>
	            <div class="ml_90">
					<div id="cboCompile" data-ax5select="cboCompile" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true, selSize:9}" style="width:calc(100%);" class="dib"></div>
				</div>
			</div>
	        <div class="width-25 dib vat">
	            <label id="lbMake" class="tit_80 poa fontStyle-cncl">&nbsp;&nbsp;&nbsp;MAKE모드</label>
	            <div class="ml_90">
					<div id="cboMake" data-ax5select="cboMake" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true, selSize:9}" style="width:calc(100%);" class="dib"></div>
				</div>
			</div>
		</div>
		<div class="margin-5-top">
			<div class="dib vat" style="width: 75.4%;">
		        <label id="lbStory" class="tit_80 poa">프로그램설명</label>
		        <div class="ml_90">
					<input id="txtStory" name="txtStory" type="text" style="width:calc(100%);" readonly>
				</div>
			</div>
			<div class="dib vat" style="width: calc(25% - 12px)"> <!-- calc(34% - 8px);  -->
	            <label id="lbTeam" class="tit_80 poa">&nbsp;&nbsp;&nbsp;관리팀</label>
	            <div class="ml_90" id="divJobCbo">
					<div id="cboTeam" data-ax5select="cboTeam" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true, selSize:9}" style="width:calc(100%);" class="dib"></div>
				</div>
			</div>
		</div>			
		<div class="margin-5-top">
			<div class="dib vat" style="width: 75.4%;">
		        <label id="lbPath" class="tit_80 poa">프로그램경로</label>
		        <div class="ml_90">
					<input id="txtPath" name="txtPath" type="text" style="width:calc(100%);" readonly>
				</div>
			</div>
			<div class="dib vat" style="width: calc(25% - 12px)"> <!-- calc(34% - 8px);  -->
	            <label id="lbCaller" class="tit_80 poa">&nbsp;&nbsp;&nbsp;관리담당자</label>
	            <div class="ml_90">
					<dd id="txtCallerToolTip"><input id="txtCaller" name="txtCaller" type="text" style="width:calc(100%);" readonly></dd>
				</div>
			</div>
		</div>			
		<div class="l_wrap width-100 dib vat margin-5-top">
			<div class="row">
			    <!-- 신규등록인 -->
		        <div class="width-25 dib vat">
		             <label id="lbCreator" class="tit_80 poa">신규등록인</label>
		             <div class="ml_90">
						<input id="txtCreator" name="txtCreator" type="text" class="width-100" readonly>
					</div>	
			    </div>		
		        <!-- 최종변경인 -->  
		        <div class="width-25 dib vat">
		            <label id="lbEditor" class="tit_90 poa">&nbsp;&nbsp;&nbsp;최종변경인</label>
		            <div class="ml_90">
			        	<input id="txtEditor" name="txtEditor" type="text" class="dib" style="width:100%;" readonly>
		        	</div>
			    </div>
			    <div class="width-25 dib vat">
		        	<div id="cboEditor" data-ax5select="cboEditor" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true, selSize:4}" class="dib" style="width:100%;"></div>
			    </div>
		        <div class="dib vat" style="width: calc(25% - 12px)">
		             <label id="lbLastCkIn" class="tit_80 poa">&nbsp;&nbsp;&nbsp;최종체크인</label>
		             <div class="ml_90">
		             	<input id="txtLstUsr" name="txtLstUsr" type="text" class="dib" style="width:calc(100%);" readonly>
		             </div>
		        </div>
			</div>
			
			<div class="row">
			    <!-- 신규등록일 -->
		        <div class="width-25 dib vat">
		             <label id="lbCreatDt" class="tit_80 poa">신규등록일</label>
		             <div class="ml_90">
				         <input id="txtCreatDt" name="txtCreatDt" type="text" class="width-100" readonly>
			         </div>
		        </div>
			    <!-- 최종변경일 -->
		        <div class="width-25 dib vat">
		            <label id="lbEditor" class="tit_90 poa">&nbsp;&nbsp;&nbsp;최종변경일</label>
		            <div class="ml_90">
			        	<input id="txtLastDt" name="txtLastDt" type="text" class="dib" style="width:100%;" readonly>
		        	</div>
			    </div>
			    <div class="width-25 dib vat">
					<label id="lbLstDat" class="tit_90 poa">&nbsp;&nbsp;&nbsp;최종체크일</label>
					<div class="ml_90">
						<input id="txtLstDat" name="txtLstDat" type="text" class="dib" style="width: 100%;" readonly>
					</div>
				</div>
				<div class="dib vat" style="width: calc(25% - 12px)">
		             <label id="lbSta" class="tit_80 poa">&nbsp;&nbsp;&nbsp;진행상태</label>
		             <div class="ml_90">
				     	<input id="txtSta" name="txtSta" type="text" class="dib" style="width:calc(100%);" readonly>
				     </div>
			    </div>
			</div>	
		</div>
		<div class="row">
			<div class="width-55 dib vat margin-5-top">
	            <label class="tit_80 poa">적용서버경로</label>
	            <div class="ml_90" id="divTxtOrderId">
					<input id="txtOrderId" name="txtOrderId" type="text" class="width-100" readonly>
				</div>
				<div class="ml_90" id="divCboOrderId">
	            	<div id="cboOrderId" data-ax5select="cboOrderId" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true, selSize:4}" class="dib" style="width:calc(100%); display: none;"></div>
				</div>
			</div>
			<div class="dib vat margin-5-top">
				<button id="btnVersion" name="btnVersion" class="btn_basic_s" >버전동기화</button>
			</div>
		</div>
		
		<div class="row">
			<div class="width-33 dib vat">
				<label class="tit_80 poa">적용서버HOME</label>
	            <div class="ml_90">
					<input id="txtOrderId0" name="txtOrderId0" type="text" class="width-100" readonly>
				</div>
			</div>
			<div class="width-20 dib vat">
				<label class="tit-150 dib poa">&nbsp;&nbsp;&nbsp;DOCUMENT</label>
		    	<div class="ml_100" style="border: 1px solid #ddd; width: 115px; padding-left: 5px;">
					<input id="rdoYes" type="radio" name="rdoGbn" value="0"/> 
					<label for="rdoYes">YES</label> 
					<input id="rdoNo" type="radio" name="rdoGbn" value="1"/> 
					<label for="rdoNo">NO</label>
		        </div>
		    </div>
		    <div class="width-30 dib vat">
				<label class="tit-150 dib poa">&nbsp;&nbsp;&nbsp;RPA대상</label>
		    	<div class="ml_100 dib" style="border: 1px solid #ddd; width: 115px; padding-left: 5px;">
					<input id="rdoRpaYes" type="radio" name="rdoGbn2" value="0"/> 
					<label for="rdoRpaYes">YES</label> 
					<input id="rdoRpaNo" type="radio" name="rdoGbn2" value="1"/> 
					<label for="rdoRpaNo">NO</label>
		        </div>
		        <div class="dib vat">
		        	<button id="btnRPA" name="btnRPA" class="btn_basic_s" >RPA변경</button>
		        </div>
		    </div>
			<div class="tar" style="float:right">
				<button id="btnProject" name="btnProject" class="btn_basic_s" style="display: none;">산출물</button>
				<button id="btnDiff" name="btnDiff" class="btn_basic_s">소스비교</button>
				<button id="btnView" name="btnView" class="btn_basic_s">소스보기</button>
				<button id="btnRegist" name="btnRegist" class="btn_basic_s">수정</button>
				<button id="btnTblDel" name="btnTblDel" class="btn_basic_s" value="delete">폐기</button>
			</div>
		</div>
	</div>
	<!--페이지버튼 E-->
	<!-- 하단 입력 E-->	
   <!-- contener E -->
	</div>
</body>

<form name="popPam">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
	<input type="hidden" name="itemid"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/tab/programinfo/ProgBaseTab.js"/>"></script>