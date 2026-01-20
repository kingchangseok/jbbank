<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp"/>

<!-- contener S -->
    <div class="contentFrame">
		<div id="history_wrap"></div>
	    <!-- 검색 S-->    
		<div class="az_search_wrap">
			<div class="az_in_wrap por">
				<!-- cell1 -->		
                <div class="width-25 dib vat">
                	<div>
	                    <label class="tit_80 poa">결재종류</label>
	                    <div class="ml_80">
							<div id="cboReqCd" data-ax5select="cboReqCd" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100 dib">
						    </div>
						</div>
					</div>
                	<div class="row">
	                    <label class="tit_80 poa">시스템</label>
	                    <div class="ml_80">
							<div id="cboSys" data-ax5select="cboSys" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100 dib">
						    </div>
						</div>
					</div>
                	<div class="row">
	                    <label class="tit_80 poa">정상결재시</label>
	                    <div class="ml_80">
							<div id="cboCommon" data-ax5select="cboCommon" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100 dib">
						    </div>
						</div>
					</div>
                	<div class="row">
	                    <label class="tit_80 poa">결재조직</label>
	                    <div class="ml_80">
							<input id="txtDept" type="text" class="width-100"></input>
						</div>
					</div>
					<div class="row view_g font_12" style="height:150px">
						<!-- 결재조직 트리-->
						<div class="scrollBind" style="height: 100%;" id="treeDeptDiv">
							<ul id="treeDept" class="ztree"></ul>
						</div>
					</div>
					<div class="row por">
						<p class="font_12">(주) 조회는 결재종류/시스템/직원/외주를 검색조건으로 하여 조회합니다.</p>
					</div>
				</div>
				<!-- cell2 -->		
                <div class="width-25 dib vat">
                	<div class="margin-10-left margin-5-right">
	                	<div>
		                    <label class="tit_80 poa">결재단계</label>
		                    <div class="ml_80">
								<div id="cboSignStep" data-ax5select="cboSignStep" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100 dib">
							    </div>
							</div>
						</div>
	                	<div class="row">
		                    <input type="checkbox" class="font_11 checkbox-pie" id="chkSys" data-label="시스템과관련없음"/><input type="checkbox" class="font_11 checkbox-pie" id="chkMember" data-label="직원"/><input type="checkbox" class="font_11 checkbox-pie" id="chkOutsourcing" data-label="외주"/>
						</div>
	                	<div class="row">
		                    <label class="tit_80 poa">부재처리시</label>
		                    <div class="ml_80">
								<div id="cboBlank" data-ax5select="cboBlank" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100 dib">
							    </div>
							</div>
						</div>
	                	<div class="row por">
		                    <label class="tit_80 poa">결재직무</label>
		                    <div class="tar">
								<input type="checkbox" class="checkbox-pie" id="chkAllRgt" data-label="전체선택"/> <!-- chkAll1 -->
							</div>
						</div>
						<div class="row view_g" style="height:150px;">
							<!-- 직무 체크리스트 -->
							<div class="scrollBind" id="lstRgtDiv" style="height: 100%">
			    				<ul class="list-group" id="lstRgt" style="height: 100%;"> <!-- 결재직무 -->
				    			</ul>
			    			</div>
						</div>
	                    <div class="row por">
							<input type="checkbox" class="checkbox-pie poa" id="chkEnd" data-label="완료처리단계"/>
							<div id="chkDelDiv" class="dis-i-b tar">
				    			<input type="checkbox" class="checkbox-pie" id="chkDel" data-label="삭제가능"/>
				    		</div>
						</div>
					</div>
				</div>
				<!-- cell3 -->		
                <div class="width-25 dib">
                	<div class="margin-5-left margin-10-right">
	                	<div>
		                    <label class="tit_80 poa">단계명칭</label>
		                    <div class="ml_80">
							<input id="txtStepName" type="text" class="width-100"></input>
							</div>
						</div>
	                	<div class="row">
		                    <label class="tit_80 poa">정상(업무후)</label>
		                    <div class="ml_80">
								<div id="cboComAft" data-ax5select="cboComAft" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100 dib">
							    </div>
							</div>
						</div>
	                	<div class="row">
		                    <label class="tit_80 poa">긴급(업무후)</label>
		                    <div class="ml_80">
								<div id="cboEmgAft" data-ax5select="cboEmgAft" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100 dib">
							    </div>
							</div>
						</div>
	                	<div class="row por">
		                    <input type="checkbox" class="checkbox-pie" id="chkGrade" data-label="프로그램속성선택"/> <!-- chkType -->
		                    <div class="poa_r">
								<input type="checkbox" class="checkbox-pie" id="chkAllGrade" data-label="전체선택"/>  <!-- chkAll2 -->
							</div>
						</div>
						<div class="row view_g" style="height:150px;">
							<!-- 프로그램속성 -->
							<div class="scrollBind" id="lstGradeDiv" style="height: 100%">
			    				<ul class="list-group" id="lstGrade" style="height: 100%;"> <!-- lstType -->
				    			</ul>
			    			</div>
						</div>
					</div>
				</div>
				<!-- cell4 -->		
                <div class="width-25 dib">
                	<div>
	                    <label class="tit_80 poa">결재구분</label>
	                    <div class="ml_80">
							<div id="cboSgnGbn" data-ax5select="cboSgnGbn" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100 dib">
						    </div>
						</div>
					</div>
                	<div class="row">
	                    <label class="tit_80 poa">자동처리</label>
	                    <div class="ml_80">
							<div id="cboSysGbn" data-ax5select="cboSysGbn" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100 dib">
						    </div>
						</div>
					</div>
                	<div class="row">
	                    <label class="tit_80 poa">긴급(업무중)</label>
	                    <div class="ml_80">
							<div id="cboEmg" data-ax5select="cboEmg" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}" class="width-100 dib">
							</div>
						</div>
					</div>
                	<div class="row por">
	                    <input type="checkbox" class="checkbox-pie" id="chkJawon" data-label="프로그램종류선택"/>  <!-- chkProg -->
	                    <div class="poa_r">
							<input type="checkbox" class="checkbox-pie" id="chkAllJawon" data-label="전체선택"/>  <!-- chkAll3 -->
						</div>
					</div>
					<div class="row view_g" style="height:150px;">
						<!-- 프로그램종류 체크리스트 -->
						<div class="scrollBind" id="lstJawonDiv" style="height: 100%">
		    				<ul class="list-group" id="lstJawon" style="height: 100%;"> <!-- lstProg --> 
			    			</ul>
		    			</div>
					</div>
				</div>
				<!--컨텐츠버튼 S-->
				<div class="tar">
					<!-- <button class="btn_basic_s" data-grid-control="excel-export" id="btnExl">엑셀저장</button> -->
					<button class="btn_basic_s" id="btnReq">등록</button>
					<button class="btn_basic_s margin-5-left" id="btnDel">폐기</button>
					<button class="btn_basic_s margin-5-left" id="btnQry">조회</button>
					<button class="btn_basic_s margin-5-left" id="btnAllQry">전체조회</button>
					<button class="btn_basic_s margin-5-left" id="btnCopy">결재정보복사</button>
					<!-- <button class="btn_basic_s margin-5-left" id="btnBlank">대결범위등록</button> -->
				</div>
				<!--컨텐츠버튼 E-->
			</div>
		</div>
	    <!-- 검색 E-->
	    <!-- 게시판 S-->
	    <div class="az_board_basic" style="height:50%">
	    	<div data-ax5grid="grdSign" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 100%;" class="resize"></div>
		</div>	
		<!-- 게시판 E -->
    </div>
<!-- contener E -->

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/administrator/ApprovalInfo.js"/>"></script>