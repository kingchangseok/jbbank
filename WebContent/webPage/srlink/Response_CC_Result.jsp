<%@ page language = "java" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ page import = "java.io.InputStream"%>
<%@ page import = "com.ecams.common.base.StringHelper"%>
<%@ page import = "com.ecams.common.logger.EcamsLogger"%>
<%@ page import = "com.ecams.service.srlink.SRRestApi"%>
<%@ page import = "org.apache.logging.log4j.Logger"%>

<%
    Logger ecamsLogger = EcamsLogger.getLoggerInstance();
    ecamsLogger.error("Response_CC_Result.jsp Start");
    request.setCharacterEncoding("UTF-8");
	String acptNo = StringHelper.evl(request.getParameter("acptno"),"");
    String retMsg = "";
    
    if (acptNo.length()!=12) {
    	ecamsLogger.error("Input Data Fail ["+acptNo+"]");
    	retMsg = "ER:Input Data Fail ["+acptNo+"]";
    } else {
	    SRRestApi srrestapi = new SRRestApi();
	    ecamsLogger.error("Response_CC_Result.jsp ["+acptNo+"]");
	    retMsg = srrestapi.callSRResult(acptNo);
	    srrestapi = null;
    }
    ecamsLogger.error("Response_CC_Result.jsp Return="+retMsg);
    ecamsLogger = null;
    
    out.println(retMsg);
%>