<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />
<style>
.wCheck-label{
	margin-right : 0px !important;
}

.write_wrap dl {
	font-size: 0px;
}

.write_wrap dl dd input[type="radio"]{
	width: 15px;
	height: 15px;
}

#opt.disabled {
	background : #ebebe4;
}

#opt.disabled input[type="radio"] {
	cursor: not-allowed;
	border: 1px solid #ddd;
    border-radius: 0px;
    box-shadow: none;
    opacity: 0.5;
}
</style>

<div class="half_wrap" style="height:100%;">
	<!--하단 좌측-->
	<div class="l_wrap width-100 vat write_wrap"><!--ver2-->
		<div class="row">
			<dl>
				<dt><label>SR-ID</label></dt>
				<dd>
					<div style="width:10%;" class="dib vat">
						<input id="txtSRID" type="text" style="width: 100%;" disabled="disabled">
					</div>
					
					<div class="dib" style="width:50%;">
						<div id="opt" style="height: 25px; border: 1px solid #ddd; margin-left: 10px; width: 205px;" class="dib vat">
							<input id="opt1" name="opt"  type="radio" value="N" style="height: 100%;" checked="checked"/>
							<label for="opt1" style="line-height: 20px;">Main-SR 접수</label>
							<input id="opt2" name="opt" type="radio" value="T" style="height: 100%;"/>
							<label for="opt2" style="line-height: 20px;">Sub-SR 등록</label>
						</div>
						
					<!-- 요청자 -->
						<label class="dib" style="margin-left: 10px; width: 43px;">요청자</label>
						<div class="dib" style="width: calc(100% - 273px);">
							<div id="cboRequest" data-ax5select="cboRequest" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-50 dib margin-5-right" style="min-width: 0;"></div>
							<input class="dib" style="width: calc(50% - 5px); " id="txtdept" type="text" disabled="disabled">
						</div>
						
					</div>
					
					
					<!-- 접수자 -->
					<div class="dib vat" style="width:20%;">
						<label class="dib vat" style="margin-left: 10px; width: 74px;">접수자</label>
						<div class="dib vat" style="width: calc(100% - 95px);">
							<input class="dib width-100" id="txtuser" type="text" disabled="disabled">
						</div>
					</div>
					
					<!-- 접수등록일 -->
					<div class="dib vat" style="width:20%;">
						<label class="dib vat" style="margin-left: 10px;">접수등록일</label>
						<div class="dib vat" style="width: calc(100% - 76px);">
							<input class="dib width-100" id="txtdate" type="text" disabled="disabled">
						</div>
					</div>
				</dd>
			</dl>
		</div>
		<div class="row" id="subSrInsertBox" style="display:none;">
			<dl>
				<dt><label>분류유형</label></dt>
				<dd>
					<div style="width:10%;" class="dib vat">
							<div id="cboCatTypeSR" data-ax5select="cboCatTypeSR" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100 dib" style="min-width: 0;"></div>
					</div>
					
					<div class="dib" style="width:50%;">
						<div class="dib" style="width:20%;">
							<label class="dib" style="margin-left: 10px;">우선순위</label>
							<div class="dib" style="width: calc(100% - 64px);">
								<div id="cboDetailType" data-ax5select="cboDetailType" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100 dib" style="min-width: 0;"></div>
							</div>
						</div>
						
						<div class="dib vat" style="width:40%;">
							<label class="dib vat" style="margin-left: 10px;">시스템</label>
							<div class="dib vat" style="width: calc(100% - 52px);">
							<div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100 dib" style="min-width: 0;"></div>
							</div>
						</div>
						
						<div class="dib vat" style="width:40%;">
							<label class="dib vat" style="margin-left: 10px;">SR업무</label>
							<div class="dib vat" style="width: calc(100% - 54px);">
								<div id="cboSrJob" data-ax5select="cboSrJob" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100 dib" style="min-width: 0;"></div>
							</div>
						</div>
					</div>
					
					<div class="dib vat" style="width:20%;">
						<label class="dib vat" style="margin-left: 10px; width: 74px;">BA</label>
						<div class="dib vat" style="width: calc(100% - 95px);">
							<div id="cboBaUser" data-ax5select="cboBaUser" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100 dib" style="min-width: 0;"></div>
						</div>
					</div>
					
					<div class="dib vat" style="width:20%;">
						<label class="dib vat" style="margin-left: 10px;">PGM담당</label>
						<div class="dib vat" style="width: calc(100% - 76px); margin-left:9px; ">
							<div id="cboPgmUser" data-ax5select="cboPgmUser" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100 dib" style="min-width: 0;"></div>
						</div>
					</div>
				</dd>
			</dl>
		</div>
		
		<div class="row">
			<dl>
				<dt><label>SR제목</label></dt>
				<dd>
					<input id=txttitle type="text" autocomplete="off" style="width: 60% !important;">
					
					<div class="dib vat" style="width:40%;">
						<label class="dib" style="margin-left: 10px; width: 74px;">반영프로세스</label>
						<div class="dib" style="width: calc(100% - 89px);">
							<div id="cboGrade" data-ax5select="cboGrade" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100 dib"></div>
						</div>
					</div>
				</dd>
			</dl>
		</div>
		<div class="row">
			<dl>
			<!-- 
				<dt><label>BA</label></dt>
				<dd>
					<div style="width:15%;" class="dib vat">
						<div id="cboRequestor" data-ax5select="cboRequestor" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100 dib" style="min-width: 0;"></div>
					</div>
			 -->					
					<!-- 개발책임자 -->
				<dt><label>개발책임자</label></dt>
				<dd>
					<div class="dib vat" style="width: 15%;">
						<div id="cboOwner" data-ax5select="cboOwner" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100 dib" style="min-width: 0;"></div>
					</div>
					
					<!-- 테스트담당자 -->
					<div class="dib vat" style="width:35%;">
						<label class="dib" style="margin-left: 10px; width: 74px;">테스트담당자</label>
						<div class="dib" style="width: calc(100% - 89px);">
							<input class="dib margin-5-right" style="width: calc(50% - 5px); " id="txtTst" type="text" >
							<div id="cboTst" data-ax5select="cboTst" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-50 dib" style="min-width: 0;"></div>
						</div>
					</div>
					
					<div class="dib vat margin-15-left">
						<input type="checkbox" class='checkbox-pie' data-label="DB오브젝트 변경시(테이블,인덱스등)" id="chkTable">
					</div>
				</dd>
			</dl>
		</div>
		<div class="row">
			<dl>
			<!-- 
				<dt><label>시스템</label></dt>
				<dd>
					<div style="width:15%;" class="dib vat">
						<div id="cboRequestor" data-ax5select="cboRequestor" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100 dib" style="min-width: 0;"></div>
					</div>
			 -->		
					<!-- 개발담당자 -->
				<dt><label>개발담당자</label></dt>
				<dd>
					<div class="dib vat" style="width: 15%;">
						<div id="cboDev" data-ax5select="cboDev" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100 dib" style="min-width: 0;"></div>
					</div>
					
					<div class="dib vat margin-5-left" style="width:35%;">
						<button id="cmdAdd" class="btn_basic_s">추가</button>
						<button id="cmdRmv" class="btn_basic_s">삭제</button>
					</div>
					<div class="dib vat poa" style="width:calc(50% - 5px); z-index: 99">
						<textarea id="txtTable" style="align-content:flex-start; width:calc(100% - 10px); height:140px; resize: none; overflow-y:auto; padding:5px; margin-left:10px;" disabled="disabled"></textarea>
					</div>
				</dd>
			</dl>
		</div>
		
		<div class="row">
			<dl style="height: 110px;">
			<!-- 
					<div style="width:15%;" class="dib vat">
						<div id="cboRequestor" data-ax5select="cboRequestor" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-100 dib" style="min-width: 0;"></div>
					</div>
			-->	
				<dt></dt>
				<dd>
					<!-- 개발담당자 그리드 -->
					<div class="dib vat" style="width:50%;">
						<div class="az_board_basic scroll_h az_board_basic_in" style="height:100%;">
					    	<div data-ax5grid="devUserGrid" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;"></div>
					    </div>
					</div>
				</dd>
			</dl>
		</div>
		<div class="row">
			<dl>
				<dt><label>착수예정일</label></dt>
				<dd>
					<div style="width:120px;" class="dib vat" data-ax5picker="basic" >
						<input class="dib az_input_group"  id="strdate" type="text" placeholder="yyyy/mm/dd" style="width: 81px;" autocomplete="off" ><button
									id="btnstrdate" class="btn_calendar" ><i class="fa fa-calendar-o"></i></button>
					</div>
					<div class="dib vat">
						<label class="dib" style="margin-left: 10px; width: 74px;">완료예정일</label>
						<div class="dib az_input_group" style="width:120px;" data-ax5picker="basic2" >
							<input class="dib" id="enddate" type="text" placeholder="yyyy/mm/dd" style="width: 81px;" autocomplete="off" ><button
									id="btnenddate" class="btn_calendar" ><i class="fa fa-calendar-o"></i></button>
						</div>
					</div>
				</dd>
			</dl>
		</div>
		<div class="row">
		    <dl>
		    	<dt><label id="lbUser">검토의견</label></dt>
			    <dd>
					<input id="txtcncl" type="text" style="min-width: 0;  padding:5px;" autocomplete="off" disabled="disabled"/>
				</dd>
			</dl>
		</div>
		<div class="btn_wrap_r" style="position:relative; float:right; margin-top:10px; bottom:0; right:0; font-size:0;">
			<button id="cmdBefore" class="btn_basic_s">접수전사전검토</button>
			<button id="cmdCopy" class="btn_basic_s" disabled="true" title="선택한 SR을 복사하여 SR등록상태까지로 신규 생성합니다.">SR복사</button>
			<button id="cmdCncl" class="btn_basic_s" disabled="true" title="관리자취소는 SR등록완료/SR접수완료/개발계획서작성완료 상태일 때만 가능합니다.">SR취소</button>
			<button id="cmdReq" class="btn_basic_s" disabled="true">접수</button>
		</div>
	</div>
</div>
<form name="popPam">
	<input type="hidden" name="acptno"/>
	<input type="hidden" name="user"/>
</form>
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/tab/sr/SRReceiveTab.js"/>"></script>
