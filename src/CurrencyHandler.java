import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CurrencyHandler {
	private static enum CurrencyPlural {PLURAL, SINGLE};
	final static double failNum = -999999999;
	
	static final String ACCESS_KEY = "254644af02faf9512c2437384d29d3e3";
    static final String BASE_URL = "http://api.currencylayer.com/";
    static final String ENDPOINT = "live";

    static CloseableHttpClient httpClient = HttpClients.createDefault();
	    
	private static HashMap<String, String> currencyAbbr = new HashMap<>() {{
		put("THB","baht");
		put("EUR","euro(s)");
		put("USD","dollar(s)");
		put("JPY","yen");
		put("KRW","won");
		put("GBP","British pound(s)");
		put("CHF","Swiss franc");
		put("CAD","Canadian dollar(s)");
		put("ZAR","rand(s)");
		
		put("MXN","Mexican peso(s)");
		put("HKD","Hong Kong dollar(s)");
		put("INR","rupee(s)");
		put("NOK","Norwegian krone(s)");
		put("TWD","New Taiwan dollar(s)");
		put("ILS","new shekel(s)");
		put("PHP","Phillipine peso(s)");
		put("ARS","Argentine peso(s)");
		put("COP","Columbian peso(s)");
		put("BRL","Brazillian real(s)");
		put("RUB","ruble(s)");
		put("AED","dirham");
		put("CNY","yuan");
		put("SEK","Swedish krona");
		put("SGD","Singapore dollar(s)");
		put("TRY","Turkish lira");
		put("NZD","New Zealand dollar(s)");
	}};
	
	private static HashMap<String, String> currencySymbol = new HashMap<>() {{
		put("THB","฿");
		put("EUR","€");
		put("USD","$");
		put("JPY","¥");
		put("KRW","₩");
		put("GBP","£");
		put("CHF","₣");
		put("CAD","$");
		put("ZAR","R");
		
		put("MXN","$");
		put("HKD","HK$");
		put("INR","₹");
		put("NOK","kr");
		put("TWD","NT$");
		put("ILS","₪");
		put("PHP","₱");
		put("ARS","$");
		put("COP","$");
		put("BRL","R$");
		put("RUB","₽");
		put("AED","د.إ");
		put("CNY","¥");
		put("SEK","kr");
		put("SGD","S$");
		put("TRY","₺");
		put("NZD","$");
	}};
	
	public static boolean canConvertCurrency(String abbr) {
		for (String key : currencyAbbr.keySet()) {
			if (key.equals(abbr)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static double convertCurrency(double amount, String abbr) {
		if (canConvertCurrency(abbr)) {
			HttpGet get = new HttpGet(BASE_URL + ENDPOINT + "?access_key=" + ACCESS_KEY);

	        try {
	            CloseableHttpResponse response =  httpClient.execute(get);
	            HttpEntity entity = response.getEntity();
	            
	            // the following line converts the JSON Response to an equivalent Java Object
	            JSONObject exchangeRates = new JSONObject(EntityUtils.toString(entity));
	            double conversionRate = exchangeRates.getJSONObject("quotes").getDouble("USD"+abbr);
	            response.close();
	            return conversionRate;
	        } catch (Exception e) {
	            return failNum;
	        }
		} else { return failNum; }
	}
	
	public static double convertCurrency(double amount, String abbrTo, String abbrFrom) {
		if (canConvertCurrency(abbrFrom) && canConvertCurrency(abbrTo)) {
			HttpGet get = new HttpGet(BASE_URL + ENDPOINT + "?access_key=" + ACCESS_KEY);

	        try {
	            CloseableHttpResponse response =  httpClient.execute(get);
	            HttpEntity entity = response.getEntity();
	            
	            // the following line converts the JSON Response to an equivalent Java Object
	            JSONObject exchangeRates = new JSONObject(EntityUtils.toString(entity));
	            double newAmount = exchangeRates.getJSONObject("quotes").getDouble(abbrFrom+abbrTo);
	            response.close();
	            return newAmount;
	        } catch (Exception e) {
	            return failNum;
	        }
		} else { return failNum; }
	}
	
	public static boolean hasErrored(double num) {
		return (num==failNum) ? true:false;
	}
	
	public static String getCurrencyName(String abbr) {
		return currencyAbbr.get(abbr);
	}
	
	public static String getCurrencySymbol(String abbr) {
		return currencySymbol.get(abbr);
	}
}
