package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;


public class BillingModel {
	
	
	int salesQuantity;
	Connection connection;
	SimpleDateFormat formatter;
	Date date;
	String dateString, invoice;
	ObservableList<String> itemPrices = FXCollections.observableArrayList();
	ObservableList<String> itemQuantity = FXCollections.observableArrayList();
	public int grandTotal;
	float sgst,cgst;
	
	public BillingModel (){
		formatter = new SimpleDateFormat("dd/MM/yyyy");  
	    date = new Date();  
	    dateString = formatter.format(date);
	}
	
	public ObservableList<String> getItemNameListAndLoadItemDetails() {
		ObservableList<String> itemNames = FXCollections.observableArrayList();
		connection = SQLiteConnection.Connector();
		String query = "SELECT ITEM_NAME,MRP,QUANTITY FROM ITEMS";
		Statement stmt;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {				
				itemNames.add(rs.getString("ITEM_NAME"));
				itemPrices.add(rs.getString("MRP"));
				itemQuantity.add(rs.getString("QUANTITY"));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("BillingModel: Medicine Items are retrieved from the Database");
		return itemNames;
	}
	
	public String getItemPrice(int itemPosition) {
		return itemPrices.get(itemPosition);
	}
	
	public String getItemQuantity(int itemPosition) {
		return itemQuantity.get(itemPosition);
	}
	
	public String getDate() {
		return formatter.format(date);
	}

	public ObservableList<String> getListViewNames(String nameText) {
		ObservableList<String> listViewNames = FXCollections.observableArrayList();
		String query = "SELECT DISTINCT CUSTOMER_NAME  FROM Bill WHERE CUSTOMER_NAME LIKE '%" + nameText + "%'";
		connection = SQLiteConnection.Connector();
		Statement stmt;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				String name = rs.getString("CUSTOMER_NAME");
				listViewNames.add(name);
			}
			connection.close();
			return listViewNames;
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getInvoiceNo() {
		connection = SQLiteConnection.Connector();
		String query = "SELECT BILL_ID  FROM Bill WHERE CUSTOMER_NAME IS NULL ORDER BY BILL_ID DESC LIMIT 1";
		Statement stmt;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if(rs.next()) {
				System.out.println("BillingModel: Invoice Number retrieved");
				String invoiceNo = Integer.toString(rs.getInt("BILL_ID"));
				for(int i = 0 ; i < 4 - Integer.parseInt(invoiceNo) ; i++){
					invoiceNo = "0" + invoiceNo;
				}
				invoice = invoiceNo;
				connection.close();
				return invoiceNo;
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("BillingModel: Invoice Number unable to retrieve");
		return null;
	}

	public void makeBill(String customerName, ObservableList<Item> items, boolean isPaymentDone) {
		connection = SQLiteConnection.Connector();
		String paymentStatus;
		if(isPaymentDone)
			paymentStatus = "PAID";
		else paymentStatus = "PENDING";
		System.out.println("BillingModel: 0. Before executing the batch");
			
		for(int i = 0; i <items.size(); i++) {
			Item item = items.get(i);
			salesQuantity+=Integer.parseInt(item.getQty());
		}	
		
		
		
		String query = "UPDATE BILL SET CUSTOMER_NAME = " + "'" + customerName + "'" + ", DATE = (SELECT date('now'))" + ", TOTAL_AMT = " + grandTotal + ", PAYMENT_STATUS = '" + paymentStatus + "', QUANTITY = " + salesQuantity + " WHERE CUSTOMER_NAME IS NULL";
		String query2 = "INSERT INTO BILL (Customer_Name,Date) VALUES(NULL,NULL);";
		System.out.println(query);
		System.out.println(query2);
		Statement stmt;
		salesQuantity=0;
		try {
			connection.setAutoCommit(false);
			stmt = connection.createStatement();
			stmt.execute(query);			
			//connection.close();
			
			System.out.println("BillingModel: 1. After executing the Update Bill statement");
			
			//connection = SQLiteConnection.Connector();
			//connection.setAutoCommit(false);
			stmt = connection.createStatement();
			stmt.execute(query2);	
			//connection.close();
			
			System.out.println("BillingModel: 2. Inserted a NULL tuple in Bill");
			
			//addItemsToCart(items);
			
			/* Add Items to cart */
			
			try {
				//connection = SQLiteConnection.Connector();
				//connection.setAutoCommit(false);
				stmt = connection.createStatement();
				for(int i = 0; i <items.size(); i++) {
					Item item = items.get(i);
					salesQuantity+=Integer.parseInt(item.getQty());
					query = "INSERT INTO CART VALUES(" + Integer.toString(Integer.parseInt(invoice)) + "," + "'" + item.getDesc() + "'" + "," + item.getRate() + "," + item.getQty() + "," + item.getDst() + "," + item.getTrade() + ")";
					stmt.executeUpdate(query);
					System.out.println("BillingModel: Item added to cart");
				}
				//connection.close();
				System.out.println("BillingModel: 3. Items added to cart");
				
				//reduceItemQuantity(items);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			
			/* Remove item in Database */
			
			try {
				//connection = SQLiteConnection.Connector();
				//connection.setAutoCommit(false);
				stmt = connection.createStatement();
				for(int i = 0; i <items.size(); i++) {
					Item item = items.get(i);
					query = "UPDATE ITEMS SET QUANTITY = QUANTITY - " + item.getQty() + " WHERE ITEM_NAME = " + "'" + item.getDesc() + "'";
					stmt.executeUpdate(query);
					System.out.println("BillingModel: Item quantity Updated");
				}
				
				System.out.println("BillingModel: 4. Item Stock reduced");
				
				connection.commit();
				connection.setAutoCommit(true);
				System.out.println("BillingModel: 5. Changes commited");
				connection.close();
				//createNewBillTuple();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			
			
		} catch (Exception e) {
			try {
				System.out.println("Error performing the transaction");
				connection.rollback();
				connection.setAutoCommit(true);
				connection.close();
				System.out.println("Rollback successful");
			} catch (SQLException e1) {
				System.out.println("Error performing the rollback");
				e1.printStackTrace();
			}
			//e.printStackTrace();
		}
		
	}
	
	private void getGSTValuesFromDatabase() {
		connection = SQLiteConnection.Connector();
		String query = "SELECT SGST,CGST FROM FLAGS";
		Statement stmt;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);				
			sgst = Float.parseFloat(rs.getString("SGST"));
			cgst = Float.parseFloat(rs.getString("CGST"));
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("SettingsModel: CGST SGST retrieved from the Database");
	}
	

	public void printBill(String billID,String name,String date,String valueInMoney,String grand) {
		try {
				getGSTValuesFromDatabase();
				InputStream in = new FileInputStream(new File("C:\\Users\\Public\\Blank_A4.jrxml"));
				JasperDesign jd=JRXmlLoader.load(in);
				String sql="SELECT ITEM_NAME,QUANTITY,PRICE,QUANTITY*PRICE AS AMT,DISCOUNT,QUANTITY*PRICE-DISCOUNT AS VALUE, Field6 AS TP FROM CART WHERE BILL_ID = " + billID;
				JRDesignQuery jdq = new JRDesignQuery();
				jdq.setText(sql);
				jd.setQuery(jdq);
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("BillID", billID);
				parameters.put("Name", name);
				parameters.put("Date", date);
				parameters.put("ValueInWords", valueInMoney + " only");
				parameters.put("Grand", grand);
				DecimalFormat numberFormat = new DecimalFormat("#.00");
				parameters.put("GST", numberFormat.format((sgst+cgst)/100*Float.parseFloat(grand)));
				parameters.put("GSTperc", Float.toString(sgst+cgst));
				
			    JasperReport js = JasperCompileManager.compileReport(jd);
			    JasperPrint jasperPrint = JasperFillManager.fillReport(js,parameters, SQLiteConnection.Connector());
			    JasperViewer.viewReport(jasperPrint,false);
			//    OutputStream os = new FileOutputStream(new File("C:\\Users\\Public"));
			//    JasperExportManager.exportReportToPdfStream(jasperPrint, os);
			    
			    
			
			
		} catch (JRException e) {
			System.out.println(e.getMessage());	
		} catch(Exception e) {
			System.out.println(e.getMessage());	
		}
		
	}

	/*
	@Deprecated
	private void addItemsToCart(ObservableList<Item> items) {
		
		try {
			connection = SQLiteConnection.Connector();
			String query = "";
			Statement stmt = connection.createStatement();
			for(int i = 0; i <items.size(); i++) {
				Item item = items.get(i);
				query = "INSERT INTO CART VALUES(" + Integer.toString(Integer.parseInt(invoice)) + "," + "'" + item.getDesc() + "'" + "," + item.getRate() + "," + item.getQty() + "," + item.getDst() + ")";
				stmt.executeUpdate(query);
				System.out.println("BillingModel: Item added to cart");
			}
			connection.close();
			reduceItemQuantity(items);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	@Deprecated
	private void reduceItemQuantity(ObservableList<Item> items) {
		try {
			connection = SQLiteConnection.Connector();
			String query = "";
			Statement stmt = connection.createStatement();
			System.out.println(items.size());
			for(int i = 0; i <items.size(); i++) {
				Item item = items.get(i);
				query = "UPDATE ITEMS SET QUANTITY = QUANTITY - " + item.getQty() + " WHERE ITEM_NAME = " + "'" + item.getDesc() + "'";
				stmt.executeUpdate(query);
				System.out.println("BillingModel: Item quantity Updated");
			}
			connection.close();
			//createNewBillTuple();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	@Deprecated
	private void createNewBillTuple() {
		try {
			connection = SQLiteConnection.Connector();
			String query = "INSERT INTO BILL (CUSTOMER_NAME,DATE) VALUES (NULL,NULL)";
			Statement stmt = connection.createStatement();
			stmt.executeQuery(query);
			System.out.println("BillingModel: Created a new Bill ID");
			connection.close();
		} catch (SQLException e) {
			// SQL Does not Return a ResultSet Caught
		}
	} */
}
