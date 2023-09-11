// Creates a session-layer bank account object for the logged in user
import java.util.Scanner;
import java.util.HashMap;

public class AppSettings {
	private static final String appName = "GoatBank";
	private boolean isDebugging = false;
	final private static String[] fieldsCanBeBlank = {"email"};
	final private static HashMap<String, Integer> minEntryChars = new HashMap<>() {{
		put("user",3);
		put("pass",3);
		put("fName",3);
		put("lName",3);
		put("email",5);
		put("newName",5);
		put("deposit",1);
		put("withdraw",1);
		put("startBal",1);
		put("transfer",1);
	}};
	
	final private static HashMap<String, Integer> maxEntryChars = new HashMap<>() {{
		put("user",15);
		put("pass",30);
		put("fName",20);
		put("lName",20);
		put("email",30);
		put("newName",50);
		put("deposit",9);
		put("withdraw",9);
		put("startBal",9);
		put("transfer",9);
	}};
	
	final private static String quitKey = "quit";
	
	public static Integer getMinEntryChar(String fieldName) {
		return minEntryChars.get(fieldName);
	}
	
	public static Integer getMaxEntryChar(String fieldName) {
		return maxEntryChars.get(fieldName);
	}
	
	public static String getAppName() {
		return appName;
	}
	
	public static String getQuitKey() {
		return quitKey;
	}
	
	public static boolean fieldCanBeBlank(String fieldName) {
		for (String field : fieldsCanBeBlank) {
			if (field.equals(fieldName)) {
				return true;
			}
		}
		return false;
	}
}
