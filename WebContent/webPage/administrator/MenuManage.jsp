<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />
<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/administrator/MenuManage.js"/>"></script>

<style>
/* Nestable list */

.dd {
    position: relative;
    display: block;
    margin: 0;
    padding: 0;
    list-style: none;
    font-size: 13px;
    line-height: 20px;
}

.dd-list {
    display: block;
    position: relative;
    margin: 0;
    padding: 0;
    list-style: none;
}

.dd-list .dd-list {
    padding-left: 30px;
}

.dd-collapsed .dd-list {
    display: none;
}

.dd-item,
.dd-empty,
.dd-placeholder {
    display: block;
    position: relative;
    margin: 0;
    padding: 0;
    min-height: 20px;
    font-size: 13px;
    line-height: 20px;
}

.dd-handle {
    display: block;
    margin: 5px 0;
    padding: 5px 10px;
    color: #333;
    text-decoration: none;
    border: 1px solid #e4e5e7;
    background: #f7f9fa;
    -webkit-border-radius: 3px;
    border-radius: 3px;
    box-sizing: border-box;
    -moz-box-sizing: border-box;
}

.dd-handle span {
    font-weight: bold;
}

.dd-handle:hover {
    background: #f0f0f0;
    cursor: pointer;
    font-weight: bold;
}

.dd-item > button {
    display: block;
    position: relative;
    cursor: pointer;
    float: left;
    width: 25px;
    height: 20px;
    margin: 5px 0;
    padding: 0;
    text-indent: 100%;
    white-space: nowrap;
    overflow: hidden;
    border: 0;
    background: transparent;
    font-size: 12px;
    line-height: 1;
    text-align: center;
    font-weight: bold;
}

.dd-item > button:before {
    content: '+';
    display: block;
    position: absolute;
    width: 100%;
    text-align: center;
    text-indent: 0;
}

.dd-item > button[data-action="collapse"]:before {
    content: '-';
}


.dd-placeholder,
.dd-empty {
    margin: 5px 0;
    padding: 0;
    min-height: 30px;
    /* background: #f2fbff;
    border: 1px dashed #e4e5e7; */
    box-sizing: border-box;
    -moz-box-sizing: border-box;
}

.dd-empty {
    border: 1px dashed #bbb;
    min-height: 130px;
    /* background-color: #e5e5e5; */
    /* background-image: -webkit-linear-gradient(45deg, #fff 25%, transparent 25%, transparent 75%, #fff 75%, #fff), -webkit-linear-gradient(45deg, #fff 25%, transparent 25%, transparent 75%, #fff 75%, #fff);
    background-image: -moz-linear-gradient(45deg, #fff 25%, transparent 25%, transparent 75%, #fff 75%, #fff), -moz-linear-gradient(45deg, #fff 25%, transparent 25%, transparent 75%, #fff 75%, #fff);
    background-image: linear-gradient(45deg, #fff 25%, transparent 25%, transparent 75%, #fff 75%, #fff), linear-gradient(45deg, #fff 25%, transparent 25%, transparent 75%, #fff 75%, #fff); */
    background-size: 60px 60px;
    background-position: 0 0, 30px 30px;
}

.dd-dragel {
    position: absolute;
    z-index: 9999;
    pointer-events: none;
}

.dd-dragel > .dd-item .dd-handle {
    margin-top: 0;
}

.dd-dragel .dd-handle {
    -webkit-box-shadow: 2px 4px 6px 0 rgba(0, 0, 0, .1);
    box-shadow: 2px 4px 6px 0 rgba(0, 0, 0, .1);
}

/**
* Nestable Extras
*/
.nestable-lists {
    display: block;
    clear: both;
    padding: 30px 0;
    width: 100%;
    border: 0;
    border-top: 2px solid #e4e5e7;
    border-bottom: 2px solid #e4e5e7;
}

#nestable-output,
#nestable2-output {
    width: 100%;
    font-size: 0.75em;
    line-height: 1.333333em;
    font-family: open sans, lucida grande, lucida sans unicode, helvetica, arial, sans-serif;
    padding: 5px;
    box-sizing: border-box;
    -moz-box-sizing: border-box;
}

#nestable-output,
#nestable2-output {
    font-size: 12px;
    padding: 25px;
    box-sizing: border-box;
    -moz-box-sizing: border-box;
}

.dd-item, .dd-empty, .dd-placeholder{
	line-height: 15px;
}

</style>

<script>

    $(function () {
        
    });
</script>


<div class="contentFrame">
      <!-- history S-->
	<div id="history_wrap">관리자 <strong>&gt; 메뉴관리</strong></div>
      <!-- history E-->   
	<div class="half_wrap_cb">
		<div class="l_wrap width-30">
			<div class="margin-5-right">				   
		        <!-- 검색 S-->    
				<div class="az_search_wrap" style="padding-top:14px">
					<div class="az_in_wrap">
						<div class="vat">
		                    <label id="lbUser" class="tit_80 poa"></label>
		                    <div class="ml_80">
		                    	<div id="Cbo_MaCode" data-ax5select="Cbo_MaCode" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}"></div> 
							</div>
						</div>
						<div class="row">
							<label id="lbUser" class="tit_80 poa">메뉴명</label>
							<div class="ml_80">
								<input id="Txt_MaCode" name="Txt_MaCode" type="text" class="width-100">
							</div>
						</div>
						<div class="row">
							<label id="lbUser" class="tit_80 poa">링크파일명</label>
							<div class="ml_80">
								<input id="Txt_MaFile" name="Txt_MaFile" type="text" class="width-100">
							</div>
						</div>
						<div class="row">
		                    <label id="lbUser" class="tit_80 poa">신청코드</label>
		                    <div class="ml_80">
								<div id="Cbo_reqcd" data-ax5select="Cbo_reqcd" data-ax5select-config="{size:'sm',theme:'primary',selecteditable: true}"></div>
							</div>
							<div class="row tar">
								<button class="btn_basic_s margin-5-left" id="btnInsert" data-grid-control="excel-export">등록</button>
								<button class="btn_basic_s margin-5-left" id="btnDelete" data-grid-control="excel-export" disabled>폐기</button>
							</div>
						</div>
					</div>
				</div>
				<!--검색E-->
				<!--메뉴체계 S-->					
				<div class="half_wrap">
					<div class="vat por">
					    <label id="lbUser" class="title">[메뉴체계]</label><button class="btn_basic_s poa_r" id="btnFact">조회</button>
					</div>
					<div class="scrollBind row" style="height: calc(100% - 260px); border: 1px dotted gray;">
						<ul id="treeMenu" class="ztree"></ul>
	    			</div>
				</div>
	   		</div>
		</div>
		<div class="r_wrap width-70">
			<div class="margin-5-left">		   
		        <!-- 검색 S-->    
				<div class="az_search_wrap">
					<div class="az_in_wrap">
						<!--메뉴트리편집-->
						<div>
		                    <label id="lbUser" class="tit_80 poa">메뉴트리편집</label>
		                    <div class="ml_80">
								<div id="Cbo_selMenu" class="width-20 dib" data-ax5select="Cbo_selMenu" data-ax5select-config="{size:'sm',theme:'primary'}"></div> 
								
								<div id="Cbo_Menu" data-ax5select="Cbo_Menu" data-ax5select-config="{size:'sm',theme:'primary'}" class="width-40 dib margin-5-left"></div>
								
								<div class="dib" style="float: right; margin-right:2.7%;">
									<button id="btnAply" class="btn_basic_s">적용</button>
								</div> 									
							</div>
						</div>							
						<!--메뉴구조-->
						<div class="row">
							<div class="l_wrap width-50">																
								<div class="l_wrap width-95">
									<div class="dd scrollBind row" id="nestable2" style="overflow-y:auto; height:119px;">
										<ol id="tmpTest" class="dd-list">
										</ol>
									</div>										
								</div>
							</div>
							<div class="r_wrap width-50">
								<div class="l_wrap width-95">
									<div class="dd scrollBind row" id="nestable" style="overflow-y:auto; height:119px;">
										<ol id="tmpTest2" class="dd-list">
										</ol>
									</div>										
								</div>
							</div>
						</div>
					</div>
				</div>
				<!--검색E-->
				<div class="row" style="margin-top: 0px;">		
					<div data-ax5grid="first-grid" data-ax5grid-config="{}" style="height: calc(100% - 213px);"></div>
				</div>
			</div>
		</div>
	</div>
</div>
