package me.matthewmerrill.ek;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import spark.Session;

public class LoginManager {
	
	// Map from Username to Secret Key
	private static Map<String, String> keyMap = new HashMap<>();
	
	// Map from Username Cookie -> Spark Session
	private static Map<String, Session> sesMap = new HashMap<String, Session>();
	
	private static int nextUserNumber = 1;
	private static String key;
	
	
	
	public static void main(String[] args) {
		
		login("TESTING!", null);
		
	}
	
	public static String login(String username, Session session) {
		
		int sesId = nextUserNumber++;
		String text = username + (sesId);
		
		String key = "XAVIERLAMPHERE!!"; // 128 bit key
        
        // Create key and cipher
        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES");
	        // encrypt the text
	        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
	        byte[] encrypted = cipher.doFinal(text.getBytes());
	        System.err.println(new String(encrypted));
	        // decrypt the text
	        cipher.init(Cipher.DECRYPT_MODE, aesKey);
	        String decrypted = new String(cipher.doFinal(encrypted));
	        System.err.println(decrypted);
	        
	        return new String(encrypted);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Session getSession(String username, String encrypted) throws Exception {
		String key = keyMap.get(username);
        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");

		Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        String decrypted = new String(cipher.doFinal(encrypted.getBytes()));
        System.err.println(decrypted);
        
        return sesMap.get(decrypted);
	}
	
}
