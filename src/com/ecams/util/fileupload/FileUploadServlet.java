package com.ecams.util.fileupload;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;

import com.ecams.common.logger.EcamsLogger;
import com.ecams.util.exception.ErrorHandler;
import com.ecams.util.fileupload.vo.FileMeta;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.eCmm.Cmm0700;
import app.eCmm.Cmm2100;

//this to be used with Java Servlet 3.0 API
@MultipartConfig
@WebServlet("/webPage/fileupload/upload")
public class FileUploadServlet extends HttpServlet {
	Logger ecamsLogger = EcamsLogger.getLoggerInstance();

	/*
	 * FileUploadServlet.java는 클라이언트 요청을 수신하는 서블릿입니다.
	 * 파일 업로드 요청을 처리하는 doPost ()와 파일 다운로드 요청을 처리하는 doGet ()의 두 가지 메소드가 있습니다.
	 * doPost () 응답 내용은 JSON 형식입니다.
	 */

	// this will store uploaded files

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/**
	 *
	 */
	/***************************************************
	 * URL: /upload
	 * doPost(): upload the files and other parameters
	 ****************************************************/
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException{
		// 1. Upload File Using Java Servlet API
		List<FileMeta> files = new LinkedList<FileMeta>();
		try {
			// 1. Upload File Using Apache FileUpload
			files.addAll(MultipartRequestHandler.uploadByApacheFileUpload(request));
			if(files.size() == 0) {
				return;
			}
			EcamsLogger.getLoggerInstance().error("****fileUpload SErvlet START4****");
			// 2. Set response type to json
			response.setContentType("application/json");

			// 3. Convert List<FileMeta> into JSON format
	    	ObjectMapper mapper = new ObjectMapper();
	    	// 4. Send resutl to client
	    	EcamsLogger.getLoggerInstance().error("****fileUpload SErvlet START4****" + files.toString());
	    	mapper.writeValue(response.getOutputStream(), files);
		} catch (ServletException e) {
			EcamsLogger.getLoggerInstance().error("error");
	    	ErrorHandler.handleError(request, response, e);
	    } catch (Exception e) {
			EcamsLogger.getLoggerInstance().error("error");
	    	ErrorHandler.handleError(request, response, e);
	    }

	}

	// 파일 다운로드
	/***************************************************
	 * URL: /upload?f=value
	 * doGet(): get file of index "f" from List<FileMeta> as an attachment
	 ****************************************************/
	/*protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException{
		String setFileName		= null;
		String fileName			= null;
		String folderPath 		= request.getParameter("folderPath");
		String userAgent 		= request.getHeader("User-Agent");

		boolean ie = (userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("Trident") > -1);

		fileName = folderPath.substring(folderPath.lastIndexOf("\\"), folderPath.length());
		System.out.println(fileName);
		if(ie) {
			setFileName = URLEncoder.encode( fileName, "UTF-8" ).replaceAll("\\+", "%20");
		} else{
			setFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");

		}
		response.setHeader("Content-Disposition", "attachment; filename=\"" + setFileName + "\"");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Connection", "close");

		try {

			FileInputStream input 	= new FileInputStream(folderPath);
			OutputStream output = response.getOutputStream();
			byte[] buffer = new byte[1024*10];

			for (int length = 0; (length = input.read(buffer)) > 0;) {
				output.write(buffer, 0, length);
			}

			output.close();
			input.close();
		}catch (Exception e) {
			e.getStackTrace();
		}

	}*/

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

		response.setContentType("application/octet-stream");
		response.setCharacterEncoding("UTF-8");
		
		String fullPath 		= request.getParameter("fullPath");
		String fileName			= URLDecoder.decode(request.getParameter("fileName"), "UTF-8");
		String zipPath			= request.getParameter("zipPath");
		String zipName			= request.getParameter("zipName");
		String userAgent 		= request.getHeader("User-Agent");
		String actpno 			= request.getParameter("actpno");
		boolean zipSw			= false;
		String outputName		= null;
		int bufferSize 			= 1024 * 10;
		BufferedInputStream bis = null;
		
		if(zipPath != null) {
			zipSw = true;
		}
		
		try {
			
			boolean ie = (userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("Trident") > -1);
			boolean edge = (userAgent.indexOf("EDGE") > -1);
			boolean winOs = System.getProperty("os.name").toLowerCase().indexOf("win") >=0 ? true : false;
			EcamsLogger.getLoggerInstance().error("check doGet winos: " + winOs );
		

			if(zipSw) {
				//zipName = URLEncoder.encode( zipName, "UTF-8" ).replaceAll("\\+", "%20");
				zipName = URLDecoder.decode(zipName, "UTF-8");
			} else {
				// 인코딩이 2번되어 넘어와 디코딩해줌.
				fullPath = URLDecoder.decode(fullPath, "UTF-8");
				//fileName = URLEncoder.encode( fileName, "UTF-8" ).replaceAll("\\+", "%20");
				fileName = URLDecoder.decode(fileName, "UTF-8");
			}


			String[] filePathArr = {"/"};
			
			Cmm0700 cmm0700 = new Cmm0700();
			Object[] eCAMSDir = cmm0700.getTab3Info();
			HashMap<String,String> rData = null;

			boolean pathCk = false;
			
			if (null != fullPath) {
				if(fullPath.indexOf("..") > -1 || fullPath.indexOf("&") > -1 || fullPath.indexOf(">") > -1 || fullPath.indexOf("<") > -1 ||
						fullPath.indexOf("|") > -1 || fullPath.indexOf("\\") > -1 || fullPath.indexOf("*") > -1 || fullPath.indexOf("?") > -1 || fullPath.indexOf(":") > -1 ) {
					throw new Exception("첨부파일명, 파일 경로를 확인해 주세요. [1]");
				}
			}
			if (null != eCAMSDir) {
				for(int i=0; i<eCAMSDir.length; i++) {
					rData = (HashMap<String,String>) eCAMSDir[i];
					if (null != fullPath && fullPath.indexOf(rData.get("cm_path")) > -1) {
						pathCk = true;
						break;
					}
				}
			}

			if(fileName != null && (!pathCk || fullPath.indexOf("..") >= 0 || fullPath.indexOf("&") >= 0 ||
					fileName.indexOf("..") >= 0 || fileName.indexOf("&") >= 0)) {
				throw new Exception("첨부파일명, 파일 경로를 확인해 주세요. [2]");
			}
			
			if (!pathCk) {
				throw new Exception("첨부파일명, 파일 경로를 확인해 주세요. [3]");
			}
			if (fileName != null && 
			   (fileName.indexOf("..") > -1 || fileName.indexOf("&") > -1 || fileName.indexOf(">") > -1 || fileName.indexOf("<") > -1 ||
			      fileName.indexOf("|") > -1 || fileName.indexOf("\\") > -1 || fileName.indexOf("*") > -1 || fileName.indexOf("?") > -1 || fileName.indexOf(":") > -1) ) {
				throw new Exception("첨부파일명, 파일 경로를 확인해 주세요. [4]");
			}

			String extCk = "OK";
			if (fileName.lastIndexOf("\\.") > -1) {
			    String ext = fileName.substring(fileName.lastIndexOf(".")+1);
			    
			    EcamsLogger.getLoggerInstance().error("11111+++++++++++++++++"+ext);
			    extCk = MultipartRequestHandler.extCk(ext);
			    EcamsLogger.getLoggerInstance().error("11111+++++++++++++++++"+extCk);
			} 
			if (null != extCk && !"OK".equals(extCk)) {
				throw new Exception("첨부파일명, 파일 경로를 확인해 주세요. [5]");
			}

			EcamsLogger.getLoggerInstance().error("check doGet fileName: " + fileName);

			outputName = zipSw ? zipName + ".zip" : fileName;
			
			String encodedFileName;
			if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
				encodedFileName = URLEncoder.encode(outputName, "UTF-8").replaceAll("\\+", "%20");
				response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
			} else if (userAgent.contains("Edge") || userAgent.contains("Chrome") || userAgent.contains("Firefox")) {
				encodedFileName = URLEncoder.encode(outputName, "UTF-8").replaceAll("\\+", "%20");
				response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
			} else {
				encodedFileName = new String(outputName.getBytes("UTF-8"), "ISO-8859-1");
				response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
			}
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Connection", "close");
			
			if(zipSw) {
				ZipOutputStream zos = null;
			    OutputStream os = response.getOutputStream();

			    if (null != os) {
				    zos = new ZipOutputStream(os);
				    // 압축 레벨 - 최대 압축률은 9, 디폴트 8
				    zos.setLevel(8);

				    if (null != zipPath && !"".equals(zipPath)) {
					    File dirFile = new File(zipPath);

					    if (dirFile.exists()) {
							File[] fileList = dirFile.listFiles();

							if (null != actpno && !"".equals(actpno)) {
								Cmm2100 cmm2100 = new Cmm2100();
								//ArrayList<HashMap<String, String>>  noticeFileList = cmm2100.getFileListArr(actpno);
								ArrayList<HashMap<String, String>>  noticeFileList = null;
								cmm2100 = null;

								if(fileList != null) {
									for(File tempFile : fileList) {
										if(tempFile.isFile()) {
				//							EcamsLogger.getLoggerInstance().error("tmpFileName: " + tempFile.getName());
											for(HashMap<String,String> map : noticeFileList) {
				//								EcamsLogger.getLoggerInstance().error("savename: " + map.get("savename"));
												if(tempFile.getName().equals(map.get("savename"))) {

													File sourceFile = new File(zipPath+File.separator+tempFile.getName());
													FileInputStream in = new FileInputStream(sourceFile);
													bis = new BufferedInputStream(in);
													ZipEntry zentry = new ZipEntry(map.get("orgname"));
													zentry.setTime(sourceFile.lastModified());
													zos.putNextEntry(zentry);

													byte[] buffer = new byte[bufferSize];
													int cnt = 0;
													while ((cnt = bis.read(buffer, 0, bufferSize)) > 0) {
														zos.write(buffer, 0, cnt);
													}

													zos.closeEntry();
													noticeFileList.remove(map);
													bis.close();

													//sourceFile.deleteOnExit();
													sourceFile.delete();
													sourceFile = null;

													in.close();
													break;
												}
											}
										}
									}
								}
								noticeFileList = null;
							}
					    }
					    //dirFile.deleteOnExit();
					    dirFile.delete();
					    dirFile = null;
				    }
					zos.finish();
				    zos.close();
				    zos = null;
			    }
			    os.close();
			    os = null;
			    if(bis != null) bis.close();

			} else {
				FileInputStream input 	= new FileInputStream(fullPath);
				OutputStream output = response.getOutputStream();
				byte[] buffer = new byte[bufferSize];

				for (int length = 0; (length = input.read(buffer)) > 0;) {
					output.write(buffer, 0, length);
				}
				output.close();
				input.close();
			}
			
			rData = null;
			eCAMSDir = null;
			cmm0700 = null;
			
		} catch(IOException e){
//			ecamsLogger.error(e);
	    	  ErrorHandler.handleError(request, response, e);
	    } catch(Exception e){
//			ecamsLogger.error(e);
	    	  ErrorHandler.handleError(request, response, e);
	    } finally {
	    	if(bis != null) bis.close();
	    }
	}

}
