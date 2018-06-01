package dblayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dblayer.interfaces.IFDBTransaction;
import modlayer.Transaction;
import modlayer.TransactionType;

public class DBTransaction implements IFDBTransaction {

	private DBConnection dbCon;
	private Connection con;

	public DBTransaction() {
		dbCon = DBConnection.getInstance();
		con = dbCon.getConnection();
	}

	@Override
	public Transaction selectTransaction(int id) {
		Transaction transaction = null;
		
		String query =
				  "SELECT * FROM [Transaction]"
				+ "WHERE id = ?";
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, id);
			
			ResultSet results = ps.executeQuery();
			if (results.next()) {
				transaction = buildTransaction(results);
			}
		} catch (SQLException e) {
			System.out.println("Transaction was not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return transaction;
	}

	@Override
	public int insertTransaction(Transaction transaction) {
		int id = -1;
		
		String query =
				  "INSERT INTO [Transaction] "
				+ "(amount, transaction_type) "
				+ "VALUES (?, ?)";
		try {
			PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setQueryTimeout(5);
			ps.setDouble(1, transaction.getAmount() * -1);
			ps.setInt(2, transaction.getType().getId());
			
			if (ps.executeUpdate() > 0) {
				ResultSet generatedKeys = ps.getGeneratedKeys();
	            if (generatedKeys.next()) {
	            	id = generatedKeys.getInt(1);
		            transaction.setId(id);
	            }
			}
			ps.close();
		}
		catch (SQLException e) {
			System.out.println("Transaction was not inserted!");
			System.out.println(e.getMessage());
			System.out.println(query);
			
			return -1;
		}
		
		return id;
	}
	
	private Transaction buildTransaction(ResultSet results) throws SQLException {
		Transaction transaction = null;
		
		String query = "";
		try {

			transaction = new Transaction();
			transaction.setId(results.getInt("id"));
			transaction.setAmount(results.getDouble("amount") * -1);
			transaction.setType(TransactionType.getType(results.getInt("transaction_type")));
			transaction.setTimestamp(results.getDate("timestamp"));
		}
		catch (SQLException e) {
			System.out.println("Transaction was not built!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return transaction;
	}
}
