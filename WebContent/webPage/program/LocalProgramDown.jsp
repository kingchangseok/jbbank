<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<div class="contentFrame">
	<div id="history_wrap">프로그램<strong>&gt; 로컬파일일괄다운</strong></div>
      
	<div class="az_search_wrap">
		<div class="az_in_wrap">
			<div class="l_wrap width-70" style="">
				<div class="vat dib width-50">
					<label class="poa tit_80">시스템</label>
					<div class="ml_80 margin-20-right" id="cboDiv" style="">
						<div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100"></div> 
					</div>
				</div>
				
				<div class="vat dib width-50">
					<label class="poa tit_80">프로그램명</label>
					<div class="ml_80 margin-20-right" id="cboDiv" style="">
						 <input id="txtPrg" type="text" class="width-100">
					</div>
				</div>
				
			</div>
			<div class="r_wrap width-30 tar">
				<div class="dib vat margin-10-right" id="">
					<input type="checkbox" class="checkbox-pie" id="chkStrdt" data-label="변경기준일"></input>
				</div>
				<div id="divPicker" class="az_input_group dib margin-10-right" data-ax5picker="basic">
		            <input id="dateSt" name="start_date" type="text" autocomplete="off" style="width:100px;">
					<span class="btn_calendar"><i class="fa fa-calendar-o"></i></span>
				</div>
				<div class="float-right vat dib">
					<button id="btnSearch" name="btnSearch" class="btn_basic_s" style="cursor: pointer;" >검색</button>
				</div>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="l_wrap width-20 padding-5-right" style="">
			<div style="overflow-y: auto; height:100%; background-color: white; border: 1px solid #ddd;">
				<ul id="treeDemo" class="ztree"></ul>				
			</div>
			<div class="dib vat margin-5-top"><input type="checkbox" class='checkbox-pie' name = 'chkbox_subnode' id ='chkbox_subnode' data-label="하위폴더 포함하여 조회" checked> </div>
		</div>
		<div class="r_wrap width-80">
			<!-- 게시판 S-->
		    <div class="az_board_basic az_board_basic_in" style="height:90%">
		    	<div data-ax5grid="first-grid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" id="fileGrid" style="height:100%" class="resize"></div>
			</div>	
			<div class="row">
				<div class="dib">
					<label class="" id="idx_lbl_path"></label>
				</div>
				<div class="dib r_wrap margin-5-top">
					<label class="dib" style="color: red;">** [기본관리>사용자환경설정]에 설정한 경로로 다운로드 합니다.</label>
					<button id="btnDown" name="btnDown" class="btn_basic_s" >다운로드</button>
				</div>
			</div>
		</div>
	</div>
<!-- 	<div class="az_board_basic margin-10-top" style="height: 88%;"> -->
<!-- 		<div data-ax5grid="fileGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%" class="resize"></div> -->
<!-- 	</div> -->
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/program/LocalProgramDown.js"/>"></script>
	