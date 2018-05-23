package dblayer;

import java.sql.Connection;
import java.util.ArrayList;

import dblayer.interfaces.IFDBSupplier;
import modlayer.Supplier;

public class DBSupplier implements IFDBSupplier {

	private Connection con;
	
	public DBSupplier() {
		con = DBConnection.getConnection();
	}

	@Override
	public ArrayList<Supplier> getSuppliers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Supplier> searchSuppliers(String keyword) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Supplier selectSupplier(String cvr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String insertSupplier(Supplier supplier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateSupplier(Supplier supplier) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteSupplier(Supplier supplier) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
