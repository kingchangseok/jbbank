<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/webPage/common/common.jsp" />

<style id="style">
	.context-menu{
		 position: absolute;
		 visibility:hidden;
		 border: 1px solid #888888;
		 border-radius: 3px;
		 background: white;
		 box-shadow: 5px 5px 5px #888888;
	}
	.context-menu ul{
		list-style: none;
		padding: 2px;
	}
	.context-menu ul li{
		padding: 8px 5px;
		margin-bottom: 3px;
		color: black;
		font-weight: bold;
		font-size: 12px;
	}
	.context-menu ul li:hover{
		cursor: pointer;
		background: #90E4FF;
	}
	ul.tabs li{
		text-overflow: ellipsis !important;
	    white-space: nowrap !important;
	    overflow: hidden !important; 
	}
}
</style>

<body>
<div class="contentFrame">
	<div id="history_wrap"></div>
	<div class="l_wrap width-30">
		<div class="margin-5-right">
			<div class="az_search_wrap">
				<div class="az_in_wrap">
					<div class="row">
						<input type="checkbox" class='checkbox-pie' id="chkUpLow" style="width:90px;" data-label="대소문자구분"></input>
						<input id="txtRsrc" name="txtRsrc" type="text" style="width: calc(100% - 270px);" placeholder="검색할 프로그램명을 입력하세요.">
						<button id="btnQry" name="btnQry" class="btn_basic_s margin-5-left margin-5-right por">검색</button>
						<input type="checkbox" class='checkbox-pie' id="chkDev" style="width:90px;" data-label="개발서버소스확인"></input>
					</div>
				</div>
			</div>
			<div class="half_wrap" style="margin-top: 0px;">
				<div class="l_wrap width-100">
					<div class="scrollBind row" style="height: calc(100% - 120px); border: 1px dotted gray;">
						<ul id="treePath" class="ztree"></ul>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="l_wrap width-70">
		<div class="margin-5-left">
			<div class="margin-5-top tar dib">
				<input id="optWord" type="radio" name="optradio" value="W" onchange="optradio_change();"/>
				<label for="optWord">단어검색</label>
				<input id="optLine" type="radio" name="optradio" value="L" onchange="optradio_change();" checked/>
				<label for="optLine">라인검색</label>
				<input id="txtSearch" name="txtSearch" type="text" style="width:200px;" placeholder="검색할 라인번호를 입력하세요.">
				<button id="btnSearch" name="btnSearch" class="btn_basic_s margin-5-left por">찾기</button>
			</div>
		</div>
		
		<div class="margin-5-left">
			<!-- tab S-->
			<div class="margin-5-top">
				<div class="tab_wrap">
					<ul class="tabs" id="tabs">
					</ul>
				</div>
				<!-- tab E-->
				<div style="height:89%" id="content"> <!--  tab_container -->

			   	</div>
			</div>
		</div>
	</div>
</div>
</body>

<div id="context-menu" class="context-menu"> 
 	<ul> 
 		<li id="contextmenu1" onclick="contextmenu_click('1');">탭삭제</li>
 		<!-- <li id="contextmenu2" onclick="contextmenu_click('2');">소스복사</li> --> 
 		<li id="contextmenu3" onclick="contextmenu_click('9');">소스저장</li>
 	</ul> 
</div>

<c:import url="/js/ecams/common/commonscript.jsp"/>
<script type="text/javascript" src="<c:url value="/js/ecams/program/SourceView.js"/>"></script>