package dblayer;

import java.sql.*;

public class DBConnection
{
    private DatabaseMetaData dma;
    private static Connection con;
    private static DBConnection instance = null;

    private DBConnection() {
    	final String server 	= "kraka.ucn.dk";
    	final String port 		= "1433";
    	final String user 		= "dmaj0917_1067616";
    	final String password	= "Password1!";
    	final String database	= "dmaj0917_1067616";
    	
    	String url = "jdbc:sqlserver://"+server+":"+port+";user="+user+";password="+password+";databaseName="+database+";";
    	
        try {
            con = DriverManager.getConnection(url);
            con.setAutoCommit(true);
            dma = con.getMetaData();
            
            //System.out.println("Connection to: " + dma.getURL());
            //System.out.println("Driver: " + dma.getDriverName());
            //System.out.println("Database product name: " + dma.getDatabaseProductName());
        }
        catch (SQLException e) {
            System.out.println("Problems with the connection to the database.");
            System.out.println("Error message: " + e.getMessage());
        }
    }
    
    public static DBConnection getInstance() {
        if (instance == null) {
        	instance = new DBConnection();
        }
        return instance;
    }
    public Connection getConnection() {
    	return con;
    }
    
    public void closeConnection() {
	   	try {
	        con.close();
	        
	        System.out.println("The connection is closed.");
	    }
	   	catch (Exception e){
	   		System.out.println("Error trying to close the database.");
	   		System.out.println("Error message: " + e.getMessage());
	   	}	
    }
    public void startTransaction() {
    	try {
    		con.setAutoCommit(false);
    		
    		System.out.println("New transaction started.");
        }
    	catch(Exception e){
    		System.out.println("Problems with starting a transaction.");
	        System.out.println("Error message: " + e.getMessage());
    	}
    }
    public void commitTransaction()
    {
    	try {
    		con.commit();
    		con.setAutoCommit(true);
    		
    		System.out.println("Transaction commited.");
        }
    	catch(Exception e){
    		System.out.println("Problems with commiting the transaction.");
	        System.out.println("Error message: " + e.getMessage());
    	}
    }
    public void rollbackTransaction()
    {
    	try {
    		con.rollback();
    		con.setAutoCommit(true);
    		
    		System.out.println("Transaction rollbacked.");
        }
    	catch(Exception e){
    		System.out.println("Problems with rollbacking the transaction.");
	        System.out.println("Error message: " + e.getMessage());
    	}
    }
}