package html.app.common.token;

public class TokenSecretKey {
	private static String secretKey;

	public static String getSecretKey() {
		return secretKey;
	}

	public static void setSecretKey(String secretKey) {
		TokenSecretKey.secretKey = secretKey;
	}
	
}
