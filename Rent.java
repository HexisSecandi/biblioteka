package prvaos.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextField;

/**
 * Controls renting and returning of the books.
 * Contains all the methods necessary for renting and returning of the books.
 * 
 * @author Hexis Secandi
 */
public class Rent implements LibInterface {

	private DatabaseConnection dbCon = DatabaseConnection.getDatabaseConnection();
	private Connection con = null;
	private Statement stmt = null;
	private PreparedStatement pStmt = null;
	
	private SimpleStringProperty viNameProperty = new SimpleStringProperty();
	private SimpleStringProperty viSurnameProperty = new SimpleStringProperty();
	private SimpleStringProperty viNumberProperty = new SimpleStringProperty();
	private SimpleStringProperty viAuthorProperty = new SimpleStringProperty();
	private SimpleStringProperty viTitleProperty = new SimpleStringProperty();
	private SimpleIntegerProperty viRentNoProperty = new SimpleIntegerProperty();
	private SimpleStringProperty viDateProperty = new SimpleStringProperty();
	
	@Override
	public boolean assertValidInformation(TextField[] info) {
		return false;
	}

	@Override
	public int getId(String identification) {
		return 0;
	}

	@Override
	public void updateData(String column, String value, int id) {
		
	}

	@Override
	public void createTableIfNotExists() {
		final String sqlCmd = "CREATE TABLE IF NOT EXISTS Rents(name text, surname text, number text, rentNo INTEGER, author text, title text, date text)";
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
	
	/**
	 * Adds new entry into the table Rents.
	 * Except for that, it increments the 'rentNo' by 1 for a specific student while
	 * decrementing 'stock' by 1 for a specific book.
	 * @param bid - ID from the table Books
	 * @param sid - ID from the table Students
	 * @author Mahir Salihbašić
	 */
	public void addRent(int bid, int sid) {
		final String sqlCmd = "INSERT INTO Rents(name, surname, number, rentNo, author, title, date) VALUES("
				+ "(SELECT name FROM Students WHERE id = " + sid + "), "
				+ "(SELECT surname FROM Students WHERE id = " + sid + "), "
				+ "(SELECT number FROM Students WHERE id = " + sid + "), "
				+ "(SELECT rentNo FROM Students WHERE id = " + sid + "), "
				+ "(SELECT author FROM Books WHERE id = " + bid + "), "
				+ "(SELECT title FROM Books WHERE id = " + bid + "),"
				+ "CURRENT_DATE)";
		final String incrementRentNo = "UPDATE Students SET rentNo = rentNo + 1 WHERE id = " + sid;
		final String decrementStock = "UPDATE Books SET stock = stock - 1 WHERE id = " + bid;
		createTableIfNotExists();
		try {
			con = dbCon.connectToDatabase();
			stmt = con.createStatement();
			stmt.executeUpdate(incrementRentNo);
			stmt.executeUpdate(decrementStock);
			stmt.executeUpdate(sqlCmd);
			stmt.close();
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Removes the book and the student from the table Rents.
	 * 
	 * @param name - Student's name
	 * @param surname - Student's surname
	 * @param classNo - Student's class
	 * @param title - Book's title
	 * @param author - Book's author
	 * @author Hexis Secandi
	 */
	public void removeRent(String name, String surname, String number, String title, String author) {
		final String sqlCmd = "DELETE FROM Rents WHERE name = ? AND surname = ? AND number = ? AND title = ?";
		final String decrementStudentRentNo = "UPDATE Students SET rentNo = rentNo - 1 WHERE name = ? AND surname = ? AND number = ?";
		final String decrementRentsRentNo = "UPDATE Rents SET rentNo = rentNo - 1 WHERE name = ? AND surname = ? AND number = ?";
		final String incrementStock = "UPDATE Books SET stock = stock + 1 WHERE title = ? AND author = ?";
		createTableIfNotExists();
		try {
			con = dbCon.connectToDatabase();
			pStmt = con.prepareStatement(sqlCmd);
			pStmt.setString(1, name);
			pStmt.setString(2, surname);
			pStmt.setString(3, number);
			pStmt.setString(4, title);
			pStmt.executeUpdate();
			pStmt.close();
			
			pStmt = con.prepareStatement(incrementStock);
			pStmt.setString(1, title);
			pStmt.setString(2, author);
			pStmt.executeUpdate();
			pStmt.close();
			
			pStmt = con.prepareStatement(decrementStudentRentNo);
			pStmt.setString(1, name);
			pStmt.setString(2, surname);
			pStmt.setString(3, number);
			pStmt.executeUpdate();
			
			pStmt = con.prepareStatement(decrementRentsRentNo);
			pStmt.setString(1, name);
			pStmt.setString(2, surname);
			pStmt.setString(3, number);
			pStmt.executeUpdate();
			
			pStmt.close();
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
		
	public String getViNameProperty() {
		return viNameProperty.get();
	}

	public void setViNameProperty(String viNameProperty) {
		this.viNameProperty.set(viNameProperty);
	}

	public String getViSurnameProperty() {
		return viSurnameProperty.get();
	}

	public void setViSurnameProperty(String viSurnameProperty) {
		this.viSurnameProperty.set(viSurnameProperty);
	}

	public String getViNumberProperty() {
		return viNumberProperty.get();
	}

	public void setViNumberProperty(String viNumberProperty) {
		this.viNumberProperty.set(viNumberProperty);
	}

	public String getViAuthorProperty() {
		return viAuthorProperty.get();
	}

	public void setViAuthorProperty(String viAuthorProperty) {
		this.viAuthorProperty.set(viAuthorProperty);
	}

	public String getViTitleProperty() {
		return viTitleProperty.get();
	}

	public void setViTitleProperty(String viTitleProperty) {
		this.viTitleProperty.set(viTitleProperty);
	}

	public Integer getViRentNoProperty() {
		return viRentNoProperty.get();
	}

	public void setViRentNoProperty(Integer viRentNoProperty) {
		this.viRentNoProperty.set(viRentNoProperty);
	}
	
	public String getViDateProperty() {
		return viDateProperty.get();
	}
	
	public void setViDateProperty(String viDateProperty) {
		this.viDateProperty.set(viDateProperty);
	}
}
