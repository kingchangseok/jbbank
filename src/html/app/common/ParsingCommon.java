/**
 * JSON STRING 각종 JAVA OBJECT 형태로 변환
 * <pre>
 * <b>History</b>
 * 	작성자: 이용문
 * 	버전 : 1.0
 *  수정일 : 2019-01-29
 */
package html.app.common;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

public class ParsingCommon {

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public static String getJsonStr(HttpServletRequest req) {
		StringBuffer json = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = req.getReader();
			while((line = reader.readLine()) != null) {
				json.append(line);
			}
		} catch(IOException e1) {
			e1.getStackTrace();
		} catch(Exception e2) {
			e2.getStackTrace();
		}
		return json.toString();
	}

	//취약점 수정 2020.06.10 김대호
	//모든 메서드에서 null을 리턴하지 않도록 수정
	// 허정규 : ERR 말고 "" 으로 넘기도록 수정
	public static String jsonEtoStr(JsonElement jsonElement, String key) {
		if (jsonElement.getAsJsonObject().has(key)) {
			String resultString = jsonElement.getAsJsonObject().get(key).toString();
			if(resultString != null) {
				return resultString;
			} else {
				return "";
			}
		} else {
			return "";
		}
	}

	public static String jsonStrToStr(String str) {
		String resultString = (String) gson.fromJson(str, String.class);
		if(resultString != null) {
			return resultString;
		} else {
			return "";
		}
	}

	public static HashMap<String, String> jsonStrToMap(String str) {
		TypeToken<HashMap<String, String>> typeToken = new TypeToken<HashMap<String, String>>(){};
		HashMap<String, String> mapData 	=  gson.fromJson(str, typeToken.getType());
		if(mapData != null) {
			return mapData;
		} else {
			return new HashMap<String, String>();
		}
	}

	public static HashMap<String, Object> jsonStrToMapObj(String str) {
		TypeToken<HashMap<String, Object>> typeToken = new TypeToken<HashMap<String, Object>>(){};
		HashMap<String, Object> mapData 	=  gson.fromJson(str, typeToken.getType());
		if(mapData != null) {
			return mapData;
		} else {
			return new HashMap<String, Object>();
		}
	}

	public static ArrayList<HashMap<String, String>> jsonArrToArr(String str) {
		TypeToken<ArrayList<HashMap<String, String>>> typeToken = new TypeToken<ArrayList<HashMap<String, String>>>(){};
		ArrayList<HashMap<String, String>> dataList 	=  gson.fromJson(str, typeToken.getType());
		if(dataList != null) {
			return dataList;
		} else {
			return new ArrayList<HashMap<String, String>>();
		}
	}

	public static ArrayList<HashMap<String, Object>> jsonStrToArrObj(String str) {
		TypeToken<ArrayList<HashMap<String, Object>>> typeToken = new TypeToken<ArrayList<HashMap<String, Object>>>(){};
		ArrayList<HashMap<String, Object>> dataList 	=  gson.fromJson(str, typeToken.getType());
		if(dataList != null) {
			return dataList;
		} else {
			return new ArrayList<HashMap<String, Object>>();
		}
	}

	public static ArrayList<String> jsonStrToArrStr(String str){
		TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>(){};
		ArrayList<String>  dataList 	=  gson.fromJson(str, typeToken.getType());
		if(dataList != null) {
			return dataList;
		} else {
			return new ArrayList<String>();
		}
	}

	public static String toJson(Object src) {
		return gson.toJson(src);
	}
}
