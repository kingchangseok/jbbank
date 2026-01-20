
/******************************************************************************
   ?”„ë¡œì ?Š¸ëª?    : ?˜•?ƒê´?ë¦? ?‹œ?Š¤?…œ
   ?„œë¸Œì‹œ?Š¤?…œëª?  : String ê´?? ¨ UTIL CLASS
   ?ŒŒ?¼ëª?       : StringHelper.java      
   ?ˆ˜? •?‚´?—­
   ?ˆ˜? •?¼         ?‹´?‹¹?       ?ˆ˜? •?‚´?š©
-------------------------------------------------------------------------------
  2006. 08. 08.  TEOK.KANG     ìµœì´ˆ?ƒ?„±
******************************************************************************/


package com.ecams.util;

import java.sql.ResultSet;

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
	 * ë¬¸ì?—´?˜ ê³µë°±?„ ? œê±°í•˜ê³? ë°˜í™˜?•œ?‹¤.
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
	
	//ResultSet getString() nullì²´í¬ -- ? ?™?˜™? ?™?˜™? ï¿? ? ?™?˜™? ï¿?
	public static String rsGetString(ResultSet rst, String columnName) throws Exception {
		if(rst.getString(columnName) != null) return rst.getString(columnName);
		else return "";
	}
	
	public static String replaceString(String str) {
		String chgStr = "";
		if(str != null && !"".equals(str)) {
			chgStr = str;
			chgStr = chgStr.replaceAll("/", "");
			chgStr = chgStr.replaceAll("\\", "");
			chgStr = chgStr.replaceAll(".", "");
			chgStr = chgStr.replaceAll("&", "");
		}
		return chgStr;
	}
	
	//IntegerOverflow? ?™?˜™? ?™?˜™? ï¿? ? ?™?˜™? ï¿?
	public static int overflowCheck(String str) throws Exception {
		if(str != null) {			
			int parsing = Integer.parseInt(str);
			if(parsing < 0) throw new Exception("Integer Overflow");
			else return parsing;
		} else {
			throw new Exception("Null Parameter");
		}
	}
	
	public static int overflowCheckSum(int num[]) throws Exception {
		if(num != null) {			
			int sum = 0;
			for (int i = 0; i < num.length; i++) {
				sum += num[i];
			}
			if(sum < 0) throw new Exception("Integer Overflow");
			else return sum;
		} else {
			throw new Exception("Null Parameter");
		}
	}
	
	public static int overflowCheckSub(int num[]) throws Exception {
		if(num != null) {			
			int sum = num[0];
			for (int i = 1; i < num.length; i++) {
				sum -= num[i];
			}
			if(sum > (Integer.MAX_VALUE - 10000)) throw new Exception("Integer Overflow");
			else return sum;
		} else {
			throw new Exception("Null Parameter");
		}
	}
	
	//XSS? ?™?˜™? ?™?˜™? ï¿? ? ?™?˜™? ï¿?
	public static String replaceXSS(String str) {
		if(str != null) {
			 str = str.replaceAll("<","&lt;");
			 str = str.replaceAll(">","&gt;");
		} else {
			str = "";
		}
		return str;
	}
	
}//end of StringHelper class statement