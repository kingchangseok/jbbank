import java.net.URLDecoder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
public class Https_Call {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sendData="";
		String sendData_utf8 ="";
		
		try {
			//int	size = args.length;
			
			sendData=sendData+args[1];
			
			//System.out.println(sendData);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        }
		
		HttpsCom httpsCom = new HttpsCom();
        byte [] receive	;	
		try {
			
		   //System.out.println("Call httpCom.process sendData= "+sendData);
		    //sendData_utf8 = new String(sendData.getBytes(),"UTF-8");
		    receive = httpsCom.process(args[0], sendData.getBytes("UTF-8"));				

			String str =new String(receive);
			
			String Rssult =new String(str.replace("\n", "").replace("\r", ""));
			
			Rssult = URLDecoder.decode(Rssult,"UTF-8");
			
//			System.out.println("# get11 : "+ Rssult);
			 
			JSONParser p = new JSONParser();
			JSONObject obj = (JSONObject) p.parse(Rssult);
			JSONArray arr = (JSONArray)  obj.get("RESULT");
			
			String test = "";
			for(int i=0;i<arr.size();i++){
				JSONObject tmp = (JSONObject) arr.get(i);
				
				test = test + tmp.get("TASK") + "\t" + tmp.get("USR_ID") + "\t" + tmp.get("FILE_NM") + "\t" + tmp.get("FILE_DESC") 
							+ "\t" + tmp.get("FILE_PATH") + "\t" + tmp.get("FILE_EXT")  + "\t" + tmp.get("FILE_LANG") + "\n";
			}
			
			TextRW trw = new TextRW();
			trw.TextWrite(test, args[2]);	
			
			//System.out.println("# get11 : "+new String(receive));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
