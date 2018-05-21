package dblayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import dblayer.interfaces.IFDBItem;
import modlayer.Item;
import modlayer.ItemCategory;
import modlayer.Unit;

public class DBItem implements IFDBItem {

	private ArrayList<Unit> units = new ArrayList<Unit>(Arrays.asList(
			new Unit("g", "gramm"),
			new Unit("dkg", "dekagramm"),
			new Unit("kg", "kilogramm"),
			new Unit("ml", "mililiter"),
			new Unit("cl", "centiliter"),
			new Unit("dl", "deciliter"),
			new Unit("l", "liter"),
			new Unit("pcs", "pieces")
	));
	private ArrayList<ItemCategory> categories = new ArrayList<ItemCategory>(Arrays.asList(
			new ItemCategory(1, "Vegetables"),
			new ItemCategory(2, "Meats"),
			new ItemCategory(3, "Fishes"),
			new ItemCategory(4, "Fruites"),
			new ItemCategory(5, "Dairy")
	));
	private ArrayList<Item> items = new ArrayList<Item>(Arrays.asList(
			new Item("1", "Carrots", 2.5, units.get(1), categories.get(0)),
			new Item("2", "Chicken breast", 4.5, units.get(0), categories.get(1)),
			new Item("3", "Salmon", 7.5, units.get(0), categories.get(2)),
			new Item("4", "Apple", 13.2, units.get(2), categories.get(3)),
			new Item("5", "Milk", 3.0, units.get(6), categories.get(4))
	));
	
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
			+ "WHERE i.barcode LIKE '%?%' "
			+ "OR i.name LIKE '%?%' ";
		
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setString(1, keyword);
			ps.setString(2, keyword);
			
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
		String barcode = "";
		String query = "";
		
		try {
			DBConnection.startTransaction();
			
			PreparedStatement ps;
			ResultSet generatedKeys;
			
			if (item.getCategory().getId() < 1) {
				int id = -1;
				
				//ItemCategory
				query =   "INSERT INTO [Item_Category] "
						+ "(name) "
						+ "VALUES (?)";
				
				ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setQueryTimeout(5);
				ps.setString(1, item.getCategory().getName());
				
				ps.executeUpdate();
				generatedKeys = ps.getGeneratedKeys();
	            generatedKeys.next();
				
				id = generatedKeys.getInt(1);
	            item.getCategory().setId(id);
				ps.close();
			}
			
			//Item
			query =   "INSERT INTO [Item] "
					+ "(barcode, name, quantity, unit, category_id) "
					+ "VALUES (?, ?, ?, ?, ?)";
			
			ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setQueryTimeout(5);
			ps.setString(1, item.getBarcode());
			ps.setString(2, item.getName());
			ps.setDouble(3, item.getQuantity());
			ps.setString(4, item.getUnit().getAbbr());
			ps.setInt(5, item.getCategory().getId());
			
			ps.executeUpdate();
			generatedKeys = ps.getGeneratedKeys();
            generatedKeys.next();
            
            barcode = generatedKeys.getString(1);
            item.setBarcode(barcode);
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
		String query = "";
		
		try {
			PreparedStatement ps;
			
			if (item.getCategory().getId() < 1) {
				int id = -1;
				
				//New ItemCategory
				query =   "INSERT INTO [Item_Category] AS c "
						+ "(name) "
						+ "VALUES (?)";
				
				ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setQueryTimeout(5);
				ps.setString(1, item.getCategory().getName());
				
				ps.executeUpdate();
				ResultSet generatedKeys = ps.getGeneratedKeys();
	            generatedKeys.next();
				
				id = generatedKeys.getInt(1);
	            item.getCategory().setId(id);
				ps.close();
				
				//Empty ItemCategory
				query =	  "DELETE c FROM [Item_Category] c "
						+ "LEFT JOIN [Item] AS i "
						+ "ON c.id = i.category_id "
						+ "WHERE c.id IS NULL";
				
				Statement st = con.createStatement();
				st.setQueryTimeout(5);
				
				success = st.executeUpdate(query) > 0;
				st.close();
			}
				
			query =
				    "UPDATE [Item] "
				  + "SET name = ?, "
				  + "quantity = ?, "
				  + "unit = ?, "
				  + "category_id = ? "
				  + "WHERE barcode = ?";
			
			ps = con.prepareStatement(query);
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
		
		String query = "DELETE FROM [Item] WHERE id = ?";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setString(1, item.getBarcode());
			
			success = ps.executeUpdate() > 0;
			ps.close();
			
			//Empty ItemCategory
			query =	  "DELETE c FROM [Item_Category] c "
					+ "LEFT JOIN [Item] AS i "
					+ "ON c.id = i.category_id "
					+ "WHERE c.id IS NULL";
			
			Statement st = con.createStatement();
			st.setQueryTimeout(5);
			
			success = st.executeUpdate(query) > 0;
			st.close();
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
			System.out.println("Item was not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return item;
	}
}
