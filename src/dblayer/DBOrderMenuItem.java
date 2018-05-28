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
	
	private Connection con;
	
	public DBOrderMenuItem() {
		con = DBConnection.getConnection();
	}
	
	@Override
	public ArrayList<OrderMenuItem> getOrderMenuItems(int orderId) {
		ArrayList<OrderMenuItem> orderMenuItems = new ArrayList<>();
		
		String query = "SELECT * FROM [Order_Menu_Item] "
					+ "WHERE order_id = ?";
		try {
			
			PreparedStatement st = con.prepareStatement(query);
			st.setQueryTimeout(5);
			
			OrderMenuItem orderMenuItem;
			ResultSet results = st.executeQuery(query);
			while (results.next()) {
				orderMenuItem = buildOrderMenuItem(results);
				orderMenuItems.add(orderMenuItem);
			}
			st.close();
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
	public boolean insertOrderMenuItem(OrderMenuItem orderMenuItem) {
		boolean success = false;
		String query = "";
		
		try {
			query = "INSERT INTO [Order_Menu_Item] "
					+ "(menu_item_id, order_id, quantity) "
					+ "VALUES (?, ?, ?)";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, orderMenuItem.getMenuItem().getId());
			ps.setInt(2, orderMenuItem.getOrder().getId());
			ps.setDouble(3, orderMenuItem.getQuantity());
			
			success = ps.executeUpdate() > 0;
			ps.close();
		}
		catch (SQLException e) {
			System.out.println("OrderMenuItem was not inserted!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return success;
	}
	
	@Override
	public boolean updateOrderMenuItem(OrderMenuItem orderMenuItem) {
		boolean success = false;
		
		String query =
				"UPDATE [Order_Menu_Item] "
			  + "SET quantity = ?, "
			  + "is_finished = ? "
			  + "WHERE order_id = ? "
			  + "AND menu_item_id = ?";
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, orderMenuItem.getQuantity());
			ps.setBoolean(2, orderMenuItem.isFinished());
			ps.setInt(3, orderMenuItem.getOrder().getId());
			ps.setInt(4, orderMenuItem.getMenuItem().getId());
			
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
