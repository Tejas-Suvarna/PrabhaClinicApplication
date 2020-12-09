package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddOrEditItemScreenModel {
	Connection connection;
	public String getEditItemQuantity;
	public String getEditItemPrice;
	
	public int isAddMode() {
		try {
			connection = SQLiteConnection.Connector();
			String query = "SELECT ADDEDITMODE FROM FLAGS LIMIT 1";
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int flag = rs.getInt("ADDEDITMODE");
			System.out.println("Add Or Edit Item Screen Model: Flag received");	
			connection.close();
			return flag;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 2;
	}

	public void addItemToDatabase(String itemName, String quantity, String price) throws SQLException {	
			connection = SQLiteConnection.Connector();
			String query = "INSERT INTO ITEMS VALUES ('" + itemName + "'," + price + "," + quantity + ")";
			Statement stmt = connection.createStatement();
			stmt.execute(query);
			System.out.println("Add Or Edit Item Screen Model: New Item added");	
			connection.close();
	}

	public String getItemToEdit() {
		try {
			connection = SQLiteConnection.Connector();
			String query = "SELECT EDIT_ITEM FROM FLAGS LIMIT 1";
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			String itemToEdit = rs.getString("EDIT_ITEM");
			System.out.println("Add Or Edit Item Screen Model: Item to Edit received");	
			connection.close();
			connection = SQLiteConnection.Connector();
			query = "SELECT ITEM_NAME, PRICE, QUANTITY FROM ITEMS WHERE ITEM_NAME = (SELECT EDIT_ITEM FROM FLAGS LIMIT 1)";
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			getEditItemQuantity = rs.getString("QUANTITY");
			getEditItemPrice = rs.getString("PRICE");
			System.out.println("Add Or Edit Item Screen Model: Item to Edit received");	
			connection.close();
			return itemToEdit;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public void updateItemDetails(String itemNameTextField, String itemQuantityTextField,
			String itemPriceTextField) throws SQLException {
		
		connection = SQLiteConnection.Connector();
		String query = "UPDATE ITEMS SET ITEM_NAME = '" + itemNameTextField.toUpperCase() + "', PRICE = " + itemPriceTextField + ", QUANTITY = " + itemQuantityTextField + " WHERE ITEM_NAME = (SELECT EDIT_ITEM FROM FLAGS);";
		Statement stmt;
		stmt = connection.createStatement();
		stmt.execute(query);
		System.out.println("Add Or Edit Item Screen Model: Item edited");	
		connection.close();
		
	}

}
