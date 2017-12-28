import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.JLayeredPane;

public class MyGUI extends JFrame implements ActionListener {
	private JTextField textField_1;//set text field to get text
	private JTextField textField_2;//set text field to get text
	private JTextField textField_3;//set text field to get text
	private JTextField textField_4;//set text field to get text
	private String imageUrl;
	public static String inputFile;
	public static String outputFile;//initialize output file name
	public static String path;//initialize path
	public static String logFile;//initialize log file
	public static String argument;//initialize flag
	public static String currentSearch;//to output the current search
	private JTextField textField;
	
	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
	
				try {
				inputFile="";
				outputFile="output.txt";//initialize output file name
				path="src/";//initialize path
				logFile = "log.txt";//initialize log file
				argument="";//initialize flag
				currentSearch ="currentSearch.txt";
			    // set flags 
				
				for (int i=0;i<args.length;i++){
                    argument = args[i];
                    int index = i+1;
                    if(argument.equals("-i") && index<args.length){//-i followed by input file name
                             inputFile = args[i+1];
                             
                    }
                    if(argument.equals("-o")&& index<args.length){//-o followed by output file name
                             outputFile = args[i+1];
                             
                    }
                    if(argument.equals("-O")&& index<args.length){//-O followed by output file name
                             outputFile = args[i+1];
                             
                             
                    }
                    if(argument.equals("-d")&& index<args.length){//-d followed by path
                             path = args[i+1]+"/";
                    }	
                    if(argument.equals("-l")&& index<args.length){//-l followed by log file
                    	     logFile = args[i+1];
                    	     
                   }
                    if(argument.equals("-s")&& index<args.length){//-l followed by current search file
                    	currentSearch = args[i+1];
               	     }
				}
		DataBase db=new DataBase();
		MyGUI myGui = new MyGUI(db);
		myGui.setVisible(true);
		BufferedWriter transitionBw = new BufferedWriter(new FileWriter(path+logFile));
		transitionBw.write("Activity |               Item                |     Time"+"\n");
	transitionBw.close();
				}
				 catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public MyGUI (DataBase database) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(2000,8000, 1481, 985);//set up frame size
		  this.setPreferredSize(new Dimension(400, 300));
		JScrollPane scrollPane = new JScrollPane();
		JPanel panel = new JPanel();

		JLabel lblNewLabel = new JLabel("Search by item number");
		
		JLabel lblCategory = new JLabel("Category");
		
        JLabel lblTo = new JLabel("To $");
		
		JLabel lblOrderBy = new JLabel("Order By");
		
		JLabel Sellers = new JLabel("Sellers");
		JLabel lblFilterByPrice = new JLabel("Filter By Price  From $");
		JLabel lblStatus = new JLabel("Status ");
		panel.setVisible(false);
		JLabel lblItemName = new JLabel("Item Name: ");

		JLabel lblFilterByAttribute = new JLabel("Filter by Attribute:");
		
		JLabel lblInternationalShipping = new JLabel("International Shipping:");
		
		JLabel lblItemNumber = new JLabel("Item Number");
		
		
		JTextPane textPane = new JTextPane();
		scrollPane.setViewportView(textPane);
		textPane.setEditable(false);
		
		textField_1 = new JTextField();
		textField_1.setText("");
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setText("");
		textField_2.setColumns(10);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setText("");
		
		

		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setText("");
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setText("");
		String[] category={
				"All category",
				"** Star Wars Collectibles **",
				"** Ugly / Festive Holiday Sweaters **",
				"Antiques",
				"Art",
				"Bath & Body",
				"Books/Movies/Music",
				"Bulk",
				"Cameras & Camcorders",
				"Clothing",
				"Collectibles",
				"Computers & Electronics",
				"Crafts & Hobbies",
				"For The Home",
				"Glass",
				"Jewelry & Gemstones",
				"Miscellaneous",
				"Musical Instruments",
				"Office Supplies",
				"Pet Supplies",
				"Religious Items",
				"Science & Education",
				"Seasonal & Holiday",
				"Sports",
				"Tableware and Kitchenware",
				"Tools",
				"Toys/Dolls/Games",
				"Transportation",
				"Travel/Luggage",
				"Wedding",
		};
	
		DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<String>(category);
		JComboBox<String> comboBox= new JComboBox<String>(comboModel);
		comboBox.setSelectedItem("All category");
		comboBox.addActionListener(new ActionListener(){
			@Override
			public void  actionPerformed(ActionEvent e) {
				
			       try {
			    	   String newline;
			    	   String t="";//the final category number,used to generate url
				        String cal = (String)comboBox.getSelectedItem();
					URL url = new URL("https://www.shopgoodwill.com/AdvancedSearch");
					  BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
					  String catRegex = "(?<=value=\")(.*)(?=\">"+cal+")";
					  while((newline=in.readLine())!=null) {        //search item features each time
							Pattern pattern1 = Pattern.compile(catRegex);
							Matcher matcher1 = pattern1.matcher(newline);
							if(matcher1.find()) {
								t= matcher1.group(); 
							  }
							}
					 database.categories=t;
					  
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		String[] order={"Ending Date","Title","Number of Bids",
				"Bid Price"
		};
		DefaultComboBoxModel<String> comboModel1 = new DefaultComboBoxModel<String>(order);
		
		JComboBox<String> comboBox_1 = new JComboBox<String>(comboModel1);
		comboBox_1.setSelectedItem("Ending Date");
		comboBox_1.addActionListener(new ActionListener(){
			@Override
			public void  actionPerformed(ActionEvent e) {
				
			     //  String newline;
				   //String t="";
				  // JComboBox<String> cb = (JComboBox<String>)e.getSource();
				    String order = (String)comboBox_1.getSelectedItem();
				if(order.equals("Title")){
					database.orderBy="2";
				}
				if(order.equals("Number of Bids")){
					database.orderBy="3";
				
				}
				if(order.equals("Bid Price")){
					database.orderBy="4";
					
				}

				
			}
		});
		String[] seller={
				"All Sellers",
				"AL - Mobile - Goodwill Easter Seals of the Gulf Coast, Inc.",
				"AR - Little Rock - Goodwill Industries of Arkansas, Inc.",
				"AZ - Tucson - Goodwill Industries of Southern Arizona",
				"CA - Bakersfield - Goodwill South Central California",
				"CA - Fairfield - Goodwill Industries of the Greater East Bay, Oakland, Fairfield CA",
				"CA - Long Beach - Goodwill Serving the People of Southern Los Angeles County",
				"CA - Los Angeles - Goodwill Industries of Southern California",
				"CA - Oxnard - Goodwill Industries of Ventura and Santa Barbara Counties, Inc.",
				"CA - Sacramento - Goodwill Industries Sacramento Valley",
				"CA - Salinas - Goodwill Central Coast",
				"CA - San Diego - Goodwill Industries of San Diego County",
				"CA - San Jose - Goodwill of Silicon Valley",
				"CA - Santa Ana - Test",
				"CA - Santa Ana - Goodwill of Orange County",
				"CA - Santa Rosa - Goodwill Industries of the Redwood Empire",
				"CA - South San Francisco - Goodwill Industries of San Francisco",
				"CO - Colorado Springs - Discover Goodwill of Southern and Western Colorado",
				"CO - Denver - Goodwill Industries of Denver",
				"DE - Wilmington - Goodwill Industries of Delaware &amp; Delaware County, Inc.",
				"FL - Fort Lauderdale - Goodwill Industries of South Florida",
				"FL - Fort Myers - Goodwill Industries of SW Florida, Inc.",
				"FL - Jacksonville - Goodwill of North Florida",
				"FL - Lake Worth - Gulfstream Goodwill Industries",
				"FL - Orlando - Goodwill Industries of Central Florida",
				"FL - Tallahassee - Goodwill Industries - Big Bend, Inc.",
				"GA - Macon - Goodwill Industries of Middle Georgia",
				"HI - Honolulu - Goodwill Hawaii",
				"IA - Iowa City - Goodwill of the Heartland",
				"IA - Johnston - Goodwill of Central Iowa",
				"IA - Oelwein - Goodwill Industries of Northeast Iowa, Inc.",
				"IA - Sioux City - Goodwill of the Great Plains - Sioux City, Iowa",
				"ID - Boise - Boise Metro Goodwill",
				"IL - East Peoria - Goodwill Industries of Central Illinois, Inc.",
				"IL - Rockford - Goodwill Industries of Northern Illinois",
				"IL - Springfield - Land of Lincoln Goodwill",
				"IN - Fort Wayne - Goodwill Industries of Northeast Indiana, Inc.",
				"IN - Indianapolis - Goodwill of Central &amp; Southern Indiana, Inc.",
				"IN - South Bend - Goodwill Industries of Michiana, Inc.",
				"IN - Terre Haute - Wabash Valley Goodwill",
				"KS - Wichita - Goodwill Industries of Kansas INC.",
				"LA - Shreveport - Goodwill of North Louisiana",
				"MD - Baltimore - Goodwill Industries of the Chesapeake, Inc.",
				"MD - Forestville - Goodwill of Greater Washington",
				"MD - Frederick - Goodwill Industries of Monocacy Valley, Inc.",
				"MD - Hagerstown - Horizon Goodwill Industries",
				"ME - Portland - Goodwill of Northern New England",
				"MI - Adrian - Goodwill Industries of Southeastern MI, Inc.",
				"MI - Charlotte - Goodwill Industries of Central Michigan&#39;s Heartland",
				"MI - Detroit - Goodwill Industries of Greater Detroit",
				"MI - Flint - Goodwill Industries of Mid Michigan",
				"MI - Grandville - Goodwill of Greater Grand Rapids",
				"MI - Kalamazoo - Goodwill Industries of Southwestern Michigan",
				"MI - Muskegon - Goodwill Industries of West Michigan, Inc.",
				"MI - Port Huron - Goodwill Industries of St. Clair County (Goodwill SCC)",
				"MI - Traverse City - Goodwill Industries of Northern Michigan, Inc.",
				"MN - Brooklyn Park - Goodwill/Easter Seals MN",
				"MN - Duluth - Goodwill Industries of Duluth, Minnesota",
				"MO - Blue Springs - Goodwill of Western Missouri &amp; Eastern Kansas",
				"MO - St. Louis - MERS/Missouri Goodwill Industries",
				"MS - Ridgeland - Goodwill Industries of Mississippi",
				"MT - Belgrade - Bozeman Goodwill",
				"MT - Billings - Billings Goodwill",
				"MT - Great Falls - Great Falls Goodwill",
				"MT - Helena - Helena Goodwill",
				"MT - Missoula - Missoula Goodwill",
				"NC - Charlotte - Goodwill Industries of the Southern Piedmont",
				"NC - Greensboro - Goodwill Industries of Central North Carolina, Inc.",
				"NC - Statesville - Goodwill Industries of Northwest North Carolina, Inc.",
				"ND - Bismarck - Easter Seals Goodwill ND, Inc.",
				"NE - Grand Island - Goodwill Industries of Greater Nebraska, Inc.",
				"NE - Omaha - Goodwill Industries Serving Eastern Nebraska &amp; Southwest Iowa",
				"NJ - Bellmawr - Goodwill Industries of Southern NJ &amp; Philadelphia",
				"NV - Las Vegas - Goodwill of Southern Nevada",
				"NY - Astoria - Goodwill Industries of Greater New York and Northern New Jersey",
				"NY - Buffalo - Goodwill Industries of Western New York",
				"NY - Rochester - Goodwill of The Finger Lakes",
				"NY - Wappingers Falls - Goodwill Industries of Greater NY &amp; Northern NJ - Wappinger Falls",
				"OH - Akron - Goodwill Industries of Akron, Ohio, Inc.",
				"OH - Chillicothe - Goodwill Industries of South Central Ohio",
				"OH - Cincinnati - Ohio Valley Goodwill Industries",
				"OH - Cleveland - Goodwill Industries of Greater Cleveland &amp; East Central Ohio",
				"OH - Columbus - Goodwill Columbus",
				"OH - Dayton - Goodwill Easter Seals Miami Valley",
				"OH - Newark - Licking/Knox Goodwill Industries, Inc.",
				"OH - Powell - Marion Goodwill Industries",
				"OH - Toledo - Goodwill Industries of Northwest Ohio",
				"OK - Lawton - Goodwill Industries of Southwest Oklahoma &amp; North Texas",
				"ON - London - Goodwill Industries Ontario Great Lakes, Canada",
				"OR - Eugene - Goodwill Industries of Lane &amp; South Coast Counties",
				"OR - Hillsboro - Goodwill Industries of the Columbia Willamette",
				"OR - Medford - Southern Oregon Goodwill Industries",
				"PA - Berwyn - Goodwill Keystone Area - Berwyn",
				"PA - Falls Creek - Goodwill Industries of North Central PA, Inc.",
				"PA - Harrisburg - Goodwill Keystone Area - Harrisburg",
				"PA - North Versailles - Goodwill of Southwestern Pennsylvania",
				"PA - Reading - Goodwill Keystone Area - Reading",
				"PA - Throop - Goodwill Industries of Northeastern Pennsylvania",
				"SC - Columbia - Goodwill Industries of Upstate/Midlands South Carolina",
				"SC - Greenville - Goodwill Industries of Upstate/Midlands South Carolina",
				"SC - North Charleston - Palmetto Goodwill/Goodwill of Lower SC",
				"SD - Sioux Falls - Goodwill of the Great Plains - Sioux Falls, South Dakota",
				"TN - Chattanooga - Chattanooga Goodwill Industries",
				"TN - Nashville - Goodwill Industries of Middle Tennessee, Inc.",
				"TX - Austin - Goodwill Industries of Central Texas",
				"TX - Corpus Christi - Goodwill Industries of South Texas",
				"TX - El Paso - Shopgoodwill of El Paso",
				"TX - Fort Worth - Goodwill Industries of Fort Worth",
				"TX - Houston - Goodwill Houston - Southwest",
				"TX - Houston - Goodwill Houston - North",
				"TX - Lubbock - Goodwill Industries of Northwest Texas",
				"TX - Lufkin - Goodwill Industries of Central East Texas, Inc.",
				"TX - San Antonio - Goodwill Industries of San Antonio",
				"TX - Tyler - Goodwill Industries of East Texas, Inc.",
				"TX - Waco - Heart of Texas Goodwill",
				"UT - Murray - SLC Metro Goodwill",
				"VA - Fredericksburg - Rappahannock Goodwill Industries Inc.",
				"VA - Roanoke - Goodwill Industries of the Valleys",
				"WA - Pasco - Goodwill Industries of the Columbia, Inc.",
				"WA - Seattle - Seattle Goodwill",
				"WA - Spokane - Goodwill Industries of the Inland Northwest",
				"WA - Spokane - Goodwill Industries of the Inland Northwest - Curve Apparel",
				"WA - Tacoma - Goodwill of the Olympics and Rainier Region",
				"WI - Appleton - Goodwill of North Central Wisconsin - http://goodwillncw.org/",
				"WI - Madison - Goodwill Industries of South Central Wisconsin, Inc.",
				"WI - Racine - Goodwill Retail Services, Inc.",
				"WV - Charleston - Goodwill Industries of Kanawha Valley, Inc.",
				"WV - Huntington - Goodwill Industries of KYOWVA Area, Inc.",
				"WY - Cheyenne - Goodwill Industries of Wyoming, Inc."
				
		};
		DefaultComboBoxModel<String> comboModel3 = new DefaultComboBoxModel<String>(seller);
		JComboBox comboBox_3 = new JComboBox(comboModel3);
		comboBox_3.setSelectedItem("All Sellers");
		comboBox_3.addActionListener(new ActionListener(){
			@Override
			public void  actionPerformed(ActionEvent e) {
				
			       try {
			    	   String newline;
			    	   String st="";
			    	   //JComboBox<String> cb = (JComboBox<String>)e.getSource();
			    	  
				        String cal = (String)comboBox_3.getSelectedItem();
					URL url = new URL("https://www.shopgoodwill.com/AdvancedSearch");//search the seller use
                       //regex to search on the main page to get the categories of sellers
					  BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
					  String catRegex = "(?<=value=\")(.*)(?=\">"+cal+")";
					  while((newline=in.readLine())!=null) {        //search item features each time
							Pattern pattern1 = Pattern.compile(catRegex);
							Matcher matcher1 = pattern1.matcher(newline);
							if(matcher1.find()) {
								st= matcher1.group(); 
							  }
							}
					 database.seller=st;
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
        JCheckBox chckbxNewCheckBox = new JCheckBox("Search for Buy Now items only?");
        chckbxNewCheckBox.addActionListener(new ActionListener() {
			@Override
			public void  actionPerformed(ActionEvent e) {
               
              if(chckbxNewCheckBox.isSelected()) {
            	 database.buyNowOnly=true;
              }
                }
		});
		
		JCheckBox chckbxSearchForBuy = new JCheckBox("Search for Pickup Only items only?");
		chckbxSearchForBuy.addActionListener(new ActionListener() {
			@Override
			public void  actionPerformed(ActionEvent e) {
               
              if(chckbxSearchForBuy.isSelected()) {
            	 database.pickupOnly=true;
              }
                }
		});
		
		JCheckBox chckbxNewCheckBox_1 = new JCheckBox("Do not include Pickup Only items?");
		chckbxNewCheckBox_1.addActionListener(new ActionListener() {
			@Override
			public void  actionPerformed(ActionEvent e) {
               
				
              if(chckbxNewCheckBox_1.isSelected()) {
            	 database.noPickupOnly=true;
              }
                }
		});
		
		JCheckBox chckbxDoNotInclude = new JCheckBox("Search for 1 cent shipping only?");
		chckbxDoNotInclude.addActionListener(new ActionListener() {
			@Override
			public void  actionPerformed(ActionEvent e) {
               
              if(chckbxDoNotInclude.isSelected()) {
            	 database.onecentShipment=true;
              }
                }
		});
		
		
		
		JCheckBox chckbxCanada = new JCheckBox("Canada");
		//chckbxCanada.setVerticalAlignment(SwingConstants.TOP);
		chckbxCanada.addActionListener(new ActionListener() {
			@Override
			public void  actionPerformed(ActionEvent e) {
               
              if(chckbxCanada.isSelected()) {
            	 database.canadaShipment=true;
              }
                }
		});
		
		
		JCheckBox chckbxOutsideUs = new JCheckBox("Outside US & Canada");
		
		chckbxOutsideUs.addActionListener(new ActionListener() {
					@Override
					public void  actionPerformed(ActionEvent e) {
		               
		              if(chckbxCanada.isSelected()) {
		            	 database.outUsshipment=true;
		              }
		                }
				});
		
	
	
		JCheckBox chckbxOlineSearch = new JCheckBox("Online search ");
		
		chckbxOlineSearch.addActionListener(new ActionListener() {
			@Override
			public void  actionPerformed(ActionEvent e) {
               AbstractButton ab = (AbstractButton)e.getSource();
              if(ab.getModel().isSelected()) {
            	  panel.setVisible(true);
              }
                }
		});
		
		
		JRadioButton rdbtnOpenAuctions = new JRadioButton("Open Auctions");
		rdbtnOpenAuctions.addActionListener(new ActionListener() {
			@Override
			public void  actionPerformed(ActionEvent e) {
				if(rdbtnOpenAuctions.isSelected()) {
	            	 database.closed=false;
	              }
                }
		});
		
		
		JRadioButton rdbtnClosedAuctions = new JRadioButton("Closed Auctions");
		rdbtnClosedAuctions.addActionListener(new ActionListener() {
			@Override
			public void  actionPerformed(ActionEvent e) {
				if(rdbtnClosedAuctions.isSelected()) {
	            	 database.closed=true;
	              }
                }
		});
		JRadioButton rdbtnAscending = new JRadioButton("Ascending");
		rdbtnAscending.addActionListener(new ActionListener() {
			@Override
			public void  actionPerformed(ActionEvent e) {
				if(rdbtnAscending.isSelected()) {
	            	 database.descendingOrder=false;
	              }
                }
		});
		JRadioButton rdbtnDescending = new JRadioButton("Descending");
		rdbtnDescending.addActionListener(new ActionListener() {
			@Override
			public void  actionPerformed(ActionEvent e) {
				if(rdbtnDescending.isSelected()) {
	            	 database.descendingOrder=true;
	              }
                }
		});
		JButton btnInsert = new JButton("Insert");
		btnInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				final JFileChooser c = new JFileChooser();
				if(e.getSource()==btnInsert){
					
				int chooseVal = c.showOpenDialog(MyGUI.this);
				if(chooseVal == JFileChooser.APPROVE_OPTION){
					File f = c.getSelectedFile();
					try {
						BufferedReader b = new BufferedReader(new FileReader(f));
						
						BufferedWriter bw = new BufferedWriter(new FileWriter(path+outputFile));
					database.updateDatabase(b,bw);
					BufferedWriter logbw = new BufferedWriter(new FileWriter(path+logFile,true));
					logbw.write(database.insertTrans("Insert File",inputFile));
					logbw.close();
						//log.write("Import File"+"|"+time+"\n");
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				}
				
			}
			
		});

		JButton btnModify = new JButton("Modify");
		btnModify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String itemN = textField.getText();
					String[] options= {"Item Title" ,"Price","Auction Date","Seller"};
					//built a option pane referenced from mkyong.com
					int itemCommands = JOptionPane.showOptionDialog(null,"Please choose a classification to modify: ",//print message 
							"Choose to modify",//window title
							JOptionPane.DEFAULT_OPTION,//option type
							JOptionPane.INFORMATION_MESSAGE,//message type 
							null, //window icon
							options, //all option string array
							options[0]);//default option 
					
					if(itemCommands==0){
						String update1 = JOptionPane.showInputDialog("Please enter the product new title");
						database.modifyHash(itemN,"Title",update1);
						//log.write("Modify"+"|"+itemN+"|"+"Number"+"|"+time+"\n");
						//log.flush();
					}
					if(itemCommands==1){
						String update2 = JOptionPane.showInputDialog("Please enter the new price in rational number");
						database.modifyHash(itemN,"Price",update2);
						//log.write("Modify"+"|"+itemN+"|"+"Price"+"|"+time+"\n");
						//log.flush();
					}
					if(itemCommands==2){
						String update3 = JOptionPane.showInputDialog("Please enter the new auction date, date format is mm-dd-yyyy");
						database.modifyHash(itemN,"AuctionDate",update3);
						//log.write("Modify"+"|"+itemN+"|"+"Quantity"+"|"+time+"\n");
						//log.flush();
					}
					if(itemCommands==3){
						String update4 = JOptionPane.showInputDialog("Please enter the new seller name");
						database.modifyHash(itemN,"Seller",update4);
						//log.write("Modify"+"|"+itemN+"|"+"PurchaseDate"+"|"+time+"\n");
						//log.flush();
					}
					BufferedWriter logbw = new BufferedWriter(new FileWriter(path+logFile,true));
					logbw.write(database.insertTrans("Modify",database.hash.get(itemN).get("Title")));
					logbw.close();

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String itemN = textField.getText();
				database.deleteHash(itemN);
				BufferedWriter logbw;
				try {
					logbw = new BufferedWriter(new FileWriter(path+logFile,true));
					logbw.write(database.insertTrans("Delete",database.hash.get(itemN).get("Title")));
					logbw.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		
		
		
		
		JButton btnSearch = new JButton("Search");
	
		btnSearch.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				final StyledDocument doc =textPane.getStyledDocument();
				textPane.setText("");
				String item = textField_3.getText();
				//database.keywords=item;
				String itemNum = textField_4.getText();
				if(!textField_1.getText().equals("")||!textField_2.getText().equals("")){
				database.priceMin= textField_1.getText();
				database.priceMax = textField_2.getText();
				}
				//String data="";
				try {
					
					if(!chckbxOlineSearch.isSelected()){
					String data1=database.offLineSearch(item);
					//System.out.println(item);
					doc.insertString(0,"Item Number | Item Title | Price | Auction Date | Seller | Timestamp\n",null);
					doc.insertString(0,data1,null);
					
					
					}
					else{
						
						if(textField_4.getText().equals("")){

							
							String data2 = database.onlineSearchResult(item);
							doc.insertString(0,"Item Number | Item Title | Price | Auction Date | Seller | Timestamp\n",null);
							doc.insertString(0, data2, null);
						}
						else{
							
						doc.insertString(0,database.itemInfoOnline(itemNum),null);
						}
					}

					BufferedWriter logbw = new BufferedWriter(new FileWriter(path+logFile,true));
					logbw.write(database.insertTrans("Search",item));
					logbw.close();
					
						
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
				}
			
		});
		
		JButton btnNewButton = new JButton("Search Report");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					String text = textPane.getText();
					BufferedWriter bw = new BufferedWriter(new FileWriter(path+currentSearch));//write on the current search file
					bw.write(text+"\n");
					bw.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		JButton button = new JButton("Database Report");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					String text =database.printAllItemInfo();
					textPane.setText(text);
					BufferedWriter bw = new BufferedWriter(new FileWriter(path+outputFile));
					bw.write(text+"\n");
					bw.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		JButton btnTransition = new JButton("Transition History");//write one the log file
		btnTransition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String line;
					String result="";
					BufferedReader logbr = new BufferedReader(new FileReader(path+inputFile));
					while((line=logbr.readLine())!=null) {
						result+=line;
					}
					logbr.close();
					textPane.setText(result);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		JButton btnImage = new JButton("Image");//get image by the database using item number to search
		btnImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			
				try {
					String itemNum = textField.getText();
					JOptionPane.showMessageDialog(null,"The image  "+ database.outputImage(itemNum,path)+ "  is in the dirctory now!");
				} catch (HeadlessException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
					
			}
		});
		
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(37)
					.addComponent(lblItemName, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
					.addGap(9)
					.addComponent(textField_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chckbxOlineSearch)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnInsert)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(button)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnTransition)
					.addContainerGap())
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(22)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 1432, Short.MAX_VALUE)
					.addGap(27))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(33)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 1434, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblItemNumber)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(91)
							.addComponent(btnModify)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnDelete)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnImage)))
					.addContainerGap(14, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(5)
									.addComponent(lblItemName))
								.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
									.addComponent(textField_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(chckbxOlineSearch)
									.addComponent(btnSearch, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(btnInsert, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(button, GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
									.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(19)
							.addComponent(btnTransition)))
					.addGap(5)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE)
					.addGap(13)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblItemNumber)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
							.addComponent(btnModify, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnDelete, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnImage))
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 671, GroupLayout.PREFERRED_SIZE)
					.addGap(36))
		);
		
				
		
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(15)
							.addComponent(lblNewLabel)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textField_4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblCategory)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 310, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(lblInternationalShipping)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chckbxOutsideUs)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chckbxCanada))
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(Sellers)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(comboBox_3, GroupLayout.PREFERRED_SIZE, 707, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblStatus)
							.addGap(18)
							.addComponent(rdbtnOpenAuctions)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(rdbtnClosedAuctions))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(14)
							.addComponent(lblFilterByPrice)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblTo)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 297, Short.MAX_VALUE)
							.addComponent(lblOrderBy)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(rdbtnAscending)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(rdbtnDescending)
							.addGap(29)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(chckbxNewCheckBox)
						.addComponent(chckbxNewCheckBox_1)
						.addComponent(chckbxDoNotInclude)
						.addComponent(chckbxSearchForBuy)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(9)
							.addComponent(lblFilterByAttribute)))
					.addGap(83))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(15)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblFilterByAttribute)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(chckbxNewCheckBox)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chckbxNewCheckBox_1)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chckbxDoNotInclude)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chckbxSearchForBuy))
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNewLabel)
								.addComponent(textField_4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblCategory)
								.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_panel.createSequentialGroup()
								.addGap(6)
								.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
									.addComponent(lblInternationalShipping)
									.addComponent(chckbxOutsideUs)
									.addComponent(chckbxCanada))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
									.addComponent(Sellers)
									.addComponent(comboBox_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(lblStatus)
									.addComponent(rdbtnOpenAuctions)
									.addComponent(rdbtnClosedAuctions))
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
									.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(lblFilterByPrice)
									.addComponent(lblTo)
									.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(lblOrderBy)
									.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(rdbtnAscending)
									.addComponent(rdbtnDescending)))))
					.addContainerGap(26, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		
		
		getContentPane().setLayout(groupLayout);
		
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
};

