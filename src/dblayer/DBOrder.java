package dblayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dblayer.interfaces.IFDBOrder;
import modlayer.Supplier;
import modlayer.Transaction;
import modlayer.TransactionType;
import modlayer.City;
import modlayer.Employee;
import modlayer.Order;
import modlayer.OrderMenuItem;

public class DBOrder implements IFDBOrder {
	
	private Connection con;
	
	public DBOrder() {
		con = DBConnection.getConnection();
	}
	
	@Override
	public ArrayList<Order> getOrders(boolean payed) {
		ArrayList<Order> orders = new ArrayList<>();
		
		String query = "SELECT * FROM [Order_View] "
					+ "WHERE transactionTimestamp IS " + (payed ? "NOT" : "") + " NULL";
		try {
			
			Statement st = con.createStatement();
			st.setQueryTimeout(5);
			
			Order order;
			ResultSet results = st.executeQuery(query);
			while (results.next()) {
				order = buildOrder(results);
				orders.add(order);
			}
			st.close();
		} catch (SQLException e) {
			System.out.println("Orders were not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return orders;
	}
	
	@Override
	public ArrayList<Order> searchOrders(String keyword, boolean payed) {
		ArrayList<Order> orders = new ArrayList<Order>();

		String query = "SELECT * FROM [Order_View] "
						+ "WHERE transactionTimestamp IS " + (payed ? "NOT" : "") + " NULL"
						+ "AND (orderId LIKE ? "
						+ "OR employeeName LIKE ? "
						+ "OR supplierName LIKE ?)";
		
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setString(1, "%" + keyword + "%");
			ps.setString(2, "%" + keyword + "%");
			ps.setString(3, "%" + keyword + "%");
			
			Order order;
			ResultSet results = ps.executeQuery();
			while (results.next()) {
				order = buildOrder(results);
				orders.add(order);
			}
			ps.close();
		}
		catch (SQLException e) {
			System.out.println("Orders were not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return orders;
	}
	
	@Override
	public Order selectOrder(int id) {
		Order order = null;
		
		String query = "SELECT * FROM [Order_View] "
						+ "WHERE orderId = ?";
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, id);
			
			ResultSet results = ps.executeQuery();
			if (results.next()) {
				order = buildOrder(results);
			}
		} catch (SQLException e) {
			System.out.println("Order was not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return order;
	}
	
	@Override
	public int insertOrder(Order order) {
		int id = -1;
		String query = "";
		
		try {
			DBConnection.startTransaction();
			
			query = "INSERT INTO [Order] "
					+ "(table_no, employee_cpr) "
					+ "VALUES (?, ?)";
			PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setQueryTimeout(5);
			ps.setInt(1, order.getTableNo());
			ps.setString(2, order.getEmployee().getCpr());
			
			if (ps.executeUpdate() > 0) {
				ResultSet generatedKeys = ps.getGeneratedKeys();
	            if (generatedKeys.next()) {
	            	id = generatedKeys.getInt(1);
		            order.setId(id);
	            }
			}
			ps.close();
			
			//OrderMenuItems
			DBOrderMenuItem dbOrderMenuItem = new DBOrderMenuItem();
			for (OrderMenuItem item : order.getItems()) {
				item.setOrder(order);
				dbOrderMenuItem.insertOrderMenuItem(item);
			}
			
			DBConnection.commitTransaction();
		}
		catch (SQLException e) {
			System.out.println("Order was not inserted!");
			System.out.println(e.getMessage());
			System.out.println(query);
			
			DBConnection.rollbackTransaction();
			return -1;
		}
		
		return id;
	}
	
	@Override
	public boolean payOrder(Order order) {
		boolean success = false;
		String query = "";
		
		try {
			DBConnection.startTransaction();
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setBoolean(1, true);
			ps.setInt(2, order.getId());
			
			success = ps.executeUpdate() > 0;
			ps.close();
		
			DBConnection.commitTransaction();
		}
		catch (SQLException e) {
			System.out.println("Item was not updated!");
			System.out.println(e.getMessage());
			System.out.println(query);
			
			DBConnection.rollbackTransaction();
		}
		
		return success;
	}
	
	@Override
	public boolean cancelOrder(Order order) {
		boolean success = false;
		String query = "";
		
		try {
			PreparedStatement ps;
			
			//OrderMenuItems
			query = "DELETE FROM [Order_Item] WHERE order_id = ?";
			ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, order.getId());
			
			success = ps.executeUpdate() > 0;
			ps.close();
			if (!success) {
				throw new SQLException("OrderMenuItems were not deleted!");
			}
			
			//Transaction
			query = "DELETE FROM [Transaction] WHERE id = ?";
			ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, order.getId());
			
			success = ps.executeUpdate() > 0;
			ps.close();
			if (!success) {
				throw new SQLException("Transaction was not deleted!");
			}
			
			//Order
			query = "DELETE FROM [Order] WHERE id = ?";
			ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, order.getId());
			
			success = ps.executeUpdate() > 0;
			ps.close();
		}
		catch (SQLException e) {
			System.out.println("Order was not deleted!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
			
		return success;
	}

	
	private Order buildOrder(ResultSet results) throws SQLException {
		Order order = null;
		
		String query = "";
		try {
			order = new Order();
			
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
				
			//Transaction
			Transaction transaction = new Transaction();
			transaction.setId(results.getInt("orderId"));
			transaction.setAmount(results.getDouble("transactionAmount"));
			transaction.setType(TransactionType.getType(results.getInt("transactionTypeId")));
			transaction.setTimestamp(results.getDate("transactionTimestamp"));
			
			//OrderMenuItems
			DBOrderMenuItem dbOrderMenuItem = new DBOrderMenuItem();
			ArrayList<OrderMenuItem> items = dbOrderMenuItem.getOrderMenuItems(order.getId());
			
			//Order
			order.setId(results.getInt("orderId"));
			order.setTableNo(results.getInt("orderTableNo"));
			order.setEmployee(employee);
			order.setTransaction(transaction);
			order.setItems(items);
		}
		catch (SQLException e) {
			System.out.println("Order was not built!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return order;
	}


}
