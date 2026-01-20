
/**
 * 2007-11-05
 * KCS File Transger Program
 */

package com.ecams.common.base;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;

import com.oroinc.net.ftp.FTP;

//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

public class eCAMSFtp {
	String server = "";
	String user = "";
	String password = ""; // password
	String serverPath = "";
	String filename = "";
	boolean ascII = false;
	String local_File = "";

	String serverPath2 = "";
	String filename2 = "";
	boolean ascII2 = true;
	String local_DIR2 = "";

	private static eCAMSFtp instance;
	FTPClient ftpClient = null;

	// FTP SEVER Connection
	public boolean ConnetServer(String ServerIP, String loginID, String pass) {
		boolean con_result = false;

		System.out.println("ServerIP   " + ServerIP);
		System.out.println("loginID   " + loginID);
		System.out.println("pass   " + pass);

		try {
			ftpClient = new FTPClient();

			ftpClient.connect(ServerIP);
			ftpClient.login(loginID, pass);
			ftpClient.enterLocalPassiveMode();

			if (ftpClient.isConnected())
				con_result = true;
		} catch (IOException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return con_result;
	}

	public void CloseServer() {
		try {
			ftpClient.logout();
			ftpClient.disconnect();
		} catch (IOException ex) {
			System.out.println("FTP SERVER CLOSE ERROR [" + ex.toString() + "]");
		} catch (Exception ex) {
			System.out.println("FTP SERVER CLOSE ERROR [" + ex.toString() + "]");
		}
	}

	public boolean downloadFile(String serverPath, String filename, boolean ascII, String local_DIR) {
		boolean state = true;
		if (!ftpClient.isConnected()) {
			state = false;
			return state;
		} else {
			OutputStream outputStream = null;
			try {
				if (ascII) {
					ftpClient.setFileType(FTP.ASCII_FILE_TYPE);
				} else {
					ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				}
				ftpClient.changeWorkingDirectory(serverPath);
				File file = new File(local_DIR); // local dir

				outputStream = new BufferedOutputStream(new FileOutputStream(file));
				boolean success = ftpClient.retrieveFile(filename, outputStream);
				if (success) {
					System.out.println("占쎄쉐�⑨옙");
				} else {
					System.out.println("占쎈뼄占쎈솭");

				}

				System.out.println(local_DIR + " *** Download Success kcs jjang ");
			} catch (IOException io) {
				System.out.println("File Opend Error or File Wirte Error[" + io.toString() + "]");
			} catch (Exception ex) {
				System.out.println(ex.toString());
			} finally {
				try {
					if (ftpClient.isConnected()) {
						ftpClient.logout();
						ftpClient.disconnect();
					}
					if (outputStream != null)
						outputStream.close();
				} catch (IOException e) {
					System.out.println(e.toString());
				} catch (Exception e) {
					System.out.println(e.toString());
				}
			}
		}
		return state;
	}

	public boolean UploadFile(String serverPath, String filename, boolean ascII, String local_File) {
		boolean state = true;
		if (!ftpClient.isConnected()) {
			state = false;
			return state;
		} else {
			InputStream inputStream = null;
			try {
				if (ascII) {
					ftpClient.setFileType(FTP.ASCII_FILE_TYPE);
				} else {
					ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				}
				ftpClient.changeWorkingDirectory(serverPath);

				inputStream = new FileInputStream(filename);

				boolean done = ftpClient.storeFile(filename, inputStream);

				if (done) {
					state = true;
				} else {
					state = false;
				}

				System.out.println(local_File + " *** UploadFile Success kcs jjang  ");
			} catch (IOException io) {
				System.out.println("File Opend Error or File Wirte Error[" + io.toString() + "]");
			} catch (Exception ex) {
				System.out.println(ex.toString());
			} finally {
				try {
					if (ftpClient.isConnected()) {
						ftpClient.logout();
						ftpClient.disconnect();
					}
					if (inputStream != null)
						inputStream.close();
				} catch (IOException e) {
					System.out.println(e.toString());
				} catch (Exception e) {
					System.out.println(e.toString());
				}

			}
			return state;
		}
	}

	public static eCAMSFtp instance() {
		// TODO Auto-generated method stub
		if (instance == null) {
			instance = new eCAMSFtp();
		}
		return instance;
	}
}