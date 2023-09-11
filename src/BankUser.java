import java.util.HashMap;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import org.apache.commons.lang3.RandomStringUtils;

public class BankUser {
	private static String accRegisterDetailFieldsOrder[] = {"fName","lName","startBal","pass","email"};
	
	private static HashMap<String,String> accDetailFields = new HashMap<>() {{
		put("fName","");
		put("lName","");
		put("startBal","0");
		put("pass","");
		put("email","");
	}};
	
	private HashMap<String, String> sessionData = new HashMap<>();
	enum SessionStatus {LOGGEDIN, LOGGEDOUT};
	private SessionStatus sessionStatus = SessionStatus.LOGGEDIN; // defaultly defined as logged in
	private String onMenu = "UserMainMenu";
	private MongoDoc thisDoc;
	
	public BankUser(MongoDoc userDoc) { // construct from a Mongodoc
		sessionData.put("fName", userDoc.getProp("fName"));
		sessionData.put("lName", userDoc.getProp("lName"));
		sessionData.put("balance", userDoc.getProp("balance"));
		sessionData.put("user", userDoc.getProp("user"));
		sessionData.put("pass", userDoc.getProp("pass"));
		sessionData.put("email", userDoc.getProp("email"));
		
		this.thisDoc = userDoc;
	}
	
	public MongoDoc getDoc() {
		return this.thisDoc;
	}
	
	public String getUser() {
		return sessionData.get("user");
	}
	
	public String getPass() {
		return sessionData.get("pass");
	}
	
	public String getFirstName() {
		return sessionData.get("fName");
	}
	
	public String getLastName() {
		return sessionData.get("lName");
	}
	
	public double getBalance() {
		return Double.parseDouble(sessionData.get("balance"));
	}
	
	public String getEmail() {
		return sessionData.get("email");
	}
	
	public void setProperty(String propertyName, String value) {
		if (isPropertyNameValid(propertyName)) {
			sessionData.put(propertyName, value);
		}
	}
	
	public boolean isPropertyNameValid(String thisPropName) {
		for (String propName : sessionData.keySet()) {
			if (thisPropName.equals(propName)) {
				return true;
			}
		}
		return false;
	}
	
	public HashMap<String,String> getSessionData() { // for saving to db
		return sessionData;
	}
	
	public boolean isLoggedIn() {
		return sessionStatus==SessionStatus.LOGGEDIN;
	}
	
	public void logOut() {
		sessionStatus = SessionStatus.LOGGEDOUT;
	}
	
	public String onMenu() { // return the menu the user is currently on
		return onMenu;
	}
	
	public void setMenu(String menuName) { // set the menu the user is on
		HashMap<String, String> thisMenuActions = UI_Base.getMenuActions(menuName);
		if (thisMenuActions != null) {
			onMenu = menuName;
		}
	}
	
	public void deposit(double amount) { //deposit with number val// create checking and savings later
		amount += Double.parseDouble(sessionData.get("balance"));
		sessionData.replace("balance", Double.toString(amount));
	}
	
	public void deposit(String amount) { // deposit with String value
		double oldAmount = Double.parseDouble(amount);
		double newAmount = oldAmount + Double.parseDouble(sessionData.get("balance"));
		sessionData.replace("balance", Double.toString(newAmount));
		UI_Base.printMsg("DepositSuccess", 1, 1);
	}
	
	public boolean canWithdraw() {
		return getBalance() >= 0;
	}
	
	public void withdraw(double amount) {
		boolean inDebt = getBalance() < 0; // continueregistration ternary operator make more efficient
		if (inDebt) {
			UI_Base.printMsg("DebtWithdraw", 1, 1);
		} else {
			double newAmount = Double.parseDouble(sessionData.get("balance"))-amount;
			//System.out.println("withdrawing "+amount);
			sessionData.replace("balance", Double.toString(newAmount));
			UI_Base.printMsg("WithdrawSuccess", 1, 1);
		}
	}
	
	public void withdraw(String amount) {
		double oldAmount = Double.parseDouble(amount);
		if (!canWithdraw()) {
			UI_Base.printMsg("DebtWithdraw", 1, 1);
		} else {
			double newAmount = Double.parseDouble(sessionData.get("balance"))-oldAmount;
			sessionData.replace("balance", Double.toString(newAmount));
		}
	}
	
	public void transfer(double amount, String userName) { // transfer money between users
		if (MongoDB.userExists(userName, "db_users")) { // if this user exists
			// update
			MongoCollection<Document> userDataCollection = MongoDB.getCollection("db_users");
			MongoDoc transfereeDoc = new MongoDoc("user", userName, userDataCollection); // create a document
			
			try {
				/*
				System.out.println("Transferee new bal: "
					+Double.parseDouble(transfereeDoc.getProp("balance"))
					+amount);
				System.out.println("Transfering "+amount);
				*/
				userDataCollection.updateMany(
					    Filters.eq("user", userName), // filters through documents for this code
					    Updates.combine(
					        Updates.set("balance", (Double.parseDouble(transfereeDoc.getProp("balance"))+amount))
					    ));
				
				withdraw(amount);
				
				UI_Base.print("Transfer was a success!");
			} catch(Exception e) {
				UI_Base.print("Transfer failed...");
			}
		}
		
	}
	
	public static String genNewPass() { //@credit to KodeJava
		 String result = RandomStringUtils.random(8, 0, 20, true, true,
	                "qw32rfHIJk9iQ8Ud7h0X".toCharArray());
	        return result;
	}
	
	public static HashMap<String,String> getAccDetailFields() {
		return accDetailFields;
	}
	
	public static String[] getRegisterAccDetailOrder() {
		return accRegisterDetailFieldsOrder;
	}
}
