package dblayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dblayer.interfaces.IFDBStocktaking;
import modlayer.Discrepancy;
import modlayer.Stocktaking;

public class DBStocktaking implements IFDBStocktaking {
	
	private DBConnection dbCon;
	private Connection con;

	public DBStocktaking() {
		dbCon = DBConnection.getInstance();
		con = dbCon.getConnection();
	}
	
	@Override
	public int insertStocktaking(Stocktaking stocktaking) {
		int id = -1;
		
		String query = "INSERT INTO [Stocktaking] (employee_cpr) VALUES (?)";
		try {
			dbCon.startTransaction();
			
			PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setQueryTimeout(5);
			ps.setString(1, stocktaking.getEmployee().getCpr());
			
			if (ps.executeUpdate() > 0) {
				ResultSet generatedKeys = ps.getGeneratedKeys();
	            if (generatedKeys.next()) {
	            	id = generatedKeys.getInt(1);
		            stocktaking.setId(id);
	            }
			}
			ps.close();
			
			//Discrepancies
			String itemBarcode;
			double quantity;
			
			for (Discrepancy discrepancy : stocktaking.getDiscrepancies()) {
				itemBarcode = discrepancy.getItem().getBarcode();
				quantity = discrepancy.getQuantity();
				
				query =   "INSERT INTO [Discrepancy] "
						+ "(stocktaking_id, item_barcode, quantity) "
						+ "VALUES (?, ?, ? - ("
						+ "	SELECT quantity"
						+ "	FROM [Item]"
						+ "	WHERE barcode = ?))";
				try {
					ps = con.prepareStatement(query);
					ps.setQueryTimeout(5);
					ps.setInt(1, id);
					ps.setString(2, itemBarcode);
					ps.setDouble(3, quantity);
					ps.setString(4, itemBarcode);
					
					if (ps.executeUpdate() < 1) {
						throw new SQLException();
					}
					ps.close();
				}
				catch (SQLException e) {
					System.out.println("Discrepancy was not inserted!");
					
					throw e;
				}
			}
			
			dbCon.commitTransaction();
		}
		catch (SQLException e) {
			System.out.println("Stocktaking was not inserted!");
			System.out.println(e.getMessage());
			System.out.println(query);
			
			dbCon.rollbackTransaction();
			return -1;
		}
		
		return id;
	}
}
