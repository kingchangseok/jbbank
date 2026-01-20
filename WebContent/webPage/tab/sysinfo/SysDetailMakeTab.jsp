<!--  
	* 화면명: Make Script 등록
	* 화면호출: 시스템정보 -> Make Script 등록
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<body id="tabBody">
	<div class="half_wrap" style="border: 0px;">
		<div>	      
			<div class="az_search_wrap">
				<div class="az_in_wrap">
                    <div class="float-left width-20">
                        <div>
							<label class="tit_40 poa">코드</label>
		                    <div class="ml_40">
								<input id="txtCode" type="text" style="width:calc(100% - 15px)" readonly disabled></input>
							</div>
						</div>
					</div>
					<div class="float-left width-80">
                        <div class="dib vat" style="width: calc(100% - 200px)">
							<label class="tit_60 poa">Script명</label>
		                    <div class="ml_60">
								<input id="txtShell" type="text" style="width:calc(100% - 15px)"></input>
							</div>
						</div>
					
						<div class="dib tar" style="width: 200px">
							<input type="checkbox" class='checkbox-pie' id='chkNew' data-label="신규"></input>
							<button id="btnQry" class="btn_basic_s">조회</button>
							<button id="btnReq" class="btn_basic_s margin-5-left">등록</button>
							<button id="btnDel" class="btn_basic_s margin-5-left">폐기</button>
						</div>
					</div>
				</div>
			</div>
			
		</div>
	</div>
	<div class="row">
		<div class="az_board_basic az_board_basic_in" style="height: calc(100% - 90px);">
	    	<div data-ax5grid="scriptGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
		</div>
	</div>
</body>	

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/tab/sysinfo/SysDetailMakeTab.js"/>"></script>