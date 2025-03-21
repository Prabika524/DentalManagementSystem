package myjdbc;

/*
import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
	final private String HOST = "localhost";
	final private String USER = "root";
	final private String PASS = "prabika456";
	final private int PORT = 3306;
	final private String DBNAME = "DentalManagementSystem";
	final private String DRIVER = "com.mysql.cj.jdbc.Driver";
	final private String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DBNAME;

	private Connection conn=null;
	
	//Connect with database
	public Connection connect() {
		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASS);
			
		}
		catch(Exception ex) {
			System.out.println("Error : "+ex.getMessage());
		}
		return conn;
		
	}
	//connect to Connection
	public void close() {
		try {
			if(!this.conn.isClosed()){
				this.conn.close();
		
		
		}
		
		}
		catch(Exception ex) {
			System.out.println("Error : "+ex.getMessage());
		}

		
	}

}
*/
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/DentalManagementSystem";
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = "prabika456"; // Replace with your MySQL password

    public static Connection connect() throws SQLException {
        try {
            // Load the MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new SQLException(" connect to database", e);
        }
    }
}

