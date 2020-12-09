package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StockModel {
	ObservableList<String> itemNames = FXCollections.observableArrayList();
//	ObservableList<String> itemQuantities = FXCollections.observableArrayList();
//	ObservableList<String> itemPrices = FXCollections.observableArrayList();
//	ObservableList<String> itemMRP = FXCollections.observableArrayList();
	Connection connection;
	
	String getEditItemQuantity, getEditItemPrice, getEditItemMRP;
	
	public ObservableList<Items> getItems(boolean isCheckBoxSelected) {
		ObservableList<Items> returnItems = FXCollections.observableArrayList();
		Items tempItem;
		connection = SQLiteConnection.Connector();
		String query;
		if(isCheckBoxSelected)
			query = "SELECT ITEM_NAME,PRICE,QUANTITY,MRP,DATE FROM ITEMS ORDER BY DATE DESC";
		else query = "SELECT ITEM_NAME,PRICE,QUANTITY,MRP,DATE FROM ITEMS WHERE QUANTITY != 0 ORDER BY DATE DESC";
		Statement stmt;
		try {
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {		
				String description = rs.getString("ITEM_NAME");
				itemNames.add(description);
				String quantity = Integer.toString(rs.getInt("QUANTITY"));
//				itemQuantities.add(quantity);
				String price = Integer.toString(rs.getInt("PRICE"));
//				itemPrices.add(price);
				String mrp = Integer.toString(rs.getInt("MRP"));
//				itemMRP.add(mrp);
				String date = rs.getString("DATE");
				tempItem = new Items(description,quantity,price,mrp,date);
				returnItems.add(tempItem);
			}
			System.out.println("StockModel: Medicine Items are retrieved from the Database");
			connection.close();
			return returnItems;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public ObservableList<String> getItemNames(){
		return itemNames;
	}

//	@Deprecated
//	public String getItemQuantity(int selectedIndex) {
//		return itemQuantities.get(selectedIndex);
//	}

//	public void addQuantityToCurrent(String quantityToAdd, String selectedItem) {
//		try {
//			connection = SQLiteConnection.Connector();
//			String query = "UPDATE ITEMS SET QUANTITY = QUANTITY + " + quantityToAdd + " WHERE ITEM_NAME = " + "'" + selectedItem + "'";
//			Statement stmt = connection.createStatement();
//			stmt.executeUpdate(query);
//			System.out.println("StockModel: Item quantity Updated");	
//			connection.close();
//		} catch (NumberFormatException e) {
//			e.printStackTrace();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}

//	public void subQuantityToCurrent(String quantityToSub, String selectedItem) {
//		try {
//			connection = SQLiteConnection.Connector();
//			String query = "UPDATE ITEMS SET QUANTITY = QUANTITY - " + quantityToSub + " WHERE ITEM_NAME = " + "'" + selectedItem + "'";
//			Statement stmt = connection.createStatement();
//			stmt.executeUpdate(query);
//			System.out.println("StockModel: Item quantity Updated");	
//			connection.close();
//		} catch (NumberFormatException e) {
//			e.printStackTrace();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		
//	}

	public void setAddOrEditFlag(int i) {
		try {
			connection = SQLiteConnection.Connector();
			String query = "UPDATE FLAGS SET ADDEDITMODE = " + i;
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);
			System.out.println("StockModel: ADD_EDIT_MODE Flag Updated");	
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void addItemToDatabase(String itemName, String quantity, String price, String mrp, String date) throws SQLException{	
		connection = SQLiteConnection.Connector();
		String query = "INSERT INTO ITEMS VALUES ('" + itemName + "'," + price + "," + quantity + ", " + mrp + ", " + "'" + date + "')";
		Statement stmt = connection.createStatement();
		stmt.execute(query);
		System.out.println("Stock Model: New Item added");	
		connection.close();
	}
	
	public void updateItemDetails(String itemNameTextField, String itemQuantityTextField,
			String itemMRPTextField, String itemPriceTextField) throws SQLException {
		
		connection = SQLiteConnection.Connector();
		String query = "UPDATE ITEMS SET ITEM_NAME = '" + itemNameTextField.toUpperCase() + "', PRICE = " + itemPriceTextField + ", QUANTITY = " + itemQuantityTextField + ", MRP = " + itemMRPTextField + " WHERE ITEM_NAME = (SELECT EDIT_ITEM FROM FLAGS);";
		Statement stmt;
		System.out.println(query);
		stmt = connection.createStatement();
		stmt.execute(query);
		System.out.println("Add Or Edit Item Screen Model: Item edited");	
		connection.close();
		
	}
	
	
//	public void getItemToEdit(String item) {
//		try {
//			String query; 
//			connection = SQLiteConnection.Connector();
//			query = "SELECT ITEM_NAME, PRICE, QUANTITY, MRP FROM ITEMS WHERE ITEM_NAME = '" + item + "'";
//			Statement stmt = connection.createStatement();
//			ResultSet rs = stmt.executeQuery(query);
//			getEditItemQuantity = rs.getString("QUANTITY");
//			getEditItemPrice = rs.getString("PRICE");
//			getEditItemMRP = rs.getString("MRP");
//			System.out.println("Stock Model: Item to Edit received");	
//			connection.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		
//	}

	public void setItemNameToEdit(String selectedItem) {
		
		try {
			connection = SQLiteConnection.Connector();
			String query = "UPDATE FLAGS SET EDIT_ITEM = '" + selectedItem + "'";
			System.out.println(query);
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);
			System.out.println("StockModel: EDIT_ITEM Flag Updated");	
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public void deleteItem(String itemName) {
		
		try {
			connection = SQLiteConnection.Connector();
			String query = "DELETE FROM ITEMS WHERE ITEM_NAME = '" + itemName + "';";
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);
			System.out.println("StockModel: Item " + itemName + " deleted.");	
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
