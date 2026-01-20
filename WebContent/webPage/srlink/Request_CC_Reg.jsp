<%@ page language = "java" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ page import = "java.io.InputStream"%>
<%@ page import = "com.ecams.common.base.StringHelper"%>
<%@ page import = "com.ecams.common.logger.EcamsLogger"%>
<%@ page import = "com.ecams.service.srlink.SRRestApi"%>
<%@ page import = "org.apache.logging.log4j.Logger"%>

<%
    Logger ecamsLogger = EcamsLogger.getLoggerInstance();
    ecamsLogger.error("Request_CC_Reg.jsp Start");
    request.setCharacterEncoding("UTF-8");
    InputStream in = request.getInputStream();
    String retMsg = "";
    
    SRRestApi srrestapi = new SRRestApi();

    ecamsLogger.error("Request_CC_Reg.jsp 1");
    String inData = srrestapi.InputConvStr(in);
    ecamsLogger.error("Request_CC_Reg.jsp ["+inData+"]");
    if (inData.length()>2 && !"ER".equals(inData.substring(0,2))) {
    	retMsg = srrestapi.setSRInfo(inData);
    } else {
    	retMsg = srrestapi.setError(retMsg);
    }
    srrestapi = null;
    
    ecamsLogger.error("Request_CC_Reg.jsp Return="+retMsg);
    ecamsLogger = null;
    
    boolean isError = (retMsg !=null && retMsg.indexOf("\"ResultCD\":\"FAIL\"") > -1);
    response.setStatus(isError ? 400:200);
    
    out.println(retMsg);
    srrestapi = null;    
%>   