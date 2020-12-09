package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SettingsModel {
	Connection connection;
	float sgst=0,cgst=0;
	public SettingsModel() {
		getGSTValuesFromDatabase();
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

	public String getCGST() {
		return Float.toString(cgst);
	}

	public String getSGST() {
		return Float.toString(sgst);
	}

	public void setGST(String sgst, String cgst) {
		this.cgst=Float.parseFloat(sgst);
		this.sgst=Float.parseFloat(cgst);				
		setGSTValuesToDatabase();
	}

	private void setGSTValuesToDatabase() {
		connection = SQLiteConnection.Connector();
		String query = "UPDATE FLAGS SET SGST='" + sgst + "', CGST='" + cgst + "'";
		Statement stmt;
		try {
			stmt = connection.createStatement();
			stmt.executeUpdate(query);				
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("SettingsModel: CGST SGST set from the Database");
	}
	
	
}
