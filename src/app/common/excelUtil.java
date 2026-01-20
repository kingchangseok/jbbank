package app.common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Header;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ecams.common.logger.EcamsLogger;


public class excelUtil {

	Logger ecamsLogger  = EcamsLogger.getLoggerInstance();

	/**
	 * <PRE>
	 * 1. MethodName	: getArrayCollection
	 * 2. ClassName		: excelUtil
	 * 3. Commnet			: 
	 * 4. 작성자				: Administrator
	 * 5. 작성일				: 2010. 12. 29. 오후 7:13:33
	 * </PRE>
	 * 		@return Object[]
	 * 		@param filePath
	 * 		@param headerDef
	 * 		@return
	 * 		@throws IOException
	 * 		@throws Exception
	 */
	/*public Object[] getArrayCollection(String filePath,ArrayList<String> headerDef) throws IOException, Exception {
		Object[]		  rtObj		  = null;

		ArrayList<HashMap<String, String>>  rtList = null;
		HashMap<String, String>			    rst	   = null;
		//HashMap<String, String>	hdhsh = null;

		FileInputStream excelFis = null;
		POIFSFileSystem excelPOIF = null;

		HSSFWorkbook wb = null;

		HSSFSheet sheet = null;

		int firstRow;
		int lastRow;
		int rowIdx;

		HSSFRow row = null;

		short firstCell;
		//short lastCell;
		int lastCell;
		short cellIdx;

		HSSFCell cell = null;

		try { 
			//ecamsLogger.error("filePath 111 : " + "["+filePath+"]");
			//filePath ="//ecams//hanabank//bin//tmp//9812370_excel.tmp";	
			filePath = filePath.trim();
			filePath = filePath.replace("//", "/");
			filePath = filePath.replace("/", "//");
			//ecamsLogger.error("filePath 222 : " + filePath);
			excelFis = new FileInputStream(filePath);
			excelPOIF = new POIFSFileSystem(excelFis);

			//워크북 오브젝트의 취득
			wb = new HSSFWorkbook(excelPOIF);

			// 총 워크시트수의 취득
			//int sheetcount = wb.getNumberOfSheets();


			sheet = wb.getSheetAt(0);

			// 워크시트에 있는 첫행과 마지막행의 인덱스를 취득
			firstRow = sheet.getFirstRowNum();
			lastRow = sheet.getLastRowNum();


			rtList = new ArrayList<HashMap<String, String>>();
			// 행 별로 데이터를 취득



			for (rowIdx = firstRow; rowIdx <= lastRow; rowIdx++) {
				//행을 표시하는 오브젝트의 취득
				row = sheet.getRow(rowIdx);

				// 행에 데이터가 없는 경우
				if (row == null) break;


				// 행에서 첫셀과 마지막 셀의 인덱스를 취득
				firstCell = row.getFirstCellNum();
				lastCell = headerDef.size()-1;
				
//				if ((lastCell-firstCell) < headerDef.size()){
//					throw new Exception("엑셀파일의 열의 갯수가 지정한 해더의 갯수보다 적습니다.");
//				}
				//셀 별로 데이터를 취득

				rst   = new HashMap<String, String>();
				for (cellIdx = firstCell ; cellIdx <= lastCell; cellIdx++) {
					String data = "";
					
//					System.out.println("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆");
//					System.out.println(headerDef.get(cellIdx));

					if (headerDef.size()-1 < cellIdx){
						break;
					}
					if (headerDef.get(cellIdx) == null){
						break;
					}
					
					// 셀을 표시하는 오브젝트를 취득
					cell = row.getCell(cellIdx);

					// 빈 셀인 경우
					if (cell == null){
						
					}else{
						//cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						
						// 셀에 있는 데이터의 종류를 취득
						int type = cell.getCellType();
	
						// 데이터 종류별로 데이터를 취득
						switch (type) {
							case HSSFCell.CELL_TYPE_BOOLEAN:
								boolean bdata = cell.getBooleanCellValue();
								data = String.valueOf(bdata);
								break;
							case HSSFCell.CELL_TYPE_NUMERIC:
								double ddata = cell.getNumericCellValue();
								data = String.valueOf(((int)ddata));
								break;
							case HSSFCell.CELL_TYPE_STRING:
								data = cell.getStringCellValue();
								break;
							case HSSFCell.CELL_TYPE_BLANK:
							case HSSFCell.CELL_TYPE_ERROR:
							case HSSFCell.CELL_TYPE_FORMULA:
							default:
								continue;
						}
					}
					rst.put(headerDef.get(cellIdx), data);
				}
				rtList.add(rst);
			}

			excelFis.close();

			rtObj =  rtList.toArray();
			
			rtList = null;

			return rtObj;

		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## excelUtil.getArrayCollection() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## excelUtil.getArrayCollection() Exception END ##");
			throw exception;
		} finally{
			if (rtObj != null)	rtObj = null;
		}
	}*/
	
	//xls와 xlsx 통합 사용
	public Object[] getArrayCollection(String filePath,ArrayList<String> headerDef) throws IOException, Exception {
		Object[]		  					rtObj  = null;
		ArrayList<HashMap<String, String>>  rtList = null;
		HashMap<String, String>			    rst	   = null;

		Workbook wb;
		Sheet sheet = null;
		Row row = null;
		Cell cell = null;
		
		int firstRow;
		int lastRow;
		int rowIdx;		

		short firstCell;
		int lastCell;
		short cellIdx;

		try { 
			filePath = filePath.trim();
			filePath = filePath.replace("//", "/");
			filePath = filePath.replace("/", "//");

			//워크북 오브젝트의 취득
			wb = WorkbookFactory.create(new FileInputStream(filePath));

			// 총 워크시트수의 취득
			//int sheetcount = wb.getNumberOfSheets();

			if (wb == null){
				throw new Exception("엑셀 sheet 읽기 실패[excelUtil]");
			}
			sheet = wb.getSheetAt(0);

			// 워크시트에 있는 첫행과 마지막행의 인덱스를 취득
			firstRow = sheet.getFirstRowNum();
			lastRow = sheet.getLastRowNum();

			rtList = new ArrayList<HashMap<String, String>>();
			// 행 별로 데이터를 취득

			for (rowIdx = firstRow; rowIdx <= lastRow; rowIdx++) {
				//행을 표시하는 오브젝트의 취득
				row = sheet.getRow(rowIdx);

				// 행에 데이터가 없는 경우
				if (row == null) break;

				// 행에서 첫셀과 마지막 셀의 인덱스를 취득
				firstCell = row.getFirstCellNum();
				lastCell = headerDef.size()-1;
				
//				if ((lastCell-firstCell) < headerDef.size()){
//					throw new Exception("엑셀파일의 열의 갯수가 지정한 해더의 갯수보다 적습니다.");
//				}
				
				//셀 별로 데이터를 취득
				rst   = new HashMap<String, String>();
				for (cellIdx = firstCell ; cellIdx <= lastCell; cellIdx++) {
					String data = "";
					
					if (headerDef.size()-1 < cellIdx){
						break;
					}
					if (headerDef.get(cellIdx) == null){
						break;
					}
					
					// 셀을 표시하는 오브젝트를 취득
					cell = row.getCell(cellIdx);

					// 빈 셀인 경우
					if (cell == null){
						
					}else{
						//cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						
						// 셀에 있는 데이터의 종류를 취득
						int type = cell.getCellType();
	
						// 데이터 종류별로 데이터를 취득
						switch (type) {
							case HSSFCell.CELL_TYPE_BOOLEAN:
								boolean bdata = cell.getBooleanCellValue();
								data = String.valueOf(bdata);
								break;
							case HSSFCell.CELL_TYPE_NUMERIC:
								double ddata = cell.getNumericCellValue();
								data = String.valueOf(((int)ddata));
								break;
							case HSSFCell.CELL_TYPE_STRING:
								data = cell.getStringCellValue();
								break;
							case HSSFCell.CELL_TYPE_BLANK:
							case HSSFCell.CELL_TYPE_ERROR:
							case HSSFCell.CELL_TYPE_FORMULA:
							default:
								continue;
						}
					}
					rst.put(headerDef.get(cellIdx), data);
				}
				rtList.add(rst);
			}

			rtObj =  rtList.toArray();
			rtList = null;
			return rtObj;

		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## excelUtil.getArrayCollection() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## excelUtil.getArrayCollection() Exception END ##");
			throw exception;
		} finally{
			if (rtObj != null)	rtObj = null;
		}
	}

	/**
	 * <PRE>
	 * 1. MethodName	: getExcelArrayCollection
	 * 2. ClassName		: excelUtil
	 * 3. Commnet			: (KICC)단위테스트/통합테스트 테스트케이스 [엑셀로드]시 사용하는 함수
	 * 4. 작성자				: no name
	 * 5. 작성일				: 2010. 12. 29. 오후 7:09:04
	 * </PRE>
	 * 		@return Object[]
	 * 		@param filePath
	 * 		@param headerDef
	 * 		@return
	 * 		@throws IOException
	 * 		@throws Exception
	 */
	public Object[] getExcelArrayCollection(String filePath,ArrayList<String> headerDef) throws IOException, Exception {
		Object[]		  rtObj		  = null;

		ArrayList<HashMap<String, String>>  rtList = null;
		HashMap<String, String>			    rst	   = null;
		//HashMap<String, String>	hdhsh = null;

		FileInputStream excelFis = null;
		POIFSFileSystem excelPOIF = null;

		HSSFWorkbook wb = null;

		HSSFSheet sheet = null;

		int firstRow;
		int lastRow;
		int rowIdx;

		HSSFRow row = null;

		short firstCell;
		short lastCell;
		short cellIdx;

		HSSFCell cell = null;

		try { 
			//ecamsLogger.error("filePath 111 : " + "["+filePath+"]");
			//filePath ="//ecams//hanabank//bin//tmp//9812370_excel.tmp";	
			filePath = filePath.trim();
			filePath = filePath.replace("//", "/");
			filePath = filePath.replace("/", "//");
			//ecamsLogger.error("filePath 222 : " + filePath);
			excelFis = new FileInputStream(filePath);
			excelPOIF = new POIFSFileSystem(excelFis);

			//워크북 오브젝트의 취득
			wb = new HSSFWorkbook(excelPOIF);

			// 총 워크시트수의 취득
			//int sheetcount = wb.getNumberOfSheets();


			sheet = wb.getSheetAt(0);

			// 워크시트에 있는 첫행과 마지막행의 인덱스를 취득
			firstRow = sheet.getFirstRowNum();
			lastRow = sheet.getLastRowNum();


			rtList = new ArrayList<HashMap<String, String>>();
			// 행 별로 데이터를 취득



			for (rowIdx = firstRow; rowIdx <= lastRow; rowIdx++) {
				//행을 표시하는 오브젝트의 취득
				row = sheet.getRow(rowIdx);

				// 행에 데이터가 없는 경우
				if (row == null) break;


				// 행에서 첫셀과 마지막 셀의 인덱스를 취득
				firstCell = row.getFirstCellNum();
				lastCell = row.getLastCellNum();
//				
//				if ((lastCell-firstCell) < headerDef.size()){
//					//(kicc)header size보다 작은경우 존재해서 주석처리.
//					//throw new Exception("엑셀파일의 열의 갯수가 지정한 해더의 갯수보다 적습니다.");
//				}
//				

				//셀 별로 데이터를 취득
				rst   = new HashMap<String, String>();
				for (cellIdx = firstCell ; cellIdx <= lastCell; cellIdx++) {
					String data = null;

					if (headerDef.size()-1 < cellIdx){
						break;
					}
					/*//해당셀 null 이여두 값을 빈칸으로 넘겨줘야해서 주석처리 - 호윤
					if (headerDef.get(cellIdx) == null){
						break;
					}
					*/
					//첫번째셀이면서 null 이면 braek;
					if (headerDef.get(cellIdx) == null && cellIdx == 1){
						break;
					}
					
					
					// 셀을 표시하는 오브젝트를 취득
					cell = row.getCell(cellIdx);

					// 빈 셀인 경우
					if (cell == null){
						//기존 빈셀인경우 break; 했는데
						//break;
						rst.put(headerDef.get(cellIdx), "");
					}else{

						//cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						// 셀에 있는 데이터의 종류를 취득
						int type = cell.getCellType();
	
						// 데이터 종류별로 데이터를 취득
	
	
						switch (type) {
							case HSSFCell.CELL_TYPE_BOOLEAN:
								boolean bdata = cell.getBooleanCellValue();
								data = String.valueOf(bdata);
								break;
							case HSSFCell.CELL_TYPE_NUMERIC:
								double ddata = cell.getNumericCellValue();
								data = String.valueOf(((int)ddata));
								break;
							case HSSFCell.CELL_TYPE_STRING:
								data = cell.getStringCellValue();
								break;
							case HSSFCell.CELL_TYPE_BLANK:
							case HSSFCell.CELL_TYPE_ERROR:
							case HSSFCell.CELL_TYPE_FORMULA:
							default:
								continue;
						}
						rst.put(headerDef.get(cellIdx), data);
					}
				}
				rtList.add(rst);
			}

			excelFis.close();

			rtObj =  rtList.toArray();
			
			rtList = null;

			return rtObj;

		} catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## excelUtil.getExcelArrayCollection() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## excelUtil.getExcelArrayCollection() Exception END ##");
			throw exception;
		} finally{
			if (rtObj != null)	rtObj = null;
		}
	}
	
	
	
	/*public String setExcel(String filePath,ArrayList<String> headerDef,ArrayList<HashMap<String,String>> aryData) throws Exception
	{
		HashMap<String, String>			    rst	   = null;
		FileOutputStream excelFis = null;
		//POIFSFileSystem excelPOIF = null;
		HSSFWorkbook wb = null;
		HSSFSheet sheet = null;
		//int firstRow;
		//int lastRow;
		//int rowIdx;
		HSSFRow row = null;
		//short firstCell;
		//short lastCell;
		//short cellIdx;
		HSSFCell cell = null;
		HSSFHeader header = null;
		HSSFCellStyle cs = null;
		HSSFDataFormat df = null;
		HSSFFont f = null;

		try {
			//ecamsLogger.error("#######   setExcel  start   #######");
			

			boolean rowSw = false;
			int     wkCnt = 0;
			int     wkRow = 0;
			wb = new HSSFWorkbook();
			
			for (int i = 0;i<aryData.size();i++){

				if (i == 0) rowSw = true;
				else {
					if ((i%60000) == 0) rowSw = true;
					else rowSw = false;
				}
				if (rowSw == true) {
					if (i > 0) wkCnt = i/60000;
					++wkCnt;
					wkRow = 0;					

					sheet = wb.createSheet("sheet"+Integer.toString(wkCnt));

					header = sheet.getHeader();
					header.setCenter("Center Header");

					cs = wb.createCellStyle();
					df = wb.createDataFormat();

					f = wb.createFont();
					f.setFontHeightInPoints((short) 10);
					//f.setColor( HSSFColor.BLUE.index );
					f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

					cs.setFont(f);
					cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
					cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
					cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
					cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());
					cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
					cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
					cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
					cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
					cs.setDataFormat(df.getFormat("#,##0.0"));
				}
				
				cs = wb.createCellStyle();
				df = wb.createDataFormat();
				
				f = wb.createFont();
				f.setFontHeightInPoints((short) 10);
				//f.setColor( HSSFColor.BLUE.index );
				f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

				cs.setFont(f);
				cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
				cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());
				cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
				cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
				cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
				cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
				cs.setDataFormat(df.getFormat("#,##0.0"));
				
                if (rowSw == true && i > 0) {
                	row = sheet.createRow(wkRow);
                	rst = aryData.get(0);
                	
                	for (short j=0;j<rst.size();j++){
                		
                		
    					row.createCell(j).setCellValue(rst.get(headerDef.get((int)j)));
    					if (wkRow ==0){
    						// 셀을 표시하는 오브젝트를 취득
    						cell = row.getCell(j);
    						cell.setCellStyle(cs);
    						// 빈 셀인 경우
    						if (cell == null) break;
    						cell.setCellStyle(cs);
    					}
    				}
                	++wkRow;
                }
                row = sheet.createRow(wkRow);
                rst = aryData.get(i);
                String str = "";
				for (short j=0;j<rst.size();j++){
					
					
//					Cell cell0 = row4.createCell(0);
//					cell0.setCellValue("시스템명");
//					cell0.setCellStyle(cellStyle1);
//					
//					
//					
//					System.out.println("cccccccccccccccccc");
					
					
					cell = row.createCell(j);
					if(headerDef.get(j).equals("sayu")){
						str = rst.get(headerDef.get(j));
						if(str.length() > 20)
							str = str.substring(0,20);
						cell.setCellValue(str);
					}	
					else
					cell.setCellValue(rst.get(headerDef.get((int)j)));
					cell.setCellStyle(cs);
//					row.createCell(j).setCellValue(rst.get(headerDef.get((int)j)));
					if (wkRow ==0){
						// 셀을 표시하는 오브젝트를 취득
						//cell = row.getCell(j);
						// 빈 셀인 경우
						if (cell == null) break;
					}
				}
				++wkRow;
			}
			excelFis = new FileOutputStream(filePath);
		    wb.write(excelFis);
		    excelFis.close();
		    //ecamsLogger.error("#######   setExcel  end   #######");
		}
		catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## excelUtil.getArrayCollection() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## excelUtil.getArrayCollection() Exception END ##");
			throw exception;
		}

		return filePath;
	}*/
	
	public String setExcel(String filePath,ArrayList<String> headerDef,ArrayList<HashMap<String,String>> aryData) throws Exception
	{
		HashMap<String, String>			rst	   	 = null;
		FileOutputStream 				excelFis = null;
		
		Workbook wb = null;
		Sheet sheet = null;
		Row row 	= null;
		Cell cell 	= null;
		
		Header header 	= null;
		CellStyle cs 	= null;
		DataFormat df 	= null;
		Font f 			= null;

		try {
			boolean rowSw = false;
			int     wkCnt = 0;
			int     wkRow = 0;
			wb = new XSSFWorkbook();
			
			for (int i = 0;i<aryData.size();i++){

				if (i == 0) rowSw = true;
				else {
					if ((i%60000) == 0) rowSw = true;
					else rowSw = false;
				}
				if (rowSw == true) {
					if (i > 0) wkCnt = i/60000;
					++wkCnt;
					wkRow = 0;					

					sheet = wb.createSheet("sheet"+Integer.toString(wkCnt));

					header = sheet.getHeader();
					header.setCenter("Center Header");

					cs = wb.createCellStyle();
					df = wb.createDataFormat();

					f = wb.createFont();
					f.setFontHeightInPoints((short) 10);
					//f.setColor( HSSFColor.BLUE.index );
					f.setBoldweight(Font.BOLDWEIGHT_BOLD);

					cs.setFont(f);
					cs.setBorderBottom(CellStyle.BORDER_THIN);
					cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
					cs.setBorderLeft(CellStyle.BORDER_THIN);
					cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());
					cs.setBorderRight(CellStyle.BORDER_THIN);
					cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
					cs.setBorderTop(CellStyle.BORDER_THIN);
					cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
					cs.setDataFormat(df.getFormat("#,##0.0"));
				}
				
				cs = wb.createCellStyle();
				df = wb.createDataFormat();
				
				f = wb.createFont();
				f.setFontHeightInPoints((short) 10);
				//f.setColor( HSSFColor.BLUE.index );
				f.setBoldweight(Font.BOLDWEIGHT_BOLD);

				cs.setFont(f);
				cs.setBorderBottom(CellStyle.BORDER_THIN);
				cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
				cs.setBorderLeft(CellStyle.BORDER_THIN);
				cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());
				cs.setBorderRight(CellStyle.BORDER_THIN);
				cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
				cs.setBorderTop(CellStyle.BORDER_THIN);
				cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
				cs.setDataFormat(df.getFormat("#,##0.0"));
				
                if (rowSw == true && i > 0) {
                	row = sheet.createRow(wkRow);
                	rst = aryData.get(0);
                	
                	for (short j=0;j<rst.size();j++){
                		
                		
    					row.createCell(j).setCellValue(rst.get(headerDef.get((int)j)));
    					if (wkRow ==0){
    						// 셀을 표시하는 오브젝트를 취득
    						cell = row.getCell(j);
    						cell.setCellStyle(cs);
    						// 빈 셀인 경우
    						if (cell == null) break;
    						cell.setCellStyle(cs);
    					}
    				}
                	++wkRow;
                }
                row = sheet.createRow(wkRow);
                rst = aryData.get(i);
                String str = "";
				for (short j=0;j<rst.size();j++){
					cell = row.createCell(j);
					cell.setCellValue(rst.get(headerDef.get((int)j)));
					cell.setCellStyle(cs);
					if (wkRow ==0){
						// 셀을 표시하는 오브젝트를 취득
						//cell = row.getCell(j);
						// 빈 셀인 경우
						if (cell == null) break;
					}
				}
				++wkRow;
			}
			excelFis = new FileOutputStream(filePath);
		    wb.write(excelFis);
		    excelFis.close();
		}
		catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## excelUtil.setExcel() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## excelUtil.setExcel() Exception END ##");
			throw exception;
		}

		return filePath;
	}
	

	/**
	 * <PRE>
	 * 1. MethodName	: setExcel
	 * 2. ClassName		: excelUtil
	 * 3. Commnet			: 
	 * 4. 작성자				: Administrator
	 * 5. 작성일				: 2010. 12. 29. 오후 7:13:25
	 * </PRE>
	 * 		@return String
	 * 		@param filePath
	 * 		@param headerDef
	 * 		@param aryData
	 * 		@return
	 * 		@throws Exception
	 */
	public String progSetExcel(String filePath,ArrayList<String> headerDef,ArrayList<HashMap<String,String>> aryData, String value) throws Exception
	{
		HashMap<String, String>			    rst	   = null;
		FileOutputStream excelFis = null;
		HSSFWorkbook wb = null;
		HSSFSheet sheet = null;
		HSSFCellStyle cs = null;
		HSSFDataFormat df = null;
		HSSFFont f = null;
	
		HSSFRow		currRow = null;
		HSSFCell	currCell = null;
		try {
			int 	rowCnt = 0;
			
			wb = new HSSFWorkbook();
			cs = wb.createCellStyle();
			sheet = wb.createSheet();
			df = wb.createDataFormat();
			f = wb.createFont();
			f.setFontHeightInPoints((short) 18);
			f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			
			
			cs.setFont(f);
			cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			
			currRow = sheet.createRow(1);
			currCell = currRow.createCell(0);
			currCell.setCellStyle(cs);
			currCell.setCellValue("프로그램 등록부 관리대장");
			
			sheet.addMergedRegion(new CellRangeAddress(1,1,0,9));
			
			
			
			f = wb.createFont();
			f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			f.setFontHeightInPoints((short)10);
			
			cs = wb.createCellStyle();
			currRow = sheet.createRow(3);
			currCell = currRow.createCell(0);
			
			cs.setFont(f);
			cs.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			currCell.setCellStyle(cs);
			currCell.setCellValue("기준일 : "+value);
			sheet.addMergedRegion(new CellRangeAddress(3,3,0,1));
		
			cs = wb.createCellStyle();
			currCell = currRow.createCell(9);
		
			cs.setFont(f);
			cs.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			currCell.setCellStyle(cs);
			currCell.setCellValue("단위 : 본수");
			sheet.addMergedRegion(new CellRangeAddress(3,3,9,10));
	
			f = wb.createFont();
			f.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
			df = wb.createDataFormat();
			cs = wb.createCellStyle();
			cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			cs.setFont(f);
			cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
			cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
			//cs.setDataFormat(df.getFormat("#,##0.0"));
			
			for (int i = 0;i<aryData.size();i++){
				rst = aryData.get(i);
				rowCnt = currRow.getRowNum()+1;
				currRow = sheet.createRow(rowCnt);
				for(int j=0;j<rst.size();j++)
				{
					cs = wb.createCellStyle();
					
					cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
					
					cs.setFont(f);
					cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
					cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
					cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
					cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());
					cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
					cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
					cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
					cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
			
					currCell = currRow.createCell(j);
					if(i > 0 && j > 1){
						cs.setAlignment(CellStyle.ALIGN_RIGHT);
						cs.setDataFormat(df.getFormat("#,##0"));
						currCell.setCellValue(Double.parseDouble( (rst.get(headerDef.get(j)) ) ));
					}
					else
						currCell.setCellValue(rst.get(headerDef.get(j)));
					currCell.setCellStyle(cs);
					cs = null;
				}
				rst = null;
			}
			excelFis = new FileOutputStream(filePath);
		    wb.write(excelFis);
		    excelFis.close();
		    //ecamsLogger.error("#######   setExcel  end   #######");
		}
		catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## excelUtil.progSetExcel() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## excelUtil.progSetExcel() Exception END ##");
			throw exception;
		}

		return filePath;
	}
	
	public String chkOutSetExcel(String filePath,ArrayList<String> headerDef,ArrayList<HashMap<String,String>> aryData, String sdate, String edate) throws Exception
	{
		HashMap<String, String>			    rst	   = null;
		FileOutputStream excelFis = null;
		HSSFWorkbook wb = null;
		HSSFSheet sheet = null;
		HSSFCellStyle cs = null;
		HSSFDataFormat df = null;
		HSSFFont f = null;
	
		HSSFRow		currRow = null;
		HSSFCell	currCell = null;
		try {
			int 	rowCnt = 0;
			
			wb = new HSSFWorkbook();
			cs = wb.createCellStyle();
			sheet = wb.createSheet();
			df = wb.createDataFormat();
			f = wb.createFont();
			f.setFontHeightInPoints((short) 18);
			f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			
			
			cs.setFont(f);
			cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			
			currRow = sheet.createRow(1);
			currCell = currRow.createCell(0);
			currCell.setCellStyle(cs);
			currCell.setCellValue("프로그램 대여현황");
			
			sheet.addMergedRegion(new CellRangeAddress(1,1,0,6));
			
			
			
			f = wb.createFont();
			f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			f.setFontHeightInPoints((short)10);
			
			cs = wb.createCellStyle();
			currRow = sheet.createRow(3);
			currCell = currRow.createCell(0);
			
			cs.setFont(f);
			cs.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			currCell.setCellStyle(cs);
			currCell.setCellValue("조회기간:"+sdate+"~"+edate);
			sheet.addMergedRegion(new CellRangeAddress(3,3,0,3));
			
			String totalCnt = Integer.toString(aryData.size()-1);
			
			cs = wb.createCellStyle();
			currCell = currRow.createCell(5);
			cs.setFont(f);
			cs.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			currCell.setCellStyle(cs);
			currCell.setCellValue("총 건수 : "+totalCnt+" 건");
			sheet.addMergedRegion(new CellRangeAddress(3,3,5,6));
			
			f = wb.createFont();
			f.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
			df = wb.createDataFormat();
			cs = wb.createCellStyle();
			cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			
			cs.setFont(f);
			cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
			cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
			cs.setDataFormat(df.getFormat("#,##0.0"));
			
			String str = "";
			for (int i = 0;i<aryData.size();i++){
				rst = aryData.get(i);
				rowCnt = currRow.getRowNum()+1;
				currRow = sheet.createRow(rowCnt);
				for(int j=0;j<rst.size();j++)
				{
					cs = wb.createCellStyle();
					cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
					cs.setFont(f);
					cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
					cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
					cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
					cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());	
					cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
					cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
					cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
					cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
					cs.setDataFormat(df.getFormat("#,##0.0"));
					
					currCell = currRow.createCell(j);
					if(headerDef.get(j).equals("cr_sayu")){
						str = rst.get(headerDef.get(j));
					
					if(str.length() > 20 )
						str = str.substring(0,20);
					
						currCell.setCellValue(str);
					}
					else
						currCell.setCellValue(rst.get(headerDef.get(j)));
					currCell.setCellStyle(cs);
					
				}
				rst = null;
			}
		
			excelFis = new FileOutputStream(filePath);
		    wb.write(excelFis);
		    excelFis.close();
		    //ecamsLogger.error("#######   setExcel  end   #######");
		}
		catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## excelUtil.chkOutSetExcel() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## excelUtil.chkOutSetExcel() Exception END ##");
			throw exception;
		}

		return filePath;
	}
	
	public String chkInSetExcel(String filePath,ArrayList<String> headerDef,ArrayList<HashMap<String,String>> aryData, String sdate, String edate) throws Exception
	{
		HashMap<String, String>			    rst	   = null;
		FileOutputStream excelFis = null;
		HSSFWorkbook wb = null;
		HSSFSheet sheet = null;
		HSSFCellStyle cs = null;
		HSSFDataFormat df = null;
		HSSFFont f = null;
	
		HSSFRow		currRow = null;
		HSSFCell	currCell = null;
		try {
			int 	rowCnt = 0;
			
			wb = new HSSFWorkbook();
			cs = wb.createCellStyle();
			sheet = wb.createSheet();
			df = wb.createDataFormat();
			f = wb.createFont();
			f.setFontHeightInPoints((short) 18);
			f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			
			
			cs.setFont(f);
			cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			
			currRow = sheet.createRow(1);
			currCell = currRow.createCell(0);
			currCell.setCellStyle(cs);
			currCell.setCellValue("프로그램 적용현황");
			
			sheet.addMergedRegion(new CellRangeAddress(1,1,0,6));
			
			
			
			f = wb.createFont();
			f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			f.setFontHeightInPoints((short)10);
			
			cs = wb.createCellStyle();
			currRow = sheet.createRow(3);
			currCell = currRow.createCell(0);
			
			cs.setFont(f);
			cs.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			currCell.setCellStyle(cs);
			currCell.setCellValue("조회기간:"+sdate+"~"+edate);
			sheet.addMergedRegion(new CellRangeAddress(3,3,0,3));
			
			String totalCnt = Integer.toString(aryData.size()-1);
			
			cs = wb.createCellStyle();
			currCell = currRow.createCell(5);
			cs.setFont(f);
			cs.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			currCell.setCellStyle(cs);
			currCell.setCellValue("총 건수 : "+totalCnt+" 건");
			sheet.addMergedRegion(new CellRangeAddress(3,3,5,6));
			
			f = wb.createFont();
			f.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
			df = wb.createDataFormat();
			cs = wb.createCellStyle();
			cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			
			cs.setFont(f);
			cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
			cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
			cs.setDataFormat(df.getFormat("#,##0.0"));
			
			String str = "";
			for (int i = 0;i<aryData.size();i++){
				rst = aryData.get(i);
				rowCnt = currRow.getRowNum()+1;
				currRow = sheet.createRow(rowCnt);
				for(int j=0;j<rst.size();j++)
				{
					cs = wb.createCellStyle();
					cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
					cs.setFont(f);
					cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
					cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
					cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
					cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());	
					cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
					cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
					cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
					cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
					cs.setDataFormat(df.getFormat("#,##0.0"));
					
					currCell = currRow.createCell(j);
					if(headerDef.get(j).equals("cr_sayu")){
						str = rst.get(headerDef.get(j));
						if(str.length() > 150 )
							str = str.substring(0,150);
						currCell.setCellValue(str);
					}
					else
						currCell.setCellValue(rst.get(headerDef.get(j)));
					currCell.setCellStyle(cs);
					
				}
				rst = null;
			}
		
			excelFis = new FileOutputStream(filePath);
		    wb.write(excelFis);
		    excelFis.close();
		    //ecamsLogger.error("#######   setExcel  end   #######");
		}
		catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## excelUtil.chkInSetExcel() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## excelUtil.chkInSetExcel() Exception END ##");
			throw exception;
		}

		return filePath;
	}
	public String CodeSetExcel(String filePath,ArrayList<String> headerDef,ArrayList<HashMap<String,String>> aryData, String sdate) throws Exception
	{
		HashMap<String, String>			    rst	   = null;
		FileOutputStream excelFis = null;
		HSSFWorkbook wb = null;
		HSSFSheet sheet = null;
		HSSFCellStyle cs = null;
		HSSFDataFormat df = null;
		HSSFFont f = null;
	
		HSSFRow		currRow = null;
		HSSFCell	currCell = null;
		try {
			int 	rowCnt = 0;
			
			wb = new HSSFWorkbook();
			cs = wb.createCellStyle();
			sheet = wb.createSheet();
			df = wb.createDataFormat();
			f = wb.createFont();
			f.setFontHeightInPoints((short) 18);
			f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			
			
			cs.setFont(f);
			cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			
			currRow = sheet.createRow(1);
			currCell = currRow.createCell(0);
			currCell.setCellStyle(cs);
			currCell.setCellValue("프로그램 신규 및 변경 코드별 내역");
			
			sheet.addMergedRegion(new CellRangeAddress(1,1,0,4));
			
			
			
			f = wb.createFont();
			f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			f.setFontHeightInPoints((short)10);
			/*
			cs = wb.createCellStyle();
			currRow = sheet.createRow(3);
			currCell = currRow.createCell(0);
			
			cs.setFont(f);
			cs.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			currCell.setCellStyle(cs);
			currCell.setCellValue("조회기간:"+sdate+"~"+edate);
			sheet.addMergedRegion(new CellRangeAddress(3,3,0,3));
			
			String totalCnt = Integer.toString(aryData.size()-1);
			
			cs = wb.createCellStyle();
			currCell = currRow.createCell(5);
			cs.setFont(f);
			cs.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			currCell.setCellStyle(cs);
			currCell.setCellValue("총 건수 : "+totalCnt+" 건");
			sheet.addMergedRegion(new CellRangeAddress(3,3,5,6));
			*/
			f = wb.createFont();
			f.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
			df = wb.createDataFormat();
			cs = wb.createCellStyle();
			cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			
			cs.setFont(f);
			cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
			cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
			cs.setDataFormat(df.getFormat("#,##0.0"));
			
			rowCnt = 2;
			
			for (int i = 0;i<aryData.size();i++){
				rst = aryData.get(i);
				//rowCnt = currRow.getRowNum()+1;
				rowCnt++;
				currRow = sheet.createRow(rowCnt);
				for(int j=0;j<rst.size();j++)
				{
					cs = wb.createCellStyle();
					cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
					cs.setFont(f);
					cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
					cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
					cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
					cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());	
					cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
					cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
					cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
					cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
					currCell = currRow.createCell(j);
					
					if(i>0 && j>2){
						cs.setAlignment(CellStyle.ALIGN_RIGHT);
						cs.setDataFormat(df.getFormat("#,##0"));
						currCell.setCellValue(Double.parseDouble(rst.get(headerDef.get(j))));
					}
					else
						currCell.setCellValue(rst.get(headerDef.get(j)));
					currCell.setCellStyle(cs);
				
				}
				rst = null;
			}
		
			excelFis = new FileOutputStream(filePath);
		    wb.write(excelFis);
		    excelFis.close();
		    //ecamsLogger.error("#######   setExcel  end   #######");
		}
		catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## excelUtil.CodeSetExcel() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## excelUtil.CodeSetExcel() Exception END ##");
			throw exception;
		}

		return filePath;
	}
	public String BeforeToConvertSetExcel01(String filePath,ArrayList<String> headerDef,ArrayList<HashMap<String,String>> aryData, String sdate,String edate) throws Exception
	{
		HashMap<String, String>			    rst	   = null;
		FileOutputStream excelFis = null;
		HSSFWorkbook wb = null;
		HSSFSheet sheet = null;
		HSSFCellStyle cs = null;
		HSSFDataFormat df = null;
		HSSFFont f = null;
	
		HSSFRow		currRow = null;
		HSSFCell	currCell = null;
		try {
			int 	rowCnt = 0;
			
			wb = new HSSFWorkbook();
			cs = wb.createCellStyle();
			sheet = wb.createSheet();
			df = wb.createDataFormat();
			f = wb.createFont();
			f.setFontHeightInPoints((short) 18);
			f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			
			
			cs.setFont(f);
			cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			
			currRow = sheet.createRow(1);
			currCell = currRow.createCell(0);
			currCell.setCellStyle(cs);
			currCell.setCellValue("프로그램 대여내역(형상이전)");
			
			sheet.addMergedRegion(new CellRangeAddress(1,1,0,6));
			
			
			
			f = wb.createFont();
			f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			f.setFontHeightInPoints((short)10);
			
			cs = wb.createCellStyle();
			currRow = sheet.createRow(3);
			currCell = currRow.createCell(0);
			
			cs.setFont(f);
			cs.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			currCell.setCellStyle(cs);
			currCell.setCellValue("조회기준일 : "+sdate+"~"+edate);
			
			sheet.addMergedRegion(new CellRangeAddress(3,3,0,2));
		
	
			cs = wb.createCellStyle();
			currCell = currRow.createCell(5);
		
			cs.setFont(f);
			cs.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			currCell.setCellStyle(cs);
			
			String totalCnt = Integer.toString(aryData.size()-1);
			
			currCell.setCellValue("총건수 : "+ totalCnt+" 건");
			sheet.addMergedRegion(new CellRangeAddress(3,3,5,6));
	
			f = wb.createFont();
			f.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
			df = wb.createDataFormat();
			cs = wb.createCellStyle();
			cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			cs.setFont(f);
			cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
			cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
			//cs.setDataFormat(df.getFormat("#,##0.0"));
			
			for (int i = 0;i<aryData.size();i++){
				rst = aryData.get(i);
				rowCnt = currRow.getRowNum()+1;
				currRow = sheet.createRow(rowCnt);
				for(int j=0;j<rst.size();j++)
				{
					cs = wb.createCellStyle();
					
					cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
					
					cs.setFont(f);
					cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
					cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
					cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
					cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());
					cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
					cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
					cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
					cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
			
					currCell = currRow.createCell(j);
					currCell.setCellValue(rst.get(headerDef.get(j)));
					currCell.setCellStyle(cs);
					cs = null;
				}
				rst = null;
			}
			excelFis = new FileOutputStream(filePath);
		    wb.write(excelFis);
		    excelFis.close();
		    //ecamsLogger.error("#######   setExcel  end   #######");
		}
		catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## excelUtil.BeforeToConvertSetExcel01() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## excelUtil.BeforeToConvertSetExcel01() Exception END ##");
			throw exception;
		}

		return filePath;
	}
	
	public String BeforeToConvertSetExcel04(String filePath,ArrayList<String> headerDef,ArrayList<HashMap<String,String>> aryData, String sdate,String edate) throws Exception
	{
		HashMap<String, String>			    rst	   = null;
		FileOutputStream excelFis = null;
		HSSFWorkbook wb = null;
		HSSFSheet sheet = null;
		HSSFCellStyle cs = null;
		HSSFDataFormat df = null;
		HSSFFont f = null;
	
		HSSFRow		currRow = null;
		HSSFCell	currCell = null;
		try {
			int 	rowCnt = 0;
			
			wb = new HSSFWorkbook();
			cs = wb.createCellStyle();
			sheet = wb.createSheet();
			df = wb.createDataFormat();
			f = wb.createFont();
			f.setFontHeightInPoints((short) 18);
			f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			
			
			cs.setFont(f);
			cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			
			currRow = sheet.createRow(1);
			currCell = currRow.createCell(0);
			currCell.setCellStyle(cs);
			currCell.setCellValue("프로그램 적용내역(형상이전)");
			
			sheet.addMergedRegion(new CellRangeAddress(1,1,0,7));
			
			
			
			f = wb.createFont();
			f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			f.setFontHeightInPoints((short)10);
			
			cs = wb.createCellStyle();
			currRow = sheet.createRow(3);
			currCell = currRow.createCell(0);
			
			cs.setFont(f);
			cs.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			currCell.setCellStyle(cs);
			currCell.setCellValue("조회기준일 : "+sdate+"~"+edate);
			
			sheet.addMergedRegion(new CellRangeAddress(3,3,0,2));
		
			cs = wb.createCellStyle();
			currCell = currRow.createCell(6);
		
			cs.setFont(f);
			cs.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			currCell.setCellStyle(cs);
			
			String totalCnt = Integer.toString(aryData.size()-1);
			
			currCell.setCellValue("총건수 : "+ totalCnt+" 건");
			sheet.addMergedRegion(new CellRangeAddress(3,3,6,7));
	
			f = wb.createFont();
			f.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
			df = wb.createDataFormat();
			cs = wb.createCellStyle();
			cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			cs.setFont(f);
			cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
			cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
			cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
			//cs.setDataFormat(df.getFormat("#,##0.0"));
			
			for (int i = 0;i<aryData.size();i++){
				rst = aryData.get(i);
				rowCnt = currRow.getRowNum()+1;
				currRow = sheet.createRow(rowCnt);
				for(int j=0;j<rst.size();j++)
				{
					cs = wb.createCellStyle();
					
					cs.setVerticalAlignment(CellStyle.ALIGN_CENTER);
					
					cs.setFont(f);
					cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
					cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
					cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
					cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());
					cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
					cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
					cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
					cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
			
					currCell = currRow.createCell(j);
					currCell.setCellValue(rst.get(headerDef.get(j)));
					currCell.setCellStyle(cs);
					cs = null;
				}
				rst = null;
			}
			excelFis = new FileOutputStream(filePath);
		    wb.write(excelFis);
		    excelFis.close();
		    //ecamsLogger.error("#######   setExcel  end   #######");
		}
		catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## excelUtil.BeforeToConvertSetExcel04() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## excelUtil.BeforeToConvertSetExcel04() Exception END ##");
			throw exception;
		}

		return filePath;
	}
	//예제
	public String setExcelExam(String filePath,ArrayList<String> headerDef,ArrayList<HashMap<String,String>> aryData) throws Exception
	{
		HashMap<String, String>			    rst	   = null;


		FileOutputStream excelFis = null;
		//POIFSFileSystem excelPOIF = null;

		HSSFWorkbook wb = null;

		HSSFSheet sheet = null;

		//int firstRow;
		//int lastRow;
		//int rowIdx;

		HSSFRow row = null;

		//short firstCell;
		//short lastCell;
		//short cellIdx;

		HSSFCell cell = null;
		HSSFHeader header = null;

		HSSFCellStyle cs = null;
		HSSFDataFormat df = null;

		HSSFFont f = null;

		try {
			//ecamsLogger.error("#######   setExcel  start   #######");
			

			boolean rowSw = false;
			int     wkCnt = 0;
			int     wkRow = 0;
			Workbook wb1 = new HSSFWorkbook();
			
			String tmsgId = "20110000000";
			
			Sheet sheet1 = wb1.createSheet(tmsgId.substring(0, 3)+"_DRQ." + tmsgId);
			   		CellStyle cellStyle1 = wb1.createCellStyle();
			   		cellStyle1.setAlignment(CellStyle.ALIGN_CENTER);
			   		cellStyle1.setVerticalAlignment(CellStyle.ALIGN_CENTER);
			   		cellStyle1.setFillBackgroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
			   		cellStyle1.setBorderBottom(CellStyle.BORDER_THIN);
			   		cellStyle1.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			   		cellStyle1.setBorderLeft(CellStyle.BORDER_THIN);
			   		cellStyle1.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			   		cellStyle1.setBorderRight(CellStyle.BORDER_THIN);
			   		cellStyle1.setRightBorderColor(IndexedColors.BLACK.getIndex());
			   		cellStyle1.setBorderTop(CellStyle.BORDER_THIN);
			   		cellStyle1.setTopBorderColor(IndexedColors.BLACK.getIndex());
				    
					Date today = new Date();
			   		DateFormat dateformat = DateFormat.getDateInstance(DateFormat.MEDIUM);

			   		Row row0 = sheet1.createRow((short)0);
					Row row1 = sheet1.createRow((short)1);
					row1.createCell(0).setCellValue("전문 정의서");
					sheet1.addMergedRegion(new CellRangeAddress(1,1,0,12));

					Row row2 = sheet1.createRow((short)2);
					Row row3 = sheet1.createRow((short)3);
					
					Row row4 = sheet1.createRow((short)4);
					Cell cell0 = row4.createCell(0);
					Cell cell1 = row4.createCell(1);
					sheet1.addMergedRegion(new CellRangeAddress(4,4,0,1));
					cell0.setCellValue("시스템명");
					cell0.setCellStyle(cellStyle1);
					cell1.setCellStyle(cellStyle1);
					row4.createCell(2).setCellValue("하나캐피탈차세대");
					sheet1.addMergedRegion(new CellRangeAddress(4,4,2,5));
					row4.createCell(6).setCellValue("작  성  일");
					sheet1.addMergedRegion(new CellRangeAddress(4,4,6,7));
					row4.createCell(8).setCellValue(dateformat.format(today));
					sheet1.addMergedRegion(new CellRangeAddress(4,4,8,12));
					
					Row row5 = sheet1.createRow((short)5);
					row5.createCell(0).setCellValue("서브시스템명");
					sheet1.addMergedRegion(new CellRangeAddress(5,5,0,1));		
					row5.createCell(2).setCellValue("abcd");
					sheet1.addMergedRegion(new CellRangeAddress(5,5,2,5));
					row5.createCell(6).setCellValue("작  성  자");
					sheet1.addMergedRegion(new CellRangeAddress(5,5,6,7));
					row5.createCell(8).setCellValue("시  스  템");
					sheet1.addMergedRegion(new CellRangeAddress(5,5,8,12));
					
					Row row6 = sheet1.createRow((short)6);
					row6.createCell(0).setCellValue("*전문명(ID)");
					sheet1.addMergedRegion(new CellRangeAddress(6,6,0,1));		
					row6.createCell(2).setCellValue(tmsgId);
					sheet1.addMergedRegion(new CellRangeAddress(6,6,2,5));
					row6.createCell(6).setCellValue("*전문셋명");
					sheet1.addMergedRegion(new CellRangeAddress(6,6,6,7));
					row6.createCell(8).setCellValue(tmsgId.substring(0, 3)+"_DRQ");
					sheet1.addMergedRegion(new CellRangeAddress(6,6,8,12));
					
					Row row7 = sheet1.createRow((short)7);
					row7.createCell(0).setCellValue("*전문타입");
					sheet1.addMergedRegion(new CellRangeAddress(7,7,0,1));		
					row7.createCell(2).setCellValue("D");
					sheet1.addMergedRegion(new CellRangeAddress(7,7,2,5));
					row7.createCell(6).setCellValue("FixedLength:F, XML:X, Delimited:D");
					sheet1.addMergedRegion(new CellRangeAddress(7,7,6,12));
					Row row8 = sheet1.createRow((short)8);
					row8.createCell(0).setCellValue("인터페이스");
					sheet1.addMergedRegion(new CellRangeAddress(8,8,0,1));		
					row8.createCell(2).setCellValue("IN");
					sheet1.addMergedRegion(new CellRangeAddress(8,8,2,12));
					Row row9 = sheet1.createRow((short)9);
					row9.createCell(0).setCellValue("전문설명");
					sheet1.addMergedRegion(new CellRangeAddress(9,9,0,1));		
					row9.createCell(2).setCellValue("12");
					sheet1.addMergedRegion(new CellRangeAddress(9,9,2,12));
					Row row10 = sheet1.createRow((short)10);
					Row row11 = sheet1.createRow((short)11);
					row11.createCell(0).setCellValue("일련번호");
					sheet1.addMergedRegion(new CellRangeAddress(11,12,0,0));
					row11.createCell(1).setCellValue("구분");
					sheet1.addMergedRegion(new CellRangeAddress(11,12,1,1));
					row11.createCell(2).setCellValue("필드명");
					sheet1.addMergedRegion(new CellRangeAddress(11,12,2,2));
					row11.createCell(3).setCellValue("설명");
					sheet1.addMergedRegion(new CellRangeAddress(11,12,3,3));
					row11.createCell(4).setCellValue("속성");
					sheet1.addMergedRegion(new CellRangeAddress(11,12,4,4));
					row11.createCell(5).setCellValue("FixedLength");
					row11.createCell(6).setCellValue("XML");
					sheet1.addMergedRegion(new CellRangeAddress(11,11,6,7));
					row11.createCell(8).setCellValue("Delimited");
					row11.createCell(9).setCellValue("부모필드");
					sheet1.addMergedRegion(new CellRangeAddress(11,12,9,9));
					row11.createCell(10).setCellValue("반복횟수값\n참조필드");
					sheet1.addMergedRegion(new CellRangeAddress(11,12,10,10));
					row11.createCell(11).setCellValue("반복횟수");
					sheet1.addMergedRegion(new CellRangeAddress(11,12,11,11));
					row11.createCell(12).setCellValue("디폴트 값");
					sheet1.addMergedRegion(new CellRangeAddress(11,12,12,12));
					Row row12 = sheet1.createRow((short)12);
					row12.createCell(5).setCellValue("길이");
					row12.createCell(6).setCellValue("태그명");
					row12.createCell(7).setCellValue("CDATA\n여부");
					row12.createCell(8).setCellValue("구분자");
			
			excelFis = new FileOutputStream(filePath);
			wb1.write(excelFis);
		    excelFis.close();
		    //ecamsLogger.error("#######   setExcel  end   #######");
		}
		catch (Exception exception) {
			exception.printStackTrace();
			ecamsLogger.error("## excelUtil.getArrayCollection() Exception START ##");
			ecamsLogger.error("## Error DESC : ", exception);
			ecamsLogger.error("## excelUtil.getArrayCollection() Exception END ##");
			throw exception;
		}

		return filePath;
	}

}
