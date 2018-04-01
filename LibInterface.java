package prvaos.database;

import javafx.scene.control.TextField;

/**
 * Interface used as a layer between all the database classes.
 * 
 * @author Hexis Secandi
 */
public interface LibInterface {

	/**
	 * Checks if all the fields are filled.
	 * 
	 * @param info - Array with the information from the fields
	 * @return {@code true} if all the fields are filled, {@code false} otherwise
	 * @author Hexis Secandi
	 */
	boolean assertValidInformation(TextField[] info);
	
	/**
	 * Takes the ID based on the data provided.
	 *  
	 * @param identification - ISBN or JMBG of the student
	 * @return ID, 0 otherwise
	 * @author Hexis Secandi
	 */
	int getId(String identification);
	
	/**
	 * Changes the data inside of a specific database cell
	 * 
	 * @param column - Column in the table
	 * @param value - New value
	 * @param id - ID of the column
	 * @author Hexis Secandi
	 */
	void updateData(String column, String value, int id);
	
	/**
	 * Call this method before any manipulations over the database.
	 * Creates the table in case that it doesn't exist.
	 * 
	 * @author Hexis Secandi
	 */
	void createTableIfNotExists();
	
}
