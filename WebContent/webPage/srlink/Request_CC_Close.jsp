<%@ page language = "java" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ page import = "java.io.InputStream"%>
<%@ page import = "com.ecams.common.base.StringHelper"%>
<%@ page import = "com.ecams.common.logger.EcamsLogger"%>
<%@ page import = "com.ecams.service.srlink.SRRestApi"%>
<%@ page import = "org.apache.logging.log4j.Logger"%>

<%
    Logger ecamsLogger = EcamsLogger.getLoggerInstance();
    ecamsLogger.error("Request_CC_Close.jsp Start");
    request.setCharacterEncoding("UTF-8");
    InputStream in = request.getInputStream();
    String retMsg = "";
    
    SRRestApi srrestapi = new SRRestApi();
    String inData = srrestapi.InputConvStr(in);
    ecamsLogger.error("Request_CC_Close.jsp ["+inData+"]");
    if (!"ER".equals(inData.substring(0,2))) {
    	retMsg = srrestapi.setSRClose(inData);
    } else {
    	retMsg = srrestapi.setError(retMsg);
    }
    srrestapi = null;
    
    ecamsLogger.error("Request_CC_Close.jsp Return="+retMsg);
    ecamsLogger = null;
    
    out.println(retMsg);
%>  