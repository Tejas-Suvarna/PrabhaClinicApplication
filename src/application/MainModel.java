package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainModel {
	Connection connection;
	
	public String [] getItemNameList() {
		String [] itemNames = null;
		int itemCount = 0;
		connection = SQLiteConnection.Connector();
		String query = "SELECT ITEM_NAME FROM ITEMS";
		Statement stmt;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				itemCount++;
			}
			itemNames = new String[itemCount];
			rs = stmt.executeQuery(query);
			int i = 0; 
			while(rs.next()) {
				itemNames[i++] = rs.getString("ITEM_NAME");
			}
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return itemNames;
	}
	
}
