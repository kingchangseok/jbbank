<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>형상관리 로그인</title>

<c:import url="/webPage/common/common.jsp" />
<%@ page import = "com.ecams.common.base.*"%>

<link rel="stylesheet" href="<c:url value="/css/ecams/login/loginPage.css" />" id="cssFile">
</head>
<body>
	<%
		RSA rsa = RSA.getEncKey();
		request.setAttribute("publicKeyModulus", rsa.getPublicKeyModulus());
		request.setAttribute("publicKeyExponent", rsa.getPublicKeyExponent());
		request.getSession().setAttribute("_ecams_encry_key_", rsa.getPrivateKey());
	
		String custIP = request.getHeader("X-FORWARDED-FOR");
	    if (custIP == null || custIP.length() == 0) {
	    	custIP = request.getHeader("Proxy-Client-IP");
	    }
	    if (custIP == null || custIP.length() == 0) {
	    	custIP = request.getHeader("WL-Proxy-Client-IP");  // 웹로직
	    }
	    if (custIP == null || custIP.length() == 0) {
	    	custIP = request.getRemoteAddr() ;
	    }
	    String Url =  request.getRequestURL().toString();
	%>
	
	<section class="height-100">
		<div class="login-form" style="display: none;">
		    <form id="ecamsLoginForm" method="post">
		    <div id="back_card">
			    <div id="imgDiv">
			    	<img src="/img/login/basic.jpg" id="loginImg">
			        <h2 class="text-center" id="title">로그인</h2>
			    </div>
		        <div id="contents">
		        <div id="siteImg"><img src="/img/jbbank_logo.png" style="height: 42px;"></div>
					<div id="title_logo" class="text-center" style="display: none;"><img src="/img/login_logo6.png" id="img"></div>
			        <div class="form-group ui input focus">
			        	<input id="idx_input_id" name="idx_input_id" class="form-control az_login_input" placeholder="개인번호를 입력하세요." required="required" />
			        </div>
			        <div class="form-group">
			            <input type ="password" id="idx_input_pwd" name="idx_input_pwd" class="form-control az_login_input" placeholder="비밀번호를 입력하세요" required="required" autocomplete="off"/>
			        </div>
			        <div class="form-group">
			        	<button class ="az_login_btn" id="idx_login_btn" style="width:100%" type="submit">로그인</button>
			        </div>
			        <div class="clearfix">
						<div style="display: inline-block; padding-top: 3px;">
							<input id="saveInfo" tabindex="8" type="checkbox" style="margin-top: 5px;" name="saveInfo"/>
						</div>
			        	<label for="saveInfo">개인번호 저장</label>
			        	<!-- <div style="display: inline-block; padding-top: 3px;">
							<input id="savePwd" tabindex="8" type="checkbox" style="margin-top: 5px;" name="savePwd"/>
						</div>
			        	<label for="savePwd">비밀번호 저장</label> -->
			            <!-- <a href="#" class="pull-right" style="display:none">Forgot Password?</a> -->
			        </div>
			        <div class="text-center" style="margin-top: 30px">
			        	<!-- <a class="gulim" href="../../eCAMS.exe"><img width="15" height="15" style="margin-right: 5px;" src="/img/download.png"/>알림메시지설치</a> -->
			        	<a class="gulim" href="../../eCAMS_manual.ppt"><img width="15" height="15" style="margin-left: 10px;margin-right: 5px;" src="/img/download.png"/>사용자가이드</a>
			        </div>  
		        </div>       
		    </div>      
		    </form>
		        <div style="margin-top: 70px;" class="bottom-contents">
				    <p class="text-center" id="bottom_info">
				 		<%-- <a href="#" class="bottom-text" >User Manual</a><a class="bottom-bar"></a> --%>
				 		<a href="#" class="bottom-text">eCAMS ver 5.0</a><%--<a class="bottom-bar"></a> --%>
				 		<%-- <a href="#" class="bottom-text" style="font-weight: bold;">Forgot Account?</a>--%>
				    </p>
				 	<h5 class="bottom-text text-center copyright">Copyright © AZSOFT. ALL rights reserved.</h5>
		        </div>
		    
		</div>
	</section>
	
	<form name="popPam">
		<input type="hidden" name="userId"/>
		<input type="hidden" name="winPopSw"/>
		<input type="hidden" name="custIP" value=<%=custIP%>>
		<input type="hidden" name="Url" value=<%=Url%>>
	    <input type="hidden" id="rsaPublicKeyModulus" value="${publicKeyModulus}">
		<input type="hidden" id="rsaPublicKeyExponent" value="${publicKeyExponent}">
	</form>
	
	<c:import url="/js/ecams/common/commonscript.jsp" />
	<script type="text/javascript" src="/js/util/jsbn.js"></script>
	<script type="text/javascript" src="/js/util/rsa.js"></script>
	<script type="text/javascript" src="/js/util/prng4.js"></script>
	<script type="text/javascript" src="/js/util/rng.js"></script>
	<script type="text/javascript" src="<c:url value="/js/ecams/login/login.js"/>"></script>
</body>
</html>