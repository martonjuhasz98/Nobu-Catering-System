package dblayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dblayer.interfaces.IFDBMenuItem;
import modlayer.City;
import modlayer.Ingredient;
import modlayer.InvoiceItem;
import modlayer.Item;
import modlayer.ItemCategory;
import modlayer.MenuItem;
import modlayer.MenuItemCategory;

public class DBMenuItem implements IFDBMenuItem {

	private Connection con;
	
	public DBMenuItem() {
		con = DBConnection.getConnection();
	}

	@Override
	public ArrayList<MenuItem> getMenuItems() {
		ArrayList<MenuItem> menuItems = new ArrayList<>();
		
		String query = "SELECT "
					+ "i.id AS itemId, "
					+ "i.name AS itemName, "
					+ "i.price AS itemPrice, "
					+ "c.id AS categoryId, "
					+ "c.name AS categoryName "
					+ "FROM [Menu_Item] AS i "
					+ "INNER JOIN [Menu_Item_Category] AS c "
					+ "ON i.category_id = c.id";
		try {
			
			Statement st = con.createStatement();
			st.setQueryTimeout(5);
			
			MenuItem menuItem;
			ResultSet results = st.executeQuery(query);
			while (results.next()) {
				menuItem = buildMenuItem(results);
				menuItems.add(menuItem);
			}
			st.close();
		} catch (SQLException e) {
			System.out.println("MenuItems were not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return menuItems;
	}

	@Override
	public ArrayList<MenuItem> searchMenuItems(String keyword) {
		ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();

		String query = "SELECT "
					+ "i.id AS itemId, "
					+ "i.name AS itemName, "
					+ "i.price AS itemPrice, "
					+ "c.id AS categoryId, "
					+ "c.name AS categoryName "
					+ "FROM [Menu_Item] AS i "
					+ "INNER JOIN [Menu_Item_Category] AS c "
					+ "ON i.category_id = c.id"
					+ "WHERE itemId LIKE ? "
					+ "OR itemName LIKE ?";
		
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setString(1, "%" + keyword + "%");
			ps.setString(2, "%" + keyword + "%");
			
			MenuItem menuItem;
			ResultSet results = ps.executeQuery();
			while (results.next()) {
				menuItem = buildMenuItem(results);
				menuItems.add(menuItem);
			}
			ps.close();
		}
		catch (SQLException e) {
			System.out.println("MenuItems were not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return menuItems;
	}

	@Override
	public MenuItem selectMenuItem(int id) {
		MenuItem menuItem = null;
		
		String query = "SELECT "
					+ "i.id AS itemId, "
					+ "i.name AS itemName, "
					+ "i.price AS itemPrice, "
					+ "c.id AS categoryId, "
					+ "c.name AS categoryName "
					+ "FROM [Menu_Item] AS i "
					+ "INNER JOIN [Menu_Item_Category] AS c "
					+ "ON i.category_id = c.id"
					+ "WHERE itemId = ?";
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, id);
			
			ResultSet results = ps.executeQuery();
			if (results.next()) {
				menuItem = buildMenuItem(results);
			}
		} catch (SQLException e) {
			System.out.println("MenuItem was not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return menuItem;
	}

	@Override
	public int insertMenuItem(MenuItem item) {
		int id = -1;
		
		String query =
				  "INSERT INTO [MenuItem] "
				+ "(id, name, price, category_id) "
				+ "VALUES (?, ?, ?, ?)";
		try {
			//Create new MenuItemCategory if needed
			if (item.getCategory().getId() < 1) {
				DBMenuItemCategory dbCategory = new DBMenuItemCategory();
				if ((id = dbCategory.insertCategory(item.getCategory())) < 0) {
					throw new SQLException("MenuItemCategory was not inserted!");
				}
				
				item.getCategory().setId(id);
				id = -1;
			}
			
			//MenuItem
			PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setQueryTimeout(5);
			ps.setInt(1, item.getId());
			ps.setString(2, item.getName());
			ps.setDouble(3, item.getPrice());
			ps.setInt(4, item.getCategory().getId());
			
			if (ps.executeUpdate() > 0) {
				ResultSet generatedKeys = ps.getGeneratedKeys();
	            if (generatedKeys.next()) {
	            	id = generatedKeys.getInt(1);
		            item.setId(id);
	            }
			}
			ps.close();
		}
		catch (SQLException e) {
			System.out.println("MenuItem was not inserted!");
			System.out.println(e.getMessage());
			System.out.println(query);
			
			return -1;
		}
		
		return id;
	}

	@Override
	public boolean updateMenuItem(MenuItem item) {
		boolean success = false;
		
		String query =
				"UPDATE [Menu_Item] "
			  + "SET id = ?, "
			  + "name = ?, "
			  + "price = ?, "
			  + "category_id = ? "
			  + "WHERE id = ?";
		try {
			//Create new ItemCategory if needed
			if (item.getCategory().getId() < 1) {
				int id = -1;
				
				DBMenuItemCategory dbCategory = new DBMenuItemCategory();
				if ((id = dbCategory.insertCategory(item.getCategory())) < 0) {
					throw new SQLException("ItemCategory was not inserted!");
				}
				
				item.getCategory().setId(id);
			}
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, item.getId());
			ps.setString(2, item.getName());
			ps.setDouble(3, item.getPrice());
			ps.setInt(4, item.getCategory().getId());
			ps.setInt(5, item.getId());
			
			success = ps.executeUpdate() > 0;
			ps.close();
		}
		catch (SQLException e) {
			System.out.println("MenuItem was not updated!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return success;
	}

	@Override
	public boolean deleteMenuItem(MenuItem menuItem) {
		boolean success = false;
		
		String query = "DELETE FROM [Menu_Item] WHERE id = ?";
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, menuItem.getId());
			
			success = ps.executeUpdate() > 0;
			ps.close();
		}
		catch (SQLException e) {
			System.out.println("MenuItem was not deleted!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
			
		return success;
	}
	
	private MenuItem buildMenuItem(ResultSet results) throws SQLException {
		MenuItem menuItem = null;
		
		String query = "";
		try {
			
			//MenuItemCategory
			MenuItemCategory category = new MenuItemCategory();
			category.setId(results.getInt("categoryId"));
			category.setName(results.getString("categoryName"));
			
			//MenuItem
			menuItem = new MenuItem();
			menuItem.setId(results.getInt("itemId"));
			menuItem.setName(results.getString("itemName"));
			menuItem.setPrice(results.getDouble("itemPrice"));
			menuItem.setCategory(category);
			
			//Ingredients
			ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
			query =   "SELECT item_barcode, quantity, waste "
					+ "FROM [Ingredient] "
					+ "WHERE menu_item_id = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, menuItem.getId());
			
			DBItem dbItem = new DBItem();
			Ingredient ingredient;
			results = ps.executeQuery();
			while (results.next()) {
				ingredient = new Ingredient();
				ingredient.setMenuItem(menuItem);
				ingredient.setItem(dbItem.selectItem(results.getString("item_barcode")));
				ingredient.setQuantity(results.getInt("quantity"));
				ingredient.setWaste(results.getDouble("waste"));
				ingredients.add(ingredient);
			}
			ps.close();
			menuItem.setIngredients(ingredients);
		}
		catch (SQLException e) {
			System.out.println("MenuItem was not built!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return menuItem;
	}
}
