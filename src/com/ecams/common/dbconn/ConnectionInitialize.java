
package com.ecams.common.dbconn;

import java.sql.DriverManager;

import javax.servlet.http.HttpServlet;


import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.logging.log4j.Logger;

import com.ecams.common.base.ConfigFactory;
import com.ecams.common.logger.EcamsLogger;
;

/**
 * @author bigeyes
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 * web.xml.. when server loading.... excute this file...
 */
public class ConnectionInitialize extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
	/**
     * Logger Class Instance Creation
     * logger
     */
    Logger ecamsLogger  = EcamsLogger.getLoggerInstance();
    
	public void init(){
		
		String  driverClassName     = null;
		String  url                 = null;
		String  username            = null;
		String  password            = null;
		boolean defaultAutoCommit   = true;
		String  defaultAutoCommit_s = null;
		boolean defaultReadOnly     = false;
		String  defaultReadOnly_s   = null;		
		int     maxActive           = 0;
		int     maxIdle             = 0;
		long    maxWait             = 0;
		
		//init driver invoke
		driverClassName = ConfigFactory.getProperties("O_driverClassName");
		url = ConfigFactory.getProperties("O_url");
		username = ConfigFactory.getProperties("O_username");
		password = ConfigFactory.getProperties("O_password");
		defaultAutoCommit_s = ConfigFactory.getProperties("O_defaultAutoCommit");
		if (defaultAutoCommit_s.equals("true")) defaultAutoCommit = true;
		defaultReadOnly_s = ConfigFactory.getProperties("O_defaultReadOnly");
		if (defaultReadOnly_s.equals("true")) defaultReadOnly = true;
		maxActive = Integer.parseInt(ConfigFactory.getProperties("O_maxActive"));
		maxIdle = Integer.parseInt(ConfigFactory.getProperties("O_maxIdle"));
		maxWait = Long.parseLong(ConfigFactory.getProperties("O_maxWait"));
		
			try{
				//jdbc driver및 기타 정의를 설정합니다.
				setupDriver(driverClassName
						  , url
						  , username
						  , password
						  , defaultAutoCommit
						  , defaultReadOnly
						  , maxActive
						  , maxIdle
						  , maxWait);
				ecamsLogger.info("Connection Initialize success");
			}catch(ClassNotFoundException cx){
				ecamsLogger.error("ERROR : ConnectionInitialize JDBC Class fail");
				cx.printStackTrace();
			}catch(Exception ex){
				ecamsLogger.error("ERROR : ConnectionInitialize fail");
				ex.printStackTrace();
			}
			
	}//end of init() method statement
	
	public void setupDriver(String  driverClassName
			              , String  url
						  , String  username
						  , String  password
						  , boolean defaultAutoCommit
						  , boolean defaultReadOnly
						  , int     maxActive
						  , int     maxIdle
						  , long    maxWait) throws ClassNotFoundException, Exception{
		
		try{
		//jdbc.class를 로딩한다.
			Class.forName(driverClassName);
		}catch(ClassNotFoundException classfoundexception){
			classfoundexception.printStackTrace();
			throw classfoundexception;
		}//end of try-catch statement

		//커넥션 풀로 사용할 commons-collections의 genericObjectPool을 생성한다.
		GenericObjectPool connectionPool = new GenericObjectPool(null);
		connectionPool.setMaxActive(maxActive);
		connectionPool.setMaxIdle(maxIdle);
		connectionPool.setMaxWait(maxWait);

		//풀이 커넥션을 생성하는데 사용하는 DriverManagerConnectionFactory를 생성한다.
		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(url
				                                                               , username
																			   , password);

		//ConnectionFactory의 래퍼 클래스인 PoolableConnectionFactory를 생성한다.
		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory
				                                                                          , connectionPool
																						  , null
																						  , null
																						  , defaultReadOnly
																						  , defaultAutoCommit);

		//마지막으로 PoolingDriver 자신을 로딩한다.
		//Class.forName("org.apache.commons.dbcp.PoolingDriver");
		//PoolingDriver driver = (PoolingDriver)DriverManager.getDriver("jdbc:apache:commons:dbcp:");
		//그리고 풀에 등록한다. 풀이름을 "ecams_db"이라고 지정하였다.
		//driver.registerPool("ecams_db", connectionPool);
		
	}//end of setupDriver() method statement
	
}//end of ConnectionInitialize class statement
