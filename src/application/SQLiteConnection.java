package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {
	public static Connection Connector() {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Public\\Database.db");
			return conn;
		}
		catch(SQLException | ClassNotFoundException e) {
			return null;
		}
	}
}
