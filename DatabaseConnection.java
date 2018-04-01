package prvaos.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.filechooser.FileSystemView;

/**
 * This class is used as an abstraction layer for the database connection.
 * It uses the Singletone pattern
 * 
 * @author Hexis Secandi
 */
public class DatabaseConnection {

	private static DatabaseConnection dbCon = new DatabaseConnection();
	private final String dbFilePath = FileSystemView.getFileSystemView().getDefaultDirectory().getAbsolutePath() + "/Biblioteka/biblioteka.db"; //Lokacija database fajla
	private File dbDirFile = new File(FileSystemView.getFileSystemView().getDefaultDirectory() + "/Biblioteka/");

	
	/**
	 * Implementation of the Singleton pattern.
	 * 
	 * @return This class
	 * @author Hexis Secandi
	 */
	public static DatabaseConnection getDatabaseConnection() {
		return dbCon;
	}
	
	/**
	 * Connects to the database.
	 * 
	 * @return Connection to the database
	 * @throws SQLException - In case that the connection couldn't be made
	 * @throws ClassNotFoundException - In case that the JDBC driver class can't be found
	 * @author Mahir Salihbašić
	 */
	public Connection connectToDatabase() throws SQLException, ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		if (!dbDirFile.exists()) {
			dbDirFile.mkdir();
		}
		return DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
	}
	
}
