<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<div class="contentFrame">
   <div id="history_wrap">기본관리 <strong>&gt; 쿼리조회</strong></div>
   <div class="az_search_wrap" style="height: 46px;">
      	<div class="az_in_wrap">
         	<div class="width-98 dib">
            	<div class="dib vat width-30">
	                <label id="lbSystem" class="" style="width:40px;">구분</label>
					<div id="cboGbnCd" data-ax5select="cboGbnCd" data-ax5select-config="{size:'sm',theme:'primary'}" class="dib" style="width:calc(100% - 193px);"></div>
					<label id="lbtit" class="tit_150 poa margin-10-left margin-5-right">[ 조회 할 쿼리문 입력 ]</label>
            	</div>
            	
            	<div class="dib vat" id="rdo2">
					<input id="rdoap"  type="radio" name="cmdRadioUsr" value="1"/>
			        <label id="lblap" for="rdoap">AP계정</label>
		        	<input id="rdoweb" type="radio" name="cmdRadioUsr" value="2"/>
					<label id="lblweb" for="rdoweb">WEB계정</label>
				</div>
				<div id="chkViewDiv" class="dib vat">
					<input id="chkView" tabindex="8" class="checkbox-pie" type="checkbox" value="chkView" style="margin-top: 5px;" name="chkView" checked="checked"/>
					<label for="chkView"  id="txtChkView" style="margin-top: -5px;">수신파일 직접보기</label>
				</div>
				<div class="dib" id="cboCharacterBox" style="display: none;">
					<label>Character Set</label><div class="dib"  id="cboCharacter" data-ax5select="cboCharacter" data-ax5select-config="{size:'sm',theme:'primary'}" style="width: 250px;"> </div>
				</div>
				<div class="dib vat width-30">
					<div class="vat dib">
						<label class="dib" id = "DBINFO" >DB계정</label>
					</div>
					<div class="width-30 vat dib">
						<div id="cboDbUsrSel" data-ax5select="cboDbUsrSel" data-ax5select-config="{size:'sm',theme:'primary'}" style="width: 100%"></div> 
					</div>
				</div>		
				<button id="btnQry" class="btn_basic_s float-right" style="cursor: pointer;">커맨드실행</button>
				<button id="btnExcel" class="btn_basic_s margin-20-left margin-5-right float-right" style="cursor: pointer;">엑셀저장</button>
			</div>
		</div>
	</div>
   
	<div class="row">
      <form>
         <textarea id="txtcmd" name="txtcmd" style="align-content:left;width:100%;height:150px;resize: none;padding: 5px 5px; box-sizing: border-box; border: 1px solid #ddd; background-color: #fff; font-size: 12px;"></textarea>
      </form>
	</div>
   
	<div class="az_board_basic margin-5-top" style="height:calc(100% - 250px);">
		<div data-ax5grid="cmdGrid" data-ax5grid-config="{showLineNumber : true, lineNumberColumnWidth: 40}" style="height:100%;" class="resize"></div>
	</div>
	<div class="row">
		<form>
			<textarea id="txtrst" name="txtrst" style="align-content:left;width:100%;height:calc(100% - 250px);resize: none;padding: 5px 5px; box-sizing: border-box; border: 1px solid #ddd; background-color: #fff; font-size: 12px;" readonly></textarea>
      	</form>
	</div>	
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/administrator/CommandExcute.js"/>"></script>
