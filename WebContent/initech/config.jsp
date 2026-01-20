<%@ page
	import="sun.misc.*,
			java.util.*,
			java.math.*,
			java.net.*,
			com.initech.eam.nls.*,
			com.initech.eam.api.*,
			com.initech.eam.base.*,
			com.initech.eam.nls.command.*,
			com.initech.eam.smartenforcer.*,
			examples.api.*"
%>

<%!
	/************************************************************************
	* 업무시스템 설정 사항 (업무 환경에 맞게 변경)
	*************************************************************************/
	// 아래의 이름에 해당하는 어플리케이션이 SSO에 자원으로 등록되어 있어야 함
	private String SERVICE_NAME = "SCM";
	// MyOffice 접속 URL
	//private String SERVER_URL = "http://scm.jbbank.co.kr";
	private String SERVER_URL = "http://scm.jbbank.co.kr";
	// MyOffice 접속 포트
	private String SERVER_PORT = "4040";
	// ASCP (MyOffice에 접근 시 SSO 인증 여부 체크 및 세션 처리
	private String ASCP_URL
		= SERVER_URL + ":" + SERVER_PORT + "/initech/login_exec.jsp";
	// 업무시스템별 인증페이지 주소 설정필요
	//String APP_AUTH_URL = SERVER_URL + ":" + SERVER_PORT + "/ecams_win_flex/main_frameset.jsp";
	String APP_AUTH_URL = SERVER_URL + ":" + SERVER_PORT + "/webPage/login/sso_login_html.jsp";
	//기본값 유지
	private String[] SKIP_URL
		= {"", "/", "/index.html", "/index.htm", "/index.jsp", "/index.asp"};

	/************************************************************************
	* SSO/EAM 환경 설정 사항 (변경사항 없음)
	*************************************************************************/
	// SSO 통합 로그인페이지 접속 URL
	private String NLS_URL = "http://sso.jbbank.co.kr";
	// SSO 통합 로그인페이지 접속 포트
	private String NLS_PORT = "7290";
	// SSO 통합 로그인을 위해 NLS로 redirect할 전체 URL (fail-over는 L4 기준)
	private String NLS_LOGIN_URL
		= NLS_URL + ":" + NLS_PORT + "/nls3/clientLogin.jsp";
		//= NLS_URL + ":" + NLS_PORT + "/nls3/cookieLogin.jsp";
	// SSO 통합 로그아웃 URL
	private String NLS_LOGOUT_URL
		//= NLS_URL + ":" + NLS_PORT + "/nls3/logout.jsp";
		//= NLS_URL + ":" + NLS_PORT + "/nls3/NCLogout.jsp";
		= NLS_URL + ":" + NLS_PORT + "/nls3/cookieLogout.jsp";
	// SSO 에러 페이지 URL
	private String NLS_ERROR_URL
		= NLS_URL + ":" + NLS_PORT + "/nls3/error.jsp";
	// Nexess API가 사용할 Nexess Daemon의 주소
	//private String ND_URL = NLS_URL + ":5480";
	private String ND_URL = "http://sso.jbbank.co.kr:5480";
	//private String ND_URL1 = "http://nd1.initech.com:5480";
	//private String ND_URL2 = "http://nd2.initech.com:5480";
	// 인증 타입 (ID/PW 방식 : 1)
	private String TOA = "1";
	// 도메인 base (.nhicin.or.kr)
	private String SSO_DOMAIN = ".jbbank.co.kr";
%>

<%!
	// EAM 데몬 접속
	public NXContext getContext()
	{
		NXContext context = null;
		try {
			List serverurlList = new ArrayList();
			serverurlList.add(ND_URL);
			context = new NXContext(serverurlList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return context;
	}

	// 통합 SSO 로그인페이지 이동
	public void goLoginPage(HttpServletResponse response, String uurl)
	throws Exception {
		com.initech.eam.nls.CookieManager.addCookie(SECode.USER_URL, uurl, SSO_DOMAIN, response);
		com.initech.eam.nls.CookieManager.addCookie(SECode.R_TOA, TOA, SSO_DOMAIN, response);
		response.sendRedirect(NLS_LOGIN_URL);
	}

	// SSO 에러페이지 URL
	public void goErrorPage(HttpServletResponse response, int error_code)
	throws Exception {
		com.initech.eam.nls.CookieManager.removeNexessCookie(SSO_DOMAIN, response);
		response.sendRedirect(NLS_ERROR_URL + "?errorCode=" + error_code);
	}

	// 통합 SSO ID 조회
	public String getSsoId(HttpServletRequest request) {
		String sso_id = null;

	System.out.println(">>>>>>>>>>>" + SECode.USER_ID);

		sso_id = com.initech.eam.nls.CookieManager.getCookieValue(SECode.USER_ID, request);
		return sso_id;
	}

	public String checkUurl(String uurl) {
		String uri = null;
		URL url = null;

		try {
			url = new URL(uurl);
			uri = url.getPath();
		} catch (Exception e) {
			// URI 인 경우
			uri = uurl;
		}

		for (int i = 0; i < SKIP_URL.length; i++) {
			if (SKIP_URL[i].equals(uri)) {
				uurl = null;
				break;
			}
		}

		return uurl;
	}

	/**
	 * SE 가 있을 경우에만 사용 가능 (즉 사용할 일이 없음)
	 */
	public int checkSsoId(HttpServletRequest request,
	HttpServletResponse response) throws Exception {
		int return_code = 0;

		return_code = com.initech.eam.nls.CookieManager.readNexessCookie(request, response,
			SSO_DOMAIN);
		return return_code;
	}

	public String getSystemAccount(String sso_id) {
		NXContext context = null;
		NXUserAPI userAPI = null;
		String retValue = null;

		try {
			context = getContext();
			userAPI = new NXUserAPI(context);
			System.out.println("##############################################");
			System.out.println("sso_id 2: "+sso_id);
			System.out.println("SERVICE_NAME : "+SERVICE_NAME);
			System.out.println("##############################################");
			NXAccount account = userAPI.getUserAccount(sso_id, SERVICE_NAME);
			System.out.println(account.toString());
			retValue = account.getAccountName();
			System.out.println("retValue = " + retValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retValue;
	}

	public String getSsoDomain(HttpServletRequest request) throws Exception {
		String sso_domain = null;

		sso_domain = NLSHelper.getCookieDomain(request);
		return sso_domain;
	}

	// 통합인증 세션을 체크 하기 위하여 사용되는 API
	public String getEamSessionCheck(HttpServletRequest request,HttpServletResponse response)
	{
		String retCode = "0";
		NXContext context = null;
		try {
			context = getContext();
			NXNLSAPI nxNLSAPI = new NXNLSAPI(context);
			retCode = nxNLSAPI.readNexessCookie(request, response, 0, 0);
		} catch(Exception npe) {
			npe.printStackTrace();
		}
		return retCode;
	}

	public Properties getUserInfo(String userid)
	throws Exception {
		NXContext context = null;
		context = getContext();

		NXUserAPI userAPI = new NXUserAPI(context);
		Properties prop = null;

		if (userid==null || userid.length() <= 0)
			return prop;

		try {
			NXUserInfo userInfo = userAPI.getUserInfo(userid);
			prop = new Properties();

			prop.setProperty("USERID", userInfo.getUserId());
			prop.setProperty("EMAIL", userInfo.getEmail());
			prop.setProperty("ENABLE", String.valueOf(userInfo.getEnable()));
			prop.setProperty("STARTVALID", userInfo.getStartValid());
			prop.setProperty("ENDVALID", userInfo.getEndValid());
			prop.setProperty("NAME", userInfo.getName());
			prop.setProperty("LASTPASSWDCHANGE", userInfo.getLastpasswdchange());
			prop.setProperty("LastLoginIP", userInfo.getLastLoginIp());
			prop.setProperty("LastLoginTime", userInfo.getLastLoginTime());
			prop.setProperty("LastLoginAuthLevel",	userInfo.getLastLoginAuthLevel());
		} catch (EmptyResultException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prop;
	}

	public String getUserInfoEamil(String userid)
	throws Exception {
		NXContext context = null;
		context = getContext();

		NXUserAPI userAPI = new NXUserAPI(context);
		String rtn = null;

		if (userid==null || userid.length() <= 0)
			return rtn;

		try {
			NXUserInfo userInfo = userAPI.getUserInfo(userid);
			rtn =  userInfo.getEmail();
		} catch (EmptyResultException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}

	/**
	 * SSO ID 가 존재하는지 검사
	 * 1. true : 존재
	 * 2. false : 존재안함
	 * 3. Exception : network
	 */
	public boolean existUser(String userid)
	throws Exception {
		NXContext context = getContext();
		NXUserAPI userAPI = new NXUserAPI(context);
		boolean returnFlag = false;

		try {
			returnFlag= userAPI.existUser(userid);
		} catch (EmptyResultException e) {
		} catch (Exception e) {
			throw e;
		}
		//System.out.println("existUser:[" + userid + ":" + returnFlag + "]");
		return returnFlag;
	}

	// 사용자의 외부계정(시스템 계정) 등록 API
	public boolean addAccountToUser(String userid, String serviceName,
	String accountName, String accountPasswd)
	throws Exception {
		NXContext context = getContext();
		NXUserAPI userAPI = new NXUserAPI(context);
		NXExternalFieldSet nxefs = null;
		boolean returnFlag = false;

		try {
			userAPI.addAccountToUser(userid, serviceName,
				accountName, accountPasswd);
			returnFlag = true;
		} catch (EmptyResultException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return returnFlag;
	}

	// 사용자의 외부계정(시스템 계정) 삭제 API
	public boolean removeAccountFromUser(String userid, String serviceName)
	throws Exception {
		NXContext context = getContext();
		NXUserAPI userAPI = new NXUserAPI(context);
		boolean returnFlag = false;

		try {
			userAPI.removeAccountFromUser(userid, serviceName);
			returnFlag = true;
		} catch (EmptyResultException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return returnFlag;
	}

	/**
	 * 사용자의 특정 확장 필드 정보 조회
	 * return : String
	 * 사용자가 존재하지 않거나 확장필드가 없다면 return null
	 */
	public String getUserExField(String userid, String exName)
	throws Exception {

		NXContext context = null;
		context = getContext();
		NXUserAPI userAPI = new NXUserAPI(context);
		NXExternalField nxef = null;
		String returnValue = null;

		if (userid==null || userid.length() <= 0)
			return returnValue;

		try {
			nxef = userAPI.getUserExternalField(userid, exName);
			returnValue = (String) nxef.getValue();

		} catch (EmptyResultException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		/*
		System.out.println("getUserExField:[" + userid + ":"
			+ exName + ":" + returnValue+ "]");
		*/
		return returnValue;
	}

	/**
	 * 사용자가 존재하지 않다면 return null
	 * 속성을 갖지 않는다면 IllegalArgumentException
	 * multi value 는 없음
	 */
	public String getUserAttribute(String userid, String attrName)
	throws Exception {
		NXContext context = null;
		context = getContext();
		NXUserAPI userAPI = new NXUserAPI(context);
		String attrValue = null;
		List valueList = null;
		NXAttributeValue nxav = null;

		if (userid==null || userid.length() <= 0)
			return attrValue;

		try {
			valueList = userAPI.getUserAttributes(userid, attrName);
			for (int i = 0, j = valueList.size(); i < j; i++) {
				nxav = (NXAttributeValue) valueList.get(i);
				attrValue = nxav.toString();
			}
		} catch (EmptyResultException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attrValue;
	}

	/*
	* 특정 Application의 권한이 있는 자원 목록 조회
	*/
	public ArrayList getUserResourceList2(String userid, String serviceName, String searchPath, int searchLevel)
	throws Exception {

		com.initech.eam.api.NXApplication app = null;
		NXTreeModel tm = null;
		NXTreeModel tmSortedByCode = null;
	
		NXTreeNode root = null;
	    NXResource reso = null;
	    NXResourcePath codePath = null;
	    NXResourcePath descriptionPath = null;
	
		NXTreeNode root1 = null;
	    NXResource reso1 = null;
	    NXResourcePath codePath1 = null;
	    NXResourcePath descriptionPath1 = null;
	
		NXTreeNode root2 = null;
	    NXResource reso2 = null;
	    NXResourcePath codePath2 = null;
	    NXResourcePath descriptionPath2 = null;
	
	    NXTreeNode root3 = null;
	    NXResource reso3 = null;
	    NXResourcePath codePath3 = null;
	    NXResourcePath descriptionPath3 = null;
		NXAllowedActionValueSet allowedActionValue3 = null;
	
	    NXTreeNode root4 = null;
	    NXResource reso4 = null;
	    NXResourcePath codePath4 = null;
	    NXResourcePath descriptionPath4 = null;
		NXAllowedActionValueSet allowedActionValue4 = null;
	
	    NXTreeNode root5 = null;
	    NXResource reso5 = null;
	    NXResourcePath codePath5 = null;
	    NXResourcePath descriptionPath5 = null;
		NXAllowedActionValueSet allowedActionValue5 = null;

		String strActionList = null;
		String strActionValue = null;

		boolean isLeaf = false;
		java.util.Iterator actionValue;
		ArrayList menuList = new ArrayList();
	
		try {
			// SSO 데몬과 Connection
			NXContext context = null;
			context = getContext();
			NXProfile eh = new NXProfile(context);

			/*
			* 권한조회 대상시스템에 대한 사용자의 권한데이터 조회
			* getNXApplication(사용자아이디, 대상시스템의 BASE URL, 조회 대상 메뉴 PATH, 조회 레벨);
			* 예시) NHIC시스템에 대해 최상위Depth에서 3레벨까지 조회 시
			* 	- getNXApplication("001117", "NHIC", "/", 3);
			* 예시) NHIC시스템에 대해 /0101 Depth에서 1레벨까지 조회 시
			* 	- getNXApplication("001117", "NHIC", "/0101", 1);
			*/
			app = eh.getNXApplication(userid, serviceName, searchPath, searchLevel);
	
			NXResourceSet resourceSet = (NXResourceSet)app.getNXResourceSetByType("General");
	
			// 자원을 트리형태로 변환
			tm = app.toTreeModel();
			tmSortedByCode = tm.sort(new DefaultResourceComparator(
										DefaultResourceComparator.CODE));
	
			root = (NXTreeNode) tmSortedByCode.getRoot();
	
	        for (Enumeration e = root.children() ; e.hasMoreElements() ;) {
	            root1 = (NXTreeNode)e.nextElement();
	            for(Enumeration e1 = root1.children() ; e1.hasMoreElements() ;) {
	                root2 = (NXTreeNode)e1.nextElement();

	                reso1 = (NXResource)(root2.getUserObject());
					// 조회 결과에 대한 최상위 Depth의 코드값
	                codePath1 = reso1.getCodePath();
	                // 조회 결과에 대한 최상위 Depth의 Description값(메뉴명)
	                descriptionPath1 = reso1.getDescriptionPath();

	                for(Enumeration e2 = root2.children() ; e2.hasMoreElements() ;) {
	                    root3 = (NXTreeNode)e2.nextElement();
	
	                    reso2 = (NXResource)(root3.getUserObject());
	                    codePath2 = reso2.getCodePath();
	                    descriptionPath2 = reso2.getDescriptionPath();
	
	                    for(Enumeration e3 = root3.children() ; e3.hasMoreElements() ;) {
	                    	root4 = (NXTreeNode)e3.nextElement();
	
							//reso3 = (NXResource)(((NXTreeNode)e3.nextElement()).getUserObject());
							reso3 = (NXResource)(root4.getUserObject());
	                        codePath3 = reso3.getCodePath();
	                        descriptionPath3 = reso3.getDescriptionPath();

							// 해당 자원(메뉴)가 최하위 Leaf 노드의 자원인지 확인
							isLeaf = root4.isLeaf();
	
							if ( isLeaf ) {
								// Leaf 노드의 자원인 경우 arrayList에 담는다.
								menuList.add(codePath3.getSelfValue()+"|"+descriptionPath3.getSelfValue());
							} else {
			                    for(Enumeration e4 = root4.children() ; e4.hasMoreElements() ;) {
			                    	root5 = (NXTreeNode)e4.nextElement();
	
									//reso4 = (NXResource)(((NXTreeNode)e4.nextElement()).getUserObject());
									reso4 = (NXResource)(root5.getUserObject());
			                        codePath4 = reso4.getCodePath();
			                        descriptionPath4 = reso4.getDescriptionPath();
	
									isLeaf = root5.isLeaf();

									if ( isLeaf ) {
										// Leaf 노드의 자원인 경우 arrayList에 담는다.
										menuList.add(codePath4.getSelfValue()+"|"+descriptionPath4.getSelfValue());
									}
								}
							}
	                    }
	                }
	            }
	        }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return menuList;
		}
	}

	/**
	 * 권한이 있는 자원 목록 조회
	 * 
	 * @param userId 사용자ID
	 * @param applicationBaseID 어플리케이션 베이스ID
	 * @param searchPath 자원
	 * @param searchLevel 조회레벨(-1=하위모든자원, 0=자기자신만, 1=차하위자원만)
	 * @return List[ Vector[ String[레벨], String[자원코드], String[자원명], String[권한정보] ] ]
	 * @throws Exception
	 */
	 
	/*
	* 특정 Application의 권한이 있는 자원 목록 조회
	*/
	public ArrayList getUserResourceList(String userId, String applicationBaseID, String searchPath, int searchLevel) throws Exception {
	//public ArrayList getUserResourceList(String userId, String applicationBaseID, String searchPath, int searchLevel) throws Exception {
		
		ArrayList resourceList = null;

		//NXProfile nxProfile = new NXProfile();
		NXProfile nxProfile = new NXProfile(getContext());
		// NXProfile(getContext());
		
		// -- 자원조회
		NXApplication nxApplication = null;		
		try {
			nxApplication = nxProfile.getNXApplication(userId, applicationBaseID, searchPath, searchLevel);
		} catch ( Exception e ) {
			e.printStackTrace();
		}		

		// -- 자원정보 리스트 생성
		if(nxApplication != null && nxApplication.size() > 0){
			
			resourceList = new ArrayList<Vector<String>>();
			
			Iterator<NXResourceSet> nxApplicationIter = nxApplication.iterator();
			while (nxApplicationIter.hasNext()) {
				
				NXResourceSet nxResourceSet = nxApplicationIter.next();
				
				Iterator<NXResource> nxResourceSetIter = nxResourceSet.iterator();				
				while(nxResourceSetIter.hasNext() ) {
					
					NXResource nxResource = nxResourceSetIter.next();
					
					// -- 자기자신은 제외한다.
					String codePath = nxResource.getCodePath().getSelfPath();
					if(searchLevel != 0 && codePath.equals(searchPath))
						continue;
					
					// -- 자원정보 벡터
					Vector<String> v = new Vector<String>();
					v.add(Integer.toString(nxResource.getCodePath().getDepth()));
					v.add(codePath);
					v.add(nxResource.getDescriptionPath().getSelfValue());
					
					String actionValues = "";
					Iterator<String> iter = nxResource.getAllowedActionValues().iterator();
					while(iter.hasNext()){
						actionValues += iter.next();
					}
					v.add(actionValues);
					
					// -- add
					resourceList.add(v);
				}
			}
		}
		
		// 리소스 소팅
		ResourceComparator resourceComparator = new ResourceComparator(ResourceComparator.CODE);
		Collections.sort(resourceList, resourceComparator);
		
		return resourceList;
	}	
	
	/**
	 * 리스소 소팅을 위한 클래스
	 * 
	 * @author jks
	 *
	 */
	 @SuppressWarnings({ "rawtypes"})
	public class ResourceComparator implements Comparator {

		public final static int CODE = 1;
		public final static int DESCRIPTION = 2;	
		
		private int sortRule;
		
		public ResourceComparator(int sortRule) {
			this.sortRule = sortRule;
		}
		
		public int compare(Object o1, Object o2) {
			
			Vector v1 = (Vector) o1;
			Vector v2 = (Vector) o2;

			String v1rs = (String)v1.get(sortRule);
			String v2rs = (String)v2.get(sortRule);
			int i = v1rs.compareTo(v2rs);

			return i;
		}
	} 
	
	
	/**
	 * 사용자가 갖는 역할명 리스트를 리턴한다.
	 * 
	 * @param sso_id 사용자ID
	 * @return List[ RoleName, RoleName,,, ]
	 */
	 @SuppressWarnings({ "rawtypes", "unchecked" })
	public List getRoleList(String sso_id) {
		
		List userRoleList = null;
		
		NXContext context = getContext();
		//NXRole roleAPI = new NXRole();// new NXRole(context);
		NXRole roleAPI =  new NXRole(context);
		try {			
			List tempRoleList = roleAPI.getRoles(sso_id);
			if(tempRoleList != null && tempRoleList.size() > 0){ 
				
				userRoleList = new ArrayList();
				for(int i=0; i<tempRoleList.size(); i++){					
					NXRoleInfo roleInfo = (NXRoleInfo)tempRoleList.get(i);
					userRoleList.add(roleInfo.getRoleName());
				}				
			}
						
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userRoleList;
	}
%>
