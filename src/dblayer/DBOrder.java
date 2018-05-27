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
					+ "WHERE transactionId IS " + (payed ? "NOT" : "") + " NULL";
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
						+ "WHERE transactionId IS " + (payed ? "NOT" : "") + " NULL"
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
			
			query = "INSERT INTO [Order] (table_no, employee_cpr) VALUES (?, ?)";
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
			double totalPrice = 0;
			String itemBarcode;
			double unitPrice;
			double quantity;
			
			for (OrderMenuItem item : order.getItems()) {
				itemBarcode = item.getItem().getBarcode();
				unitPrice = item.getUnitPrice();
				quantity = item.getQuantity();
				totalPrice += quantity * unitPrice;
				
				query =   "INSERT INTO [Order_Item] "
						+ "(item_barcode, order_id, quantity, unit_price) "
						+ "VALUES (?, ?, ?, ?)";
				try {
					ps = con.prepareStatement(query);
					ps.setQueryTimeout(5);
					ps.setString(1, itemBarcode);
					ps.setInt(2, id);
					ps.setDouble(3, quantity);
					ps.setDouble(4, unitPrice);
					
					boolean success = ps.executeUpdate() > 0;
					ps.close();
					if (!success) {
						throw new SQLException();
					}
				}
				catch (SQLException e) {
					System.out.println("OrderMenuItem was not inserted!");
					
					throw e;
				}
			}
			
			//Transaction
			Transaction transaction = new Transaction();
			transaction.setId(id);
			transaction.setAmount(totalPrice);
			transaction.setType(TransactionType.ACCOUNT);
			DBTransaction dbTransaction = new DBTransaction();
			if (dbTransaction.insertTransaction(transaction) < 0) {
				throw new SQLException("Transaction was not inserted!");
			}
			ps.close();
			
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
		
		String query =
				"UPDATE [Order] "
			  + "SET is_payed = ?,"
			  + "date_payed = GETDATE() "
			  + "WHERE id = ?";
		try {
			DBConnection.startTransaction();
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setBoolean(1, true);
			ps.setInt(2, order.getId());
			
			success = ps.executeUpdate() > 0;
			ps.close();
			
			//Update Item quantity
			String itemBarcode;
			double quantity;
			
			for (OrderMenuItem item : order.getItems()) {
				itemBarcode = item.getItem().getBarcode();
				quantity = item.getQuantity();
				if (quantity == 0) continue;
				
				query =   "UPDATE [Item] "
						+ "SET quantity = quantity + ? "
						+ "WHERE barcode = ?";
				try {
					ps = con.prepareStatement(query);
					ps.setQueryTimeout(5);
					ps.setDouble(1, quantity);
					ps.setString(2, itemBarcode);
					
					if (ps.executeUpdate() < 1) {
						throw new SQLException();
					}
					ps.close();
				}
				catch (SQLException e) {
					System.out.println("OrderMenuItem was not inserted!");
					
					throw e;
				}
			}
		
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
			
			//Order
			order.setId(results.getInt("orderId"));
			order.setDelivered(results.getBoolean("orderIsDelivered"));
			order.setTimestamp(results.getDate("orderTimestamp"));
			order.setDateDelivered(results.getDate("orderDateDelivered"));
			order.setPlacedBy(employee);
			order.setSupplier(supplier);
			order.setTransaction(transaction);
			
			//OrderMenuItem
			ArrayList<OrderMenuItem> items = new ArrayList<OrderMenuItem>();
			query =   "SELECT item_barcode, quantity, unit_price "
					+ "FROM [Order_item] "
					+ "WHERE order_id = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, order.getId());
			
			DBItem dbItem = new DBItem();
			OrderMenuItem item;
			results = ps.executeQuery();
			while (results.next()) {
				item = new OrderMenuItem();
				item.setOrder(order);
				item.setItem(dbItem.selectItem(results.getString("item_barcode")));
				item.setQuantity(results.getInt("quantity"));
				item.setUnitPrice(results.getDouble("unit_price"));
				items.add(item);
			}
			ps.close();
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
