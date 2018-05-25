package dblayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dblayer.interfaces.IFDBTransaction;
import modlayer.Transaction;
import modlayer.TransactionType;

public class DBTransaction implements IFDBTransaction {

	private Connection con;
	
	public DBTransaction() {
		con = DBConnection.getConnection();
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
				+ "(id, amount, transaction_type) "
				+ "VALUES (?, ?, ?)";
		try {
			PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setQueryTimeout(5);
			ps.setInt(1, transaction.getId());
			ps.setDouble(2, transaction.getAmount());
			ps.setInt(3, transaction.getType().getId());
			
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
			transaction.setAmount(results.getDouble("amount"));
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
