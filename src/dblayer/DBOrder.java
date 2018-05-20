package dblayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dblayer.interfaces.IFDBOrder;
import modlayer.Customer;
import modlayer.Order;
import modlayer.OrderProduct;
import modlayer.Product;

//FROM PERSISTANCE PROJECT
public class DBOrder implements IFDBOrder {
	
	private Connection con;

	public DBOrder() {
		con = DBConnection.getConnection();
	}

	@Override
	public ArrayList<Order> getOrders() {
		ArrayList<Order> orders = new ArrayList<>();
		
		String query = "SELECT * FROM [Order]";
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
	public ArrayList<Order> searchOrders(String keyword) {
		ArrayList<Order> orders = new ArrayList<Order>();

		String query =
				  "SELECT [Order].* FROM [Order] "
				+ "LEFT JOIN Customer "
				+ "ON [Order].customer_id = [Customer].id "
				+ "WHERE [Order].id LIKE '%?%' "
				+ "OR Customer.name LIKE '%?%' "
				+ "OR Customer.id LIKE '%?%'";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setString(1, keyword);
			ps.setString(2, keyword);
			ps.setString(3, keyword);
			
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
	public Order selectOrder(int orderId) {
		Order order = null;
		
		String query = "SELECT * FROM [Order] WHERE [Order].id = ?";
		try {
			
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, orderId);
			
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
		
		String query =
				  "INSERT INTO [Order] "
				+ "(customer_id, price, discount, delivery_price) "
				+ "VALUES (?, ?, ?, ?)";
		try {
			DBConnection.startTransaction();
			
			PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setQueryTimeout(5);
			ps.setInt(1, order.getCustomer().getId());
			ps.setDouble(2, order.getPrice());
			ps.setDouble(3, order.getDiscount());
			ps.setDouble(4, order.getDeliveryPrice());
			
			ps.executeUpdate();
			ResultSet generatedKeys = ps.getGeneratedKeys();
            generatedKeys.next();
            
            id = generatedKeys.getInt(1);
            order.setId(id);
			ps.close();
			
			//OrderProducts
			for (OrderProduct orderProduct : order.getOrderProducts()) {
				int productId = orderProduct.getProduct().getId();
				int amount = orderProduct.getAmount();
				
				query =   "INSERT INTO [Order_product] "
						+ "(product_id, order_id, amount) "
						+ "VALUES (?, ?, ?)";
				try {
					ps = con.prepareStatement(query);
					ps.setQueryTimeout(5);
					ps.setInt(1, productId);
					ps.setInt(2, id);
					ps.setInt(3, amount);
					
					ps.executeUpdate();
					ps.close();
				}
				catch (SQLException e) {
					System.out.println("OrderProduct was not inserted!");
					
					throw e;
				}
				
				query =   "UPDATE [Product] "
						+ "SET stock = stock - ? "
						+ "WHERE (id = ?)";
				try {
					ps = con.prepareStatement(query);
					ps.setQueryTimeout(5);
					ps.setInt(1, amount);
					ps.setInt(2, productId);
					
					ps.executeUpdate();
					ps.close();
				}
				catch (SQLException e) {
					System.out.println("Product stock was not decreased!");
					
					throw e;
				}
			}
			
			DBConnection.commitTransaction();
		}
		catch (SQLException e) {
			System.out.println("Order was not inserted!");
			System.out.println(e.getMessage());
			System.out.println(query);
			
			DBConnection.rollbackTransaction();
		}
		
		return id;
	}
	@Override
	public boolean updateOrder(Order order) {
		boolean success = false;
		
		String query =
				    "UPDATE [Order] "
				  + "SET delivery_status_id = ? "
				  + ",delivery_date = ? "
				  + ",payment_status = ? "
				  + ",payment_date = ? "
				  + "WHERE id = ?";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, order.getDeliveryStatus());
			ps.setDate(2, order.getDeliveryDate());
			ps.setInt(3, order.getPaymentStatus());
			ps.setDate(4, order.getPaymentDate());
			ps.setInt(5, order.getId());
			
			success = ps.executeUpdate() > 0;
			ps.close();
		}
		catch (SQLException e) {
			System.out.println("Order was not updated!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return success;
	}
	@Override
	public boolean deleteOrder(Order order) {
		boolean success = false;
		
		String query = "DELETE FROM [Order] WHERE id = ?";
		try {
			PreparedStatement ps = con.prepareStatement(query);
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
			
			//Order
			order = new Order();
			order.setId(results.getInt("id"));
			order.setPurchaseDate(results.getDate("purchase_date"));
			order.setPrice(results.getDouble("price"));
			order.setDiscount(results.getDouble("discount"));
			order.setDeliveryPrice(results.getDouble("delivery_price"));
			order.setPaymentStatus(results.getInt("payment_status"));
			order.setPaymentDate(results.getDate("payment_date"));
			order.setDeliveryStatus(results.getInt("delivery_status_id"));
			order.setDeliveryDate(results.getDate("delivery_date"));
			
			//Customer
			DBCustomer dbc = new DBCustomer();
			Customer customer = dbc.selectCustomer(results.getInt("customer_id"));
			order.setCustomer(customer);
	
			//OrderProducts
			query = "SELECT product_id, amount FROM [Order_product] WHERE order_id = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setQueryTimeout(5);
			ps.setInt(1, order.getId());
			
			OrderProduct orderProduct;
			DBProduct dbp = new DBProduct();
			results = ps.executeQuery();
			while (results.next()) {
				orderProduct = new OrderProduct();
				orderProduct.setProduct(dbp.selectProduct(results.getInt(1)));
				orderProduct.setAmount(results.getInt(2));
				order.addOrderProduct(orderProduct);
			}
			ps.close();
		}
		catch (SQLException e) {
			System.out.println("Order was not found!");
			System.out.println(e.getMessage());
			System.out.println(query);
		}
		
		return order;
	}
}
