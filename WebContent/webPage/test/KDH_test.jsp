<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/webPage/common/common.jsp" />

<style type="text/css">
/* 탭 메뉴 css */
ul.tabs {
    margin: 0;
    padding: 0;
    float: left;
    list-style: none;
    height: 32px;
    border-bottom: 1px solid #eee;
    border-left: 1px solid #eee;
    width: 100%;
    font-family:"dotum";
    font-size:12px;
}
ul.tabs li {
    float: left;
    text-align:center;
    cursor: pointer;
    width:82px;
    height: 31px;
    line-height: 31px;
    border: 1px solid #eee;
    border-left: none;
    font-weight: bold;
    background: #fafafa;
    overflow: hidden;
    position: relative;
}
ul.tabs li.active {
    background: #FFFFFF;
    border-bottom: 1px solid #FFFFFF;
}
.tab_container {
    border: 1px solid #eee;
    border-top: none;
    clear: both;
    float: left;
    background: #FFFFFF;
    width:100%;
    height: 30%;
}
.tab_content {
    padding: 5px;
    font-size: 12px;
    display: none;
}
.tab_container .tab_content ul {
    width:100%;
    margin:0px;
    padding:0px;
}
.tab_container .tab_content ul li {
    padding:5px;
    list-style:none;
}

.no-padding-side {
	padding-left: 0px;
	padding-right: 0px;
}

 #container {
    
}
</style>

<div class="col-lg-4"></div>
<div class="hpanel">
	<div class="col-lg-12 col-md-12 col-sm-12" style="border-bottom: 1px solid #ddd; margin-bottom: 10px;">
		<div class="col-lg-6 col-md-6 col-sm-6 padding-5-top">
			<div class="col-lg-3 col-md-3 col-sm-3">
				<div class="col-lg-3 col-md-3 col-sm-3 no-padding">
					<label class="padding-5-top">이름</label>
				</div>
				<div class="col-lg-9 col-md-9 col-sm-9 no-padding">
					<input class="form-control" type="text" id="inputName" onkeyup="enterKey();">
				</div>
			</div>
			<div class="col-lg-3 col-md-3 col-sm-3">
				<div class="col-lg-3 col-md-3 col-sm-3 no-padding">
					<label class="padding-5-top">ID</label>
				</div>
				<div class="col-lg-9 col-md-9 col-sm-9 no-padding">
					<input class="form-control" type="text" id="inputId" onkeyup="enterKey();">
				</div>
			</div>
			<div class="col-lg-3 col-md-3 col-sm-3">
				<div class="col-lg-3 col-md-3 col-sm-3 no-padding">
					<label class="padding-5-top">직위</label>
				</div>
				<div class="col-lg-9 col-md-9 col-sm-9 no-padding">
					<div class="form-group" style="margin-bottom: 10px;">
		            	<div id="selPosition" data-ax5select="selPosition" data-ax5select-config="{}"></div>
		        	</div>
				</div>
			</div>
			<div class="col-lg-3 col-md-3 col-sm-3">
				<div class="col-lg-3 col-md-3 col-sm-3 no-padding">
					<label class="padding-5-top">직책</label>
				</div>
				<div class="col-lg-9 col-md-9 col-sm-9 no-padding">
					<div class="form-group" style="margin-bottom: 0px;">
		            	<div id="selDuty" data-ax5select="selDuty" data-ax5select-config="{}"></div>
		        	</div>
				</div>
			</div>
		</div>
		
		<div class="col-lg-6 col-md-6 col-sm-6 padding-5-top no-padding-side">
			<div class="col-lg-3 col-md-3 col-sm-3 no-padding-side">
				<div style="margin-top: 5px;">
					<label class="wLabel-left" style="width: 0px;"></label>
					<input id="active1" tabindex="8" type="radio" name="active" value="optCkOut" checked="checked"/>
					<label for="radioCkOut" style="margin-right: 10px;">활성사용자만</label>
					<input id="active2" tabindex="8" type="radio" name="active" value="optCkIn"/>
					<label for="radioCkIn">비활성사용자만</label>
				</div>
			</div>
			
			<div class="col-lg-6 col-md-6 col-sm-6">
				<div class="col-lg-6 col-md-6 col-sm-6">
					<div class="input-group" data-ax5picker="date1" data-picker-date="date1">
						<input id="stDt" type="text" class="form-control" placeholder="yyyy-mm-dd" value="2010-01-01">
						<span class="input-group-addon"><i class="fa fa-calendar-o"></i></span>
					</div>
				</div>			
				<div class="col-lg-6 col-md-6 col-sm-6">
					<div class="input-group" data-ax5picker="date2" data-picker-date="date2">
						<input id="edDt" type="text" class="form-control" placeholder="yyyy-mm-dd" value="2019-01-01">
						<span class="input-group-addon"><i class="fa fa-calendar-o"></i></span>
					</div>
				</div>
			</div>	
			
			<div>
				<button id="btnSearch" class="btn btn-default">
					조회 <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
				</button>
				<button id="btnExcel" class="btn btn-default">
					엑셀저장 <span class="glyphicon glyphicon-file" aria-hidden="true"></span>
				</button>
			</div>
		</div>
	</div>
</div>

<div class="col-lg-8 col-md-6 col-sm-6">
	<div class="hpanel">
		<div data-ax5grid="first-grid" data-ax5grid-config="{}" style="width:100%; height:55%;"></div>
	</div>
</div>
<div class="col-lg-4 col-md-6 col-sm-6">
	<div>
		<div data-ax5layout="ax1" data-config='{layout:"dock-panel}' style="border: 1px solid #ccc; height: 55%;">
			<div data-dock-panel='{dock:"top", split:true, height: 80}' style="text-align: center; background-image:linear-gradient(white, #ededed)">
				<label style="padding-top: 5px;">사용자 정보</label>
			</div>
			<div class="binder-form" style="border-top: 1px solid #ccc; padding: 10px;">
			    <div class="form-group">
			        <label>이름</label>
			        <input type="text" class="form-control" data-ax-path="name" style="">
			    </div>
			    <div class="form-group">
			        <label>연락처</label>
			        <input type="text" class="form-control" data-ax-path="tel">
			    </div>
			    <div class="form-group">
			        <label>email</label>
			        <input type="text" class="form-control" data-ax-path="email">
			    </div>
			    <div class="form-group">
			        <label>등록일</label>
			        <input type="text" class="form-control" data-ax-path="creatDt">
			    </div>
			    <div class="form-group">
			        <label>최종로그인</label>
			        <input type="text" class="form-control" data-ax-path="lastLogin">
			    </div>
			    <div class="form-group">
			        <label>IP</label>
			        <input type="text" class="form-control" data-ax-path="ip">
			    </div>
	
	    	</div>
		</div>
	</div>
</div>

 
<section>
<div id="container" class="padding-15-top">
	<ul class="tabs">
        <li class="active" rel="tab1" style="color: darkred;">tab1</li>
        <li rel="tab2">tab2</li>
        <li rel="tab3">tab3</li>
   	</ul>
   	<div class="tab_container">
       	<div id="tab1" class="tab_content">
       		<div class="col-lg-7 col-md-3 col-sm-3 padding-5-top padding-5-bottom">
				<div class="col-lg-1 col-md-3 col-sm-3">
					<label class="padding-5-top">이름</label>
				</div>
				<div class="col-lg-4 col-md-9 col-sm-9">
					<input class="form-control" type="text" id="tab1Name">
				</div>
			</div>

       		<div class="col-lg-7 col-md-3 col-sm-3 padding-5-top padding-5-bottom">
				<div class="col-lg-1 col-md-3 col-sm-3">
					<label class="padding-5-top">ID</label>
				</div>
				<div class="col-lg-4 col-md-9 col-sm-9">
					<input class="form-control" type="text" id="tab1Id">
				</div>
			</div>

       		<div class="col-lg-7 col-md-3 col-sm-3 padding-5-top padding-5-bottom">
				<div class="col-lg-1 col-md-3 col-sm-3">
					<label class="padding-5-top">직위</label>
				</div>
				<div class="col-lg-4 col-md-9 col-sm-9">
					<input class="form-control" type="text" id="tab1Position">
				</div>
			</div>

       		<div class="col-lg-7 col-md-3 col-sm-3 padding-5-top padding-10-bottom">
				<div class="col-lg-1 col-md-3 col-sm-3">
					<label class="padding-5-top">직책</label>
				</div>
				<div class="col-lg-4 col-md-9 col-sm-9">
					<input class="form-control" type="text" id="tab1Duty">
				</div>
			</div>
       	</div>
       
       <div id="tab2" class="tab_content">
       		<div>
       			<div data-ax5grid="tab2-grid" data-ax5grid-config="{}" style="width:100%; height:95%;"></div>
       		</div>
       </div>
       
       <div id="tab3" class="tab_content">
       		<div>
       			<div data-ax5grid="tab3-grid" data-ax5grid-config="{}" style="width:100%; height:95%;"></div>
       		</div>
       </div>
       
   	</div>
</div>
</section>

<form name="popPam">
	<input type="hidden" name="formName"/>
	<input type="hidden" name="formId"/>
	<input type="hidden" name="formPosition"/>
	<input type="hidden" name="formDuty"/>
</form>

<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/dev/KDH_test.js"/>"></script>