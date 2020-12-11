package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
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

public class SearchModel {
	
	
	int totalUnits=0, totalAmount=0;
	StringBuffer tempBuffer;
	float sgst,cgst;
	
	
	Connection connection;
	Calendar todayCalender = Calendar.getInstance();
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	String defaultQuery = "SELECT BILL_ID,CUSTOMER_NAME,strftime('%d/%m/%Y',DATE) AS DATE,TOTAL_AMT,PAYMENT_STATUS,QUANTITY,DELETEFLAG FROM BILL B WHERE CUSTOMER_NAME IS NOT NULL ORDER BY BILL_ID DESC";
	String commonQuery;
	
	public ObservableList<String> getListItems() {
		
		ObservableList<String> itemNames = FXCollections.observableArrayList();
		connection = SQLiteConnection.Connector();
		String query = "SELECT ITEM_NAME FROM ITEMS";
		Statement stmt;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {				
				itemNames.add(rs.getString("ITEM_NAME"));
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("SearchModel: Medicine Items are retrieved from the Database");
		return itemNames;
	
	}


	public ObservableList<BillItems> getBillItems(String flag) {

		ObservableList<BillItems> returnItems = FXCollections.observableArrayList();
		BillItems tempItem;
		connection = SQLiteConnection.Connector();
		totalUnits=0;
		totalAmount=0;
		Statement stmt;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(defaultQuery);
			while(rs.next()) {		
				String billId = Integer.toString(rs.getInt("BILL_ID"));
				String customerName = rs.getString("CUSTOMER_NAME");
				String date = rs.getString("DATE");
				String totalAmt = Integer.toString(rs.getInt("TOTAL_AMT"));
				String paymentStatus = rs.getString("PAYMENT_STATUS");
				String quantity = rs.getString("QUANTITY");
				//String deleteFlag = Integer.toString(rs.getInt("DELETEFLAG"));
				

				
				totalUnits+=Integer.parseInt(quantity);
				totalAmount+=Integer.parseInt(totalAmt);
				
				tempItem = new BillItems(billId, customerName, date, totalAmt, paymentStatus, quantity);
				returnItems.add(tempItem);
			}
			System.out.println("SearchModel: Bill Items are retrieved from the Database");
			connection.close();
			return returnItems;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	
	}

/*

	private ObservableList<BillItems> getItemsOfThisYear() {
		ObservableList<BillItems> returnItems = FXCollections.observableArrayList();
		BillItems tempItem;
		connection = SQLiteConnection.Connector();
		Statement stmt;
		try {
			stmt = connection.createStatement();
			System.out.println(commonQuery.indexOf("WHERE"));
			String temp = commonQuery.substring(0,commonQuery.indexOf("WHERE") + "WHERE".length());
			
			//adding DATE = TODAY 
			temp = temp.concat(commonQuery.substring(commonQuery.indexOf("WHERE") + "WHERE".length(),commonQuery.indexOf("ORDER")) + "AND DATE = (SELECT date('now')) " + commonQuery.substring(commonQuery.indexOf("ORDER"))); 
			System.out.println(temp);
			ResultSet rs = stmt.executeQuery(temp);
			while(rs.next()) {		
				String billId = Integer.toString(rs.getInt("BILL_ID"));
				String customerName = rs.getString("CUSTOMER_NAME");
				String date = rs.getString("DATE");
				String totalAmt = Integer.toString(rs.getInt("TOTAL_AMT"));
				tempItem = new BillItems(billId,customerName,date,totalAmt);
				returnItems.add(tempItem);
			}
			System.out.println("SearchModel: Bill Items are retrieved from the Database");
			connection.close();
			return returnItems;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
*/
/*
	private ObservableList<BillItems> getItemsOfToday() {
		ObservableList<BillItems> returnItems = FXCollections.observableArrayList();
		BillItems tempItem;
		connection = SQLiteConnection.Connector();
		Statement stmt;
		try {
			stmt = connection.createStatement();
			removeDate(commonQuery);
			
			String temp = commonQuery.substring(0,commonQuery.indexOf("WHERE") + "WHERE".length());
			//adding DATE = TODAY 
			temp = temp.concat(commonQuery.substring(commonQuery.indexOf("WHERE") + "WHERE".length(),commonQuery.indexOf("ORDER")) + "AND DATE = (SELECT date('now')) " + commonQuery.substring(commonQuery.indexOf("ORDER"))); 
			System.out.println(temp);
			ResultSet rs = stmt.executeQuery(temp);
			while(rs.next()) {		
				String billId = Integer.toString(rs.getInt("BILL_ID"));
				String customerName = rs.getString("CUSTOMER_NAME");
				String date = rs.getString("DATE");
				String totalAmt = Integer.toString(rs.getInt("TOTAL_AMT"));
				tempItem = new BillItems(billId,customerName,date,totalAmt);
				returnItems.add(tempItem);
			}
			System.out.println("SearchModel: Bill Items are retrieved from the Database");
			connection.close();
			return returnItems;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
*/
	
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

	public ObservableList<BillItems> generateQueryAndGetBills(String billNoTextField, String nameTextField, String dateCombo, LocalDate betweenStartDate, LocalDate betweenEndDate, boolean searchCheckboxSelected) {
		tempBuffer = new StringBuffer();
		tempBuffer.append(defaultQuery.substring(0, defaultQuery.indexOf("WHERE CUSTOMER_NAME IS NOT NULL") + "WHERE CUSTOMER_NAME IS NOT NULL".length()));
		
		//Generating condition for Bill No
		if(!billNoTextField.equals("")) {
			String s = " AND BILL_ID = " + billNoTextField;
			tempBuffer.append(s);
		}
		
		//Generating condition for Bill No
		if(!nameTextField.equals("")) {
			if(!searchCheckboxSelected) {
				String s = " AND CUSTOMER_NAME LIKE '%" + nameTextField + "%'";
				tempBuffer.append(s);
			}
			else {
				String s = " AND CUSTOMER_NAME = '" + nameTextField + "'";
				tempBuffer.append(s);
			}
		}	
		
//		simpleDateFormat.format(cal.getTime())
		
		//Generating condition for date combobox

		if(dateCombo != null) {
			
			Calendar tempCalender = Calendar.getInstance();
			if(dateCombo.equals("Today")) {
				String s = " AND DATE = (SELECT date('now'))";
				tempBuffer.append(s);
			}
			else if(dateCombo.equals("This Week")) {
				tempCalender.add(Calendar.DATE, -7);
				String s = " AND DATE <= '" + simpleDateFormat.format(todayCalender.getTime()) + "' AND DATE >= '" + simpleDateFormat.format(tempCalender.getTime()) + "'";
				tempBuffer.append(s);
			}
			else if(dateCombo.equals("This Month")) {
				String thisMonth;
				if(todayCalender.get(Calendar.MONTH) + 1 < 10) {
					thisMonth = "0" + (todayCalender.get(Calendar.MONTH)+1);
				}
				else thisMonth = Integer.toString((todayCalender.get(Calendar.MONTH)+1));
				String s = " AND DATE <= '" + simpleDateFormat.format(todayCalender.getTime()) + "' AND DATE >= '" + todayCalender.get(Calendar.YEAR) + "-" + thisMonth + "-01'";
				tempBuffer.append(s);
			}
			else if(dateCombo.equals("This Year")) {
				String s = " AND DATE <= '" + simpleDateFormat.format(todayCalender.getTime()) + "' AND DATE >= '" + todayCalender.get(Calendar.YEAR) + "-01-01'";
				tempBuffer.append(s);
			}
			/*		else if(dateCombo.equals("Last 3 Months")) {
			String thisMonth;
			tempCalender.add(Calendar.MONTH, -3);
			if(tempCalender.get(Calendar.MONTH) + 1 < 10) {
				thisMonth = "0" + (tempCalender.get(Calendar.MONTH)+1);
			}
			else thisMonth = Integer.toString((tempCalender.get(Calendar.MONTH)+1));
			String s = " AND DATE <= '" + simpleDateFormat.format(todayCalender.getTime()) + "' AND DATE >= '" + tempCalender.get(Calendar.YEAR) + "-" + thisMonth + "-01'";
			tempBuffer.append(s);
		}
		else if(dateCombo.equals("Last 6 Months")) {
			String thisMonth;
			tempCalender.add(Calendar.MONTH, -6);
			if(tempCalender.get(Calendar.MONTH) + 1 < 10) {
				thisMonth = "0" + (tempCalender.get(Calendar.MONTH)+1);
			}
			else thisMonth = Integer.toString((tempCalender.get(Calendar.MONTH)+1));
			String s = " AND DATE <= '" + simpleDateFormat.format(todayCalender.getTime()) + "' AND DATE >= '" + tempCalender.get(Calendar.YEAR) + "-" + thisMonth + "-01'";
			tempBuffer.append(s);
		}	*/			
		}		
		
		//Generating condition for between day Picker
		if(betweenEndDate != null && betweenStartDate != null) {
			String s = " AND DATE <= '" + betweenEndDate + "' AND DATE >= '" + betweenStartDate + "'";
			tempBuffer.append(s);
		}
		
		String s = " ORDER BY BILL_ID DESC";
		tempBuffer.append(s);	
		
		System.out.println("SearchModel: Generated query:" + tempBuffer);	
		
		ObservableList<BillItems> returnItems = FXCollections.observableArrayList();
		BillItems tempItem;
		System.out.println(tempBuffer.toString());
		connection = SQLiteConnection.Connector();
		totalUnits=0;
		totalAmount=0;
		Statement stmt;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(tempBuffer.toString());
			while(rs.next()) {		
				String billId = Integer.toString(rs.getInt("BILL_ID"));
				String customerName = rs.getString("CUSTOMER_NAME");
				String date = rs.getString("DATE");
				String totalAmt = Integer.toString(rs.getInt("TOTAL_AMT"));
				String paymentStatus = rs.getString("PAYMENT_STATUS");
				String quantity = rs.getString("QUANTITY");
				totalUnits+=Integer.parseInt(rs.getString("QUANTITY"));
				totalAmount+=rs.getInt("TOTAL_AMT");
				tempItem = new BillItems(billId, customerName, date, totalAmt, paymentStatus, quantity);
				returnItems.add(tempItem);
			}
			System.out.println("SearchModel: Bill Items are retrieved from the Database");
			connection.close();
			return returnItems;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}


	public void changePayment(String billId, String paymentStatus) {
		connection = SQLiteConnection.Connector();
		String newPaymentStatus;
		if(paymentStatus.equals("PAID"))
			newPaymentStatus = "PENDING";
		else
			newPaymentStatus = "PAID";
		String query = "UPDATE BILL SET PAYMENT_STATUS = '" + newPaymentStatus + "' WHERE BILL_ID = " + billId;
		Statement stmt;
		try {
			stmt = connection.createStatement();
			stmt.executeUpdate(query);
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("IndividualBillModel: Bill Model has retrieved bill details from Database");
		
	}
	
	public void changeDeleteFlag(String billId) {
		connection = SQLiteConnection.Connector();

		String query = "DELETE FROM BILL WHERE BILL_ID = " + billId;
		System.out.println(query);
		Statement stmt;
		try {
			stmt = connection.createStatement();
			stmt.executeUpdate(query);
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("IndividualBillModel: Bill Model has retrieved bill details from Database");
		
	}


	public void printReport(String name, LocalDate betweenStartDate, LocalDate betweenEndDate, String duration, String sales, String total) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = sdf.parse(betweenStartDate.toString());
		SimpleDateFormat sdfNew = new SimpleDateFormat("dd/MM/yyyy");

	
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		Date date2 = sdf.parse(betweenEndDate.toString());
		SimpleDateFormat sdfNew2 = new SimpleDateFormat("dd/MM/yyyy");
		
		String startDate = null,endDate=null;
		getGSTValuesFromDatabase();
		if(betweenStartDate!=null && betweenEndDate!=null) {
			startDate="Start Date: " + betweenStartDate.toString();
			endDate="End Date: " + betweenEndDate.toString();
		}
		else if(duration!=null) {
			Calendar tempCalender = Calendar.getInstance();
			if(duration.equals("Today")) {
				startDate="Date: " + simpleDateFormat.format(todayCalender.getTime());		
				endDate="";
			}
			else if(duration.equals("This Week")) {
				tempCalender.add(Calendar.DATE, -7);
				startDate="Start Date: " + simpleDateFormat.format(tempCalender.getTime());
				endDate="End Date: " + simpleDateFormat.format(todayCalender.getTime());
			}
			else if(duration.equals("This Month")) {
				String thisMonth;
				if(todayCalender.get(Calendar.MONTH) + 1 < 10) {
					thisMonth = "0" + (todayCalender.get(Calendar.MONTH)+1);
				}
				else thisMonth = Integer.toString((todayCalender.get(Calendar.MONTH)+1));
				startDate="Start Date: " + todayCalender.get(Calendar.YEAR) + "-" + thisMonth + "-01";
				endDate="End Date: " + simpleDateFormat.format(todayCalender.getTime());
			}
			else if(duration.equals("This Year")) {
				System.out.println("This year");
				startDate="Date: " + todayCalender.get(Calendar.YEAR);
				endDate="";
			}
			else if(duration.equals("All")) {
				startDate="All transactions";
			}		
		}
		try {
			
			InputStream in = new FileInputStream(new File("C:\\Users\\Public\\A4report.jrxml"));
			JasperDesign jd=JRXmlLoader.load(in);
			JRDesignQuery jdq = new JRDesignQuery();
			if(tempBuffer == null || tempBuffer.equals("") ) {
				jdq.setText(defaultQuery);
				System.out.println(defaultQuery);
			}
			else {
				jdq.setText(tempBuffer.toString());
				System.out.println(tempBuffer.toString());
			}
			jd.setQuery(jdq);
			Map<String, Object> parameters = new HashMap<String, Object>();
			if(betweenStartDate!=null && betweenEndDate!=null) {
				parameters.put("StartDate", sdfNew.format(date1));
				parameters.put("EndDate", sdfNew2.format(date2));
			}
			else if(duration!=null){
				parameters.put("StartDate", startDate);
				parameters.put("EndDate", endDate);
			}
			else {
				parameters.put("StartDate", "");
				parameters.put("EndDate", "");
			}
			if(name!=null && !name.equals("")) {
				parameters.put("Name", "Name: " + name);
			}
			//Hello There
			else { 
				parameters.put("Name", "");
			}
			parameters.put("GST", Float.toString(sgst+cgst));
			DecimalFormat numberFormat = new DecimalFormat("#.00");			
			parameters.put("gstPrice", numberFormat.format((sgst+cgst)/100*Float.parseFloat(total)));
			parameters.put("Sales", sales);
			parameters.put("Total", total);
		    JasperReport js = JasperCompileManager.compileReport(jd);
		    JasperPrint jasperPrint = JasperFillManager.fillReport(js,parameters, SQLiteConnection.Connector());
		    JasperViewer.viewReport(jasperPrint,false);
		//    OutputStream os = new FileOutputStream(new File("C:\\Users\\Public"));
		//    JasperExportManager.exportReportToPdfStream(jasperPrint, os);
		    
		    
		
		
	} catch (JRException e) {
		e.printStackTrace();
	} catch(Exception e) {
		e.printStackTrace();	
	}
		
	}
	
	public int getSales() {
		return totalUnits;
		
	}
	
	public int getTotal() {
		return totalAmount;
		
	}


	public void clearTempBuffer() {
		tempBuffer = StringBuffer("");
	}


	private StringBuffer StringBuffer(String string) {
		// TODO Auto-generated method stub
		return null;
	}

}
