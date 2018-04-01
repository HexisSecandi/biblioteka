package prvaos.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextField;

/**
 * Implementation of Books within the database.
 * Contains all the methods used for manipulation of books in the database.
 * 
 * @author Hexis Secandi
 */
public class Book implements LibInterface {
	
	private DatabaseConnection dbCon = DatabaseConnection.getDatabaseConnection();
	private Connection con = null;
	private Statement stmt = null;
	private PreparedStatement pStmt = null;
	
	private SimpleStringProperty authorProperty = new SimpleStringProperty();
	private SimpleStringProperty titleProperty = new SimpleStringProperty();
	private SimpleStringProperty isbnProperty = new SimpleStringProperty();
	private SimpleIntegerProperty stockProperty = new SimpleIntegerProperty();

	@Override
	public boolean assertValidInformation(TextField[] info) {
		for (int i = 0; i < info.length; i++) {
			if (info[i].getText().equals("")) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int getId(String identification) {
		final String sqlCmd = "SELECT id FROM Books WHERE isbn = '" + identification + "'";
		int id = 0;
		createTableIfNotExists();
		try {
			con = dbCon.connectToDatabase();
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sqlCmd);
			while (rs.next()) {
				id = rs.getInt("id");
			}
			rs.close();
			stmt.close();
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public void createTableIfNotExists() {
		final String sqlCmd = "CREATE TABLE IF NOT EXISTS Books(author text NOT NULL, title text NOT NULL, isbn text NOT NULL, stock INTEGER NOT NULL, id INTEGER PRIMARY KEY)";
		try {
			con = dbCon.connectToDatabase();
			stmt = con.createStatement();
			stmt.executeUpdate(sqlCmd);
			stmt.close();
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void updateData(String column, String value, int id) {
		final String sqlCmd = "UPDATE Books SET " + column + "= ? WHERE id = ?";
		createTableIfNotExists();
		try {
			con = dbCon.connectToDatabase();
			pStmt = con.prepareStatement(sqlCmd);
			pStmt.setString(1, value);
			pStmt.setInt(2, id);
			pStmt.executeUpdate();
			pStmt.close();
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds the book into the database. 
	 * @param information - An array with field information.
	 * @author Hexis Secandi
	 */
	public void addBook(TextField[] information) {
		final String sqlCmd = "INSERT INTO Books(author, title, isbn, stock) VALUES(?, ?, ?, ?)";
		createTableIfNotExists();
		try {
			if (!assertValidInformation(information)) {
				return;
			}
			con = dbCon.connectToDatabase();
			pStmt = con.prepareStatement(sqlCmd);
			for (int i = 0, j = 1; i < information.length; i++, j++) {
				if (j == 4) { //4. index je 'na stanju', te pošto je vrijednost u tom TextField uvijek int, specifično će koristiti setInt() metodu
					pStmt.setInt(j, Integer.valueOf(information[i].getText()));
				}
				pStmt.setString(j, information[i].getText());
			}
			pStmt.executeUpdate();
			pStmt.close();
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Removes the book from the database based on its ID.
	 * @param id - ID of the book
	 * @author Hexis Secandi
	 */
	public void removeBook(int id) {
		final String sqlCmd = "DELETE FROM Books WHERE id = '" + id + "'";
		createTableIfNotExists();
		try {
			con = dbCon.connectToDatabase();
			stmt = con.createStatement();
			stmt.executeUpdate(sqlCmd);
			stmt.close();
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getAuthorProperty() {
		return authorProperty.get();
	}

	public void setAuthorProperty(String authorProperty) {
		this.authorProperty.set(authorProperty);
	}

	public String getTitleProperty() {
		return titleProperty.get();
	}

	public void setTitleProperty(String titleProperty) {
		this.titleProperty.set(titleProperty);
	}

	public String getIsbnProperty() {
		return isbnProperty.get();
	}

	public void setIsbnProperty(String isbnProperty) {
		this.isbnProperty.set(isbnProperty);
	}

	public Integer getStockProperty() {
		return stockProperty.get();
	}

	public void setStockProperty(Integer stockProperty) {
		this.stockProperty.set(stockProperty);
	}
}
