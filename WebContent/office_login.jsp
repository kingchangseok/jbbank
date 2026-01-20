<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<%@ page language="java" contentType="text/html;charset=euc-kr" pageEncoding="euc-kr" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page import = "com.ecams.common.base.StringHelper"%>
<%@ page import = "com.ecams.service.list.RC4"%>

<c:import url="/js/ecams/common/commonscript.jsp"/>

<%
	String custIP = request.getRemoteAddr();
	String Url = request.getRequestURL().toString();
	String strUsr_UserId  = StringHelper.evl(request.getParameter("strUserId"),"");
	
	if(strUsr_UserId.length() == 6){
%>
        <script>
        window.open('about:blank', '_self').close();
        </script>
<%
	} 	

	RC4 rc4 = new RC4();
	String decId = rc4.decryptRC4String("jbbank",strUsr_UserId);
	if (decId.length() >= 8){ 
		strUsr_UserId = decId.substring(8);
	} 
	
	String urlReferer = request.getHeader("REFERER");
	String ssoYN = "N";
  	System.out.println("urlReferer : " + urlReferer);
  	System.out.println("strUsr_UserId : " + strUsr_UserId);
 	
	urlReferer = "uc.jbbank.co.kr";
 	
	System.out.println("urlReferer : " + urlReferer);
	System.out.println("strUsr_UserId : " + strUsr_UserId);
 	
	if (urlReferer == null || urlReferer == ""){
%>
        <script>
            alert('접근 정보가 없습니다. 관리자에게 문의해주세요.');
            window.close();
        </script>
<%
	} else if (strUsr_UserId == null || strUsr_UserId == "") {
%>
        <script>
            alert('사용자정보가 없습니다. 관리자에게 문의해주세요.');
            window.close();
        </script>
<% 
	}else{
		if (urlReferer.indexOf("uc.jbbank.co.kr")>-1){
            ssoYN = "Y";
		}else{
%>
           <script>
	          alert('접근이 제한되였습니다.');
              window.close();
           </script>
<%
		}
	}
  	System.out.println("ssoYN : " + ssoYN);
  	
  	if("Y".equals(ssoYN)) {
%>

<html>
	<title>▒▒ JBBANK 형상관리시스템 ▒▒</title>
	<form name='ssoForm' method='post'>
		<input type=hidden name="userId" value=<%=strUsr_UserId%>>
		<input type="hidden" name="custIP" value=<%=custIP%>>
		<input type="hidden" name="url" value=<%=Url%>>
	</form>
	
	<script type="text/javascript">
		var ipAddr = ssoForm.custIP.value;
		var url = ssoForm.url.value;
		var userId = ssoForm.userId.value;
		var ajaxReturnData = null;
						
		//ISVALIDLOGIN 함수 호출 시 token 생성(common.js)
		var userInfo = {
			userId		: userId,
		  	gnb			: "Real",
			userPwd		: "SSO",
		  	ipAddr		: ipAddr,
		  	url 		: url,
		  	sso			: true,
		  	requestType	: 'ISVALIDLOGIN'
	  	}
		console.log('userInfo',userInfo);
	  	ajaxReturnData = ajaxCallWithJson('/webPage/login/Login', userInfo, 'json');
		
	  	if(typeof ajaxReturnData === 'string') {
	    	if (ajaxReturnData.indexOf('ENCERROR')>-1) {
	    		dialog.alert('비정상접근입니다. 다시 로그인 하시기 바랍니다. [abnormal approach. Please log-in again]', function() {
	    			return;	
	    		});
		      }
		    
		    if (ajaxReturnData == 'ERR') {
		    	dialog.alert('오류발생. [Login Fail]', function() {
		    		return;	
		    	});
		    }
	    }
		
		var authCode = ajaxReturnData.substr(0,1);
		var erMsg = '';
	    if (ajaxReturnData.length > 1) erMsg = ajaxReturnData.substr(1);
	    
	    console.log('authCode='+authCode+', erMsg='+erMsg);
	    /* if (authCode == '9') {
	    	if(confirm(erMsg)) { //이미 로그인되어 사용하고 있는 아이디
	    		loginNext();
			} 
	    }else{
	    	loginNext();
	    } */
	    
	    if (authCode == '0' || authCode == '9') {
	    	loginNext();
	    }
	    
	    function loginNext() {
	    	//Storage에 id먼저 넣어줘야 updateLoginIp 수행 가능 (common.js에서 tokencheck할 때 id없으면 clear시킴)
	    	sessionStorage.removeItem('id');
		    sessionStorage.setItem('id', userId);
		    
			var ret = updateLoginIp();
	    	
		  	document.ssoForm.action = '/webPage/main/eCAMSBase.jsp';
	 		document.ssoForm.method = "post";
	 		document.ssoForm.submit();
	    }
	    
	    function updateLoginIp() {
	    	userInfo = {
				userId		: userId,
				IpAddr		: ipAddr,
				Url 		: url, 
				requestType	: 'UPDATELOGINIP'
		  	}
			var ret = ajaxCallWithJson('/webPage/login/Login', userInfo);
	    }
	</script>
	<%
		}
	%>
</html>