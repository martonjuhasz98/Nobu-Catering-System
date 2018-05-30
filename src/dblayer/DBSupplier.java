package dblayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dblayer.interfaces.IFDBSupplier;
import modlayer.City;
import modlayer.Supplier;

public class DBSupplier implements IFDBSupplier {

	private DBConnection dbCon;
	private Connection con;

	public DBSupplier() {
		dbCon = DBConnection.getInstance();
		con = dbCon.getConnection();
	}

	@Override
	public ArrayList<Supplier> getSuppliers() {
		ArrayList<Supplier> suppliers = new ArrayList<>();
		
		String query =
			  "SELECT * FROM [Supplier]";
		try {
			
			Statement st = con.createStatement();
			st.setQueryTimeout(5);
			
			Supplier supplier;
			ResultSet results = st.executeQuery(query);
			while (results.next()) {
				supplier = buildSupplier(results);
				suppliers.add(supplier);
			}
			st.close();
		} catch (SQLException e) {
			System.out.println("Suppliers were not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return suppliers;
	}

	@Override
	public ArrayList<Supplier> searchSuppliers(String keyword) {
		ArrayList<Supplier> suppliers = new ArrayList<Supplier>();

		String query =
			    "SELECT * FROM [Supplier]"
			  + "WHERE cvr LIKE ? "
			  + "OR name LIKE ?";
		
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setString(1, "%" + keyword + "%");
			ps.setString(2, "%" + keyword + "%");
			
			Supplier supplier;
			ResultSet results = ps.executeQuery();
			while (results.next()) {
				supplier = buildSupplier(results);
				suppliers.add(supplier);
			}
			ps.close();
		}
		catch (SQLException e) {
			System.out.println("Suppliers were not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return suppliers;
	}

	@Override
	public Supplier selectSupplier(String cvr) {
		Supplier supplier = null;
		
		String query =
				  "SELECT * FROM [Supplier]"
				+ "WHERE cvr = ?";
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setString(1, cvr);
			
			ResultSet results = ps.executeQuery();
			if (results.next()) {
				supplier = buildSupplier(results);
			}
		} catch (SQLException e) {
			System.out.println("Supplier was not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return supplier;
	}

	@Override
	public String insertSupplier(Supplier supplier) {
		String cvr = null;
		
		String query =
				  "INSERT INTO [Supplier] "
				+ "(cvr, name, address, zip_code, phone, email) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setString(1, supplier.getCvr());
			ps.setString(2, supplier.getName());
			ps.setString(3, supplier.getAddress());
			ps.setString(4, supplier.getCity().getZipCode());
			ps.setString(5, supplier.getPhone());
			ps.setString(6, supplier.getEmail());
			
			if (ps.executeUpdate() > 0) {
				cvr = supplier.getCvr();
			}
			ps.close();
		}
		catch (SQLException e) {
			System.out.println("Supplier was not inserted!");
			System.out.println(e.getMessage());
			System.out.println(query);
			
			return null;
		}
		
		return cvr;
	}

	@Override
	public boolean updateSupplier(Supplier supplier) {
		boolean success = false;
		
		String query =
				"UPDATE [Supplier] "
			  + "SET name = ?, "
			  + "address = ?, "
			  + "zip_code = ?, "
			  + "phone = ?, "
			  + "email = ? "
			  + "WHERE cvr = ?";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setString(1, supplier.getName());
			ps.setString(2, supplier.getAddress());
			ps.setString(3, supplier.getCity().getZipCode());
			ps.setString(4, supplier.getPhone());
			ps.setString(5, supplier.getEmail());
			ps.setString(6, supplier.getCvr());
			
			success = ps.executeUpdate() > 0;
			ps.close();
		}
		catch (SQLException e) {
			System.out.println("Supplier was not updated!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return success;
	}

	@Override
	public boolean deleteSupplier(Supplier supplier) {
		boolean success = false;
		
		String query = "DELETE FROM [Supplier] WHERE cvr = ?";
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setString(1, supplier.getCvr());
			
			success = ps.executeUpdate() > 0;
			ps.close();
		}
		catch (SQLException e) {
			System.out.println("Supplier was not deleted!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
			
		return success;
	}
	
	private Supplier buildSupplier(ResultSet results) throws SQLException {
		Supplier supplier = null;
		
		String query = "";
		try {
			
			//City
			DBCity dbCity = new DBCity();
			City city = dbCity.selectCity(results.getString("zip_code"));
			
			//Supplier
			supplier = new Supplier();
			supplier.setCvr(results.getString("cvr"));
			supplier.setName(results.getString("name"));
			supplier.setAddress(results.getString("address"));
			supplier.setCity(city);
			supplier.setPhone(results.getString("phone"));
			supplier.setEmail(results.getString("email"));
		}
		catch (SQLException e) {
			System.out.println("Supplier was not built!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return supplier;
	}
}
