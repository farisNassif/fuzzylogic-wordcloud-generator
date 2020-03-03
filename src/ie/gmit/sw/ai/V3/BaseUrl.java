package ie.gmit.sw.ai.V3;

public class BaseUrl {
	public static String getBaseUrl(String absUrl) {
		String baseUrl = null;
		try {
			baseUrl = absUrl.substring(0, absUrl.indexOf('/', absUrl.indexOf("//") + 2));
			// System.out.println(baseUrl + "<== Base");
		} catch (Exception e) {
			System.out.println(e);
		}
		return baseUrl;
	}
}
