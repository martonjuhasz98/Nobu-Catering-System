package dblayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dblayer.interfaces.IFDBOrderMenuItem;
import modlayer.Supplier;
import modlayer.Transaction;
import modlayer.TransactionType;
import modlayer.City;
import modlayer.Employee;
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
			  + "is_finished = ; "
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
			orderMenuItem = new OrderMenuItem();
			
			//City
			City city = new City();
			city.setZipCode(results.getString("employeeCityZipCode"));
			city.setName(results.getString("employeeCityName"));
			
			//Employee
			Employee employee = new Employee();
			employee.setCpr(results.getString("employeeCpr"));
			employee.setName(results.getString("employeeName"));
			employee.setUsername(results.getString("employeeUsername"));
			employee.setPassword(results.getString("employeePassword"));
			employee.setAddress(results.getString("employeeAddress"));
			employee.setCity(city);
			employee.setPhone(results.getString("employeePhone"));
			employee.setEmail(results.getString("employeeEmail"));
			employee.setAccessLevel(results.getInt("employeeAccessLevel"));
			
			//City
			city = new City();
			city.setZipCode(results.getString("supplierCityZipCode"));
			city.setName(results.getString("supplierCityName"));

			//Supplier
			Supplier supplier = new Supplier();
			supplier.setCvr(results.getString("supplierCvr"));
			supplier.setName(results.getString("supplierName"));
			supplier.setAddress(results.getString("supplierAddress"));
			supplier.setCity(city);
			supplier.setPhone(results.getString("supplierPhone"));
			supplier.setEmail(results.getString("supplierEmail"));
				
			//Transaction
			Transaction transaction = new Transaction();
			transaction.setId(results.getInt("orderId"));
			transaction.setAmount(results.getDouble("transactionAmount"));
			transaction.setType(TransactionType.getType(results.getInt("transactionTransactionType")));
			transaction.setTimestamp(results.getDate("transactionTimestamp"));
			
			//OrderMenuItem
			orderMenuItem.setId(results.getInt("orderId"));
			orderMenuItem.setDelivered(results.getBoolean("orderMenuItemIsDelivered"));
			orderMenuItem.setTimestamp(results.getDate("orderMenuItemTimestamp"));
			orderMenuItem.setDateDelivered(results.getDate("orderMenuItemDateDelivered"));
			orderMenuItem.setPlacedBy(employee);
			orderMenuItem.setSupplier(supplier);
			orderMenuItem.setTransaction(transaction);
			
			//OrderMenuItemMenuItem
			ArrayList<OrderMenuItemMenuItem> items = new ArrayList<OrderMenuItemMenuItem>();
			query =   "SELECT item_barcode, quantity, unit_price "
					+ "FROM [OrderMenuItem_item] "
					+ "WHERE orderMenuItem_id = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, orderMenuItem.getId());
			
			DBItem dbItem = new DBItem();
			OrderMenuItem item;
			results = ps.executeQuery();
			while (results.next()) {
				item = new OrderMenuItem();
				item.setOrderMenuItem(orderMenuItem);
				item.setItem(dbItem.selectItem(results.getString("item_barcode")));
				item.setQuantity(results.getInt("quantity"));
				item.setUnitPrice(results.getDouble("unit_price"));
				items.add(item);
			}
			ps.close();
			orderMenuItem.setItems(items);
		}
		catch (SQLException e) {
			System.out.println("OrderMenuItem was not built!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return orderMenuItem;
	}
}
