/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stockgrabber;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import redis.clients.jedis.Jedis;
import stockgrabber.model.StockData;


/**
 *
 * @author tiago
 */
public class StockGrabber {

    public static final int BULK_SIZE = 10;
    
    private static Jedis jedis;
    
    private static Connection c = null;

    static {
        try {
            c = DriverManager.getConnection(
                                        "jdbc:derby://localhost:1527/quotes");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        jedis = new Jedis("localhost");
    }
    
    public static Connection getConnection() {
    	return c;
    }
    
    public static Jedis getPublisher() {
    	return jedis;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
    	String[] filenames = new String[]{"NASDAQ-part1.txt","NASDAQ-part2.txt","NASDAQ-part3.txt",
    									  "NASDAQ-part4.txt","NASDAQ-part5.txt","NASDAQ-part6.txt"};
    	
    	for(String filename : filenames) {
    		new GrabberThread(filename).start();
    	}
    	//StockData d = new StockData("blah", "nasdaq", 100, Calendar.getInstance(), 100, 10);
    	
    	//JSONObject o = (JSONObject)JSONObject.wrap(d);
    	
    	//System.out.println(d.toString());
//    	ArrayList<String> t = new ArrayList<String>();
//    	t.add("GOOG");
//    	t.add("MSFT");
//    	
//    	YahooProvider p = new YahooProvider("NASDAQ");
//    	
//    	List<StockData> q = p.getManyStockQuotes(t);
//    	
//    	for(StockData s : q) {
//    		System.out.println(s);
//    	}
//    	System.exit(0);
    	
//    	GoogleProvider provider = new GoogleProvider("NASDAQ");
    	
    	
    }
    
    
    
    private static void debug(List<StockData> ds) {
        for(StockData d : ds) {
            System.out.println(d);
        }
    }
}
