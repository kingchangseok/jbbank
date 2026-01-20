package com.ecams.util.fileupload.vo;


import java.io.File;
import java.io.InputStream;

import org.apache.logging.log4j.Logger;

import com.ecams.common.logger.EcamsLogger;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties({"content"})
public class FileMeta {
	private String fileName;
	private String saveName;
	private String fileSize;
	private String filegb;
	private String fileType;
	private String noticeAcptno;
	private String seq;


	private InputStream content;



	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {

        //ecamsLogger.error("1MultipartrequestHandler.getFilename=======>"+fileName);
		if(fileName.indexOf(File.separator) > -1) {
			fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1);
		} else if (fileName.indexOf("\\") > -1) {
			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
		}
        //ecamsLogger.error("2MultipartrequestHandler.getFilename=======>"+fileName);

		this.fileName = fileName;
	}
	public String getSaveName() {
		return saveName;
	}
	public void setSaveName(String saveName) {
		this.saveName = saveName;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public String getFilegb() {
		return filegb;
	}
	public void setFilegb(String filegb) {
		this.filegb = filegb;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public InputStream getContent(){
		return this.content;
	}
	public void setContent(InputStream content){
		this.content = content;
	}
	public String getNoticeAcptno() {
		return noticeAcptno;
	}
	public void setNoticeAcptno(String noticeAcptno) {
		this.noticeAcptno = noticeAcptno;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	@Override
	public String toString() {
		return "FileMeta [fileName=" + fileName + ", saveName= "+ saveName + ", fileSize=" + fileSize + ", fileType=" + fileType
				+ ", noticeAcptno=" + noticeAcptno + ", content=" + content + "]";
	}


}
