package dblayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dblayer.interfaces.IFDBItemCategory;
import modlayer.ItemCategory;

public class DBItemCategory implements IFDBItemCategory {
	
	private DBConnection dbCon;
	private Connection con;

	public DBItemCategory() {
		dbCon = DBConnection.getInstance();
		con = dbCon.getConnection();
	}
	
	@Override
	public ArrayList<ItemCategory> getCategories() {
		ArrayList<ItemCategory> categories = new ArrayList<>();
		
		String query = "SELECT * FROM [Item_Category]";
		try {
			
			Statement st = con.createStatement();
			st.setQueryTimeout(5);
			
			ItemCategory category;
			ResultSet results = st.executeQuery(query);
			while (results.next()) {
				category = buildItemCategory(results);
				categories.add(category);
			}
			st.close();
		} catch (SQLException e) {
			System.out.println("ItemCategories were not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return categories;
	}
	@Override
	public ItemCategory selectCategory(int id) {
		ItemCategory category = null;
		
		String query = "SELECT * FROM [Item_Category] WHERE id = ?";
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, id);
			
			ResultSet results = ps.executeQuery();
			if (results.next()) {
				category = buildItemCategory(results);
			}
		} catch (SQLException e) {
			System.out.println("ItemCategory was not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return category;
	}
	@Override
	public int insertCategory(ItemCategory category) {
		int id = -1;
		
		String query = "INSERT INTO [Item_Category] (name) VALUES (?)";
		try {
			
			PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setQueryTimeout(5);
			ps.setString(1, category.getName());
			
			if (ps.executeUpdate() > 0) {
				ResultSet generatedKeys = ps.getGeneratedKeys();
	            if (generatedKeys.next()) {
	            	id = generatedKeys.getInt(1);
		            category.setId(id);
	            }
			}
			ps.close();
			
		}
		catch (SQLException e) {
			System.out.println("ItemCategory was not inserted!");
			System.out.println(e.getMessage());
			System.out.println(query);
			
			return -1;
		}
		
		return id;
	}
	private ItemCategory buildItemCategory(ResultSet results) throws SQLException {
		ItemCategory category = null;
		
		String query = "";
		try {
			
			category = new ItemCategory();
			category.setId(results.getInt("id"));
			category.setName(results.getString("name"));
		}
		catch (SQLException e) {
			System.out.println("ItemCategory was not built!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return category;
	}
}
