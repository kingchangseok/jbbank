package com.ecams.service.list;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class RC4 {
	
    // RC4로 문자열 암호화
    public static String encryptRC4String(String keyString, String plainText) {
	  	byte[] cipherText=null;
	  	//Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	  	  
	  	try {
	        byte[] keyBytes = keyString.getBytes("UTF-8");
	        byte[] plainTextBytes = plainText.getBytes("UTF-8");
	      
	        SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "RC4");
	        Cipher cipher = Cipher.getInstance("RC4");
	        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
	        cipherText = cipher.doFinal(plainTextBytes); 
	  	} catch (Exception e) {
	  		cipherText=null;	
	  	}
	      
	  	
	  	return BinArray2Hex(cipherText);
	      //return cipherText;
	      
    }
    
    public static String decryptRC4String(String keyString, String cipherText) {
    	String plainText="";
    	//Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    	  
    	try {
    		byte[] keyBytes = keyString.getBytes("UTF-8");
    		byte[] cipherTextBytes = Hex2BinArray(cipherText);
    		
    		SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "RC4");
    		Cipher cipher = Cipher.getInstance("RC4");
    		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    		plainText = new String(cipher.doFinal(cipherTextBytes),"UTF-8"); 
    	} catch (Exception e) {
    		plainText="";	
    	}
        return plainText;
    }
    

    public static String BinArray2Hex(byte buf[]) {
	  	if (buf==null) return "";
	
	  	StringBuffer strbuf = new StringBuffer(buf.length * 2);
	  	int i;

	  	for (i = 0; i < buf.length; i++) {
	  		if (((int) buf[i] & 0xff) < 0x10)
	  			strbuf.append("0");

	  		strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
	  	}

	  	return strbuf.toString();
    }
    

    public static byte[] Hex2BinArray( String hexStr ){
    	byte bArray[] = new byte[hexStr.length()/2];  
    	for(int i=0; i<(hexStr.length()/2); i++){
    		byte firstNibble  = Byte.parseByte(hexStr.substring(2*i,2*i+1),16); // [x,y)
    		byte secondNibble = Byte.parseByte(hexStr.substring(2*i+1,2*i+2),16);
    		int finalByte = (secondNibble) | (firstNibble << 4 ); // bit-operations only with numbers, not bytes.
    		bArray[i] = (byte) finalByte;
    	}
    	return bArray;
    }
}
