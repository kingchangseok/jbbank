<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/webPage/common/common.jsp" />

<style>
.ml_100>label {
	font-weight: normal;
	height: 25px;
}

.ml_100>textarea {
	width: 98%;
	border: 0px;
	resize: none;
	margin-top: 5px;
}

.myInfo_title {
    padding-bottom: 5px;
    padding-top: 5px;
    margin-bottom: 10px;
    width: 100%;
    background: #f6f6f6;
}

.myInfo_card {
	height: 290px;
	background-color: #fff;
	box-shadow: #DDD 1px 2px 3px 1px;
}

.card_contents{
	margin-left: 15px;
}

.title_label{
	font-size: 16px;
	margin-left: 7px;
}
</style>

<div class="contentFrame">
	<div class="az_search_wrap">
	<div style="width:57%;margin:auto; min-width:1050px;"><!-- background-color: #fff; padding: 5px 20px;  -->
		<div class="cb">
			<div class="float-left width-30 margin-10-top myInfo_card"> 
				<fieldset style="border:3px #2477c1;margin: 10px;">
					<div class="myInfo_title"><label class="title_label">기본정보</label></div>
					<div class="card_contents">
		               	<div class="sm-row">
							<label class="tit_80 poa">사용자ID</label>
		                	<div class="ml_100">
								<label id="txtUserId"></label>
							</div>
						</div>
		               	<div class="sm-row">
							<label class="tit_80 poa">이름</label>
			                <div class="ml_100">
								<label id="txtUserName"></label>
							</div>
		               	</div>
		               	<div class="sm-row">
							<label class="tit_80 poa">IP Address</label>
			                <div class="ml_100">
								<label id="txtIp"></label>
							</div>
		               	</div>
		               	<div class="sm-row">
							<label class="tit_80 poa">E-mail</label>
			                <div class="ml_100">
								<label id="txtEMail"></label>
							</div>
		               	</div>
		               	<div class="sm-row">
							<label class="tit_80 poa">전화번호1</label>
			                <div class="ml_100">
								<label id="txtTel1"></label>
							</div>
		               	</div>
		               	<div class="sm-row">
							<label class="tit_80 poa">전화번호2</label>
			                <div class="ml_100">
								<label id="txtTel2"></label>
							</div>
		               	</div>
		               	<div class="sm-row">
							<label class="tit_80 poa">비밀번호</label>
			              	<div class="ml_100">
								<label id="txtPassWd" style="color:#0000ff;font-weight:bold;text-decoration:underline;cursor: pointer;">변경하기</label>
							</div>
						</div>
					</div>
				</FIELDSET>
			</div>
			
			<div class="float-left width-33 margin-10-top myInfo_card" style="margin-left: 22px;">
				<fieldset style="border:3px #2477c1;margin: 10px;">
					<div class="myInfo_title"><label class="title_label">조직정보</label></div>
	               	<div class="card_contents">
	               	<div class="sm-row">
						<label class="tit_80 poa">직급</label>
	                	<div class="ml_100">
							<label id="txtPosition"></label>
						</div>
					</div>
	               	<div class="sm-row">
						<label class="tit_80 poa">직위</label>
	                	<div class="ml_100">
							<label id="txtDuty"></label>
						</div>
					</div>
	               	<div class="sm-row">
						<label class="tit_80 poa">소속조직</label>
	                	<div class="ml_100">
							<label id="txtOrg"></label>
						</div>
					</div>
	               	<div class="sm-row">
						<label class="tit_80 poa">소속(겸직)</label>
	                	<div class="ml_100">
							<label id="txtOrgAdd"></label>
						</div>
					</div>
					</div>
				</fieldset>
			</div>
			
			<div class="float-left width-33 margin-20-left margin-10-top myInfo_card" style="float: right;">
				<fieldset style="border:3px #2477c1;margin: 10px;">
					<div class="myInfo_title"><label class="title_label">부재등록정보</label></div>
	               	<div class="card_contents">
	               	<div class="sm-row">
						<label class="tit_80 poa">대결지정</label>
	                	<div class="ml_100">
							<label id="txtDaeGyul"></label>
						</div>
					</div>
	               	<div class="sm-row">
						<label class="tit_80 poa">부재기간</label>
	                	<div class="ml_100">
							<label id="txtBlankTerm"></label>
						</div>
					</div>
	               	<div class="sm-row">
						<label class="tit_80 poa">부재사유</label>
	                	<div class="ml_100 margin-5-right">
							<textarea id="txtBlankSayu" rows="6" style="overflow-y:auto; width:100%; border: 1px solid #ddd; padding: 5px;" readonly></textarea>
						</div>
					</div>
	               	<div class="sm-row">
						<button class="btn_basic_s" style="margin-top:10px; margin-right: 5px; float: right;" id="btnDaeGyul">부재설정하기</button>
					</div>
					</div>
				</fieldset>
			</div>
		</div>
		
		<div class="cb">
			<div class="float-left width-100 margin-15-top myInfo_card" style="height: 480px;">
				<fieldset style="border:3px #2477c1;margin: 10px;">
					<div class="myInfo_title"><label class="title_label">권한정보</label></div>
					<div class="card_contents">
						<label id="basicInfo" style="color:#2477c1;font-size:15;"></label>
			            <div class="sm-row" style="margin-top:20px;">
							<label class="tit_80 poa">담당직무</label>
			              	<div class="ml_100 margin-5-right">
								<textarea id="txtRgtCd" rows="2" style="width:100%; border: 1px solid #ddd; padding: 5px;" readonly></textarea>
							</div>
						</div>
			            <div class="sm-row" style="margin-top:10px;">
							<label class="tit_80 poa">담당업무</label>
			              	<div class="ml_100 margin-5-right">
								<div data-ax5grid="jobGrid" class="width-100" data-ax5grid-config="{showLineNumber: true, lineNumberColumnWidth: 40}" style="height: 300px;"></div>	
							</div>
						</div>
					</div>
		    	</fieldset>
			</div>
		</div>
	</div>
	</div>
</div>


<c:import url="/js/ecams/common/commonscript.jsp" />
<script type="text/javascript" src="<c:url value="/js/ecams/tab/mypage/MyInfoView.js"/>"></script>