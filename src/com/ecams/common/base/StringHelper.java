
/******************************************************************************
   프로젝트명    : 형상관리 시스템
   서브시스템명  : String 관련 UTIL CLASS
   파일명       : StringHelper.java      
   수정내역
   수정일         담당자       수정내용
-------------------------------------------------------------------------------
  2006. 08. 08.  TEOK.KANG     최초생성
******************************************************************************/


package com.ecams.common.base;


/**
 * @author bigeyes
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StringHelper{
	
	/**
	 * @param param
	 * @param param2
	 * @return
	 */
	public static String nvl(String param, String param2){
	        return param != null ? param : param2;
	}//end of nvl method() statement
	
	/**
	 * @param param
	 * @param param2
	 * @return
	 */
	public static String evl(String param, String param2){
        return param != null && !param.trim().equals("") ? param : param2;
    }//end of evl method() statement
	
	/**
	 * @param s
	 * @return
	 */
	public static String toAsc(String s)
	{
		if(s == null)
			return null;
		try
		{
			return new String(s.getBytes("KSC5601"), "8859_1");
		}
		catch(Exception exception)
		{
			return s;
		}
	}//end of toAsc method() statement
	
	/**
	 * @param s
	 * @return
	 */
	public static String toKsc(String s)
	{
		if(s == null)
			return null;
		try
		{
			return new String(s.getBytes("8859_1"), "KSC5601");
		}
		catch(Exception exception)
		{
			return s;
		}
	}//end of toKsc method() statement
	
	/**
	 * 문자열의 공백을 제거하고 반환한다.
	 * @param s
	 * @return
	 */
	public static String deleteWhiteSpace(String s){
		if (s == null)
			return null;
		int sz                 = s.length();
		StringBuffer strbuffer = new StringBuffer(sz); 
		
		for (int i = 0; i < sz; i++){
			if (!Character.isWhitespace(s.charAt(i))){
				strbuffer.append(s.charAt(i));
			}
		}//end of for-loop statement
		return strbuffer.toString();
	}//end of deleteWhiteSpace() method statement

	
	/**
	 * <PRE>
	 * 1. MethodName	: toFilter
	 * 2. ClassName		: StringHelper
	 * 3. Commnet			: xss 공격에 대한 보안취약점 대비용 filter
	 * 4. 작성자				: Administrator
	 * 5. 작성일				: 2011. 3. 3. 오후 2:14:24
	 * </PRE>
	 * 		@return String
	 * 		@param param : 입력값
	 * 		@param param2 : 출력값
	 * 		@param useFilter : filter 사용여부
	 * 		@return
	 */
	public static String toFilter(String param,String param2,boolean Filter){
		if (!Filter){
			if (param2 == null) param2 = "";
			return param != null && !param.trim().equals("") ? param : param2;
		}
		if (param == null || param.length()<1) return null;
		int paramLen           = param.length();
		StringBuffer strbuffer = new StringBuffer(paramLen);
		
		//System.out.println("param : " + param);
		for (int i = 0; i < paramLen; i++){
			//System.out.println("["+i+"] charAt : " + param.charAt(i));
			if (param.charAt(i) == '\'') {
				strbuffer.append("");
				//System.out.println("1");
            } else if (param.charAt(i) == '\"') {
            	strbuffer.append("");
            	//System.out.println("2");
            } else if (param.charAt(i) == '<') {
            	strbuffer.append("&lt;");
            	//System.out.println("3");
            } else if (param.charAt(i) == '>') {
            	strbuffer.append("&gt;");
            	//System.out.println("4");
            } else if (param.charAt(i) == '&') {
            	strbuffer.append("&amp;");
            	//System.out.println("5");
            } else  if (param.charAt(i) == '(') {
            	strbuffer.append("&#40;");
            	//System.out.println("6");
            } else if (param.charAt(i) == ')') {
            	strbuffer.append("&#41;");
            	//System.out.println("7");
            } else {
            	strbuffer.append(param.charAt(i));
            	//System.out.println("8");
            } 
		}//end of for-loop statement
		//System.out.println("strbuffer.toString() : " + strbuffer.toString());
		
        return strbuffer.toString();
		
	}//end of toFilter() method statement
	
}//end of StringHelper class statement