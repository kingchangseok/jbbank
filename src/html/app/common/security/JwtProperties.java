package html.app.common.security;

public class JwtProperties {

	public static final String SECURITY_SECRET_KEY = "ecams123!";

	//validity in milliseconds
	public static final long SECURITY_TOKEN_EXPIRE = 180000000; // 1h(1000*60*60), 30m(1000*60*30), 1m(1000*60)

	public static final String SECURITY_TOKEN_HEADER = "Authorization";
}