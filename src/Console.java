import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;
import org.slf4j.LoggerFactory;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

import java.util.Scanner;
import java.util.HashMap;

public class Console {
    public static void main( String[] args ) {
    	// Replace the placeholder with your MongoDB deployment's connection string
        String uri = "mongodb+srv://pompompies:QAIrhJsAt7tFRz6I@cis207final.hmn0gcf.mongodb.net/?retryWrites=true&w=majority";
        Scanner input = new Scanner(System.in);
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
        rootLogger.setLevel(Level.OFF);
        
        MongoClient mongoClient = MongoDB.attemptConnection(uri);
        String entry;
        while (mongoClient==null) {
        	UI_Base.printMsg("NeedsInternet");
        	entry = input.nextLine();
        	if (entry.equals(AppSettings.getQuitKey())) { return; } // end program
        	mongoClient = MongoDB.attemptConnection(uri);
        }
        
        UI_Base.printMsg("Loading");
    	if (mongoClient != null) { // connected to mongo
        	MongoDB thisdbase = new MongoDB("db_users", mongoClient);
        	UI_Base.printDebug("client exists");
        	if (thisdbase != null) { // user database found
        		UI_Base.printDebug("db exists");
        		// log the user in
        		String quitKey = AppSettings.getQuitKey();
        		thisdbase.addCollection("db_users"); // prepare collection to be searched
        		
        		while (true) {
        			UI_Base.printMenu("Main");
        			UI_Base.printMsg("MainMenuPrompt");
        			String mainAction = input.nextLine();
        			
        			UI_Base.performMenuAction(mainAction, thisdbase);
        		}
        	}
        }
    }
}