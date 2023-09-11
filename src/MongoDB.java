import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.HashMap;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class MongoDB {
	private static MongoDB[] dbList = new MongoDB[10];
	private MongoDatabase db;
	private MongoClient dbClient;
	private static HashMap<String, MongoCollection<Document>> collectionCache = new HashMap<>();
	public static HashMap<String,String> propertyValTypes = new HashMap<>() {{
		put("balance","double");
	}};
	
	public MongoDB(String dbName, MongoClient mongoClient) {
		this.dbClient = mongoClient;
		this.db = getDB(dbName);
	}

	public MongoDatabase getDB(String dbName) { // create database
		MongoDatabase database = this.dbClient.getDatabase(dbName);
		return database;
	}
	
	public static MongoClient attemptConnection(String uri) {
		try {
			MongoClient mongoClient = ConnectMongo(uri);
			//mongoClent.close();
			return mongoClient;
		} catch (Exception e) {
			return null;
		}
	}
	
	private static MongoClient ConnectMongo(String uri) { // create client
		MongoClient mongoclient = MongoClients.create(uri);
		return mongoclient;
	}
	
	public static MongoCollection<Document> getCollection(String colName) {
		return MongoDB.collectionCache.get(colName);
	}
	
	public void addCollection(String colName) {
		MongoCollection<Document> collection = this.db.getCollection(colName);
		if (collection != null) {
			this.collectionCache.put(colName,collection);
		}
	}
	
	public MongoDoc getDoc(String field, String fieldValue, String colName) {
		MongoCollection<Document> collection = this.getCollection(colName);
		UI_Base.printDebug("doc searching ["+field+"/"+fieldValue+"]");
		MongoDoc doc = new MongoDoc(field, fieldValue,collection);
		UI_Base.printDebug("doc search success");
		
		return doc;
	}
	
	public static boolean userExists(String userName, String colName) {
		MongoCollection<Document> collection = MongoDB.getCollection(colName);
		return collection.find(new Document("user", userName)).first() != null;
	}
	
	public static boolean userRegister(String user, String pass) {
		if (!userExists(user,"db_users")) { // user does not exist
			MongoCollection<Document> collection = MongoDB.getCollection("db_users");
			
			Document doc = new Document("user",user);
			doc.append("pass",pass);
			doc.append("fName","John");
			doc.append("lName","Doe");
			doc.append("balance",0.00);
			
			collection.insertOne(doc);
			
			return true;
		}
		
		return false;
	}
	
	public static boolean userRegister(String user, String pass, String fName, String lName, double balance) {
		if (!userExists(user,"db_users")) { // user does not exist
			MongoCollection<Document> collection = MongoDB.getCollection("db_users");
			
			Document doc = new Document("user",user);
			doc.append("pass",pass);
			doc.append("fName",fName);
			doc.append("lName",lName);
			doc.append("balance",balance);
			doc.append("email", "");
			
			collection.insertOne(doc);
			
			return true;
		}
		
		return false;
	}
	
	public static boolean userRegister(String user, String pass, String fName, String lName, double balance, String email) {
		if (!userExists(user,"db_users")) { // user does not exist
			MongoCollection<Document> collection = MongoDB.getCollection("db_users");
			
			Document doc = new Document("user",user);
			doc.append("pass",pass);
			doc.append("fName",fName);
			doc.append("lName",lName);
			doc.append("balance",balance);
			doc.append("email", email);
			
			collection.insertOne(doc);
			
			return true;
		}
		
		return false;
	}
	
	public static boolean userSave(BankUser thisUser) {
		if (userExists(thisUser.getUser(), "db_users")) {
			HashMap<String, String> userSessionData = thisUser.getSessionData();
			
			// import bson [ ]
			// finish update code [ ]
			// allow user to configure their name (substring), user, or change their password [ ]
			// attempt to integrate currency conversion [ ]
			// implement password reset through email api [ ]
			MongoCollection<Document> userDataCollection = getCollection("db_users");
			
			try {
				userDataCollection.updateMany(
					    Filters.eq("user", thisUser.getUser()), // filters through documents for this code
					    Updates.combine(
					        Updates.set("balance", Double.parseDouble(userSessionData.get("balance"))),
					        Updates.set("fName", userSessionData.get("fName")),
					        Updates.set("lName", userSessionData.get("lName")),
					        Updates.set("user", userSessionData.get("user")),
					        Updates.set("pass", userSessionData.get("pass")),
					        Updates.set("email", userSessionData.get("email"))
					    ));
				
				//System.out.println("update successful");
				return true;
			} catch (Exception e) {
				UI_Base.printMsg("FailSave",2,2);
			}
		}
		return false;
	}
	
	public static boolean closeAccount(BankUser thisUser) {
		MongoCollection<Document> userDataCollection = getCollection("db_users");
		
		try {
			Bson filter = Filters.eq("user", thisUser.getUser());
			userDataCollection.deleteOne(filter);
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}
	
	public static String genNewPass(String userName,String sendEmail) {
		if (userExists(userName, "db_users")) {
			try {
				MongoCollection<Document> userDataCollection = getCollection("db_users");
				
				String newPass = BankUser.genNewPass();
				
				userDataCollection.updateMany(
					    Filters.eq("user", userName), // filters through documents for this code
					    Updates.combine(
					        Updates.set("pass", newPass)
					    ));
				
				return newPass;
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}
	
	public static String genNewPass(String userName) {
		if (userExists(userName, "db_users")) {
			try {
				MongoCollection<Document> userDataCollection = getCollection("db_users");
				
				String newPass = BankUser.genNewPass();
				
				userDataCollection.updateMany(
					    Filters.eq("user", userName), // filters through documents for this code
					    Updates.combine(
					        Updates.set("pass", newPass)
					    ));
				
				return newPass;
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}
	
}
