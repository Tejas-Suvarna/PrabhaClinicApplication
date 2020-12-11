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

public class IndividualBillModel {
	
	Connection connection;
	String dateString, invoice;
	String billNo, toLabelText, dateLabelText, grandTotalText, paymentStatusText;
	ObservableList<Item> items = FXCollections.observableArrayList();
	float sgst,cgst;

	public IndividualBillModel (String billNo){
		this.billNo = new String(billNo);
		connection = SQLiteConnection.Connector();
		String query = "SELECT * FROM BILL WHERE BILL_ID = " + billNo;
		Statement stmt;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			toLabelText = rs.getString("CUSTOMER_NAME");
			dateLabelText = rs.getString("DATE");
			grandTotalText = Integer.toString(rs.getInt("TOTAL_AMT"));
			paymentStatusText = rs.getString("PAYMENT_STATUS");
			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("IndividualBillModel: Bill Model has retrieved bill details from Database");
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
		/*	System.out.println(getClass().getResource("Blank_A4.jrxml").getPath());
			   JasperReport js = JasperCompileManager.compileReport("C:\\Users\\Public\\Blank_A4.jrxml");
		       Map<String, Object> parameters = new HashMap<String, Object>();
		       parameters.put("billID", 32);
		       parameters.put("Name", "Tehas");
		       parameters.put("Date", "2019-05-12");
		       parameters.put("ValueInWords", "Sixty nine thousand one thrity five");
		 
		       JasperPrint jasperPrint = JasperFillManager.fillReport(js,parameters, SQLiteConnection.Connector());
		       JasperExportManager.exportReportToPdfFile(jasperPrint,"C:/jasperoutput/StyledTextReport.pdf");
		       JasperViewer.viewReport(jasperPrint);
		       OutputStream os = new FileOutputStream(new File("C:\\temp"));
		       JasperExportManager.exportReportToPdfStream(jasperPrint, os);*/
			
			
			//C:/Users/tejas/eclipse-workspace/Prabha%20Clinic/bin/application/Blank_A4.jrxml
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date2 = sdf.parse(date);
				SimpleDateFormat sdfNew = new SimpleDateFormat("dd/MM/yyyy");
				date=sdfNew.format(date2);
				
				getGSTValuesFromDatabase();
				InputStream in = new FileInputStream(new File("C:\\Users\\Public\\Blank_A4.jrxml"));
				JasperDesign jd=JRXmlLoader.load(in);
				String sql="SELECT ITEM_NAME,QUANTITY,PRICE,QUANTITY*PRICE AS AMT,DISCOUNT,QUANTITY*PRICE-DISCOUNT AS VALUE, Field6 AS TP FROM CART WHERE BILL_ID = " + billID;
				System.out.println(billID+name+date+valueInMoney+grand+"\n"+sql);
				JRDesignQuery jdq = new JRDesignQuery();
				jdq.setText(sql);
				jd.setQuery(jdq);
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("BillID", billID);
				parameters.put("Name", name);
				parameters.put("Date", date);
				parameters.put("ValueInWords", valueInMoney);
				parameters.put("Grand", grand);
				parameters.put("GSTperc", Float.toString(sgst+cgst));
				DecimalFormat numberFormat = new DecimalFormat("#.00");
				parameters.put("GST", numberFormat.format((sgst+cgst)/100*Float.parseFloat(grand)));
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
	
	

	public ObservableList<Item> retrieveItems(String billNo) {
		
		connection = SQLiteConnection.Connector();
		String query = "SELECT ITEM_NAME,PRICE,QUANTITY,DISCOUNT FROM CART WHERE BILL_ID = " + billNo;
		Statement stmt;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int i = 1;
			while(rs.next()) {				
				Item item = new Item();
				item.setSr(Integer.toString(i++));
				item.setDesc(rs.getString("ITEM_NAME"));
				item.setQty(Integer.toString(rs.getInt("QUANTITY")));
				item.setRate(Integer.toString(rs.getInt("PRICE")));
				item.setAmt(Integer.toString(Integer.parseInt(item.getQty()) * Integer.parseInt(item.getRate()))); 
				item.setDst(Integer.toString(rs.getInt("DISCOUNT")));
				item.setValue(Integer.toString(Integer.parseInt(item.getQty()) * Integer.parseInt(item.getRate()) - Integer.parseInt(item.getDst())));
				item.setTrade(Integer.toString(Integer.parseInt(item.getRate()) - Integer.parseInt(item.getDst())/Integer.parseInt(item.getQty())));
				//System.out.println(item.getTrade());
				
				items.add(item);
			}
			connection.close();
			return items;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("IndividualBillModel: Bill Model has initialized Medicine Items are retrieved from the Database");
		return null;
	}

	public String getToLabelText() {
		
		return toLabelText;
	}

	public String getInvoiceLabelText() {
		
		return billNo;
	}

	public String getDateLabelText() {
		
		return dateLabelText;
	}

	public String getGrandTotalText() {
		
		return grandTotalText;
	}

	public String getPaymentStatus() {
		
		return paymentStatusText;
	}

}
