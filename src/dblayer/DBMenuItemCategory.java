package dblayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dblayer.interfaces.IFDBMenuItemCategory;
import modlayer.MenuItemCategory;

public class DBMenuItemCategory implements IFDBMenuItemCategory {
	
	private DBConnection dbCon;
	private Connection con;

	public DBMenuItemCategory() {
		dbCon = DBConnection.getInstance();
		con = dbCon.getConnection();
	}
	
	@Override
	public ArrayList<MenuItemCategory> getCategories() {
		ArrayList<MenuItemCategory> categories = new ArrayList<>();
		
		String query = "SELECT * FROM [Menu_Item_Category]";
		try {
			
			Statement st = con.createStatement();
			st.setQueryTimeout(5);
			
			MenuItemCategory category;
			ResultSet results = st.executeQuery(query);
			while (results.next()) {
				category = buildMenuItemCategory(results);
				categories.add(category);
			}
			st.close();
		} catch (SQLException e) {
			System.out.println("MenuItemCategorys were not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return categories;
	}
	@Override
	public MenuItemCategory selectCategory(int id) {
		MenuItemCategory category = null;
		
		String query = "SELECT * FROM [Menu_Item_Category] WHERE id = ?";
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, id);
			
			ResultSet results = ps.executeQuery();
			if (results.next()) {
				category = buildMenuItemCategory(results);
			}
		} catch (SQLException e) {
			System.out.println("MenuItemCategory was not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return category;
	}
	@Override
	public int insertCategory(MenuItemCategory category) {
		int id = -1;
		
		String query = "INSERT INTO [Menu_Item_Category] (name) VALUES (?)";
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
			System.out.println("MenuItemCategory was not inserted!");
			System.out.println(e.getMessage());
			System.out.println(query);
			
			return -1;
		}
		
		return id;
	}
	private MenuItemCategory buildMenuItemCategory(ResultSet results) throws SQLException {
		MenuItemCategory category = null;
		
		String query = "";
		try {
			
			category = new MenuItemCategory();
			category.setId(results.getInt("id"));
			category.setName(results.getString("name"));
		}
		catch (SQLException e) {
			System.out.println("MenuItemCategory was not built!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return category;
	}
}
