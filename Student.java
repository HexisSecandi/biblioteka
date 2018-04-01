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
 * Implementation of Student inside of the database.
 * Contains all the methods necessary for student manipulation.
 * 
 * @author Hexis Secandi
 */
public class Student implements LibInterface {
	
	private DatabaseConnection dbCon = DatabaseConnection.getDatabaseConnection();
	private Connection con = null;
	private Statement stmt = null;
	private PreparedStatement pStmt = null;
	
	private SimpleStringProperty nameProperty = new SimpleStringProperty();
	private SimpleStringProperty surnameProperty = new SimpleStringProperty();
	private SimpleStringProperty numberProperty = new SimpleStringProperty();
	private SimpleStringProperty classProperty = new SimpleStringProperty();
	private SimpleIntegerProperty rentNoProperty = new SimpleIntegerProperty();

	@Override
	public boolean assertValidInformation(TextField[] info) {
		for (int i = 0; i < info.length; i++) {
			if (info[i].getText().equalsIgnoreCase("")) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int getId(String identification) {
		final String sqlCmd = "SELECT id FROM Students WHERE number = '" + identification + "'";
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
	public void updateData(String column, String value, int id) {
		final String sqlCmd = "UPDATE Students SET " + column + " = ? WHERE id = ?";
		try {
			con = dbCon.connectToDatabase();
			pStmt = con.prepareStatement(sqlCmd);
			pStmt.setString(1, value);
			pStmt.setInt(2, id);
			pStmt.executeUpdate();
			pStmt.cancel();
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createTableIfNotExists() {
		final String sqlCmd = "CREATE TABLE IF NOT EXISTS Students(name text NOT NULL, surname text NOT NULL, number text NOT NULL, class text NOT NULL, rentNo INTEGER NOT NULL, id INTEGER PRIMARY KEY)";
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
	 * Adds a student into the database.
	 * @param information - An array with information for the fields.
	 * @author Hexis Secandi
	 */
	public void addStudent(TextField[] information) {
		final String sqlCmd = "INSERT INTO Students(name, surname, number, class, rentNo) VALUES(?, ?, ?, ?, ?)";
		createTableIfNotExists();
		try {
			if (!assertValidInformation(information)) {
				return;
			}
			con = dbCon.connectToDatabase();
			pStmt = con.prepareStatement(sqlCmd);
			for (int i = 0, j = 1; i < information.length; i++, j++) {
				if (j == 4 || j == 5) { //4th and 5th index have an 'int' value, therefore the 'setInt()' method is called
					pStmt.setInt(j, Integer.valueOf(information[i].getText()));
				}
				pStmt.setString(j, information[i].getText());
			}
			pStmt.setInt(5, Integer.valueOf(information[4].getText()));
			pStmt.executeUpdate();
			pStmt.close();
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Removies the student from the database based on it's ID.
	 * @param id - ID of the student
	 * @author Hexis Secandi
	 */
	public void removeStudent(int id) {
		final String sqlCmd = "DELETE FROM Students WHERE id = '" + id + "'";
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
	
	public String getNameProperty() {
		return nameProperty.get();
	}

	public void setNameProperty(String nameProperty) {
		this.nameProperty.set(nameProperty);
	}

	public String getSurnameProperty() {
		return surnameProperty.get();
	}

	public void setSurnameProperty(String surnameProperty) {
		this.surnameProperty.set(surnameProperty);
	}

	public String getNumberProperty() {
		return numberProperty.get();
	}

	public void setNumberProperty(String numberProperty) {
		this.numberProperty.set(numberProperty);
	}

	public String getClassProperty() {
		return classProperty.get();
	}

	public void setClassProperty(String classProperty) {
		this.classProperty.set(classProperty);
	}

	public Integer getRentNoProperty() {
		return rentNoProperty.get();
	}

	public void setRentNoProperty(Integer rentNoProperty) {
		this.rentNoProperty.set(rentNoProperty);
	}

}
