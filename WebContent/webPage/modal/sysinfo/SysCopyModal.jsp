<!--  
	* 화면명: 시스템정보복사
	* 화면호출: 시스템정보 -> 시스템정보복사 클릭
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />


<div class="pop-header">
	<div>
		<label id="lbSub">시스템정보복사</label>
	</div>
	<div>
		<button type="button" class="close" aria-label="닫기" onclick="popClose()">
		  <span aria-hidden="true">&times;</span>
		</button>
	</div> 
</div>
<div class="container-fluid pop_wrap">
	<!--line1-->
	<div>
		<label class="tit_80 poa">시스템 [From]</label>
        <div class="ml_80">
			<div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" style="width:100%;"></div>
            <span class="dib vat margin-5-top margin-5-left">시스템속성은 선택한 내용 그대로 복사됩니다.</span>
		</div>
	</div>
	<!--line2-->					
	<div class="row half_wrap_cb">
		<!--left wrap-->
		<div class="l_wrap width-70">
			<div class="l_wrap width-50">
				<div class="margin-5-right">
					<div class="por">
						<label class="title">시스템 [To]</label>
						<div class="poa_r">
							<input type="checkbox" class="checkbox-all " id="chkAllToSys" data-label="전체선택"  />
						</div>
					</div>
					<div class="scrollBind row" style="height: 195px;">
		   				<ul class="list-group" id="ulToSys"></ul>
		   			</div>
				</div>
			</div>
			<div class="r_wrap width-50">
				<div class="margin-5-left">
					<div class="por">
						<label class="title">시스템속성</label>
						<div class="poa_r">
							<input type="checkbox" class="checkbox-all" id="chkCopy" data-label="복사" checked="checked" />
							<input type="checkbox" class="checkbox-all" id="chkAllProp" data-label="전체선택"  />
						</div>
					</div>
					<div class="scrollBind row" style="height: 195px;">
		   				<ul class="list-group" id="ulProp"></ul>
		   			</div>
		   		</div>
			</div>
	 	</div>
	 	<!--right wrap-->
	 	<div class="r_wrap width-30">
	 		<div class="margin-10-left">
				<div class="por">
					<label id="lblSvrUsr" class="title">프로그램종류</label>
					<div class="poa_r">
						<input type="checkbox" class="checkbox-all" id="chkAllPrg" data-label="전체선택"  />
					</div>
				</div>
				<div class="scrollBind row" style="height: 195px;">
	   				<ul class="list-group" id="ulPrg"></ul>
	   			</div>
			</div>
		</div>
	</div>
	<!--line3-->
	<div class="row half_wrap_cb">
		<!--left-->
		<div class="l_wrap width-50">
			<div class="margin-5-right">
				<div class="width-100 dib vat">
                    <label class="title">시스템상세정보</label>
				</div>				
				<div class="row">
					<div class="az_board_basic az_board_basic_in" style="height: 50%">
				    	<div data-ax5grid="detailGrid" data-ax5grid-config="{showLineNumber: false , lineNumberColumnWidth: 40}" style="height: 100%;"></div>
					</div>
				</div>
			</div>
		</div>
		<!--right-->
		<div class="r_wrap width-50">
			<div class="margin-5-right">
				<div class="width-100 dib vat">
                    <label class="title">공통디렉토리</label>
				</div>				
				<div class="row">
					<div class="az_board_basic az_board_basic_in" style="height: 50%">
				    	<div data-ax5grid="dirGrid" data-ax5grid-config="{showLineNumber: false, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
					</div>
				</div>
			</div>
		</div>	
	</div>
	<!--line4-->	
	<div class="row">
		<input type="checkbox" class="checkbox-all" id="chkItem" data-label="선택한 서버에 대한 형상항목 연결정보도 복사합니다."  checked="checked"/>
		<!-- <input type="checkbox" class="checkbox-all" id="chkMonitor" data-label="선택한 시스템에 대한 모니터링 체크리스트도 복사합니다." checked="checked"/> -->
		
		<div class="dib float-right">
			<button id="btnReq" class="btn_basic_s">복사</button>
			<button id="btnExit" class="btn_basic_s margin-5-right">닫기</button>
		</div>
	</div>
</div>

<c:import url="/js/ecams/common/commonscript.jsp" />	
<script type="text/javascript" src="<c:url value="/js/ecams/modal/sysinfo/SysCopyModal.js"/>"></script>