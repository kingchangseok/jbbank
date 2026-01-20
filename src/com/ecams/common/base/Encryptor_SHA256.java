package com.ecams.common.base;



import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryptor_SHA256 {

	private static Encryptor_SHA256 instance;
  	public static Encryptor_SHA256 instance()

  	{

  		if(instance == null)

  		{

  			instance = new Encryptor_SHA256();	

  		}

  		return instance;

  	}

	

    public String encryptSHA256(String str){



    	String sha = ""; 



    	try{

    		MessageDigest sh = MessageDigest.getInstance("SHA-256"); 

    		sh.update(str.getBytes()); 

    		byte byteData[] = sh.digest();

    		StringBuffer sb = new StringBuffer(); 

    		for(int i = 0 ; i < byteData.length ; i++){

    			sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));

    		}



    		sha = sb.toString();



    	}catch(NoSuchAlgorithmException e){

    		System.out.println("Encrypt Error - NoSuchAlgorithmException");

    		sha = null; 

    	}



    	return sha;

    }	

}

