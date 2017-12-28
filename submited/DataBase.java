
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
//import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
//import java.net.URLConnection;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.Hashtable;
//import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

public class DataBase {
	public static Hashtable<String,Hashtable <String,String>> hash = new  Hashtable<String,Hashtable<String,String>>();
	public int rows=0;
	public String [][] table;
	private Object[][] results;
	//String key;
	//inisual all attribues to generate url
	public String categories="";//inisual the product categories on shopgoodwill.com
	public String seller="";//inisual seller number
	public String priceMin="0";//inisual the low price
	public String priceMax="999999";//inisual the high price
	public String orderBy="1";//inisual the order number
	public boolean closed=false;//inisual the closed auction is false
	public boolean descendingOrder=false;//inisual in ascending order
	public boolean buyNowOnly=false;//inisual no buy now only
	public boolean pickupOnly=false;//inisual no pickup onluy
	public boolean noPickupOnly=false;//inisual pick up can be unlimited
	public boolean onecentShipment=false;//inisual no one cent shipment
	public boolean canadaShipment=false;//inisual the not in canada
	public boolean outUsshipment=false;//inisual not out side of us&canada
	static BufferedImage image;
	void updateDatabase(BufferedReader in,BufferedWriter bf) throws IOException {
		String item;
		while((item=in.readLine())!=null) {
				bf.write(onlineSearchResult(item));
		}
		
	}
	public  String onlineSearchResult(String keyword) throws IOException {
		
		String newline;
		int totalPages=1;
		String productNum="";
		String result="";
		String item;
		try {
			item = URLEncoder.encode(keyword, "UTF-8");
			item = keyword.replaceAll("\\s", "%20");
			DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("MM/d/yyyy");
			LocalDate localDate = LocalDate.now();
		int page=1; //initial first page as first url
		do {
			
			URL url = new URL("https://www.shopgoodwill.com/Listings?st=%20"+item+"&sg=&c="+categories+"&s="
					+seller+"&lp="+priceMin+"&hp="+priceMax+"&sbn="+buyNowOnly
					+"&spo="+pickupOnly+"&snpo="+noPickupOnly+"&socs="+onecentShipment+"&sd=false&sca="+closed+"&caed="
					+dateformat.format(localDate)+"&cadb=7&scs="
					+canadaShipment+"&sis="+outUsshipment+"&col="+orderBy+"&p="+page+"&ps=40&desc="+descendingOrder+"&ss=0&UseBuyerPrefs=true");
			
		  BufferedReader in = new BufferedReader(
			        new InputStreamReader(url.openStream()));
		String productNumberRegex = "(?<=Product #: </span>)(.*)(?=<\\/div>)";//search the productnumber by observe in the url
		String totalPagesRegex = "(?<=\"last\" data-page=\")(.*)(?=\" onclick)";
		while((newline=in.readLine())!=null) {        //search item features each time
			Pattern pattern1 = Pattern.compile(productNumberRegex);
			Matcher matcher1 = pattern1.matcher(newline);
			Pattern pattern2 = Pattern.compile(totalPagesRegex);
			Matcher matcher2 = pattern2.matcher(newline);
			if(matcher1.find()) {
			productNum = matcher1.group();
			result +=itemInfoOnline(productNum);
			}
			if(matcher2.find()) {
				totalPages = Integer.parseInt(matcher2.group());
			}
			
		}
	
		in.close();
		page++;
		
		}while(page<=totalPages);
		} catch (UnsupportedEncodingException ignored) {
		    // Can be safely ignored because UTF-8 is always supported
		}
		
		return result;
	}
	public void insertSearchOnlineData(BufferedWriter bw){
		
	}
	public  String itemInfoOnline(String productNum) throws IOException {
		String title="";
		String price="";
		String dateOfAuction="";
		String seller="";
		String picUrl="";
		String newline;
		String result="";
	Timestamp time = new Timestamp(System.currentTimeMillis());
	URL url = new URL ("https://www.shopgoodwill.com/Item/"+productNum);
	BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream()));
		String titleRegex = "(?<=\\<title>)(.*)(?=\\s-)";
		String priceRegex = "(?<=current-price\"\\>)(.*)(?=<\\/span>)";
		String dateOfAuctionRegex = "(?<=Ends On: <\\/b>)(.*)(?=Pacific Time)";
		String sellerRegex = "(?<=Seller: <\\/b>)(.*)(?=<\\/li>)";
		String picUrlRegex = "(?<=<img src=\")(.*)(?=\" alt=\"\" class=\"product-image\">)";
		while((newline=bf.readLine())!=null) {
			Pattern pattern1 = Pattern.compile(titleRegex);
			Matcher matcher1 = pattern1.matcher(newline);
			if(matcher1.find()) {
				String t= matcher1.group();
			  title = t.replaceAll("&#39;","'");
			title =title.replaceAll("&amp;", "&");
				}
			Pattern pattern2 = Pattern.compile(priceRegex);
			Matcher matcher2 = pattern2.matcher(newline);
			if(matcher2.find()) {
				price = matcher2.group();
			}
			Pattern pattern3 = Pattern.compile(dateOfAuctionRegex);
			Matcher matcher3 = pattern3.matcher(newline);
			if(matcher3.find()) {
				dateOfAuction = matcher3.group();
			}
			Pattern pattern4 = Pattern.compile(sellerRegex);
			Matcher matcher4 = pattern4.matcher(newline);
			if(matcher4.find()) {
				seller = matcher4.group();
			}
			Pattern pattern5 = Pattern.compile(picUrlRegex);
			Matcher matcher5 = pattern5.matcher(newline);
			if(matcher5.find()) {
				picUrl = matcher5.group();
			}
		}
		result = productNum+" | "+title+" | "+ price+" | "+ dateOfAuction+" | "+ seller+" | "+ time+"\n";
		insertHash(productNum,title,price,dateOfAuction,seller,picUrl);
		return result;
	}
    //insert to hash
	public void insertHash(String key, String title, String price, String dateOfAuction, String seller, String image) throws IOException{
		
		if(!hash.containsKey(key)){
		Hashtable <String,String> inHash=new Hashtable <String,String>();
		hash.put(key, inHash);
		inHash.put("Title", title);
		inHash.put("Price", price);
		inHash.put("AuctionDate", dateOfAuction);
		inHash.put("Seller", seller);
		inHash.put("Image",image);
		rows++;
		}
	
}
	
	public String offLineSearch(String item){//search in hash
		String result="";
		 Enumeration<String> names;
		 names = hash.keys();
		 String searchPatternRegex="";
		 String[] s = item.split("\\s");
		 for(int i=0;i<s.length;i++) {
			 
			 searchPatternRegex+="(?=.*"+s[i]+")";
		 }
		 Pattern searchPattern=Pattern.compile(searchPatternRegex,Pattern.CASE_INSENSITIVE);
		 
		 Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		while(names.hasMoreElements()){
			String key = names.nextElement();
			Matcher matchString = searchPattern.matcher(hash.get(key).get("Title"));
			if(matchString.find()) {
				result +=key+" | "+hash.get(key).get("Title")+" | "+hash.get(key).get("Price")+" | "+hash.get(key).get("AuctionDate")+" | "+hash.get(key).get("Seller")+"|"+timestamp+"\n";
			}
			
	}
		return result;
	}
	public static void setBufferedImage(URL u) throws IOException{
    	image = ImageIO.read(u);
    }
	
	public String outputImage(String itemNum,String path) throws IOException {
		String u="";
		String newFileName="";
		if(hash.containsKey(itemNum) && hash.get(itemNum).containsKey("Image")){
			u= hash.get(itemNum).get("Image");
			 newFileName = u.substring(u.lastIndexOf("/")+1);
			URL url = new URL(u);
			setBufferedImage(url); //set buffered image with url address
			ImageIO.write(image, "jpg", new File(path+"/"+newFileName)); //use imageio release image buffer into a newfile
			
		}
		return newFileName;
		
	}
	public void modifyHash(String key,String modifyClass,String upd) throws IOException{
		if(hash.containsKey(key) && hash.get(key).containsKey(modifyClass)){
			hash.get(key).put(modifyClass,upd);
			}
	
}
	public void deleteHash(String d){
		if(hash.containsKey(d)){
		hash.remove(d);
		}
	}
	
	
	
	public void insertDatabase(String databaseFileName) throws IOException{
		
	    String newLine;
	    String[] data;
		BufferedReader bf = new BufferedReader(new FileReader(databaseFileName));
		
		while((newLine=bf.readLine())!=null){
			data = newLine.split("|");
			insertHash(data[0],data[1],data[2],data[3],data[4],data[5]);
		}
		bf.close();
		
	}
	
	public Object[][] offLineSearchResultTable(String item){
		results = null;
		 Enumeration<String> names;
		 names = hash.keys();
		 int count=0;
		 Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		while(names.hasMoreElements()){
			String key = names.nextElement();
			if(hash.get(key).get("Title").contains(item)){
				Object[] rows={key,hash.get(key).get("Title"),hash.get(key).get("Price"),hash.get(key).get("AuctionDate"),hash.get(key).get("Seller"),timestamp,false};
				results[count]=rows;
				count++;
			}
			
	}
		return results;
	}
	
	
	
 public String printItemInfo(String key){
	 if(hash.contains(key)){
		 Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		 String ans = "Item Number | Item Title | Price | Auction Date | Seller | Timestamp"+"\n";
		ans +=key+" | "+hash.get(key).get("Title")+" | "+hash.get(key).get("Price")+" | "+hash.get(key).get("AuctionDate")+" | "+hash.get(key).get("Seller")+"|"+timestamp+"\n";
	 return ans;
	 }
	 else return "Can't find this item!";
 }
 
 public String  insertTrans(String activity,String item){
	 Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	 
	 String result = activity+" | "+item+" | "+ timestamp+"\n";
	 return result;
 }
 
 public String searchByCatelog(String c) throws IOException{
	
	 int cal=0;
	 if (c.equals("** Star Wars Collectibles **")){
		 cal = 399;
	 }
		 if (c.equals("** Ugly / Festive Holiday Sweaters **" ) ){
			 cal = 352;
		 }
		 if (c.equals("Antiques")){
			 cal = 1;
		 }
		 if (c.equals("Art")){
			 cal = 15;
		 }
		 if (c.equals("Bath & Body")){
			 cal = 336;
		 }
		 if (c.equals("Books/Movies/Music")){
			 cal = 2;
		 }
		 if (c.equals("Bulk")){
			 cal = 2208;
		 }
		 if (c.equals("Cameras & Camcorders")){
			 cal = 170;
		 }
		 if (c.equals("Clothing")){
			 cal = 10;
		 }
		 if (c.equals("Collectibles")){
			 cal = 4;
		 }
		 if (c.equals("Computers & Electronics")){
			 cal = 7;
		 }
		 if (c.equals("Crafts & Hobbies")){
			 cal = 8;
		 }
		 if (c.equals("For The Home")){
			 cal = 195;
		 }
		 if (c.equals("Glass")){
			 cal = 14;
		 }
		 if (c.equals("Jewelry & Gemstones")){
			 cal = 6;
		 }
		 if (c.equals("Miscellaneous")){
			 cal = 113;
		 }
		 if (c.equals("Musical Instruments")){
			 cal = 13;
		 }
		 if (c.equals( "Office Supplies")){
			 cal = 215;
		 }
		 if (c.equals("Pet Supplies")){
			 cal = 34;
		 }
		 if (c.equals( "Religious Items")){
			 cal = 115;
		 }
		 if (c.equals("Science & Education")){
			 cal = 364;
		 }
		 if (c.equals("Seasonal & Holiday")){
			 cal = 18;
		 }
		 if (c.equals("Sports")){
			 cal = 12;
		 }
		 if (c.equals("Tableware and Kitchenware")){
			 cal = 20;
		 }
		 if (c.equals("Tools")){
			 cal = 114;
		 }
		 if (c.equals("Toys/Dolls/Games")){
			 cal = 9;
		 }
		 if (c.equals("Transportation")){
			 cal = 23;
		 }
		 if (c.equals("Travel/Luggage")){
			 cal = 427;
		 }
		 if (c.equals("Wedding")){
			 cal = 468;
		 }
		 return Integer.toString(cal);
		 
 }
 public  String printAllItemInfo(){
	 Enumeration<String> names;
	 names = hash.keys();
	 Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	 String ans = "Item Number | Item Title | Price | Auction Date | Seller | Timestamp"+"\n";
	 while(names.hasMoreElements()){
 		String s = (String) names.nextElement();
 		ans +=s +" | "+ hash.get(s).get("Title")+" | "+hash.get(s).get("Price")+" | "+hash.get(s).get("AuctionDate")+" | "+hash.get(s).get("Seller")+" | "+timestamp+"\n";
 	}
	 return ans;
 }
	
}
