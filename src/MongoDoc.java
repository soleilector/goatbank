import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

public class MongoDoc {
	private MongoCollection<Document>collection;
	private Document doc;
	
	public MongoDoc(String field, String fieldValue, MongoCollection<Document> collection) {
		UI_Base.printDebug("doc creating");
		doc = collection.find(eq(field, fieldValue)).first();
		
		if (doc == null) {
			UI_Base.printDebug("doc create failure");
		} else {
			UI_Base.printDebug("doc create success");
		}
	}
	
	public String getJson() {
		return doc.toJson();
	}

	public Document getDoc() {
		return this.doc;
	}
	
	public String getProp(String key) {
		String propertyValType = MongoDB.propertyValTypes.get(key);
		if (propertyValType == null) {
			return this.doc.getString(key);
		} else {
			switch(propertyValType) {
			case "double":
				return String.valueOf(this.doc.getDouble(key));
			default:
				return this.doc.getString(key);
			}
		}
	}
}
