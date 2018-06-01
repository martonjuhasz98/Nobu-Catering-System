package dblayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dblayer.interfaces.IFDBOrderMenuItem;
import modlayer.MenuItem;
import modlayer.Order;
import modlayer.OrderMenuItem;

public class DBOrderMenuItem implements IFDBOrderMenuItem {
	
	private DBConnection dbCon;
	private Connection con;

	public DBOrderMenuItem() {
		dbCon = DBConnection.getInstance();
		con = dbCon.getConnection();
	}
	
	@Override
	public ArrayList<OrderMenuItem> getOrderMenuItems(int orderId) {
		ArrayList<OrderMenuItem> orderMenuItems = new ArrayList<>();
		
		String query = "SELECT * FROM [Order_Menu_Item] "
					+ "WHERE order_id = ?";
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, orderId);
			
			OrderMenuItem orderMenuItem;
			ResultSet results = ps.executeQuery();
			while (results.next()) {
				orderMenuItem = buildOrderMenuItem(results);
				orderMenuItems.add(orderMenuItem);
			}
			ps.close();
		} catch (SQLException e) {
			System.out.println("OrderMenuItems were not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return orderMenuItems;
	}
	
	@Override
	public OrderMenuItem selectOrderMenuItem(int orderId, int menuItemId) {
		OrderMenuItem orderMenuItem = null;
		
		String query = "SELECT * FROM [Order_Menu_Item] "
						+ "WHERE order_id = ? "
						+ "AND menu_item_id = ?";
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, orderId);
			ps.setInt(2, menuItemId);
			
			ResultSet results = ps.executeQuery();
			if (results.next()) {
				orderMenuItem = buildOrderMenuItem(results);
			}
		} catch (SQLException e) {
			System.out.println("OrderMenuItem was not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return orderMenuItem;
	}
	
	@Override
	public boolean canCreateOrderMenuItem(OrderMenuItem menuItem) {
		boolean canCreate = false;
		
		String query = "SELECT "
					+ "SUM (CASE WHEN ((inventory.available - inventory.reserved) - (ordered.quantity * ?) >= 0) THEN 1 ELSE 0 END) - COUNT(*) AS can_create "
					+ "FROM [Ingredient] AS ordered "
					+ "INNER JOIN ( "
					+ "	SELECT "
					+ "	it.barcode AS barcode, "
					+ "	it.quantity AS available, "
					+ "	SUM(ing.quantity * omi.quantity) AS reserved "
					+ "	FROM [Order_Menu_Item] as omi "
					+ "	INNER JOIN [Menu_Item] as mi "
					+ "	ON omi.menu_item_id = mi.id "
					+ "	INNER JOIN [Ingredient] as ing "
					+ "	ON ing.menu_item_id = mi.id "
					+ "	INNER JOIN [Item] as it "
					+ "	ON ing.item_barcode = it.barcode "
					+ "	WHERE omi.is_finished = 0 "
					+ "	GROUP BY ing.quantity, it.quantity, it.name, it.barcode "
					+ ") inventory "
					+ "ON inventory.barcode = ordered.item_barcode "
					+ "WHERE ordered.menu_item_id = ?";
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, menuItem.getQuantity());
			ps.setInt(2, menuItem.getMenuItem().getId());
			
			ResultSet results = ps.executeQuery();
			if (results.next()) {
				canCreate = results.getInt("can_create") == 0;
			}
			ps.close();
		}
		catch (SQLException e) {
			System.out.println("MenuItem was not checked!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
			
		return canCreate;
	}
	
	@Override
	public boolean hasOrderMenuItem(OrderMenuItem item) {
		boolean has = false;
		String query = "";
		
		try {
			query = "SELECT COUNT(*) AS has "
					+ "FROM [Order_Menu_Item] "
					+ "WHERE order_id = ? "
					+ "AND menu_item_id = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, item.getOrder().getId());
			ps.setInt(2, item.getMenuItem().getId());
			
			ResultSet results = ps.executeQuery();
			if (results.next()) {
				has = results.getInt("has") > 0;
			}
			ps.close();
		}
		catch (SQLException e) {
			System.out.println("MenuItem was not checked!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
			
		return has;
	}
	
	@Override
	public boolean insertOrderMenuItem(OrderMenuItem item, boolean force) {
		boolean success = false;
		String query = "";
		
		try {
			dbCon.startTransaction();
			
			if (!force && !canCreateOrderMenuItem(item)) {
				throw new SQLException("There are not enough ingredients for this Order Menu Item!");
			}
			
			query = "INSERT INTO [Order_Menu_Item] "
					+ "(menu_item_id, order_id, quantity) "
					+ "VALUES (?, ?, ?)";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, item.getMenuItem().getId());
			ps.setInt(2, item.getOrder().getId());
			ps.setDouble(3, item.getQuantity());
			
			success = ps.executeUpdate() > 0;
			ps.close();
			
			dbCon.commitTransaction();
		}
		catch (SQLException e) {
			System.out.println("OrderMenuItem was not inserted!");
			System.out.println(e.getMessage());
			System.out.println(query);
			
			dbCon.rollbackTransaction();
			success = false;
		}
		
		return success;
	}
	
	@Override
	public boolean updateOrderMenuItem(OrderMenuItem item, boolean force) {
		boolean success = false;
		String query = "";
		
		try {
			dbCon.startTransaction();
			
			OrderMenuItem prevItem = selectOrderMenuItem(item.getOrder().getId(), item.getMenuItem().getId());
			int diffQuantity = prevItem.getQuantity() - item.getQuantity();
			if (!force && diffQuantity > 0) {
				prevItem.setQuantity(diffQuantity);
				if (!canCreateOrderMenuItem(prevItem)) {
					throw new SQLException("There are not enough ingredients for this Order Menu Item!");
				}
			}
			
			query =
					"UPDATE [Order_Menu_Item] "
				  + "SET quantity = ? "
				  + "WHERE order_id = ? "
				  + "AND menu_item_id = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, item.getQuantity());
			ps.setInt(2, item.getOrder().getId());
			ps.setInt(3, item.getMenuItem().getId());
			
			success = ps.executeUpdate() > 0;
			ps.close();
			
			dbCon.commitTransaction();
		}
		catch (SQLException e) {
			System.out.println("Item was not updated!");
			System.out.println(e.getMessage());
			System.out.println(query);
			
			dbCon.rollbackTransaction();
			success = false;
		}
		
		return success;
	}
	
	@Override
	public boolean confirmOrderMenuItem(OrderMenuItem item) {
		boolean success = false;
		String query = "";
		
		try {
			dbCon.startTransaction();
			PreparedStatement ps;
			
			query = "UPDATE [Order_Menu_Item] "
				  + "SET is_finished = ? "
				  + "WHERE order_id = ? "
				  + "AND menu_item_id = ?";
			ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setBoolean(1, true);
			ps.setInt(2, item.getOrder().getId());
			ps.setInt(3, item.getMenuItem().getId());
			
			success = ps.executeUpdate() > 0;
			ps.close();
			if (!success) {
				throw new SQLException("Item was not updated!");
			}
			
			query = "UPDATE it "
					+ "SET it.quantity = it.quantity - (omi.quantity * ing.quantity) "
					+ "FROM [Item] AS it "
					+ "INNER JOIN [Ingredient] AS ing "
					+ "ON it.barcode = ing.item_barcode "
					+ "INNER JOIN [Menu_Item] AS mi "
					+ "ON mi.id = ing.menu_item_id "
					+ "INNER JOIN [Order_Menu_Item] AS omi "
					+ "ON omi.menu_item_id = mi.id "
					+ "WHERE omi.order_id = ? "
					+ "AND omi.menu_item_id = ?";
			ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, item.getOrder().getId());
			ps.setInt(2, item.getMenuItem().getId());
			
			success = ps.executeUpdate() > 0;
			ps.close();
			
			dbCon.commitTransaction();
		}
		catch (SQLException e) {
			System.out.println("Item was not confirmed!");
			System.out.println(e.getMessage());
			System.out.println(query);
			
			dbCon.rollbackTransaction();
			success = false;
		}
		
		return success;
	}
	
	@Override
	public boolean deleteOrderMenuItem(OrderMenuItem orderMenuItem) {
		boolean success = false;
		String query = "";
		
		try {
			PreparedStatement ps;
			
			query = "DELETE FROM [Order_Menu_Item] "
					+ "WHERE order_id = ? "
					+ "AND menu_item_id = ?";
			ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, orderMenuItem.getOrder().getId());
			ps.setInt(2, orderMenuItem.getMenuItem().getId());
			
			success = ps.executeUpdate() > 0;
			ps.close();
		}
		catch (SQLException e) {
			System.out.println("OrderMenuItem was not deleted!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
			
		return success;
	}
	
	private OrderMenuItem buildOrderMenuItem(ResultSet results) throws SQLException {
		OrderMenuItem orderMenuItem = null;
		
		String query = "";
		try {
			//Order
			Order order = new Order();
			order.setId(results.getInt("order_id"));
			
			//MenuItem
			DBMenuItem dbMenuItem = new DBMenuItem();
			MenuItem menuItem = dbMenuItem.selectMenuItem(results.getInt("menu_item_id"));
			
			//OrderMenuItem
			orderMenuItem = new OrderMenuItem();
			orderMenuItem.setOrder(order);
			orderMenuItem.setMenuItem(menuItem);
			orderMenuItem.setQuantity(results.getInt("quantity"));
			orderMenuItem.setFinished(results.getBoolean("is_finished"));
		}
		catch (SQLException e) {
			System.out.println("OrderMenuItem was not built!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return orderMenuItem;
	}
}
