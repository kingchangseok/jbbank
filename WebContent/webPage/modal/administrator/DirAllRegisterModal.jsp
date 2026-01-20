<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp"/>
	
<body>
	<div class="pop-header">
		<div>
			<label>자동신규</label>
		</div>
	</div>
	<div>
		<button type="button" class="close" aria-label="닫기" onclick="popClose()">
		  <span aria-hidden="true">&times;</span>
		</button>
	</div>
	
	<div class="container-fluid pop_wrap">
		<div class="az_search_wrap">
			<div class="az_in_wrap">
				<div class="cb">
					<div class="width-50 float-left">
						<div class="margin-5-right">
							<label class="tit_70 poa">시스템</label>
	                        <div class="ml_80">
								<input id="txtSys" type="text" class="width-100" readonly="readonly"></input>
							</div>
						</div>
					</div>
                    <div class="width-50 float-left">
						<div class="margin-5-left">
							<label class="tit_70 poa">대상서버선택</label>
	                        <div class="ml_80 tal">
								<div id="cboSvr" data-ax5select="cboSvr" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100"></div>
							</div>
						</div>
					</div>
				</div>
				<div class="row vat cb">
					<div class="width-100 float-left">
                    	<label class="tit_70 poa">기준디렉토리</label>
                    	<div class="vat ml_80">
							<input id="txtDirBase" style="width:calc(100% - 50px);" type="text" placeholder="기준디렉토리를 입력하여 조회하세요."/>
							<button class="btn_basic_s margin-5-left poa" id="btnQry">조회</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<div class="row">
			<div class="l_wrap width-40 padding-5-right">
				<div style="overflow-y: auto; height:78%; background-color: white; border: 1px solid #ddd;">
					<ul id="treePath" class="ztree"></ul>				
				</div>
			</div>
			<div class="r_wrap width-60">
				<!-- 게시판 S-->
			    <div class="az_board_basic az_board_basic_in" style="height:72%">
			    	<div data-ax5grid="firstGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" id="fileGrid" style="height:100%" class="resize"></div>
				</div>	
				<div class="row">
					<div class="dib r_wrap margin-5-top">
						<button id="btnReg" class="btn_basic_s">등록</button>
						<button id="btnExit" class="btn_basic_s">닫기</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>

<div id="rMenu"> 
 	<ul> 
 		<li id="contextmenu1" onclick="contextmenu_click('1');">추가</li> 
 		<li id="contextmenu2" onclick="contextmenu_click('2');">추가(하위폴더포함)</li>
 	</ul> 
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/administrator/DirAllRegisterModal.js"/>"></script>