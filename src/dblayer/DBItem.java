package dblayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dblayer.interfaces.IFDBItem;
import modlayer.Item;
import modlayer.ItemCategory;
import modlayer.Unit;

public class DBItem implements IFDBItem {
	
	private Connection con;

	public DBItem() {
		con = DBConnection.getConnection();
	}
	
	@Override
	public ArrayList<Item> getItems() {
		ArrayList<Item> items = new ArrayList<>();
		
		String query =
			  "SELECT i.barcode AS itemBarcode, "
			+ "i.name AS itemName, "
			+ "i.quantity AS itemQuantity, "
			+ "u.abbreviation AS unitAbbrevation, "
			+ "u.name AS unitName, "
			+ "c.id AS categoryId, "
			+ "c.name AS categoryName "
			+ "FROM [Item] AS i "
			+ "INNER JOIN [Unit] AS u "
			+ "ON i.unit = u.abbreviation "
			+ "INNER JOIN [Item_Category] AS c "
			+ "ON i.category_id = c.id";
		try {
			
			Statement st = con.createStatement();
			st.setQueryTimeout(5);
			
			Item item;
			ResultSet results = st.executeQuery(query);
			while (results.next()) {
				item = buildItem(results);
				items.add(item);
			}
			st.close();
		} catch (SQLException e) {
			System.out.println("Items were not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return items;
	}

	@Override
	public ArrayList<Item> searchItems(String keyword) {
		ArrayList<Item> items = new ArrayList<Item>();

		String query =
			  "SELECT i.barcode AS itemBarcode, "
			+ "i.name AS itemName, "
			+ "i.quantity AS itemQuantity, "
			+ "u.abbreviation AS unitAbbrevation, "
			+ "u.name AS unitName, "
			+ "c.id AS categoryId, "
			+ "c.name AS categoryName "
			+ "FROM [Item] AS i "
			+ "INNER JOIN [Unit] AS u "
			+ "ON i.unit = u.abbreviation "
			+ "INNER JOIN [Item_Category] AS c "
			+ "ON i.category_id = c.id "
			+ "WHERE i.barcode LIKE ? "
			+ "OR i.name LIKE ?";
		
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setString(1, "%" + keyword + "%");
			ps.setString(2, "%" + keyword + "%");
			
			Item item;
			ResultSet results = ps.executeQuery();
			while (results.next()) {
				item = buildItem(results);
				items.add(item);
			}
			ps.close();
		}
		catch (SQLException e) {
			System.out.println("Items were not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return items;
	}

	@Override
	public Item selectItem(String barcode) {
		Item item = null;
		
		String query =
				  "SELECT i.barcode AS itemBarcode, "
				+ "i.name AS itemName, "
				+ "i.quantity AS itemQuantity, "
				+ "u.abbreviation AS unitAbbrevation, "
				+ "u.name AS unitName, "
				+ "c.id AS categoryId, "
				+ "c.name AS categoryName "
				+ "FROM [Item] AS i "
				+ "INNER JOIN [Unit] AS u "
				+ "ON i.unit = u.abbreviation "
				+ "INNER JOIN [Item_Category] AS c "
				+ "ON i.category_id = c.id "
				+ "WHERE i.barcode = ?";
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setString(1, barcode);
			
			ResultSet results = ps.executeQuery();
			if (results.next()) {
				item = buildItem(results);
			}
		} catch (SQLException e) {
			System.out.println("Item was not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return item;
	}

	@Override
	public String insertItem(Item item) {
		String barcode = null;
		
		String query =
				  "INSERT INTO [Item] "
				+ "(barcode, name, quantity, unit, category_id) "
				+ "VALUES (?, ?, ?, ?, ?)";
		try {
			DBConnection.startTransaction();
			
			//Create new ItemCategory if needed
			if (item.getCategory().getId() < 1) {
				int id = -1;
				
				DBItemCategory dbCategory = new DBItemCategory();
				if ((id = dbCategory.insertCategory(item.getCategory())) < 0) {
					throw new SQLException("ItemCategory was not inserted!");
				}
				
				item.getCategory().setId(id);
			}
			
			//Item
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setString(1, item.getBarcode());
			ps.setString(2, item.getName());
			ps.setDouble(3, item.getQuantity());
			ps.setString(4, item.getUnit().getAbbr());
			ps.setInt(5, item.getCategory().getId());
			
			if (ps.executeUpdate() > 0) {
				barcode = item.getBarcode();
			}
			ps.close();
			
			DBConnection.commitTransaction();
		}
		catch (SQLException e) {
			System.out.println("Item was not inserted!");
			System.out.println(e.getMessage());
			System.out.println(query);
			
			DBConnection.rollbackTransaction();
		}
		
		return barcode;
	}

	@Override
	public boolean updateItem(Item item) {
		boolean success = false;
		
		String query =
				"UPDATE [Item] "
			  + "SET name = ?, "
			  + "quantity = ?, "
			  + "unit = ?, "
			  + "category_id = ? "
			  + "WHERE barcode = ?";
		try {
			//Create new ItemCategory if needed
			if (item.getCategory().getId() < 1) {
				int id = -1;
				
				DBItemCategory dbCategory = new DBItemCategory();
				if ((id = dbCategory.insertCategory(item.getCategory())) < 0) {
					throw new SQLException("ItemCategory was not inserted!");
				}
				
				item.getCategory().setId(id);
			}
		
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setString(1, item.getName());
			ps.setDouble(2, item.getQuantity());
			ps.setString(3, item.getUnit().getAbbr());
			ps.setInt(4, item.getCategory().getId());
			ps.setString(5, item.getBarcode());
			
			success = ps.executeUpdate() > 0;
			ps.close();
		}
		catch (SQLException e) {
			System.out.println("Item was not updated!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return success;
	}

	@Override
	public boolean deleteItem(Item item) {
		boolean success = false;
		
		String query = "DELETE FROM [Item] WHERE barcode = ?";
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setString(1, item.getBarcode());
			
			success = ps.executeUpdate() > 0;
			ps.close();
		}
		catch (SQLException e) {
			System.out.println("Item was not deleted!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
			
		return success;
	}
	
	private Item buildItem(ResultSet results) throws SQLException {
		Item item = null;
		
		String query = "";
		try {
			
			//Unit
			Unit unit = new Unit();
			unit.setAbbr(results.getString("unitAbbrevation"));
			unit.setName(results.getString("unitName"));
			
			//ItemCategory
			ItemCategory category = new ItemCategory();
			category.setId(results.getInt("categoryId"));
			category.setName(results.getString("categoryName"));
			
			//Item
			item = new Item();
			item.setBarcode(results.getString("itemBarcode"));
			item.setName(results.getString("itemName"));
			item.setQuantity(results.getDouble("itemQuantity"));
			item.setUnit(unit);
			item.setCategory(category);
		}
		catch (SQLException e) {
			System.out.println("Item was not built!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return item;
	}
}
