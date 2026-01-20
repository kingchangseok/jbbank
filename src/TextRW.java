
import java.io.*;
import java.util.*;

public class TextRW
{
	public TextRW()
	{
	}

	public void TextWrite(String str, String filename)   //flag 1:CheckOut 신청목록  2:CheckIn 신청목록  3:Error
	{
		String temp=filename;
		try{
			File file=null;
			file = new File(temp);              //File 불러온다.
			if( !(file.isFile()) )              //File이 없으면 
			{
				file.createNewFile();          //File 생성
			}
			//FileOutputStream fos= new FileOutputStream(file);   //File을 불러옴(덮어씀.)
			//fos.write(str.getBytes());              //File List(에러부분)을 Wirte
			//fos.close();                           

			OutputStreamWriter writer = new OutputStreamWriter( new FileOutputStream(filename)); 
			writer.write(str);
			writer.close();

		}catch(IOException e){       
			System.out.println(e);
		}
	}
	
	public byte[] TextRead(String filename)
	{
		ByteArrayOutputStream baosRetStream = new ByteArrayOutputStream();
		String url=filename;
		try
		{
			File file=null;
			file = new File(url);
			byte[] byteTmpBuf = new byte[8192];
			FileInputStream fis = new FileInputStream(file); 

			int i;
			String str="";
			while( (i=fis.read(byteTmpBuf)) > -1 )
			{ 
				baosRetStream.write(byteTmpBuf, 0, i);
			} 
			fis.close();
		}
		catch(IOException e)
		{    
			System.out.println(e);
		}
		return baosRetStream.toByteArray();
	}
}
