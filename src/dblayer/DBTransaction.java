package dblayer;

import java.sql.Connection;

import dblayer.interfaces.IFDBTransaction;
import modlayer.Transaction;

public class DBTransaction implements IFDBTransaction {

	private Connection con;
	
	public DBTransaction() {
		con = DBConnection.getConnection();
	}

	@Override
	public Transaction selectTransaction(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insertTransaction(Transaction transaction) {
		// TODO Auto-generated method stub
		return 0;
	}
}
