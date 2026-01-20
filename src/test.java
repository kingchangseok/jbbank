import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.ecams.common.dbconn.ConnectionContext;
import com.ecams.common.dbconn.ConnectionResource;

public class test {
	public static void main(String[] args) throws Exception{
		String TEMP = "2.시스템명세모델-01.공용DTO정의-03.Const-PCO.공통정보.efx";
		
		System.out.println("lastIndexOf : " + TEMP.lastIndexOf("01.공용DTO정의"));
		System.out.println("IndexOf : " + TEMP.indexOf("01.공용DTO정의"));
	}
}

