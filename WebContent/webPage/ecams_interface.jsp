<!-- 알람메시지 등 외부에서 신청상세 오픈시 호출 
	 http://ip:port/webPage/ecams_interface.jsp?userid=MASER&url=/webPage/winpop/PopRequestDetail.jsp&acptno=202207000007
	 http://scm.jbbank.co.kr:4040/webPage/ecams_interface.jsp?userid=211077&url=/webPage/approval/ApprovalStatus.jsp
-->
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<%@ page language = "java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import = "com.ecams.common.base.StringHelper"%>
<%@ page import = "com.ecams.service.list.LoginManager"%>
<%@ page import = "app.common.UserInfo"%>
<%@ page import = "java.io.*"%>

<%
	String pUserId = StringHelper.evl(request.getParameter("UserID"),"");
	String pAcptNo = StringHelper.evl(request.getParameter("acptno"),"");
	String pURL = StringHelper.evl(request.getParameter("url"),"");
	
	if(pUserId == null || "".equals(pUserId)) {
%>
	<script>
		alert('사원번호를 정확하게 입력하여 주시기 바랍니다.');
		window.open('about:blank','_self').self.close();
	</script>
<%-- <%
	}else if(pAcptNo == null || "".equals(pAcptNo)) {
%>
	<script>
		alert('신청번호를 정확하게 입력하여 주시기 바랍니다.');
		window.open('about:blank','_self').self.close();
	</script> --%>
<%
	}else if(pURL == null || "".equals(pURL)) {
%>
	<script>
		alert('URL을 정확하게 입력하여 주시기 바랍니다.');
		window.open('about:blank','_self').self.close();
	</script>
<%
	}
%>

<form name='getReqData'>
	<input type="hidden" name="user" value="<%=pUserId%>"/>
	<input type="hidden" name="acptno" value="<%=pAcptNo%>"/>
	<input type="hidden" name="url" value="<%=pURL%>"/>
	<input type="hidden" name="rgtList"/>
</form>

<!-- form 아래에 있어야하고, tokenCheck 함수 위에 있어야 함 -->
<c:import url="/js/ecams/common/commonscript.jsp"/>
	
<html>
	<title>eCAMS</title>

	<script type="text/javascript">
		console.log('getReqData',getReqData);
		console.log('url value=',getReqData.url.value);
		
		if(getReqData.url.value != null && getReqData.url.value != '') {
			goeCAMS();
		}
		
		function goeCAMS() {
			var userId = getReqData.user.value;
			var homePath = window.location.protocol+'//'+window.location.hostname + ':' + window.location.port;
			
			//token 생성
			var requestJson = JSON.stringify( {user:getReqData.user.value, requestType:'CREATE_TOKEN'} );
			
			var ajax = $.ajax({
				type 	: 'POST',
				url 	: homePath+'/webPage/tokenCheckServlet',
				data 	: requestJson,
				dataType: 'json',
				contentType: "application/json; charset=UTF-8",
				headers: {"Authorization": 'Bearer '+'' },
				async 	: false,
				success : function(data) {
					var obj2 = JSON.parse(requestJson);
					if (null != data.token && '' != data.token && undefined != data.token) {
						sessionStorage.removeItem('access_token');
			        	sessionStorage.setItem("access_token", data.token);
			        	sessionStorage.removeItem('id');
					  	sessionStorage.setItem('id', getReqData.user.value);
			        	goPopDetail();
					} else {
						dialog.alert('token 생성 실패')
					}
				},
				error : function(req, stat, error) {
					console.log(req, stat, error);
				}
			});
			
			ajax = null;
		}
		
		function goPopDetail() {
			document.getReqData.action = getReqData.url.value;
	 		document.getReqData.method = "post";
	 		document.getReqData.submit();
		}
	</script>
</html>