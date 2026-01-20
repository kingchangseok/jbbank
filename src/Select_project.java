import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Select_project

{ 
	  private static Connection con;
	  private static Statement stmt;
	  private static ResultSet rs;

	  public static void main(String[] args)
	  {
		  //String PRO_ACPTNO="111111111111";
		  //String PRJ_SEND_DT="2012-11-26";
		  //String PRO_NAME="방카";  
	    String PRO_ACPTNO = args[0];
	    String PRO_NAME = args[1];
	    String PRJ_SEND_DT=args[2];

	    
	    try
	    {
	      Class.forName("oracle.jdbc.driver.OracleDriver");
	      System.out.println("OracleDriver의 로딩이 정상적으로 이뤄졌습니다.");

	      con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.156.23:3122:dchn", "frty", "wjsqnr1%"); 
	      System.out.println("데이터베이스의 연결에 성공하였습니다.");

	      stmt = con.createStatement();

	      String sql = "  SELECT  PRO_NAME,  PRO_SEND_CLSS, PRO_SEND_DT,PRO_PATH || '/' || PRO_FILENAME AS PRO_PATH , PRO_FILENAME ,PRO_ACPTNO FROM FORTIFY_SCA_NAME" +
	      		       "  WHERE PRO_ACPTNO='"+PRO_ACPTNO+"' AND PRO_SEND_CLSS = '1' AND PRO_NAME = '"+PRO_NAME+"' AND REQC = 'F' AND SERVER_TYPE = 'W' ";

	      System.out.println(sql); 
	      rs = stmt.executeQuery(sql);

	      FileWriter fw = null;
	      FileWriter fw2 = null;
	      try {
	    	  fw = new FileWriter ("F:/fortify/shell/ilgan/lists/"+PRO_ACPTNO+"_"+PRJ_SEND_DT+".txt");
	    	  fw2 = new FileWriter ("F:/fortify/shell/ilgan/lists/"+PRO_ACPTNO+"_"+PRJ_SEND_DT+"_file.txt");
	    	  
	        while (rs.next()) {

	          String PRO_PATH = rs.getString("PRO_PATH");
	          String s = PRO_PATH;
	          fw.write(s);
	          fw.write("\n");
	          
	          
	          String PRO_FILENAME = rs.getString("PRO_FILENAME");

	          String s2 = PRO_FILENAME;
	          fw2.write(s2);
	          fw2.write("\n");

	        }

	        
	        sql = "update FORTIFY_SCA_NAME set PRO_SEND_CLSS='2' where PRO_ACPTNO='"+PRO_ACPTNO+"' and PRO_NAME = '"+PRO_NAME+"' AND REQC = 'F'"; 


	  	    System.out.println(PRO_ACPTNO + ":"+ PRO_ACPTNO);       
	  	    System.out.println(PRO_NAME + ":"+ PRO_NAME);
	  	    System.out.println(PRJ_SEND_DT + ":"+ PRJ_SEND_DT);
	  	    	       
	  	    System.out.println(sql);
	  	    stmt.executeUpdate(sql); 
	        
	        
	      }
	      catch (SQLException sqlex) {
	        System.err.println("에러! 외부 명령 실행에 실패했습니다. " + sqlex.getMessage());
	        System.exit(-1);
   }
   catch (IOException ioex) {
      System.err.println("에러! 외부 명령 실행에 실패했습니다. " + ioex.getMessage());
      System.exit(-1);
   }
   finally {
      if (fw != null) try { fw.close(); } catch (IOException iex) {}
      if (fw2 != null) try { fw2.close(); } catch (IOException iex) {}
      if (rs != null) try { rs.close(); } catch (SQLException sex) {}
   }

   stmt.close(); 
  }catch(ClassNotFoundException cnfe){ 
   System.out.println("oracle.jdbc.driver.OracleDriver를 찾을 수 없습니다."); 
  }catch(SQLException  sql){ 
   System.out.println("Connection 실패!"); 
  }catch(Exception e){ 
   System.out.println(e.toString()); 
  }finally{ 
   System.out.println("성공!!");
  } 
 } 
} 