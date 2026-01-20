<%@ include file="config.jsp" %>
<%@ page import = "com.ecams.service.list.LoginManager"%>

<%
        LoginManager loginManager = LoginManager.getInstance();
%>


<%
  // 변수 초기화 
	String sso_id = null;
	String uurl = null;
	String system_id = null;
	String ext_info = null;
	String info_mail = null;
	String retCode = "0";

	// 암호화 관련(쿠키)
	com.initech.eam.nls.CookieManager.setEncStatus(true);

	// 1. SSO 인증정보 조회
	sso_id = getSsoId(request);
	
		System.out.println("##############################################");
		System.out.println("retCode3 : "+sso_id);
		System.out.println("##############################################");
	// 2. UURL 수신
	uurl = request.getParameter("UURL");

		System.out.println("##############################################");
		System.out.println("retCode4 : "+uurl);
		System.out.println("##############################################");
  //로그인 하지 않았고,
	if (sso_id == null) {

    //uurl 이 없다면, 근데 여기서 uurl 은 뭘 체크하는거지? uurl 에 test.jbbank.co.kr:7080/se/login_exec.jsp 를 넣는구나. 
		if (uurl == null)	uurl = ASCP_URL;

		// 3. SSO 인증 정보가 없으면 통합 로그인 페이지로 이동
		goLoginPage(response, uurl);
		return;
	} else {

		System.out.println("##############################################");
		System.out.println("retCode5 : "+retCode);
		System.out.println("##############################################");
		// 3. 인증 정보에 대한 유효성 검증을 수행한다.
		retCode = getEamSessionCheck(request,response);
		//retCode = "0";
		System.out.println("##############################################");
		System.out.println("retCode6 : "+retCode);
		System.out.println("##############################################");

		//4. 인증토큰이 유효여부에 따른 처리
		/*
		 * Nexess Cookie 자동갱신
		 * retCode = 0 : 정상적 로그인 절차에 따른 리턴값, 그외는 오류코드
		 * retCode = 1000 : 꼭 필요한 쿠키중2011-12-19 일부가 없을때
		 * retCode = 1001 : 세션 타임 초과
		 * retCode = 1002 : 쿠키값들은 다 존재하나 넥세스가 정상적으로 발급한 쿠기가 아닐때
		*/
		System.out.println("##############################################");
		System.out.println("retCode : "+retCode);
		System.out.println("##############################################");
		if(!retCode.equals("0")){

			com.initech.eam.nls.CookieManager.removeNexessCookie(SSO_DOMAIN, response);
			goErrorPage(response, Integer.parseInt(retCode));
			return;
		}else{
			System.out.println("SSO_ID : "+sso_id);
			System.out.println("APP_AUTH_URL : "+APP_AUTH_URL);
			
			session.setAttribute("SSO_ID", sso_id);
			session.setAttribute("strUsr_UserId", sso_id);

			session.setAttribute("strUsr_UserName", loginManager.getUserName(sso_id));
			
			
			// 외부계정 조회 (serviceName default)
			// system_id = getSystemAccount(sso_id);
			 // 확장 정보 조회
			// ext_info = getUserExField(sso_id, "EXTINFO");

			// 메일 정보 조회
			// info_mail = getUserInfoEamil(sso_id);
			
			response.sendRedirect(APP_AUTH_URL);
		}
	}

%>
  

